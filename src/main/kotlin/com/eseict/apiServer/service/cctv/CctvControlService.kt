package com.eseict.apiServer.service.cctv

import com.eseict.apiServer.dto.cctv.PtzInfo
import com.eseict.apiServer.util.httpGet
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service(value = "cctvControlService")
class CctvControlService(val gson: Gson) {

    private final val logger = LoggerFactory.getLogger(CctvControlService::class.java)

    @Value("\${platform.xeus.url}")
    lateinit var xeusUrl: String;

    @Value("\${platform.xeus.subUrls.setPtz}")
    lateinit var setPtzUrl: String;

//    cctvMgrNo=CTV0000194&action=start&code=Up


    fun doControl(controlInfo: PtzInfo): String {

        val isDev = System.getenv()["IS_DEV"]?.toBoolean() ?: false
        val response = if (!isDev) {
            httpGet(
                "$xeusUrl$setPtzUrl",
                listOf<Pair<String,Any>>(
                    "cctvMgrNo" to controlInfo.cctvId!!,
                    "action" to controlInfo.action,
                    "code" to controlInfo.code
                )
            ) ?: ""
        } else {
            "{\"result\" : true}"
        }

        logger.info("PTZ RESPONSE , {}", response)

        val resJsonObj = JsonParser.parseString(response).asJsonObject
        return if (response.contains("result")) { //요청 자체가 성공한 경우
            resJsonObj.get("result").asString
        } else if (response.contains("error")) {
            resJsonObj.get("error").asString
        } else {
            "FAIL"
        }
    }


}