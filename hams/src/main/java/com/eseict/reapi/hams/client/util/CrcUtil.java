package com.eseict.reapi.hams.client.util;

/**
 * CRC16-CCITT 유틸.
 * 레거시 CrcUtil.kt 이식 (Java 21).
 * init: 0xFFFF, polynomial: 0x1021
 */
public class CrcUtil {

    private CrcUtil() {}

    /**
     * @return 4자리 hex 대문자 (부족하면 앞에 0 패딩)
     */
    public static String crc16Ccitt(byte[] bytes) {
        int crc = 0xFFFF;
        int polynomial = 0x1021;

        for (byte b : bytes) {
            for (int i = 0; i <= 7; i++) {
                boolean bit = ((b >> (7 - i)) & 1) == 1;
                boolean c15 = ((crc >> 15) & 1) == 1;
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
            }
        }
        crc &= 0xFFFF;
        String hex = Integer.toHexString(crc).toUpperCase();
        while (hex.length() < 4) {
            hex = "0" + hex;
        }
        return hex;
    }
}
