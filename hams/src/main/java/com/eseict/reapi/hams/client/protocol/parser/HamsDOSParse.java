package com.eseict.reapi.hams.client.protocol.parser;

import com.eseict.reapi.hams.client.protocol.HamsCmd;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * DOS 응답 파서 (도어 상태 조회).
 * ACK+DOS 응답에서 앞/뒷문 개폐·잠금 상태 추출.
 *
 * 전체 조회(body=0) 응답 형식:
 * $PGL,SV,{id},ADOS,{seq},{v},5,0,{frontDoor},{frontLock},{backDoor},{backLock}*{CRC}\r\n
 */
@Slf4j
public class HamsDOSParse implements HamsParsePolicy {

    private final String message;
    private final Map<String, String> headerMap;

    public HamsDOSParse(String message, Map<String, String> headerMap) {
        this.message = message;
        this.headerMap = headerMap;
    }

    @Override
    public Map<String, String> parseBody() {
        try {
            String cmd = headerMap.get("cmd");
            String fieldCntStr = headerMap.get("bodyFiledCnt");
            String[] parts = message.split(",");

            String expectedCmd = HamsCmd.CHAR.ACK.word
                + HamsCmd.TARGET.DOOR_LOCK.target
                + HamsCmd.CHAR.STATUS.word;

            if (!expectedCmd.equals(cmd)) return null;

            int fieldCnt = Integer.parseInt(fieldCntStr);

            if (fieldCnt == 4) {
                // 개별 조회 (앞문 or 뒷문)
                String checkTarget = parts[7];
                if ("1".equals(checkTarget)) {
                    return Map.of(
                        HamsCmd.FIELD_NM.FRONT_DOOR_S.name(), parts[8],
                        HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name(), parts[9].split("\\*")[0]
                    );
                } else {
                    return Map.of(
                        HamsCmd.FIELD_NM.BACK_DOOR_S.name(), parts[8],
                        HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name(), parts[9].split("\\*")[0]
                    );
                }
            } else {
                // 전체 조회 (body=0): result(7), ?(8), frontDoor(9), frontLock(10), backDoor(11), backLock(12)
                String result = parts[7];
                if (String.valueOf(HamsCmd.RESULT_CODE.SUCCESS.num).equals(result)) {
                    return Map.of(
                        HamsCmd.FIELD_NM.FRONT_DOOR_S.name(),      parts[9],
                        HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name(), parts[10],
                        HamsCmd.FIELD_NM.BACK_DOOR_S.name(),       parts[11],
                        HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name(),  parts[12].split("\\*")[0]
                    );
                }
                return null;
            }
        } catch (Exception e) {
            log.error("HamsDOSParse error: {}", e.getMessage());
            return null;
        }
    }
}
