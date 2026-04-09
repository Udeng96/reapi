package com.eseict.reapi.hams.hams.service;

import com.eseict.reapi.hams.client.HamsSendClient;
import com.eseict.reapi.hams.client.protocol.HamsCmd;
import com.eseict.reapi.hams.equip.domain.TbEquipmentData;
import com.eseict.reapi.hams.equip.repository.TbEquipmentDataRepository;
import com.eseict.reapi.hams.hams.dto.HamsInfoDto;
import com.eseict.reapi.hams.hams.dto.HamsStatusDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true, transactionManager = "mariaTransactionManager")
public class HamsStatusService {

    private static final int MIN_SEQ = 500;
    private static final int MAX_SEQ = 999;

    private final TbEquipmentDataRepository equipmentRepository;
    private final HamsSendClient hamsSendClient;

    private final AtomicInteger seqCounter = new AtomicInteger(MIN_SEQ);

    public HamsStatusService(TbEquipmentDataRepository equipmentRepository,
                              HamsSendClient hamsSendClient) {
        this.equipmentRepository = equipmentRepository;
        this.hamsSendClient = hamsSendClient;
    }

    // ──────────────────────────────────────────────────────────
    // 공개 API
    // ──────────────────────────────────────────────────────────

    /** 전체 국사 목록 조회 — 각 국사의 도어 상태 포함 */
    public List<HamsInfoDto> getList() {
        return equipmentRepository.findAllUsable().stream()
            .map(eq -> {
                HamsStatusDto status = checkDoor(eq);
                return toDto(eq, status != null ? status : HamsStatusDto.empty());
            })
            .collect(Collectors.toList());
    }

    /** 단일 국사 조회 — 도어 상태 + 온습도 포함 */
    public HamsInfoDto get(String id) {
        Long eqId = parseHamsId(id);
        if (eqId == null) return null;
        return equipmentRepository.findByIdUsable(eqId)
            .map(eq -> {
                HamsStatusDto doorStatus = checkDoor(eq);
                if (doorStatus == null) {
                    return toDto(eq, HamsStatusDto.empty());
                }
                HamsStatusDto fullStatus = checkTempHumi(eq, doorStatus);
                return toDto(eq, fullStatus != null ? fullStatus : doorStatus);
            })
            .orElse(null);
    }

    // ──────────────────────────────────────────────────────────
    // TCP 조회 메서드
    // ──────────────────────────────────────────────────────────

    /**
     * DOS 명령으로 도어 상태 조회.
     * 연결 안 됐으면 null 반환 → 호출 측에서 empty() 사용.
     */
    public HamsStatusDto checkDoor(TbEquipmentData eq) {
        if (!ensureConnected()) {
            log.debug("HAMS 연결 없음 — checkDoor skip (eq={})", eq.getId());
            return null;
        }

        try {
            String seq = nextSeq();
            HamsCmd cmd = new HamsCmd(true, eq.getId().toString());
            cmd.setHeader(1, HamsCmd.TARGET.DOOR_LOCK.target + HamsCmd.CHAR.STATUS.word, seq)
               .setBody(List.of("0"))  // 0 = 전체 조회
               .setTail();

            byte[] message = cmd.makeMessage();
            if (message == null) return null;

            log.debug("HAMS checkDoor — eq={}", eq.getId());
            String response = hamsSendClient.doWriteAndFlush(message);
            if (response == null || response.isEmpty()) return null;

            HamsCmd returnCmd = new HamsCmd(response, true);
            if (returnCmd.getParse() == null) return null;

            Map<String, String> body = returnCmd.getParse().parseBody();
            if (body == null) return null;

            log.debug("HAMS checkDoor result: {}", body);
            return new HamsStatusDto(
                body.get(HamsCmd.FIELD_NM.FRONT_DOOR_S.name()),
                body.get(HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name()),
                body.get(HamsCmd.FIELD_NM.BACK_DOOR_S.name()),
                body.get(HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name()),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                true
            );
        } catch (Exception e) {
            log.error("HAMS checkDoor error (eq={}): {}", eq.getId(), e.getMessage());
            return null;
        }
    }

    /**
     * THS 명령으로 온습도 조회. 기존 doorStatus에 온습도를 덮어씌워 반환.
     */
    public HamsStatusDto checkTempHumi(TbEquipmentData eq, HamsStatusDto doorStatus) {
        if (!ensureConnected()) {
            log.debug("HAMS 연결 없음 — checkTempHumi skip (eq={})", eq.getId());
            return doorStatus;
        }

        try {
            String seq = nextSeq();
            HamsCmd cmd = new HamsCmd(true, eq.getId().toString());
            // THS 명령: setHeader 두 번째 오버로드 사용 (헤더 끝 ',' 생략)
            cmd.setHeader(0, HamsCmd.TARGET.TEMPERATURE_AND_HUMI.target + HamsCmd.CHAR.STATUS.word, seq)
               .setBody(List.of())
               .setTail();

            byte[] message = cmd.makeMessage();
            if (message == null) return doorStatus;

            log.debug("HAMS checkTempHumi — eq={}", eq.getId());
            String response = hamsSendClient.doWriteAndFlush(message);
            if (response == null || response.isEmpty()) return doorStatus;

            HamsCmd returnCmd = new HamsCmd(response, true);
            if (returnCmd.getParse() == null) return doorStatus;

            Map<String, String> body = returnCmd.getParse().parseBody();
            if (body == null) return doorStatus;

            log.debug("HAMS checkTempHumi result: {}", body);
            BigDecimal temp = parseBigDecimal(body.get(HamsCmd.FIELD_NM.TEMP.name()));
            BigDecimal humi = parseBigDecimal(body.get(HamsCmd.FIELD_NM.HUMI.name()));

            return new HamsStatusDto(
                doorStatus.frontDoor(),
                doorStatus.frontDoorLock(),
                doorStatus.backDoor(),
                doorStatus.backDoorLock(),
                temp,
                humi,
                doorStatus.isConnect()
            );
        } catch (Exception e) {
            log.error("HAMS checkTempHumi error (eq={}): {}", eq.getId(), e.getMessage());
            return doorStatus;
        }
    }

    // ──────────────────────────────────────────────────────────
    // 내부 유틸
    // ──────────────────────────────────────────────────────────

    private boolean ensureConnected() {
        if (hamsSendClient.isConnect()) return true;
        return hamsSendClient.doConnect();
    }

    private String nextSeq() {
        int seq = seqCounter.getAndUpdate(v -> v >= MAX_SEQ ? MIN_SEQ : v + 1);
        return String.valueOf(seq);
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null) return BigDecimal.ZERO;
        try { return new BigDecimal(value); }
        catch (NumberFormatException e) { return BigDecimal.ZERO; }
    }

    private HamsInfoDto toDto(TbEquipmentData eq, HamsStatusDto status) {
        String groupNm = eq.getRefTbGroupData() != null ? eq.getRefTbGroupData().getName() : null;
        String address = eq.getRoadnameAddress() != null ? eq.getRoadnameAddress() : eq.getJibunAddress();
        return new HamsInfoDto(
            "HAMS_" + eq.getId(), String.valueOf(eq.getId()),
            eq.getName(), groupNm, eq.getIp(), eq.getPort(),
            address, eq.getLatitude(), eq.getLongitude(), status
        );
    }

    public Long parseHamsId(String id) {
        String numId = id.startsWith("HAMS_") ? id.substring(5) : id;
        try { return Long.parseLong(numId); }
        catch (NumberFormatException e) { return null; }
    }
}
