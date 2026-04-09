package com.eseict.reapi.hams.client.protocol.parser;

import com.eseict.reapi.hams.client.protocol.HamsCmd;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * DOE(Door Open Event) 응답 파서.
 * 레거시 HamsDOEParse.kt 이식 (Java 21).
 *
 * 메시지 형식:
 * $PGL,CL,{eqId},DOE,{seq},{v},{cnt},
 *   {rcvDate},{ordererType},{userId},{ordererIp},
 *   {doorType},{eventDoorType},{eventType},
 *   {doorState},{doorLockState}*{CRC}\r\n
 */
@Slf4j
public class HamsDOEParse implements HamsParsePolicy {

    private final String message;
    private final Map<String, String> headerMap;

    public HamsDOEParse(String message, Map<String, String> headerMap) {
        this.message = message;
        this.headerMap = headerMap;
    }

    @Override
    public Map<String, String> parseBody() {
        Map<String, String> result = new HashMap<>(headerMap);
        try {
            String[] parts = message.split(",");

            String eqId          = safeGet(parts, 2);
            String rcvDate       = safeGet(parts, 7);
            String ordererType   = safeGet(parts, 8);
            String userId        = safeGet(parts, 9);
            String ordererIp     = safeGet(parts, 10);
            String doorTypeStr   = safeGet(parts, 11); // "1"=앞문, "2"=뒷문
            String eventDoorType = safeGet(parts, 12);
            String eventType     = safeGet(parts, 13);
            String doorState     = safeGet(parts, 14);

            // parts[15] 형식: "{doorLockState}*{CRC}\r\n"
            String doorLockStateTail = safeGet(parts, 15);
            String doorLockState = doorLockStateTail.contains("*")
                    ? doorLockStateTail.split("\\*")[0].trim()
                    : doorLockStateTail.trim();

            int doorTypeNum = 0;
            try { doorTypeNum = Integer.parseInt(doorTypeStr); } catch (NumberFormatException ignored) {}

            result.put(HamsCmd.FIELD_NM.EQ_ID.name(),          eqId);
            result.put(HamsCmd.FIELD_NM.RCV_DATE.name(),        rcvDate);
            result.put(HamsCmd.FIELD_NM.ORDERER_TYPE.name(),    ordererType);
            result.put(HamsCmd.FIELD_NM.USER_ID.name(),         userId);
            result.put(HamsCmd.FIELD_NM.ORDERER_IP.name(),      ordererIp);
            result.put(HamsCmd.FIELD_NM.EVENT_DOOR_TYPE.name(), eventDoorType);
            result.put(HamsCmd.FIELD_NM.EVENT_TYPE.name(),      eventType);

            // doorType 에 따라 FRONT/BACK 키를 동적으로 결정
            if (doorTypeNum == HamsCmd.DOOR_TYPE.front.num) {
                result.put(HamsCmd.FIELD_NM.FRONT_DOOR_S.name(),      doorState);
                result.put(HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name(), doorLockState);
            } else if (doorTypeNum == HamsCmd.DOOR_TYPE.back.num) {
                result.put(HamsCmd.FIELD_NM.BACK_DOOR_S.name(),      doorState);
                result.put(HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name(), doorLockState);
            }

            // 원본 값도 별도 키로 보존 (HamsDoeService 에서 직접 사용)
            result.put("doorTypeRaw",   doorTypeStr);
            result.put("doorState",     doorState);
            result.put("doorLockState", doorLockState);

        } catch (Exception e) {
            log.error("HamsDOEParse.parseBody 실패: {}", e.getMessage());
        }
        return result;
    }

    private static String safeGet(String[] arr, int idx) {
        return (idx < arr.length) ? arr[idx].trim() : "";
    }
}
