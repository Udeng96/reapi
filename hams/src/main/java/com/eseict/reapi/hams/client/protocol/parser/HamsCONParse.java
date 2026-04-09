package com.eseict.reapi.hams.client.protocol.parser;

import com.eseict.reapi.hams.client.protocol.HamsCmd;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * CON 응답 파서.
 * ACK+CON 응답에서 loginResult(0=성공) 추출.
 */
@Slf4j
public class HamsCONParse implements HamsParsePolicy {

    private final String message;
    private final Map<String, String> headerMap;

    public HamsCONParse(String message, Map<String, String> headerMap) {
        this.message = message;
        this.headerMap = headerMap;
    }

    @Override
    public Map<String, String> parseBody() {
        try {
            String cmd = headerMap.get("cmd");
            String fieldCnt = headerMap.get("bodyFiledCnt");
            String[] parts = message.split(",");

            if ((HamsCmd.CHAR.ACK.word + HamsCmd.COMMAND.CON.name()).equals(cmd)) {
                if ("1".equals(fieldCnt)) {
                    // parts[7] = result code, '*' 뒤 CRC 제거
                    String result = parts[7].split("\\*")[0];
                    return Map.of(HamsCmd.FIELD_NM.LOGIN_RESULT.name(), result);
                }
            }
            return null;
        } catch (Exception e) {
            log.error("HamsCONParse error: {}", e.getMessage());
            return null;
        }
    }
}
