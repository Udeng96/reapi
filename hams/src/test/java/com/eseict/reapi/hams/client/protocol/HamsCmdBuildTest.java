package com.eseict.reapi.hams.client.protocol;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("HamsCmd — 메시지 빌드 검증")
class HamsCmdBuildTest {

    // ──────────────────────────────────────────────────────────
    // targetId 패딩 검증
    // ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("targetId 패딩")
    class TargetIdPaddingTest {

        @Test
        @DisplayName("CON 생성자(isNoTSToC=false) 는 targetId 를 9자리로 0패딩한다")
        void conConstructor_padsTargetIdTo9Digits() {
            HamsCmd cmd = new HamsCmd("1");
            assertThat(cmd.getTargetId()).isEqualTo("000000001");
        }

        @Test
        @DisplayName("이미 9자리인 targetId 는 변환 없이 그대로 사용된다")
        void alreadyNineDigits_noChange() {
            HamsCmd cmd = new HamsCmd("123456789");
            assertThat(cmd.getTargetId()).isEqualTo("123456789");
        }

        @Test
        @DisplayName("DOS/DOC/THS 생성자(isNoTSToC=true) 는 앞 3자리를 '010' 으로 교체한다")
        void dosConstructor_replacesPrefixWith010() {
            HamsCmd cmd = new HamsCmd(true, "1");
            assertThat(cmd.getTargetId()).isEqualTo("010000001");
        }

        @Test
        @DisplayName("isNoTSToC=true 이고 9자리 id 는 앞 3자리만 010 으로 교체된다")
        void dosConstructor_nineDigitId_replacesPrefixOnly() {
            HamsCmd cmd = new HamsCmd(true, "123456789");
            assertThat(cmd.getTargetId()).isEqualTo("010456789");
        }

        @Test
        @DisplayName("targetId 가 null 이면 getTargetId() 는 null 을 반환한다")
        void nullTargetId_returnsNull() {
            HamsCmd cmd = new HamsCmd(false, null);
            assertThat(cmd.getTargetId()).isNull();
        }

        @Test
        @DisplayName("targetId 가 null 이면 setHeader 호출 시 NullPointerException 이 발생한다")
        void nullTargetId_setHeader_throwsNullPointerException() {
            HamsCmd cmd = new HamsCmd(false, null);
            assertThatThrownBy(() -> cmd.setHeader(2, HamsCmd.COMMAND.CON, "000"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("targetId is null");
        }
    }

    // ──────────────────────────────────────────────────────────
    // CON 메시지 빌드 검증
    // ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("CON 메시지 빌드")
    class ConMessageBuildTest {

        @Test
        @DisplayName("CON 메시지는 '$PGL,CL,...' 로 시작하고 '*{CRC4}\\r\\n' 으로 끝난다")
        void conMessage_hasCorrectFormat() {
            HamsCmd cmd = new HamsCmd("1");
            byte[] msg = cmd.setHeader(2, HamsCmd.COMMAND.CON, "000")
                            .setBody(List.of("admin", "encPw"))
                            .setTail()
                            .makeMessage();

            assertThat(msg).isNotNull();
            String msgStr = new String(msg, StandardCharsets.UTF_8);
            assertThat(msgStr).startsWith("$PGL,CL,");
            assertThat(msgStr).endsWith("\r\n");
            assertThat(msgStr).contains("*");

            // '*' 뒤 CRC 가 4자리 대문자 16진수인지 확인
            String afterStar = msgStr.substring(msgStr.indexOf('*') + 1).trim();
            assertThat(afterStar).hasSize(4).matches("[0-9A-F]{4}");
        }

        @Test
        @DisplayName("CON 메시지 헤더에 targetId, 명령어, seq, 바디 개수가 포함된다")
        void conMessage_containsExpectedHeaderFields() {
            HamsCmd cmd = new HamsCmd("1");
            byte[] msg = cmd.setHeader(2, HamsCmd.COMMAND.CON, "000")
                            .setBody(List.of("admin", "encPw"))
                            .setTail()
                            .makeMessage();

            String msgStr = new String(msg, StandardCharsets.UTF_8);
            assertThat(msgStr).contains("000000001");  // targetId 9자리 패딩
            assertThat(msgStr).contains("CON");
            assertThat(msgStr).contains("000");         // seq
            assertThat(msgStr).contains(",2,");         // bodyItemSize
            assertThat(msgStr).contains("admin");
            assertThat(msgStr).contains("encPw");
        }

        @Test
        @DisplayName("CON 헤더는 바디 카운트 뒤에 ',' 가 포함된다 (appendTrailingComma=true)")
        void conHeader_appendsTrailingCommaAfterBodyCount() {
            HamsCmd cmd = new HamsCmd("1");
            byte[] msg = cmd.setHeader(2, HamsCmd.COMMAND.CON, "000")
                            .setBody(List.of("admin", "encPw"))
                            .setTail()
                            .makeMessage();

            String msgStr = new String(msg, StandardCharsets.UTF_8);
            // 형식: ...,CON,000,1,2,admin,...
            assertThat(msgStr).contains(",2,admin");
        }

        @Test
        @DisplayName("header 또는 tail 이 null 이면 makeMessage() 는 null 을 반환한다")
        void makeMessage_withoutSetTail_returnsNull() {
            HamsCmd cmd = new HamsCmd("1");
            cmd.setHeader(2, HamsCmd.COMMAND.CON, "000")
               .setBody(List.of("admin", "encPw"));
            // setTail() 호출 안 함 → tail == null
            byte[] msg = cmd.makeMessage();
            assertThat(msg).isNull();
        }
    }

    // ──────────────────────────────────────────────────────────
    // DOS 메시지 빌드 검증
    // ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("DOS 메시지 빌드")
    class DosMessageBuildTest {

        @Test
        @DisplayName("DOS 메시지는 'DOS' 명령어와 seq '500' 을 포함한다")
        void dosMessage_containsDosCommandAndSeq() {
            HamsCmd cmd = new HamsCmd(true, "1");
            byte[] msg = cmd.setHeader(1, "DOS", "500")
                            .setBody(List.of("0"))
                            .setTail()
                            .makeMessage();

            assertThat(msg).isNotNull();
            String msgStr = new String(msg, StandardCharsets.UTF_8);
            assertThat(msgStr).contains("DOS");
            assertThat(msgStr).contains("500");
        }

        @Test
        @DisplayName("DOS 메시지의 targetId 앞 3자리는 '010' 이다")
        void dosMessage_targetIdPrefixIs010() {
            HamsCmd cmd = new HamsCmd(true, "1");
            byte[] msg = cmd.setHeader(1, "DOS", "500")
                            .setBody(List.of("0"))
                            .setTail()
                            .makeMessage();

            String msgStr = new String(msg, StandardCharsets.UTF_8);
            assertThat(msgStr).contains("010000001");
        }

        @Test
        @DisplayName("DOS 헤더는 바디 카운트 뒤에 ',' 가 포함된다")
        void dosHeader_appendsTrailingCommaAfterBodyCount() {
            HamsCmd cmd = new HamsCmd(true, "1");
            byte[] msg = cmd.setHeader(1, "DOS", "500")
                            .setBody(List.of("0"))
                            .setTail()
                            .makeMessage();

            String msgStr = new String(msg, StandardCharsets.UTF_8);
            assertThat(msgStr).contains(",1,0");
        }
    }

    // ──────────────────────────────────────────────────────────
    // THS 메시지 빌드 검증 (헤더 끝 ',' 생략)
    // ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("THS 메시지 빌드")
    class ThsMessageBuildTest {

        @Test
        @DisplayName("THS 명령어 헤더는 바디 카운트 뒤 ',' 를 생략한다")
        void thsHeader_omitsTrailingCommaAfterBodyCount() {
            HamsCmd cmd = new HamsCmd(true, "1");
            byte[] msg = cmd.setHeader(0, "THS", "501")
                            .setTail()
                            .makeMessage();

            assertThat(msg).isNotNull();
            String msgStr = new String(msg, StandardCharsets.UTF_8);
            // THS 헤더: ...,THS,501,1,0*{CRC}\r\n  → ',0,' 가 아니라 ',0*' 형태
            // ',' 생략이므로 바디 카운트(0) 다음에 '*' 가 바로 온다
            assertThat(msgStr).contains(",THS,");
            assertThat(msgStr).contains(",501,");
        }

        @Test
        @DisplayName("THS 명령어일 때 appendTrailingComma=false 이므로 헤더 끝이 숫자로 끝난다")
        void thsHeader_endsWithBodyCount_notComma() {
            HamsCmd cmd = new HamsCmd(true, "1");
            // body 없이 setTail 을 바로 호출하는 경우
            cmd.setHeader(0, "THS", "501").setTail();
            byte[] msg = cmd.makeMessage();

            String msgStr = new String(msg, StandardCharsets.UTF_8);
            // THS=TH+S, 헤더가 ',1,0*' 형태여야 함 (바디 카운트 바로 뒤 '*')
            // 즉 ',0,' 가 나타나선 안 된다 (ths 전용 경우)
            int thsIdx = msgStr.indexOf("THS");
            // THS 이후 내용 중 바디카운트 뒤 ',' 없이 '*' 가 나타나야 함
            String afterBodyCnt = msgStr.substring(thsIdx);
            assertThat(afterBodyCnt).contains("*");
        }

        @Test
        @DisplayName("다른 명령어(DOS)는 THS 조건에 해당하지 않아 ',' 가 포함된다")
        void nonThsCommand_appendsTrailingComma() {
            HamsCmd cmd = new HamsCmd(true, "1");
            byte[] msg = cmd.setHeader(1, "DOS", "500")
                            .setBody(List.of("0"))
                            .setTail()
                            .makeMessage();

            String msgStr = new String(msg, StandardCharsets.UTF_8);
            // DOS 는 appendTrailingComma=true → ',1,' 포함
            assertThat(msgStr).contains(",1,");
        }
    }

    // ──────────────────────────────────────────────────────────
    // DOC 메시지 빌드 검증
    // ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("DOC 메시지 빌드")
    class DocMessageBuildTest {

        @Test
        @DisplayName("DOC 메시지는 'DOC' 명령어와 seq '502' 를 포함한다")
        void docMessage_containsDocCommandAndSeq() {
            HamsCmd cmd = new HamsCmd(true, "1");
            byte[] msg = cmd.setHeader(3, "DOC", "502")
                            .setBody(List.of("1", "2", "3"))
                            .setTail()
                            .makeMessage();

            assertThat(msg).isNotNull();
            String msgStr = new String(msg, StandardCharsets.UTF_8);
            assertThat(msgStr).contains("DOC");
            assertThat(msgStr).contains("502");
        }
    }

    // ──────────────────────────────────────────────────────────
    // setTail — CRC 계산 검증
    // ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("CRC 계산 (setTail)")
    class TailCrcTest {

        @Test
        @DisplayName("동일한 헤더/바디로 빌드한 두 메시지는 동일한 CRC 를 가진다")
        void sameContent_produceSameCrc() {
            HamsCmd cmd1 = new HamsCmd("1");
            byte[] msg1 = cmd1.setHeader(2, HamsCmd.COMMAND.CON, "000")
                               .setBody(List.of("admin", "pw"))
                               .setTail()
                               .makeMessage();

            HamsCmd cmd2 = new HamsCmd("1");
            byte[] msg2 = cmd2.setHeader(2, HamsCmd.COMMAND.CON, "000")
                               .setBody(List.of("admin", "pw"))
                               .setTail()
                               .makeMessage();

            String crc1 = extractCrc(new String(msg1, StandardCharsets.UTF_8));
            String crc2 = extractCrc(new String(msg2, StandardCharsets.UTF_8));
            assertThat(crc1).isEqualTo(crc2);
        }

        @Test
        @DisplayName("바디가 다르면 CRC 도 달라야 한다")
        void differentBody_producesDifferentCrc() {
            HamsCmd cmd1 = new HamsCmd("1");
            byte[] msg1 = cmd1.setHeader(2, HamsCmd.COMMAND.CON, "000")
                               .setBody(List.of("admin", "pw1"))
                               .setTail()
                               .makeMessage();

            HamsCmd cmd2 = new HamsCmd("1");
            byte[] msg2 = cmd2.setHeader(2, HamsCmd.COMMAND.CON, "000")
                               .setBody(List.of("admin", "pw2"))
                               .setTail()
                               .makeMessage();

            String crc1 = extractCrc(new String(msg1, StandardCharsets.UTF_8));
            String crc2 = extractCrc(new String(msg2, StandardCharsets.UTF_8));
            assertThat(crc1).isNotEqualTo(crc2);
        }

        private String extractCrc(String msg) {
            return msg.substring(msg.indexOf('*') + 1).trim();
        }
    }
}
