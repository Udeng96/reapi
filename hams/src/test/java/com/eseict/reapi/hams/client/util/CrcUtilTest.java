package com.eseict.reapi.hams.client.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CrcUtil — CRC16-CCITT 계산 검증")
class CrcUtilTest {

    @Test
    @DisplayName("빈 바이트 배열은 FFFF 를 반환한다 (초기값 그대로)")
    void emptyBytes_returnsInitialCrc() {
        // CRC16-CCITT 초기값 0xFFFF, 입력이 없으면 XOR 없이 그대로 반환
        String result = CrcUtil.crc16Ccitt(new byte[]{});
        assertThat(result).isEqualTo("FFFF");
    }

    @Test
    @DisplayName("단일 바이트 0x00 은 올바른 CRC 를 반환한다")
    void singleZeroByte_returnsExpectedCrc() {
        String result = CrcUtil.crc16Ccitt(new byte[]{0x00});
        assertThat(result).hasSize(4);
        assertThat(result).matches("[0-9A-F]{4}");
    }

    @Test
    @DisplayName("동일 입력에 대해 항상 동일한 결과를 반환한다 (결정론적)")
    void sameInput_returnsSameOutput() {
        byte[] input = "PGL,CL,000000001,CON,000,1,2,admin,password".getBytes();
        String first  = CrcUtil.crc16Ccitt(input);
        String second = CrcUtil.crc16Ccitt(input);
        assertThat(first).isEqualTo(second);
    }

    @Test
    @DisplayName("결과는 항상 4자리 대문자 16진수 문자열이다")
    void result_isAlways4UpperHexDigits() {
        String[] inputs = {"A", "hello", "PGL,CL,000000001,CON,000,1,2"};
        for (String input : inputs) {
            String crc = CrcUtil.crc16Ccitt(input.getBytes());
            assertThat(crc)
                .as("입력 '%s' 의 CRC 는 4자리 대문자 16진수여야 한다", input)
                .hasSize(4)
                .matches("[0-9A-F]{4}");
        }
    }

    @Test
    @DisplayName("입력이 다르면 CRC 값도 달라야 한다 (충돌 없음 — 기본 검증)")
    void differentInputs_produceDifferentCrc() {
        String crc1 = CrcUtil.crc16Ccitt("ABC".getBytes());
        String crc2 = CrcUtil.crc16Ccitt("ABD".getBytes());
        assertThat(crc1).isNotEqualTo(crc2);
    }

    @Test
    @DisplayName("알려진 CRC16-CCITT 벡터: '123456789' → 29B1")
    void knownVector_123456789() {
        // CRC-16/CCITT-FALSE (초기값 0xFFFF, 폴리 0x1021, 반전 없음) 표준 벡터
        byte[] input = "123456789".getBytes();
        String result = CrcUtil.crc16Ccitt(input);
        assertThat(result).isEqualTo("29B1");
    }

    @Test
    @DisplayName("HAMS 헤더 문자열에 대해 CRC 를 계산하면 4자리 16진수를 반환한다")
    void hamsHeaderString_returns4HexDigitCrc() {
        // '$' 제외한 실제 CRC 대상 문자열
        String crcTarget = "PGL,CL,000000001,CON,000,1,2,admin,password";
        String crc = CrcUtil.crc16Ccitt(crcTarget.getBytes());
        assertThat(crc).hasSize(4).matches("[0-9A-F]{4}");
    }
}
