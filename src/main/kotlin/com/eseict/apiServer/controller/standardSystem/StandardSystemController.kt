package com.eseict.apiServer.controller.standardSystem

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.common.Message
import com.eseict.apiServer.dto.fireResponder.ChannelResult
import com.eseict.apiServer.dto.standardSystem.ChannelInfo
import com.eseict.apiServer.service.standardSsytem.StandardSystemService
import com.eseict.apiServer.util.CommonUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * 소방차 영상요청시에는 rtsp 정보도 함께 전달하고
 * xeus player에서도 영상을 표출할때 mgr_no 와 rtspUrl을 셋팅해주어야 영상이 표출된다.
 *
 */
@CrossOrigin("*")
@RestController
@RequestMapping("standard/system")
class StandardSystemController(
    val standardSystemService: StandardSystemService,
    val gson: Gson
) {

    private final val isDev = com.eseict.common.base.SystemProperties.getProperty("IS_DEV").toBoolean()
    private final val LOGGER = LoggerFactory.getLogger(StandardSystemController::class.java)

    /**
     * TODO FIXME 추후에 실제 연계되는것을 확인하고 변경해야한다.
     */
    @GetMapping
    fun getAccidentId(
        @RequestParam("statEvetOutbSeqn") statEvetOutbSeqn: String
    ): ResponseEntity<Any> {
        return try {
            val result = standardSystemService.getStandardSystemFireCar(statEvetOutbSeqn)

//            val result: Any? = null

//            if (isDev) {
//                val data = CommonUtil.getSampleData("/exData/fire/standardSystem.json")
//                ResponseEntity.ok(gson.fromJson(data, object : TypeToken<ChannelInfo>() {}.type))
//            } else {
//                val result = standardSystemService.getStandardSystemFireCar(statEvetOutbSeqn)
//                ResponseEntity.ok(null)
//            }

            if(null != result) {
                ResponseEntity.ok(result)
            }else{
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message(ApiConstants.RESULT.FAIL_CODE))
            }
        } catch (error: Exception) {
            LOGGER.error(error.message, error)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(error.message!!))
        }
    }
}