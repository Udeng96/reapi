package com.eseict.reapi.hams.hams.service;

import com.eseict.reapi.hams.client.HamsSendClient;
import com.eseict.reapi.hams.client.protocol.HamsCmd;
import com.eseict.reapi.hams.equip.domain.TbEquipmentData;
import com.eseict.reapi.hams.equip.repository.TbEquipmentDataRepository;
import com.eseict.reapi.hams.hams.dto.HamsInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class HamsDoorControlService {

    private final HamsStatusService hamsStatusService;
    private final HamsDoeEventService hamsDoeEventService;
    private final HamsSendClient hamsSendClient;
    private final TbEquipmentDataRepository equipmentRepository;

    public HamsDoorControlService(HamsStatusService hamsStatusService,
                                   HamsDoeEventService hamsDoeEventService,
                                   HamsSendClient hamsSendClient,
                                   TbEquipmentDataRepository equipmentRepository) {
        this.hamsStatusService = hamsStatusService;
        this.hamsDoeEventService = hamsDoeEventService;
        this.hamsSendClient = hamsSendClient;
        this.equipmentRepository = equipmentRepository;
    }

    /**
     * 도어 잠금/해제 TCP 제어 명령 전송.
     *
     * @param id       hams ID (ex: "HAMS_1" or "1")
     * @param target   1=앞문, 2=뒷문
     * @param lockType 1=해제, 2=잠금
     */
    public Map<String, Object> doLock(String id, int target, int lockType) {
        HamsInfoDto info = hamsStatusService.get(id);
        if (info == null) {
            return Map.of("result", false, "message", "장비를 찾을 수 없습니다.");
        }

        Long eqId = hamsStatusService.parseHamsId(id);
        if (eqId == null) {
            return Map.of("result", false, "message", "잘못된 ID 형식입니다.");
        }

        TbEquipmentData eq = equipmentRepository.findByIdUsable(eqId).orElse(null);
        if (eq == null) {
            return Map.of("result", false, "message", "장비를 찾을 수 없습니다.");
        }

        // TCP 연결 확인
        if (!hamsSendClient.isConnect() && !hamsSendClient.doConnect()) {
            log.warn("HAMS TCP 미연결 — doLock skip (eq={})", eqId);
            // 이력은 저장하고 TCP 실패 메시지 반환
            saveHist(info.hamsId(), target, lockType);
            return Map.of("result", false, "message", "장비와 연결되지 않았습니다. 이력만 저장합니다.");
        }

        // DOC 명령 전송
        try {
            String seq = String.valueOf(System.currentTimeMillis() % 1000); // 임시 seq
            HamsCmd cmd = new HamsCmd(true, eq.getId().toString());
            cmd.setHeader(2, HamsCmd.TARGET.DOOR_LOCK.target + HamsCmd.CHAR.CONTROL.word, seq)
               .setBody(List.of(String.valueOf(target), String.valueOf(lockType)))
               .setTail();

            byte[] message = cmd.makeMessage();
            if (message == null) {
                return Map.of("result", false, "message", "DOC 명령 생성 실패");
            }

            log.info("HAMS sendDoc — eq={}, target={}, lockType={}", eqId, target, lockType);
            String response = hamsSendClient.doWriteAndFlush(message);

            if (response == null || response.isEmpty()) {
                log.warn("HAMS DOC 응답 없음 (eq={})", eqId);
                saveHist(info.hamsId(), target, lockType);
                return Map.of("result", false, "message", "장비 응답 없음. 이력만 저장합니다.");
            }

            HamsCmd returnCmd = new HamsCmd(response, true);
            boolean success = returnCmd.getParse() != null
                && returnCmd.getParse().parseBody() != null;

            saveHist(info.hamsId(), target, lockType);

            String action = lockType == 2 ? "잠금" : "해제";
            if (success) {
                log.info("HAMS DOC 성공 — eq={}, action={}", eqId, action);
                return Map.of("result", true, "message", action + " 완료");
            } else {
                log.warn("HAMS DOC 실패 응답 — eq={}, response={}", eqId, response);
                return Map.of("result", false, "message", action + " 명령 실패");
            }

        } catch (Exception e) {
            log.error("HAMS sendDoc error (eq={}): {}", eqId, e.getMessage());
            saveHist(info.hamsId(), target, lockType);
            return Map.of("result", false, "message", "내부 오류: " + e.getMessage());
        }
    }

    private void saveHist(String hamsId, int target, int lockType) {
        char doorType      = target   == 1 ? '1' : '2';
        char doorState     = lockType == 2 ? '3' : '2';  // 3=잠금, 2=해제
        char doorLockState = lockType == 2 ? '3' : '2';
        hamsDoeEventService.saveDoorHist(hamsId, doorType, doorState, doorLockState);
    }
}
