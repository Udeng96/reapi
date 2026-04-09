package com.eseict.apiServer.util

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.common.base.ByteSupport
import com.eseict.common.base.StringUtil
import com.eseict.common.base.StringUtilException
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object HeaderUtil {
    private val LOGGER = LoggerFactory.getLogger(HeaderUtil::class.java)
    /*헤더 정보*/
    /** 헤더 타입 코드.  */
    const val HEDR_TYP_CD = 0

    /** 송신 시스템 코드.  */
    const val SND_SYS_CD = 1

    /** 메시지 교환 패턴.  */
    const val MSG_EXCH_PATRN = 2

    /** 데이터부 길이.  */
    const val BODY_LEN = 3

    /** 메시지 타입 코드.  */
    const val MSG_TYP_CD = 4

    /** 트레이스 아이디.  */
    const val TRCE_ID = 5

    /** 전송 시간.  */
    const val SND_DTM = 6

    /** The header item len ar.  */
    private val headerItemLenAr = intArrayOf(2, 9, 1, 4, 3, 24, 17)

    /** The header item pos ar.  */
    private val headerItemStartPosAr = intArrayOf(0, 2, 11, 12, 16, 19, 43)

    /** The header item end pos ar.  */
    private val headerItemEndPosAr = intArrayOf(2, 11, 12, 16, 19, 43, 60)

    /**
     * Gets the header item length.
     *
     * @param fieldId the field id
     * @return the header item length
     */
    fun getHeaderItemLength(fieldId: Int): Int {
        return headerItemLenAr[fieldId]
    }

    /**
     * Gets the header item pos.
     *
     * @param fieldId the field id
     * @return the header item pos
     */
    fun getHeaderItemStartPos(fieldId: Int): Int {
        return headerItemStartPosAr[fieldId]
    }

    /**
     * Gets the header item end pos.
     *
     * @param fieldId the field id
     * @return the header item end pos
     */
    fun getHeaderItemEndPos(fieldId: Int): Int {
        return headerItemEndPosAr[fieldId]
    }

    fun getHeaderTotalLen(): Int {
        var totalLen = 0
        for (i in headerItemLenAr) {
            totalLen += i
        }
        return totalLen
    }

    /**
     * 송신 헤더 생성 - statEvet
     * @param headerTypCd - 헤더 타입 코드
     * @param sndSysCd - 송신 시스템 코드
     * @param msgTypCd - 메시지 타입 코드
     * @param mepTypCd - 메시지 교환 방식 코드
     * @param trceId -
     * @param bodyByteArrayLength - 바디 길이
     * @return String header - 헤더
     */
    fun makeHeader(
        headerTypCd: String?,
        sndSysCd: String?,
        msgTypCd: String?,
        mepTypCd: String?,
        trceId: String?,
        bodyByteArrayLength: Int
    ): ByteArray? {
        var headerByte: ByteArray? = null
        try {
            // 임시 바디길이 byte 크기
            val tempBodyLen = "0000"
            // 시스템 코드가 3자리일 경우 9자리 변경
            val sndSysCdSetPadded = StringUtil.setPad(sndSysCd, 9, ' ', StringUtil.PAD_LEFT)
            // TRCE_ID 24자리로 변경
            val trceIdSetPadded = StringUtil.setPad(trceId, 24, ' ', StringUtil.PAD_RIGHT)

            val formatter = DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_17)
            val sndDtm = LocalDateTime.now().format(formatter)

            val header = StringBuilder()
            header.append(headerTypCd)
            header.append(sndSysCdSetPadded)
            header.append(mepTypCd)
            header.append(tempBodyLen)
            header.append(msgTypCd)
            header.append(trceIdSetPadded)
            header.append(sndDtm)
            headerByte = header.toString().toByteArray(StandardCharsets.UTF_8)

            // 바디 길이 byte 변경
            val bodyLengthByte = ByteSupport.intToByteArray(bodyByteArrayLength, ByteOrder.LITTLE_ENDIAN)
            System.arraycopy(bodyLengthByte, 0, headerByte, 12, bodyLengthByte.size)

        } catch (e: StringUtilException) {
            LOGGER.error(e.message, e)
        } catch (e: UnsupportedEncodingException) {
            LOGGER.error(e.message, e)
        }
        return headerByte
    }


    @Throws(UnsupportedEncodingException::class)
    fun makeAckHeader(header: ByteArray, sndSysCd: String?, mep: String): ByteArray {
        // header 클론 생성
        val ackHeader = header.clone()
        // 송신 수신 코드
        var sndSysCdByte: ByteArray? = null
        try {
            sndSysCdByte = StringUtil.setPad(
                sndSysCd,
                getHeaderItemLength(SND_SYS_CD),
                ' ',
                StringUtil.PAD_LEFT
            ).toByteArray(
                StandardCharsets.UTF_8
            )
        } catch (e: StringUtilException) {
            LOGGER.error(e.message, e)
        } // StringUtilException 처리
        // 메시지 교환 패턴
        val mepByte = mep.toByteArray(StandardCharsets.UTF_8)
        // 바디 길이 (0 으로 지정)
        val bodyLenByte = ByteSupport.intToByteArray(0, ByteOrder.BIG_ENDIAN)
        System.arraycopy(
            sndSysCdByte,
            0,
            ackHeader,
            getHeaderItemStartPos(SND_SYS_CD),
            getHeaderItemLength(SND_SYS_CD)
        )
        System.arraycopy(
            mepByte,
            0,
            ackHeader,
            getHeaderItemStartPos(MSG_EXCH_PATRN),
            getHeaderItemLength(MSG_EXCH_PATRN)
        )
        System.arraycopy(
            bodyLenByte,
            0,
            ackHeader,
            getHeaderItemStartPos(BODY_LEN),
            getHeaderItemLength(BODY_LEN)
        )
        return ackHeader
    }
}