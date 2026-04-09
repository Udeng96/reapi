package com.eseict.apiServer.dto.hams

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.hams.parser.*
import com.eseict.apiServer.util.CRC16_CCITT
import com.eseict.apiServer.util.hams.HamsParser
import org.slf4j.LoggerFactory
import java.nio.charset.Charset

class HamsCmd {

    private final val logger = LoggerFactory.getLogger(HamsCmd::class.java)
    private val NEW_LINE = "\r\n"
    var packetVersion: Int? = 1
    var targetId: String? = null

    var header: ByteArray? = null
    var body: ByteArray? = null
    var tail: ByteArray? = null

    var parse: HamsParsePolicy? = null


    /**
     *isNoTSToC : 서버 to client의 경우에만 000으로 시작 , 아닌경우 010으로 시작
     */
    constructor(packetVersion: Int? = 1, isNoTSToC: Boolean = false, targetId: String?) {
        this.packetVersion = packetVersion

        if (targetId?.length ?: 0 <= 9) {

            var nTargetId = targetId
            for (i in 0 until 9 - targetId!!.length) {
                nTargetId = "0$nTargetId"
            }

            this.targetId = nTargetId
        } else {
            this.targetId = targetId
        }

        if (isNoTSToC) {
            this.targetId = this.targetId?.replaceRange(0 until 3, "010")
        }

    }


    //TODO response 받은 메세지를 가지고 hamscmd를 만든다. 그때 parser를 할당한다. this를 전달해서 pair<String, Map<String,String>>을 전달
    constructor(msg: String) {
        //parser 로 parse 를 실시하는데 , header ,body, tail 만 자르고 셋
        //cmd 에 따라서 parser를 지정해서 만들게한다.
        try {
            selectParserPolicy(msg)
            logger.debug("msg {}", msg)
        } catch (e: Exception) {
            logger.error(e.message, e)
            logger.error("FAIL TO CREATE HAMS CMD")
        }

    }


    enum class COMMAND {
        CON, //연결
        SET, //조작
        ERR, //에러발생시
        RBT, // 재부팅
    }

    enum class CHAR(val word: String) {
        START_TAIL("*"),
        START_MESSAGE("\$PGL"),

        //        END_MESSAGE("<${Integer.toHexString(0x0D).uppercase()}><${Integer.toHexString(0x0A).uppercase()}>"),
        END_MESSAGE("<CR><LF>"),
        ACK("A"),
        STATUS("S"),
        CONTROL("C"),
        CANCEL("X"),
        EVENT("E"),
        SPLIT(","),
        CLIENT("CL"),
        SERVER("SV")
    }

    enum class TARGET(val target: String) {
        DOOR_LOCK("DO"),
        POWER("PO"),
        DC("DC"),//DC,DC 12V
        UPPER_TEMPERATURE("UT"),
        TEMPERATURE_AND_HUMI("TH"),
    }

    enum class RESULT_CODE(val num: Int) {
        SUCCESS(0),
        FAIL_CON(-1), FAIL_ERR(1), NO_DATA_FW(2), FAIL_SET(3),
        ALREADY_CONTROL(4), CANCEL_SET(5), FAIL_IP_SET(31),
        FAIL_RBT_NETWORK(32), WRONG_MSG(51), WRONG_DATA(52), NOT_CMD(53),
        NOT_MATCH_FIELD_CNT(54), EMPTY_FIELD(55), CRC_ERROR_BUFFER(56), CRC_NOT_MATCH(57),
        FAIL_LOGIN_ID(81), FAIL_LOGIN_PW(82), FAIL_LOGIN_NONE(83),
        NONE_CMD(97), NONE_EQ(98), NONE_ERR(99), FAIL_CON_DB(100)
    }

    enum class DOOR_TYPE(val num: Int) {
        front(1), back(2)
    }

    enum class FIELD_NM {
        LOGIN_RESULT, CRC16, ETX, FRONT_DOOR_S, FRONT_DOOR_LOCK_S, BACK_DOOR_S, BACK_DOOR_LOCK_S,
        TEMP, HUMI, ORDERER_TYPE, USER_ID, ORDERER_IP, EVENT_TYPE, EVENT_DOOR_TYPE,
        RCV_DATE, EQ_ID
    }


    /**
     * seq 값은 문서에 000이라고 작성되어있었음, 001 부터 넘버링을 할 수 있기때문에 문자열로 지정
     * packVersion 은 지정하지않으면 1로 셋팅
     */
    fun setHeader(bodyItemSize: Int, command: COMMAND, seq: String = "000"): HamsCmd {

        if (this.targetId.isNullOrEmpty()) {
            throw NullPointerException()
        }

        if (this.targetId?.length != 6) {
            for (i in 0 until 6 - this.targetId!!.length) {
                this.targetId = "0${this.targetId}"
            }
        }

        var headerStr = StringBuilder(CHAR.START_MESSAGE.word)
            .append(CHAR.SPLIT.word)
            .append(CHAR.CLIENT.word)
            .append(CHAR.SPLIT.word)
            .append(this.targetId)
            .append(CHAR.SPLIT.word)
            .append(command)
            .append(CHAR.SPLIT.word)
            .append(seq)
            .append(CHAR.SPLIT.word)
            .append(this.packetVersion)
            .append(CHAR.SPLIT.word)
            .append(bodyItemSize)
            .append(CHAR.SPLIT.word)

        //TODO command 이것에 따라서 parse 정책을 삽입한다.

        this.header = headerStr.toString().toByteArray(Charsets.UTF_8)

        return this
    }

    fun setHeader(bodyItemSize: Int, command: String, seq: String = "000"): HamsCmd {

        if (this.targetId.isNullOrEmpty()) {
            throw NullPointerException()
        }

        if (this.targetId?.length != 6) {
            for (i in 0 until 6 - this.targetId!!.length) {
                this.targetId = "0${this.targetId}"
            }
        }

        var headerStr = StringBuilder(CHAR.START_MESSAGE.word)
            .append(CHAR.SPLIT.word)
            .append(CHAR.CLIENT.word)
            .append(CHAR.SPLIT.word)
            .append(this.targetId)
            .append(CHAR.SPLIT.word)
            .append(command)
            .append(CHAR.SPLIT.word)
            .append(seq)
            .append(CHAR.SPLIT.word)
            .append(this.packetVersion)
            .append(CHAR.SPLIT.word)
            .append(bodyItemSize)

        if (!(command == TARGET.TEMPERATURE_AND_HUMI.target.plus(CHAR.STATUS.word))) {
            headerStr.append(CHAR.SPLIT.word)
        }

        this.header = headerStr.toString().toByteArray(Charsets.UTF_8)

        return this
    }


    fun setBody(values: List<String>): HamsCmd {
        this.body = values.joinToString(",").toByteArray(Charsets.UTF_8)
        return this
    }

    fun setTail(): HamsCmd {
        val headerStr = String(this.header!!, Charset.forName(ApiConstants.ENCODING))
        val bodyStr = String(this.body!!, Charset.forName(ApiConstants.ENCODING))


        var tail = StringBuilder()//CHAR.START_TAIL.word
        tail = tail.insert(0, headerStr.substring(1 until headerStr.length)).insert(headerStr.length - 1, bodyStr)

        var crc16 = CRC16_CCITT(tail.toString().toByteArray())

        if (crc16.length != 4) {
            for (i in 0 until 4 - crc16.length) {
                crc16 = "0$crc16"
            }
        }


        this.tail = (CHAR.START_TAIL.word + crc16
//                + CHAR.END_MESSAGE.word
                + NEW_LINE
                ).toByteArray(Charsets.UTF_8)

        return this
    }

    /**
    명령어를 만드는 메소드
    장비 명, Map<String,String> body에 해당하는값

    body 부분은 값이 없을 수 도 있음
     */

    fun makeMessage(): ByteArray? {

        return try {
            var message: ByteArray? = null
            if (this.header != null &&
//            this.body != null &&
                this.tail != null
            ) {
                message = this.header!! + this.body!! + this.tail!!
            }
            logger.info("make message is ${String(message!!)}")
            message
        } catch (e: Exception) {
            null
        }
    }


    @kotlin.jvm.Throws(Exception::class)
    fun selectParserPolicy(message: String) {// cmd 에 따라서 파서를 선택하게한다.
        logger.info("HAMS ACK MESSAGE IS {}", message)
        val header = HamsParsePolicy.parseHeader(message)

        val cmd = header.get(HamsParser.Companion.KEY_HEADER.cmd.name)
        val filedCnt = header.get(HamsParser.Companion.KEY_HEADER.bodyFiledCnt.name)

        when (cmd) {
            CHAR.ACK.word.plus(COMMAND.CON.name) -> this.parse = HamsCONParse(message, header)
            CHAR.ACK.word.plus(COMMAND.RBT.name) -> null //리부트 ack
            CHAR.ACK.word.plus(TARGET.DOOR_LOCK.target.plus(CHAR.STATUS.word)) -> this.parse = HamsDOSParse(message, header)//도어락 상태
            CHAR.ACK.word.plus(TARGET.DOOR_LOCK.target.plus(CHAR.CONTROL.word)) -> this.parse = HamsDOCParse(message, header) //도어락 조작
            TARGET.DOOR_LOCK.target.plus(CHAR.EVENT.word) -> this.parse = HamsDOEParse(message, header) //도어락 이벤트
            CHAR.ACK.word.plus(TARGET.UPPER_TEMPERATURE.target.plus(CHAR.STATUS.word)) -> null//상한온도 확인 ack
            CHAR.ACK.word.plus(TARGET.UPPER_TEMPERATURE.target.plus(CHAR.CONTROL.word)) -> null//상한온도 변경 ack
            CHAR.ACK.word.plus(TARGET.TEMPERATURE_AND_HUMI.target.plus(CHAR.STATUS.word)) -> this.parse = HamsTHSParse(message, header)//온/습도 확인 ack
            COMMAND.ERR.name -> this.parse = HamsDOEParse(message,header)
            else -> null //아무것도 아닌경우
        }

    }


}