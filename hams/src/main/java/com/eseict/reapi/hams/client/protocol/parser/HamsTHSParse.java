package com.eseict.reapi.hams.client.protocol.parser;

import com.eseict.reapi.hams.client.protocol.HamsCmd;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * THS 응답 파서 (온·습도 조회).
 * ACK+THS 응답에서 온도·습도 값 추출.
 *
 * 응답 형식:
 * $PGL,SV,{id},ATHS,{seq},{v},2,0,{temp},{humi}*{CRC}\r\n
 */
@Slf4j
public class HamsTHSParse implements HamsParsePolicy {

    private final String message;
    private final Map<String, String> headerMap;

    public HamsTHSParse(String message, Map<String, String> headerMap) {
        this.message = message;
        this.headerMap = headerMap;
    }

    @Override
    public Map<String, String> parseBody() {
        try {
            String cmd = headerMap.get("cmd");
            String[] parts = message.split(",");

            String expectedCmd = HamsCmd.CHAR.ACK.word
                + HamsCmd.TARGET.TEMPERATURE_AND_HUMI.target
                + HamsCmd.CHAR.STATUS.word;

            if (!expectedCmd.equals(cmd)) return null;

            String result = parts[7];
            if (!String.valueOf(HamsCmd.RESULT_CODE.SUCCESS.num).equals(result)) return null;

            return Map.of(
                HamsCmd.FIELD_NM.TEMP.name(), parts[8],
                HamsCmd.FIELD_NM.HUMI.name(), parts[9].split("\\*")[0]
            );
        } catch (Exception e) {
            log.error("HamsTHSParse error: {}", e.getMessage());
            return null;
        }
    }
}
