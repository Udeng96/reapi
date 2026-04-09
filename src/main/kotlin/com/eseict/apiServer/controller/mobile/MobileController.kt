package com.eseict.apiServer.controller.mobile

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.common.Message
import com.eseict.apiServer.dto.mobile.MobileInfo
import com.eseict.apiServer.proc.rino.msgProc.SocketMsgQueue
import com.eseict.apiServer.service.mobile.MobileService
import com.eseict.apiServer.util.CommonUtil
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.simple.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.nio.charset.Charset


@CrossOrigin("*")
@RestController
@RequestMapping(value = ["mobile"], produces = [ApiConstants.API_PRODUCES])
class MobileController(val gson: Gson, val mobileService: MobileService) {

    private final val logger = LoggerFactory.getLogger(MobileController::class.java)


    /**
     * mon_mb_desc d실시간 모바일 접속정보에서 해당 정보를 불러와서 출력하는 api
     *
     * 사용자정보를 mappling 하여, 담당자 부서 및 성명을 함께 전달하게한다.
     *
     */
    @GetMapping("live")
    fun getLiveList(): ResponseEntity<Any> {
        return try {
            val list = mobileService.getLiveList()

            ResponseEntity.ok(list)
        } catch (e: Exception) {
            logger.error(e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(ApiConstants.RESULT.FAIL_MESSAGE))
        }
    }


    /**
     * id 값은 userId를 받아서 처리한다.
     */
    @GetMapping("live/{userId}")
    fun getLive(@PathVariable(name = "userId") id: String): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(mobileService.getLiveInfo(id))
        } catch (e: Exception) {
            logger.error(e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(ApiConstants.RESULT.FAIL_MESSAGE))
        }
    }


    /**
     *  mon_mb_desc_log 테이블에서 촬영이 종료 혹은 앱이 종료된 경우에것만을 조회하고,
     *  mb_url이 rtsp 가 아닌경우에만 가져온다.
     *
     *  mon_mb_desc 와의 차이를 확인하고 sql문을 작성해야함
     *
     */
    @GetMapping("past")
    fun getPastList(
        @RequestParam("startDtm", required = false, defaultValue = "") startDtm: String,
        @RequestParam("endDtm", required = false, defaultValue = "") endDtm: String,
        @RequestParam(name = "page", required = false, defaultValue = "-1") page: Int? = -1,
        @RequestParam(name = "rows", required = false, defaultValue = "10") rows: Int? = 10
    ): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(mobileService.getPastList(startDtm, endDtm, rows!!, page!!))
        } catch (e: Exception) {
            logger.error(e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(ApiConstants.RESULT.FAIL_MESSAGE))
        }
    }

    /**
     * id 값은 mb_seq을 사용하도록 한다.
     */
    @GetMapping("past/{seq}")
    fun getPast(@PathVariable(name = "seq") id: String): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(mobileService.getPestInfo(id))
        } catch (e: Exception) {
            logger.error(e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(ApiConstants.RESULT.FAIL_MESSAGE))
        }
    }

    @GetMapping("test")
    fun doTest(): ResponseEntity<Any> {

        var json = JSONObject()
        json["data"] = "mobile"
        SocketMsgQueue.getInstance()
            .put(String(json.toJSONString().toByteArray(), Charset.forName(ApiConstants.ENCODING)))


        return ResponseEntity.ok(true)
    }
}