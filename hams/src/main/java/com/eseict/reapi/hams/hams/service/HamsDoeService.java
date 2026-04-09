package com.eseict.reapi.hams.hams.service;

import com.eseict.reapi.hams.client.protocol.HamsCmd;
import com.eseict.reapi.hams.client.protocol.parser.HamsParsePolicy;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * HAMS DOE(Door Open Event) 수신 처리 서비스.
 * 레거시 HamsService.receiveDoe() 이식 (Java 21).
 *
 * 처리 흐름:
 *   1. HamsCmd 로 DOE 메시지 파싱
 *   2. ordererIp != 서버 IP → TbHamsDoorHist 저장
 *   3. userId/ordererIp/ordererType 모두 "0" → TbHamsDoorEventHist 저장
 *   4. WebSocket /topic/hams.doe 브로드캐스트
 */
@Slf4j
@Service
public class HamsDoeService {

    private final HamsDoeEventService doeEventService;
    private final SimpMessagingTemplate messagingTemplate;

    /** 서버 자체 IP — 기동 시 1회 캐싱 (IP는 운영 중 변경되지 않음) */
    private String serverIp = "";

    public HamsDoeService(HamsDoeEventService doeEventService,
                          SimpMessagingTemplate messagingTemplate) {
        this.doeEventService = doeEventService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostConstruct
    public void init() {
        try {
            serverIp = InetAddress.getLocalHost().getHostAddress();
            log.info("HamsDoeService 서버 IP 캐싱: {}", serverIp);
        } catch (Exception e) {
            log.warn("서버 IP 조회 실패, ordererIp 비교 없이 항상 저장: {}", e.getMessage());
        }
    }

    public void receiveDoe(String msg) {
        if (msg == null || msg.isBlank() || "nothing".equals(msg)) {
            return;
        }

        try {
            HamsCmd cmd = new HamsCmd(msg, true);
            HamsParsePolicy parser = cmd.getParse();
            if (parser == null) {
                log.warn("HamsDoeService: DOE 파서 없음 — msg={}", msg);
                return;
            }

            Map<String, String> body = parser.parseBody();

            String eqId        = body.getOrDefault(HamsCmd.FIELD_NM.EQ_ID.name(),       "");
            String ordererIp   = body.getOrDefault(HamsCmd.FIELD_NM.ORDERER_IP.name(),   "");
            String userId      = body.getOrDefault(HamsCmd.FIELD_NM.USER_ID.name(),      "");
            String ordererType = body.getOrDefault(HamsCmd.FIELD_NM.ORDERER_TYPE.name(), "");
            String eventType   = body.getOrDefault(HamsCmd.FIELD_NM.EVENT_TYPE.name(),   "");
            String doorTypeRaw  = body.getOrDefault("doorTypeRaw",   "");
            String doorState    = body.getOrDefault("doorState",     "");
            String doorLockState = body.getOrDefault("doorLockState", "");

            // ── 1. 도어 이력 저장 (서버 자신이 보낸 이벤트가 아닌 경우) ──────────────
            // serverIp 캐싱 실패 시(빈 문자열) 비교 skip → 항상 저장
            if (serverIp.isBlank() || !serverIp.equals(ordererIp)) {
                int doorTypeNum = -1;
                try { doorTypeNum = Integer.parseInt(doorTypeRaw); } catch (NumberFormatException ignored) {}
                // 유효하지 않은 doorType (1=앞문, 2=뒷문 외) 이면 이력 저장 skip
                if (doorTypeNum != 1 && doorTypeNum != 2) {
                    log.warn("DOE: 유효하지 않은 doorTypeRaw='{}' — saveDoorHist skip (eqId={})", doorTypeRaw, eqId);
                } else {
                    char doorTypeChar  = (char) ('0' + doorTypeNum);
                    char doorStateChar = doorState.isEmpty()     ? '0' : doorState.charAt(0);
                    char doorLockChar  = doorLockState.isEmpty() ? '0' : doorLockState.charAt(0);
                    doeEventService.saveDoorHist(eqId, doorTypeChar, doorStateChar, doorLockChar);
                    log.debug("saveDoorHist — eqId={}, doorType={}, doorState={}, doorLock={}",
                            eqId, doorTypeChar, doorStateChar, doorLockChar);
                }
            } else {
                log.debug("DOE: ordererIp==serverIp → saveDoorHist skip (eqId={})", eqId);
            }

            // ── 2. 도어 이벤트 이력 저장 (비정상 작동: userId/ordererIp/ordererType 모두 "0") ──
            if ("0".equals(userId) && "0".equals(ordererIp) && "0".equals(ordererType)) {
                doeEventService.saveDoorEventHist(eqId, doorTypeRaw, doorLockState, eventType);
                log.debug("saveDoorEventHist — eqId={}, doorType={}, eventType={}",
                        eqId, doorTypeRaw, eventType);
            }

            // ── 3. WebSocket 브로드캐스트 ────────────────────────────────────────────
            Map<String, Object> payload = new HashMap<>();
            payload.put("eqId",          eqId);
            payload.put("doorType",      doorTypeRaw);
            payload.put("doorState",     doorState);
            payload.put("doorLockState", doorLockState);
            payload.put("eventType",     eventType);
            messagingTemplate.convertAndSend("/topic/hams.doe", payload);
            log.info("DOE 브로드캐스트 → /topic/hams.doe: eqId={}", eqId);

        } catch (Exception e) {
            log.error("HamsDoeService.receiveDoe 실패: {}", e.getMessage(), e);
        }
    }

}
