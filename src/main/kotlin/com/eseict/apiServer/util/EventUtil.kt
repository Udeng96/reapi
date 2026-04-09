package com.eseict.apiServer.util

import com.eseict.common.config.ConfFileInfo
import com.eseict.common.core.unique.IdGenerator
import com.eseict.common.core.unique.IdGeneratorException
import com.eseict.apiServer.conf.ApiConstants
import com.google.common.base.Strings
import com.google.common.collect.Lists
import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.SimpleDateFormat
import java.util.*

class EventUtil {

    val LOGGER: Logger = LoggerFactory.getLogger(EventUtil::class.java)
    val digits = charArrayOf(
        '0',
        '1',
        '2',
        '3',
        '4',
        '5',
        '6',
        '7',
        '8',
        '9',
        'a',
        'b',
        'c',
        'd',
        'e',
        'f',
        'g',
        'h',
        'i',
        'j',
        'k',
        'l',
        'm',
        'n',
        'o',
        'p',
        'q',
        'r',
        's',
        't',
        'u',
        'v',
        'w',
        'x',
        'y',
        'z'
    )


    @Throws(UnsupportedEncodingException::class)
    fun getHead(jsonData: String, type: String, msgTypCd: String, traceId: String): ByteArray? {
        val smf = SimpleDateFormat(ApiConstants.DATE.FORMAT_17)
        val dtm = smf.format(Calendar.getInstance().time)
        var headerData = "A1      " + type + "1    " + msgTypCd
        if (!Strings.isNullOrEmpty(traceId)) {
            headerData += traceId
            headerData = "$headerData                       $dtm"
        } else {
            headerData = headerData + com.eseict.common.core.unique.IdGenerator.getUUID32().substring(0, 24) + dtm
        }
        val header = headerData.toByteArray(charset("UTF-8"))
        val body = jsonData.toByteArray(charset("UTF-8"))
        var bodyLenByte: ByteArray? = null
        bodyLenByte = intToByte(body.size, ByteOrder.LITTLE_ENDIAN)
        if (bodyLenByte != null) {
            for (i in bodyLenByte.indices) {
                header[12 + i] = bodyLenByte[i]
            }
        }
        return header
    }

    fun intToByte(integer: Int, order: ByteOrder?): ByteArray? {
        val buff = ByteBuffer.allocate(Integer.SIZE / 8)
        buff.order(order)
        buff.putInt(integer)
        return buff.array()
    }


    fun makeStatEvetBody(paramMap: Map<String?, Any?>): String? {
        var uSvcId = Strings.emptyToNull(paramMap["uSvcId"] as String?)
        var procSt = Strings.emptyToNull(paramMap["procSt"] as String?)
        var clrDtm = ""
        var clsDtm = ""
        if (Strings.isNullOrEmpty(procSt)) {
            procSt = ApiConstants.STAT_EVET_PROC_ST.OUTB
        }
        if (procSt == ApiConstants.STAT_EVET_PROC_ST.CLS) {
            val smf = SimpleDateFormat(ApiConstants.DATE.FORMAT_17)
            val date = Date()
            clrDtm = smf.format(date)
            clsDtm = clrDtm
        }
        val xCoordinate = Strings.emptyToNull(paramMap["xCoordinate"] as String?)
        val yCoordinate = Strings.emptyToNull(paramMap["yCoordinate"] as String?)
        val zCoordinate = Strings.emptyToNull(paramMap["zCoordinate"] as String?)
        val addrNm = paramMap["outbPlac"] as String?
        val gson = Gson()
        val body: MutableMap<String, Any> = HashMap()
        val bodyMap: MutableMap<String, Any?> = HashMap()
        val znCd = Strings.emptyToNull(paramMap["znCd"] as String?)
        val svcThemeCd = Strings.emptyToNull(paramMap["svcThemeCd"] as String?)
        val unitSvcCd = Strings.emptyToNull(paramMap["unitSvcCd"] as String?)
        val evetCode = "E"
        val statEvetCd = Strings.emptyToNull(paramMap["statEvetCd"] as String?)
        val outbDtm =
            if (Strings.emptyToNull(paramMap["outbDtm"] as String?) != null) Strings.emptyToNull(paramMap["outbDtm"] as String?) else getCurrentDtm()
        val statEvetGdCd =
            if (Strings.emptyToNull(paramMap["statEvetGdCd"] as String?) != null) Strings.emptyToNull(paramMap["statEvetGdCd"] as String?) else ApiConstants.EVENT.EVET_GD_CD_NORMAL
        val statEvetNm =
            if (Strings.emptyToNull(paramMap["statEvetNm"] as String?) != null) Strings.emptyToNull(paramMap["statEvetNm"] as String?) else ApiConstants.EVENT.STAT_EVET_NM
        val statEvetCntn =
            if (Strings.emptyToNull(paramMap["statEvetCntn"] as String?) != null) Strings.emptyToNull(paramMap["statEvetCntn"] as String?) else ApiConstants.EVENT.STAT_EVET_CNTN
        val statEvetId = znCd + svcThemeCd + unitSvcCd + evetCode + statEvetCd
        try {
            if (Strings.isNullOrEmpty(uSvcId)) {
                uSvcId = IdGenerator.getUniqueIdByTime(
                    ConfFileInfo.get("system.code"),
                    IdGenerator.TIME_17,
                    IdGenerator.PAD_END_4
                )
            }
        } catch (e: IdGeneratorException) {
            LOGGER.error(e.message, e)
        }
        bodyMap["uSvcOutbId"] = uSvcId
        bodyMap["statEvetId"] = statEvetId
        bodyMap["statEvetNm"] = statEvetNm
        bodyMap["statEvetCntn"] = statEvetCntn
        bodyMap["statEvetGdCd"] = statEvetGdCd
        bodyMap["procSt"] = procSt
        bodyMap["outbPosNm"] = addrNm
        bodyMap["outbPosCnt"] = 1
        val putbPosList: MutableList<Map<String, String?>> = ArrayList()
        val outPos: MutableMap<String, String?> = HashMap()
        outPos["x"] = xCoordinate
        outPos["y"] = yCoordinate
        outPos["z"] = zCoordinate
        putbPosList.add(outPos)
        bodyMap["outbPos"] = putbPosList
        bodyMap["statEvetOutbDtm"] = outbDtm
        val outbScopRads = "0"
        bodyMap["outbScopRads"] = outbScopRads
        var statEvetItemLst: List<Map<String, Any>> = ArrayList()
        statEvetItemLst =
            if ((paramMap["itemList"] as List<Map<String?, Any?>?>?)!!.size > 0) paramMap["itemList"] as List<Map<String, Any>> else Lists.newArrayList<Map<String, Any>>()
        bodyMap["statEvetItem"] = statEvetItemLst //상황이벤트 배열|new ArrayList<Map<String, Object>>();
        bodyMap["statEvetItemCnt"] = statEvetItemLst!!.size //int 상황이벤트 항목 건수

        //발생 -> 해지의 경우 조치가 아니기때문에 값을 채우지않는다.
        bodyMap["statEvetActnCntn"] = "" //이벤트 조치 내용
        bodyMap["statEvetActnDtm"] = "" //이벤트 조치일시
        bodyMap["statEvetActnMn"] = "" // 이벤트 조치자
        bodyMap["statEvetClrDtm"] = clrDtm // 이벤트 해제 일시   --종료 일시 삽입
        bodyMap["statEvetClsDtm"] = clsDtm // 이벤트 종료 일시   --종료 일시 삽입
        bodyMap["statEvetActnRslt"] = ""
        val cpxRelEvetOutbSeqnLst = ArrayList<Map<String, Any>>()
        bodyMap["cpxRelEvetOutbSeqnCnt"] = 0 //복합 연관 상황 이벤트 발생 시퀀스 건수 0으로 고정
        bodyMap["cpxRelEvetOutbSeqn"] = cpxRelEvetOutbSeqnLst
        val outbMainGb = "P" //이벤트발생주체구분
        bodyMap["outbMainGb"] = outbMainGb
        body["StatEvet"] = bodyMap
        return gson.toJson(body)
    }


    fun makeRandom4String(): String? {
        val randomString = UUID.randomUUID().toString().replace("-".toRegex(), "")
        return randomString.substring(0, 4)
    }

    fun getCurrentDtm(): String? {
        val smf = SimpleDateFormat(ApiConstants.DATE.FORMAT_17)
        val date = Date()
        return smf.format(date)
    }
}