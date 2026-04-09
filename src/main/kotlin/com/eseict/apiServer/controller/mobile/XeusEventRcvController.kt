package com.eseict.apiServer.controller.mobile

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.domain.eas.XeusEventObj
import com.eseict.apiServer.dto.common.CommonResponse
import com.eseict.apiServer.dto.mobile.AddEventInfo
import com.eseict.apiServer.proc.rino.msgProc.SocketMsgQueue
import com.eseict.apiServer.service.mobile.XeusLiveViewEventService
import com.eseict.common.config.ConfFileInfo
import com.eseict.common.core.unique.IdGenerator
import com.eseict.common.datetime.DateTimeUtil
import com.google.gson.Gson
import org.json.simple.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.Charset
import java.util.*


@RestController
@RequestMapping(value = ["/xeus/ws"], produces = [ApiConstants.API_PRODUCES])
class XeusEventRcvController {

    private final val logger: Logger = LoggerFactory.getLogger(XeusEventRcvController::class.java)

    @Autowired
    lateinit var service: XeusLiveViewEventService


    /**
     * 영상 시작을 알리는 api
     */
    @RequestMapping(value = ["addEvent"])
    fun addEvent(
        @RequestParam(value = "userId", required = false, defaultValue = "") userId: String?,
        @RequestParam("json") eventDataStr: String?
//        @RequestBody eventDataStr: AddEventInfo?
    ): String? {
        val gson = Gson()
        logger.info("addEvent :{}",eventDataStr)
        val eventInfoMap: Map<String, Any> = gson.fromJson<Map<String, Any>>(
            eventDataStr,
            MutableMap::class.java
        )
        val eventObj = XeusEventObj(eventInfoMap)
        if (eventObj.procSt == "10") {
            eventObj.uSvcOutbId = IdGenerator.getUUID64()
        }
        if ("local".equals(ConfFileInfo.get("system.env"), ignoreCase = true)) {
            val c = Calendar.getInstance()
            eventObj.statEvetOutbDtm =
                DateTimeUtil.toPlaneDateTimeString(c, DateTimeUtil.TYPE_DATE_TIME_MILISECOND, true)

        }
        val response = CommonResponse(
            service.handleLiveViewEvent(eventObj)
        )

        Thread.sleep(3000)

        var json = JSONObject()
        json["data"] = "mobile"
        SocketMsgQueue.getInstance()
            .put(String(json.toJSONString().toByteArray(), Charset.forName(ApiConstants.ENCODING)))

        logger.info("Send to Websocket Client Live View Add Event")
        return gson.toJson(response)
    }

    @RequestMapping(value = ["/get/list"])
    fun getLiveViewList(): String? {
        val list: List<XeusEventObj?>? = service.getLiveViewEventList()
        val gson = Gson()
        return gson.toJson(list)
    }

}