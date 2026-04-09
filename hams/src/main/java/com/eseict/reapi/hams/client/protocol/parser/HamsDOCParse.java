package com.eseict.reapi.hams.client.protocol.parser;

import com.eseict.reapi.hams.client.protocol.HamsCmd;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * DOC 응답 파서 (도어 제어).
 * ACK+DOC 응답에서 도어 제어 결과 추출.
 *
 * 응답 형식:
 * $PGL,SV,{id},ADOC,{seq},{v},3,0,{doorType},{doorState},{lockState}*{CRC}\r\n
 */
@Slf4j
public class HamsDOCParse implements HamsParsePolicy {

    private final String message;
    private final Map<String, String> headerMap;

    public HamsDOCParse(String message, Map<String, String> headerMap) {
        this.message = message;
        this.headerMap = headerMap;
    }

    @Override
    public Map<String, String> parseBody() {
        try {
            String cmd = headerMap.get("cmd");
            String[] parts = message.split(",");

            String expectedCmd = HamsCmd.CHAR.ACK.word
                + HamsCmd.TARGET.DOOR_LOCK.target
                + HamsCmd.CHAR.CONTROL.word;

            if (!expectedCmd.equals(cmd)) return null;

            String result = parts[7];
            if (!String.valueOf(HamsCmd.RESULT_CODE.SUCCESS.num).equals(result)) return null;

            int doorType = Integer.parseInt(parts[8]);
            boolean isFront = doorType == HamsCmd.DOOR_TYPE.front.num;

            String doorKey     = isFront ? HamsCmd.FIELD_NM.FRONT_DOOR_S.name()      : HamsCmd.FIELD_NM.BACK_DOOR_S.name();
            String doorLockKey = isFront ? HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name() : HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name();

            return Map.of(
                HamsCmd.TARGET.DOOR_LOCK.name(), parts[8],
                doorKey,     parts[9],
                doorLockKey, parts[10].split("\\*")[0]
            );
        } catch (Exception e) {
            log.error("HamsDOCParse error: {}", e.getMessage());
            return null;
        }
    }
}
