package com.eseict.reapi.hams.client.protocol.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * HAMS 응답 메시지 파서 인터페이스.
 * 레거시 HamsParsePolicy.kt 이식 (Java 21).
 */
public interface HamsParsePolicy {

    /**
     * 헤더 파싱 — 응답 전문에서 공통 헤더 맵 추출
     * 형식: $PGL,SV,{targetId},{cmd},{seq},{v},{cnt},...
     */
    static Map<String, String> parseHeader(String message) {
        String[] parts = message.split(",");
        Map<String, String> map = new HashMap<>();
        map.put("SID",         safeGet(parts, 0));
        map.put("SERVER",      safeGet(parts, 1));
        map.put("targetId",    safeGet(parts, 2));
        map.put("cmd",         safeGet(parts, 3));
        map.put("seq",         safeGet(parts, 4));
        map.put("packetV",     safeGet(parts, 5));
        map.put("bodyFiledCnt", safeGet(parts, 6));
        return map;
    }

    /**
     * 바디 파싱 — 명령어 타입별 구현체에서 정의
     */
    Map<String, String> parseBody();

    private static String safeGet(String[] arr, int idx) {
        return idx < arr.length ? arr[idx] : "";
    }
}
