package com.eseict.reapi.hams.client.protocol;

import com.eseict.reapi.hams.client.protocol.parser.*;
import com.eseict.reapi.hams.client.util.CrcUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * HAMS 프로토콜 메시지 빌더/파서.
 * 레거시 HamsCmd.kt 이식 (Java 21).
 *
 * 전문 형식: $PGL,CL,{targetId},{cmd},{seq},{v},{cnt},{body}*{CRC4}\r\n
 */
@Slf4j
public class HamsCmd {

    private static final String NEW_LINE = "\r\n";

    private Integer packetVersion = 1;
    private String targetId;

    private byte[] header;
    private byte[] body;
    private byte[] tail;

    private HamsParsePolicy parse;

    public enum COMMAND {
        CON, SET, ERR, RBT
    }

    public enum CHAR {
        START_TAIL("*"),
        START_MESSAGE("$PGL"),
        END_MESSAGE("<CR><LF>"),
        ACK("A"),
        STATUS("S"),
        CONTROL("C"),
        CANCEL("X"),
        EVENT("E"),
        SPLIT(","),
        CLIENT("CL"),
        SERVER("SV");

        public final String word;
        CHAR(String word) { this.word = word; }
    }

    public enum TARGET {
        DOOR_LOCK("DO"),
        POWER("PO"),
        DC("DC"),
        UPPER_TEMPERATURE("UT"),
        TEMPERATURE_AND_HUMI("TH");

        public final String target;
        TARGET(String target) { this.target = target; }
    }

    public enum RESULT_CODE {
        SUCCESS(0),
        FAIL_CON(-1), FAIL_ERR(1), NO_DATA_FW(2), FAIL_SET(3),
        ALREADY_CONTROL(4), CANCEL_SET(5), FAIL_IP_SET(31),
        FAIL_RBT_NETWORK(32), WRONG_MSG(51), WRONG_DATA(52), NOT_CMD(53),
        NOT_MATCH_FIELD_CNT(54), EMPTY_FIELD(55), CRC_ERROR_BUFFER(56), CRC_NOT_MATCH(57),
        FAIL_LOGIN_ID(81), FAIL_LOGIN_PW(82), FAIL_LOGIN_NONE(83),
        NONE_CMD(97), NONE_EQ(98), NONE_ERR(99), FAIL_CON_DB(100);

        public final int num;
        RESULT_CODE(int num) { this.num = num; }
    }

    public enum DOOR_TYPE {
        front(1), back(2);

        public final int num;
        DOOR_TYPE(int num) { this.num = num; }
    }

    public enum FIELD_NM {
        LOGIN_RESULT, CRC16, ETX,
        FRONT_DOOR_S, FRONT_DOOR_LOCK_S,
        BACK_DOOR_S, BACK_DOOR_LOCK_S,
        TEMP, HUMI,
        ORDERER_TYPE, USER_ID, ORDERER_IP,
        EVENT_TYPE, EVENT_DOOR_TYPE, RCV_DATE, EQ_ID
    }

    // ──────────────────────────────────────────────────────────
    // 생성자 — 메시지 빌드용 (targetId 지정)
    // ──────────────────────────────────────────────────────────

    /**
     * @param isNoTSToC true이면 targetId 앞 3자리를 "010"으로 교체 (클라이언트→서버 방향)
     */
    public HamsCmd(Integer packetVersion, boolean isNoTSToC, String targetId) {
        this.packetVersion = packetVersion != null ? packetVersion : 1;
        this.targetId = padTargetId(targetId, isNoTSToC);
    }

    public HamsCmd(boolean isNoTSToC, String targetId) {
        this(1, isNoTSToC, targetId);
    }

    /** CON 명령용 — isNoTSToC=false */
    public HamsCmd(String targetId) {
        this(1, false, targetId);
    }

    // ──────────────────────────────────────────────────────────
    // 생성자 — 응답 메시지 파싱용
    // ──────────────────────────────────────────────────────────

    public HamsCmd(String responseMsg, boolean parseMode) {
        try {
            selectParserPolicy(responseMsg);
        } catch (Exception e) {
            log.error("FAIL TO CREATE HAMS CMD: {}", e.getMessage());
        }
    }

    // ──────────────────────────────────────────────────────────
    // targetId 패딩
    // ──────────────────────────────────────────────────────────

    private String padTargetId(String rawId, boolean isNoTSToC) {
        if (rawId == null) return null;
        String id = rawId;
        // 9자리로 좌측 0패딩
        while (id.length() < 9) {
            id = "0" + id;
        }
        // 클라이언트→서버 방향: 앞 3자리를 "010"으로 교체
        if (isNoTSToC) {
            id = "010" + id.substring(3);
        }
        return id;
    }

    // ──────────────────────────────────────────────────────────
    // 헤더 빌드 (COMMAND enum 버전)
    // ──────────────────────────────────────────────────────────

    public HamsCmd setHeader(int bodyItemSize, COMMAND command, String seq) {
        return setHeaderInternal(bodyItemSize, command.name(), seq, true);
    }

    public HamsCmd setHeader(int bodyItemSize, COMMAND command) {
        return setHeader(bodyItemSize, command, "000");
    }

    // ──────────────────────────────────────────────────────────
    // 헤더 빌드 (String 명령어 버전 — DOS/DOC/THS 등)
    // ──────────────────────────────────────────────────────────

    public HamsCmd setHeader(int bodyItemSize, String command, String seq) {
        // THS 명령일 때만 바디 카운트 뒤 ',' 생략 (레거시 동일)
        boolean appendTrailingComma = !command.equals(
            TARGET.TEMPERATURE_AND_HUMI.target + CHAR.STATUS.word
        );
        return setHeaderInternal(bodyItemSize, command, seq, appendTrailingComma);
    }

    public HamsCmd setHeader(int bodyItemSize, String command) {
        return setHeader(bodyItemSize, command, "000");
    }

    private HamsCmd setHeaderInternal(int bodyItemSize, String command, String seq, boolean appendTrailingComma) {
        if (targetId == null || targetId.isEmpty()) {
            throw new NullPointerException("targetId is null");
        }

        StringBuilder sb = new StringBuilder(CHAR.START_MESSAGE.word)
            .append(CHAR.SPLIT.word)
            .append(CHAR.CLIENT.word)
            .append(CHAR.SPLIT.word)
            .append(targetId)
            .append(CHAR.SPLIT.word)
            .append(command)
            .append(CHAR.SPLIT.word)
            .append(seq)
            .append(CHAR.SPLIT.word)
            .append(packetVersion)
            .append(CHAR.SPLIT.word)
            .append(bodyItemSize);

        if (appendTrailingComma) {
            sb.append(CHAR.SPLIT.word);
        }

        this.header = sb.toString().getBytes(StandardCharsets.UTF_8);
        return this;
    }

    // ──────────────────────────────────────────────────────────
    // 바디/테일 빌드
    // ──────────────────────────────────────────────────────────

    public HamsCmd setBody(java.util.List<String> values) {
        this.body = String.join(",", values).getBytes(StandardCharsets.UTF_8);
        return this;
    }

    public HamsCmd setTail() {
        String headerStr = new String(this.header, StandardCharsets.UTF_8);
        String bodyStr = this.body != null ? new String(this.body, StandardCharsets.UTF_8) : "";

        // '$' 제외한 부분 + body 를 CRC 계산 대상으로 사용
        // 레거시: tail = headerStr.substring(1) 에 bodyStr 삽입
        // 실제로는 headerStr[1..] + bodyStr 이 CRC 대상
        String crcTarget = headerStr.substring(1) + bodyStr;
        String crc16 = CrcUtil.crc16Ccitt(crcTarget.getBytes(StandardCharsets.UTF_8));

        // 이미 4자리이지만 혹시 모르니 패딩
        while (crc16.length() < 4) {
            crc16 = "0" + crc16;
        }

        this.tail = (CHAR.START_TAIL.word + crc16 + NEW_LINE).getBytes(StandardCharsets.UTF_8);
        return this;
    }

    // ──────────────────────────────────────────────────────────
    // 최종 전문 생성
    // ──────────────────────────────────────────────────────────

    public byte[] makeMessage() {
        try {
            if (header == null || tail == null) return null;
            byte[] b = body != null ? body : new byte[0];
            byte[] result = new byte[header.length + b.length + tail.length];
            System.arraycopy(header, 0, result, 0, header.length);
            System.arraycopy(b, 0, result, header.length, b.length);
            System.arraycopy(tail, 0, result, header.length + b.length, tail.length);
            log.info("make message: {}", new String(result, StandardCharsets.UTF_8).trim());
            return result;
        } catch (Exception e) {
            log.error("makeMessage failed: {}", e.getMessage());
            return null;
        }
    }

    // ──────────────────────────────────────────────────────────
    // 파서 정책 선택 (응답 메시지 분기)
    // ──────────────────────────────────────────────────────────

    public void selectParserPolicy(String message) {
        log.info("HAMS ACK MESSAGE: {}", message);
        java.util.Map<String, String> headerMap = HamsParsePolicy.parseHeader(message);
        String cmd = headerMap.get("cmd");

        if (cmd == null) {
            log.warn("HAMS ACK: cmd is null in header");
            return;
        }

        if (cmd.equals(CHAR.ACK.word + COMMAND.CON.name())) {
            this.parse = new HamsCONParse(message, headerMap);
        } else if (cmd.equals(CHAR.ACK.word + TARGET.DOOR_LOCK.target + CHAR.STATUS.word)) {
            this.parse = new HamsDOSParse(message, headerMap);
        } else if (cmd.equals(CHAR.ACK.word + TARGET.DOOR_LOCK.target + CHAR.CONTROL.word)) {
            this.parse = new HamsDOCParse(message, headerMap);
        } else if (cmd.equals(TARGET.DOOR_LOCK.target + CHAR.EVENT.word)) {
            this.parse = new HamsDOEParse(message, headerMap);
        } else if (cmd.equals(CHAR.ACK.word + TARGET.TEMPERATURE_AND_HUMI.target + CHAR.STATUS.word)) {
            this.parse = new HamsTHSParse(message, headerMap);
        } else if (cmd.equals(COMMAND.ERR.name())) {
            log.warn("HAMS ERR cmd received: {}", message);
        } else {
            log.debug("HAMS unknown cmd: {}", cmd);
        }
    }

    // ──────────────────────────────────────────────────────────
    // Getter
    // ──────────────────────────────────────────────────────────

    public HamsParsePolicy getParse() { return parse; }
    public String getTargetId() { return targetId; }
}
