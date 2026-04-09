package com.eseict.reapi.hams.client.protocol;

import com.eseict.reapi.hams.client.protocol.parser.HamsDOCParse;
import com.eseict.reapi.hams.client.protocol.parser.HamsParsePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HamsCmd — 응답 메시지 파서 검증")
class HamsCmdParseTest {

    // ──────────────────────────────────────────────────────────
    // CON 응답 파서
    // ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("CON 응답 파서 (HamsCONParse)")
    class ConParseTest {

        @Test
        @DisplayName("로그인 성공 응답(result=0)을 파싱하면 LOGIN_RESULT='0' 을 반환한다")
        void conSuccessResponse_returnsLoginResult0() {
            String response = "$PGL,SV,000000002,ACON,000,1,1,0*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);

            assertThat(cmd.getParse()).isNotNull();
            Map<String, String> body = cmd.getParse().parseBody();

            assertThat(body).isNotNull();
            assertThat(body).containsKey(HamsCmd.FIELD_NM.LOGIN_RESULT.name());
            assertThat(body.get(HamsCmd.FIELD_NM.LOGIN_RESULT.name())).isEqualTo("0");
        }

        @Test
        @DisplayName("CON 응답에서 CRC 부분은 파싱 결과에서 제외된다")
        void conResponse_crcIsStrippedFromResult() {
            String response = "$PGL,SV,000000002,ACON,000,1,1,0*1234\r\n";
            HamsCmd cmd = new HamsCmd(response, true);

            Map<String, String> body = cmd.getParse().parseBody();
            String loginResult = body.get(HamsCmd.FIELD_NM.LOGIN_RESULT.name());

            assertThat(loginResult).doesNotContain("*");
            assertThat(loginResult).isEqualTo("0");
        }

        @Test
        @DisplayName("fieldCnt 가 1 이 아닌 CON 응답은 parseBody() 가 null 을 반환한다")
        void conResponse_wrongFieldCnt_returnsNull() {
            // fieldCnt=2 는 지원하지 않음
            String response = "$PGL,SV,000000002,ACON,000,1,2,0,extra*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);

            Map<String, String> body = cmd.getParse().parseBody();
            assertThat(body).isNull();
        }
    }

    // ──────────────────────────────────────────────────────────
    // DOS 응답 파서
    // ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("DOS 응답 파서 (HamsDOSParse)")
    class DosParseTest {

        @Test
        @DisplayName("전체 조회 성공(fieldCnt=5, result=0) 응답을 파싱하면 4개 도어 상태를 반환한다")
        void dosAllQuerySuccess_returnsFourDoorStates() {
            // fieldCnt=5: result(7), ?(8), frontDoor(9), frontLock(10), backDoor(11), backLock(12)
            String response = "$PGL,SV,010000001,ADOS,500,1,5,0,0,2,2,2,2*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);

            assertThat(cmd.getParse()).isNotNull();
            Map<String, String> body = cmd.getParse().parseBody();

            assertThat(body).isNotNull();
            assertThat(body).containsKey(HamsCmd.FIELD_NM.FRONT_DOOR_S.name());
            assertThat(body).containsKey(HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name());
            assertThat(body).containsKey(HamsCmd.FIELD_NM.BACK_DOOR_S.name());
            assertThat(body).containsKey(HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name());
        }

        @Test
        @DisplayName("전체 조회 성공 응답의 각 도어 상태 값이 올바르다")
        void dosAllQuerySuccess_doorStateValuesAreCorrect() {
            String response = "$PGL,SV,010000001,ADOS,500,1,5,0,0,2,2,2,2*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);
            Map<String, String> body = cmd.getParse().parseBody();

            assertThat(body.get(HamsCmd.FIELD_NM.FRONT_DOOR_S.name())).isEqualTo("2");
            assertThat(body.get(HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name())).isEqualTo("2");
            assertThat(body.get(HamsCmd.FIELD_NM.BACK_DOOR_S.name())).isEqualTo("2");
            assertThat(body.get(HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name())).isEqualTo("2");
        }

        @Test
        @DisplayName("DOS 전체 조회 응답에서 BACK_DOOR_LOCK_S 값의 CRC 부분이 제거된다")
        void dosAllQuerySuccess_crcIsStrippedFromLastField() {
            String response = "$PGL,SV,010000001,ADOS,500,1,5,0,0,2,2,2,3*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);
            Map<String, String> body = cmd.getParse().parseBody();

            String backDoorLockS = body.get(HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name());
            assertThat(backDoorLockS).doesNotContain("*");
            assertThat(backDoorLockS).isEqualTo("3");
        }

        @Test
        @DisplayName("전체 조회 실패(result!=0) 응답은 parseBody() 가 null 을 반환한다")
        void dosAllQueryFail_returnsNull() {
            // result=1 (실패)
            String response = "$PGL,SV,010000001,ADOS,500,1,5,1,0,0,0,0,0*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);
            Map<String, String> body = cmd.getParse().parseBody();
            assertThat(body).isNull();
        }

        @Test
        @DisplayName("앞문 개별 조회(fieldCnt=4, doorType=1) 응답을 파싱하면 앞문 상태를 반환한다")
        void dosFrontDoorIndividualQuery_returnsFrontDoorState() {
            // fieldCnt=4, doorType(7)=1(front), frontDoor(8), frontLock(9)
            String response = "$PGL,SV,010000001,ADOS,500,1,4,1,1,1*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);
            Map<String, String> body = cmd.getParse().parseBody();

            assertThat(body).isNotNull();
            assertThat(body).containsKey(HamsCmd.FIELD_NM.FRONT_DOOR_S.name());
            assertThat(body).containsKey(HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name());
            assertThat(body.get(HamsCmd.FIELD_NM.FRONT_DOOR_S.name())).isEqualTo("1");
        }

        @Test
        @DisplayName("뒷문 개별 조회(fieldCnt=4, doorType!=1) 응답을 파싱하면 뒷문 상태를 반환한다")
        void dosBackDoorIndividualQuery_returnsBackDoorState() {
            // fieldCnt=4, doorType(7)=2(back), backDoor(8), backLock(9)
            String response = "$PGL,SV,010000001,ADOS,500,1,4,2,1,1*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);
            Map<String, String> body = cmd.getParse().parseBody();

            assertThat(body).isNotNull();
            assertThat(body).containsKey(HamsCmd.FIELD_NM.BACK_DOOR_S.name());
            assertThat(body).containsKey(HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name());
        }

        @Test
        @DisplayName("cmd 가 ADOS 가 아닌 응답은 DOS 파서가 선택되지 않으므로 DOS 필드가 반환되지 않는다")
        void dosResponse_wrongCmd_doesNotReturnDosFields() {
            // ADOC cmd 로 오면 DOCParse 가 선택됨 → DOS 전용 필드(FRONT/BACK_DOOR_S 4개 모두)는 없음
            String response = "$PGL,SV,010000001,ADOC,500,1,5,0,0,2,2,2,2*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);

            if (cmd.getParse() != null) {
                Map<String, String> body = cmd.getParse().parseBody();
                // DOCParse 결과이므로 FRONT_DOOR_LOCK_S 단독 키가 있거나 null 이다
                // DOS 형식의 4개 키가 모두 존재하지는 않아야 한다
                assertThat(body == null
                    || !(body.containsKey(HamsCmd.FIELD_NM.FRONT_DOOR_S.name())
                         && body.containsKey(HamsCmd.FIELD_NM.BACK_DOOR_S.name())
                         && body.containsKey(HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name())
                         && body.containsKey(HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name()))
                ).isTrue();
            }
        }
    }

    // ──────────────────────────────────────────────────────────
    // THS 응답 파서
    // ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("THS 응답 파서 (HamsTHSParse)")
    class ThsParseTest {

        @Test
        @DisplayName("온·습도 조회 성공 응답을 파싱하면 TEMP 와 HUMI 를 반환한다")
        void thsSuccessResponse_returnsTempAndHumi() {
            String response = "$PGL,SV,010000001,ATHS,501,1,2,0,25,60*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);

            assertThat(cmd.getParse()).isNotNull();
            Map<String, String> body = cmd.getParse().parseBody();

            assertThat(body).isNotNull();
            assertThat(body).containsKey(HamsCmd.FIELD_NM.TEMP.name());
            assertThat(body).containsKey(HamsCmd.FIELD_NM.HUMI.name());
        }

        @Test
        @DisplayName("온·습도 응답의 TEMP 와 HUMI 값이 올바르다")
        void thsSuccessResponse_tempAndHumiValuesAreCorrect() {
            String response = "$PGL,SV,010000001,ATHS,501,1,2,0,25,60*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);
            Map<String, String> body = cmd.getParse().parseBody();

            assertThat(body.get(HamsCmd.FIELD_NM.TEMP.name())).isEqualTo("25");
            assertThat(body.get(HamsCmd.FIELD_NM.HUMI.name())).isEqualTo("60");
        }

        @Test
        @DisplayName("THS 응답에서 HUMI 값의 CRC 부분이 제거된다")
        void thsSuccessResponse_crcIsStrippedFromHumi() {
            String response = "$PGL,SV,010000001,ATHS,501,1,2,0,25,60*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);
            Map<String, String> body = cmd.getParse().parseBody();

            assertThat(body.get(HamsCmd.FIELD_NM.HUMI.name())).doesNotContain("*");
            assertThat(body.get(HamsCmd.FIELD_NM.HUMI.name())).isEqualTo("60");
        }

        @Test
        @DisplayName("THS 조회 실패(result!=0) 응답은 parseBody() 가 null 을 반환한다")
        void thsFailResponse_returnsNull() {
            // result=1 (실패)
            String response = "$PGL,SV,010000001,ATHS,501,1,2,1,0,0*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);
            Map<String, String> body = cmd.getParse().parseBody();
            assertThat(body).isNull();
        }

        @Test
        @DisplayName("cmd 가 ATHS 가 아닌 응답은 THS 파서가 선택되지 않는다")
        void thsResponse_wrongCmd_parserNotSelected() {
            // ADOS cmd 로 오면 DOSParse 가 선택됨
            String response = "$PGL,SV,010000001,ADOS,501,1,2,0,25,60*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);

            if (cmd.getParse() != null) {
                Map<String, String> body = cmd.getParse().parseBody();
                // DOS 파서의 결과이므로 TEMP 키는 없다
                assertThat(body == null || !body.containsKey(HamsCmd.FIELD_NM.TEMP.name())).isTrue();
            }
        }
    }

    // ──────────────────────────────────────────────────────────
    // DOC 응답 파서
    // ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("DOC 응답 파서 (HamsDOCParse)")
    class DocParseTest {

        @Test
        @DisplayName("앞문 제어 성공(doorType=1) 응답을 파싱하면 DOOR_LOCK, FRONT_DOOR_S, FRONT_DOOR_LOCK_S 를 반환한다")
        void docFrontDoorSuccess_returnsFrontDoorFields() {
            // parts: [0]$PGL [1]SV [2]010000001 [3]ADOC [4]502 [5]1 [6]3 [7]0 [8]1 [9]2 [10]3*ABCD
            String response = "$PGL,SV,010000001,ADOC,502,1,3,0,1,2,3*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);

            assertThat(cmd.getParse()).isNotNull();
            Map<String, String> body = cmd.getParse().parseBody();

            assertThat(body).isNotNull();
            assertThat(body).containsKey(HamsCmd.TARGET.DOOR_LOCK.name());   // "DOOR_LOCK"
            assertThat(body).containsKey(HamsCmd.FIELD_NM.FRONT_DOOR_S.name());
            assertThat(body).containsKey(HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name());
        }

        @Test
        @DisplayName("앞문 제어 성공 응답의 각 필드 값이 올바르다")
        void docFrontDoorSuccess_fieldValuesAreCorrect() {
            String response = "$PGL,SV,010000001,ADOC,502,1,3,0,1,2,3*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);
            Map<String, String> body = cmd.getParse().parseBody();

            assertThat(body.get(HamsCmd.TARGET.DOOR_LOCK.name())).isEqualTo("1");     // doorType
            assertThat(body.get(HamsCmd.FIELD_NM.FRONT_DOOR_S.name())).isEqualTo("2");
            assertThat(body.get(HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name())).isEqualTo("3");
        }

        @Test
        @DisplayName("뒷문 제어 성공(doorType=2) 응답을 파싱하면 DOOR_LOCK, BACK_DOOR_S, BACK_DOOR_LOCK_S 를 반환한다")
        void docBackDoorSuccess_returnsBackDoorFields() {
            String response = "$PGL,SV,010000001,ADOC,502,1,3,0,2,1,0*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);
            Map<String, String> body = cmd.getParse().parseBody();

            assertThat(body).isNotNull();
            assertThat(body).containsKey(HamsCmd.TARGET.DOOR_LOCK.name());
            assertThat(body).containsKey(HamsCmd.FIELD_NM.BACK_DOOR_S.name());
            assertThat(body).containsKey(HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name());
        }

        @Test
        @DisplayName("뒷문 제어 성공 응답의 각 필드 값이 올바르다")
        void docBackDoorSuccess_fieldValuesAreCorrect() {
            String response = "$PGL,SV,010000001,ADOC,502,1,3,0,2,1,0*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);
            Map<String, String> body = cmd.getParse().parseBody();

            assertThat(body.get(HamsCmd.TARGET.DOOR_LOCK.name())).isEqualTo("2");    // doorType=back
            assertThat(body.get(HamsCmd.FIELD_NM.BACK_DOOR_S.name())).isEqualTo("1");
            assertThat(body.get(HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name())).isEqualTo("0");
        }

        @Test
        @DisplayName("DOC 응답에서 마지막 필드의 CRC 부분이 제거된다")
        void docSuccess_crcIsStrippedFromLastField() {
            String response = "$PGL,SV,010000001,ADOC,502,1,3,0,1,2,3*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);
            Map<String, String> body = cmd.getParse().parseBody();

            assertThat(body.get(HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name())).doesNotContain("*");
            assertThat(body.get(HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name())).isEqualTo("3");
        }

        @Test
        @DisplayName("DOC 제어 실패(result!=0) 응답은 parseBody() 가 null 을 반환한다")
        void docFailResponse_returnsNull() {
            // result=1 (실패)
            String response = "$PGL,SV,010000001,ADOC,502,1,3,1,1,0,0*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);
            Map<String, String> body = cmd.getParse().parseBody();
            assertThat(body).isNull();
        }

        @Test
        @DisplayName("cmd 가 ADOC 가 아닌 응답을 DOCParse 에 직접 전달하면 parseBody() 가 null 을 반환한다")
        void docResponse_wrongCmd_returnsNull() {
            // HamsDOCParse 에 ADOS cmd 를 담은 메시지를 직접 넘겨 null 반환 경로 확인
            HamsDOCParse docParse = new HamsDOCParse(
                "$PGL,SV,010000001,ADOS,502,1,3,0,1,2,3*ABCD\r\n",
                HamsParsePolicy.parseHeader("$PGL,SV,010000001,ADOS,502,1,3,0,1,2,3*ABCD\r\n")
            );
            assertThat(docParse.parseBody()).isNull();
        }
    }

    // ──────────────────────────────────────────────────────────
    // 알 수 없는 cmd / 형식 오류 응답
    // ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("알 수 없는 cmd 및 형식 오류")
    class UnknownCmdTest {

        @Test
        @DisplayName("알 수 없는 cmd 를 가진 응답은 getParse() 가 null 을 반환한다")
        void unknownCmd_parseIsNull() {
            String response = "$PGL,SV,010000001,AUNK,000,1,1,0*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);
            assertThat(cmd.getParse()).isNull();
        }

        @Test
        @DisplayName("ERR cmd 를 가진 응답은 getParse() 가 null 을 반환한다")
        void errCmd_parseIsNull() {
            String response = "$PGL,SV,000000001,ERR,000,1,1,51*ABCD\r\n";
            HamsCmd cmd = new HamsCmd(response, true);
            assertThat(cmd.getParse()).isNull();
        }

        @Test
        @DisplayName("빈 문자열 응답은 예외 없이 처리되며 getParse() 가 null 을 반환한다")
        void emptyResponse_noExceptionAndParseIsNull() {
            HamsCmd cmd = new HamsCmd("", true);
            assertThat(cmd.getParse()).isNull();
        }
    }
}
