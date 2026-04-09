package com.eseict.apiServer.controller.fireResponder

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.common.Message
import com.eseict.apiServer.dto.fireResponder.ChannelResult
import com.eseict.apiServer.util.CommonUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin
@RequestMapping(value = ["fireResponder"], produces = [ApiConstants.API_PRODUCES])
@RestController
class FireResponderController(
//    val fireResponderService: FireResponderService
    val gson: Gson
) {
    private final val isDev = com.eseict.common.base.SystemProperties.getProperty("IS_DEV").toBoolean()
    private final val LOGGER = LoggerFactory.getLogger(FireResponderController::class.java)

    /**
     *
     * TODO FIXME 추후에 실제 연계되는것을 확인하고 변경해야한다.
     * 전체 출동차량 정보 조회
     * 요청 차량 정보를 받아서 값이 있는경우 해당하는 차량정보만 출력
     */
    @GetMapping("")
    fun fetchChList(
        @RequestParam(name = "carnumList", required = false, defaultValue = "") carnumList: List<String>,
        @RequestParam(required = false, defaultValue = "true") isLive: Boolean
    ): ResponseEntity<Any> {

        return try {
//            ResponseEntity.ok(fireResponderService.fetchChannel(carnumList,isLive))
            if (isDev) {

                val data = CommonUtil.getSampleData("/exData/fire/fireResponder.json")

                ResponseEntity.ok(gson.fromJson(data, object : TypeToken<List<ChannelResult>>() {}.type))
            } else {
//                ResponseEntity.ok(fireResponderService.fetchChannel(carnumList,isLive))
                ResponseEntity.ok(null)
            }
        } catch (e: Exception) {
            LOGGER.error(e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Message(ApiConstants.RESULT.FAIL_MESSAGE))
        }

    }


}