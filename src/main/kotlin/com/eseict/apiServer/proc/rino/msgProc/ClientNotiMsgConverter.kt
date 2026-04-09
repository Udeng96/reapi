package com.eseict.apiServer.proc.rino.msgProc

import com.eseict.common.config.ConfFileInfo
import com.eseict.common.log.LoggerManager
import com.google.common.collect.Maps
import flexjson.JSONDeserializer
import flexjson.JSONSerializer
import java.util.*

class ClientNotiMsgConverter private constructor() {
    var logger = LoggerManager.getLogger(ClientNotiMsgConverter::class.java)

    private val EVENT_STAT = "StatEvet"
    private val EVENT_FAC = "FacEvet"
    private val EVENT_DSP = "DSPEvet"
    val EVENT_LAYOUT = "LayoutEvet"
    val NOTIFY_MSG = "NOTIFY_MSG"
    val WALL_WIDGET_CONFR = "WALL_WIDGET_CONFR"
    val CHANGED_LAYOUT_LIST = "CHANGED_LAYOUT_LIST"


    companion object {

        @Volatile
        private var instance: ClientNotiMsgConverter = ClientNotiMsgConverter()

        @JvmStatic
        fun getInstance(): ClientNotiMsgConverter =
            instance ?: synchronized(this) {
                instance ?: ClientNotiMsgConverter().also {
                    instance = it
                }
            }
    }

    fun msgConvert(msg: String?): String {
        logger.debug("ClientNotiMsgConverter- {}", msg)
        val msgMap = JSONDeserializer<Any?>().deserialize(msg) as Map<String, Any>?
        val msgKeySet = msgMap!!.keys
        val msgIterator = msgKeySet.iterator()
        val msgTypNm = msgIterator.next()
        logger.info("msgTypNm,[{}]", msgTypNm)
        val msgItem = JSONSerializer().deepSerialize(msgMap[msgTypNm])
        //		logger.debug("{}", msgItem);
        val itemMap: MutableMap<String, Any> = JSONDeserializer<Any?>().deserialize(msgItem) as MutableMap<String, Any>


        var resMap: MutableMap<String, String>? = null
        val locale = Locale("ko", "KR")
        var rtnMsg = ""
        when (msgTypNm) {
            EVENT_STAT -> {
                resMap = statEvetFormat(itemMap, locale)
                resMap!!["type"] = msgTypNm
                rtnMsg = JSONSerializer().deepSerialize(resMap)
            }

            else -> rtnMsg = msg ?: ""
        }
        return rtnMsg
    }

    // 상황이벤트 NOTI 생성
    fun statEvetFormat(itemMap: Map<String, Any>, locale: Locale?): MutableMap<String, String> {
        val resMap: MutableMap<String, String> = HashMap<String, String>()
        val outbPosStr = StringBuilder()
        resMap["evetNm"] = itemMap!!["statEvetNm"] as String
        resMap["outbPosNm"] = itemMap["outbPosNm"] as String
        resMap["outbDtm"] = itemMap["statEvetOutbDtm"] as String
        for (outbPosMap in (itemMap["outbPos"] as ArrayList<Map<*, *>>?)!!) {
            outbPosStr.append(outbPosMap["x"]).append("@")
            outbPosStr.append(outbPosMap["y"]).append("@")
            outbPosStr.append(outbPosMap["z"]).append("#")
        }
        resMap["outbPos"] = outbPosStr.toString()
        resMap["outbScopRads"] = itemMap["outbScopRads"] as String
        resMap["statEvetId"] = itemMap["statEvetId"] as String
        resMap["uSvcOutbId"] = itemMap["uSvcOutbId"] as String

        val statEvetOutbSeqn = itemMap["statEvetOutbSeqn"]
        resMap["statEvetOutbSeqn"] = if (statEvetOutbSeqn != null) statEvetOutbSeqn as String else ""

        resMap["procSt"] = itemMap["procSt"] as String
        resMap["evetItems"] = itemMap["statEvetItem"].toString()
        return resMap
    }


    fun dspEvetFormat(itemMap: Map<String?, Any?>, locale: Locale?): Map<String, String?>? {
        val result: MutableMap<String, String?>? = Maps.newHashMap()
        result!!["evetOutbProcSeqn"] = itemMap["evetOutbProcSeqn"] as String?
        result["evetClfyCd"] = itemMap["evetClfyCd"] as String?
        result["evetClfyNm"] = itemMap["evetClfyCdNm"] as String?
        result["evetOutbDvcId"] = itemMap["evetOutbDvcId"] as String?
        result["evetNm"] = itemMap["evetNm"] as String?
        result["evetGdCd"] = itemMap["evetGdCd"] as String?
        result["evetProcSt"] = itemMap["evetProcSt"] as String?
        val outbDtm = itemMap["outbDtm"] as String?
        val sb = StringBuffer()
        sb
            .append(outbDtm!!.substring(0, 4)).append("/")
            .append(outbDtm.substring(4, 6)).append("/")
            .append(outbDtm.substring(6, 8)).append(" ")
            .append(outbDtm.substring(8, 10)).append(":")
            .append(outbDtm.substring(10, 12)).append(":")
            .append(outbDtm.substring(12, 14)).append(".")
            .append(outbDtm.substring(14))
        result["outbDtm"] = sb.toString()
        result["lat"] = itemMap["lat"] as String?
        result["lon"] = itemMap["lon"] as String?
        return result
    }

    fun getEventLayout(): String? {
        return EVENT_LAYOUT
    }

}