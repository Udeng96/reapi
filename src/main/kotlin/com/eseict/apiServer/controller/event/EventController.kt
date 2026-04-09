package com.eseict.apiServer.controller.event

import com.eseict.apiServer.conf.ApSystemProperties
import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.common.ClfyInfo
import com.eseict.apiServer.dto.common.Message
import com.eseict.apiServer.dto.event.EventInfo
import com.eseict.apiServer.service.rino.event.EventItemService
import com.eseict.apiServer.service.rino.event.EventService
import com.eseict.apiServer.service.rino.oms.OmsService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@CrossOrigin("*")
@RestController
@RequestMapping(value = ["event"], produces = [ApiConstants.API_PRODUCES])
class EventController(
    val systemProperties: ApSystemProperties,
    val eventService: EventService,
    val eventItemService: EventItemService,
    val omsService: OmsService
) {
    private final val LOGGER = LoggerFactory.getLogger(EventController::class.java)

    /**
     * 페이징 추가로 이벤트
     */
    @GetMapping("")
    fun getEvents(
        @RequestHeader("token") token: String,
        @RequestParam(
            "eventIdList",
            required = false,
            defaultValue = ""
        ) eventIdList: List<String>, //이벤트 타입 리스트 //TODO 이거는 상황이벤트까지 혹은 unit 까지 할지
        @RequestParam("locationCdList", required = false, defaultValue = "") locationCdList: List<String>, //znCd 값의 리스트
        @RequestParam("startDtm", required = false, defaultValue = "") startDtm: String,
        @RequestParam("endDtm", required = false, defaultValue = "") endDtm: String,
        @RequestParam(name = "page", required = false) page: Int?,
        @RequestParam(name = "rows", required = false) rows: Int?
    ): ResponseEntity<Any> {

        val useEventAuth = systemProperties.getValue("useEventAuth").toBoolean()

        val omsApi = if (useEventAuth) {//권한 필터를 사용하는지 유무에 따라 OMS 에 요청을하고 말고를 결정한다.
            omsService.fetchUserInfo(token)
        } else {
            null
        }

        return try {

            if (useEventAuth) { //권한 처리를 하는경우
                if (omsApi == null) { //권한이 없는경우
                    LOGGER.info("DETECT UNAUTHORIZED ACCESS")
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
                } else {//권한이 존재하는경우
                    ResponseEntity.ok(
                        eventService.getEvents(
                            useEventAuth,
                            eventIdList,
                            locationCdList,
                            omsApi.data.eventAuthList,
                            startDtm,
                            endDtm,
                            page,
                            rows
                        )
                    )

                }
            } else {//권한처리를하지않는경우
                ResponseEntity.ok(
                    eventService.getEvents(
                        useEventAuth,
                        eventIdList,
                        locationCdList,
                        null,
                        startDtm,
                        endDtm,
                        page,
                        rows
                    )
                )
            }


        } catch (e: Exception) {
            LOGGER.error(e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(ApiConstants.RESULT.FAIL_MESSAGE))
        }

    }

    @GetMapping("/{id}")
    fun getEvent(@PathVariable("id") id: String): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(eventService.getEvent(id))
        } catch (e: Exception) {
            LOGGER.error(e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(ApiConstants.RESULT.FAIL_MESSAGE))
        }
    }

    @GetMapping("/uSvcOutbId/{id}")
    fun getEventByUSvcId(@PathVariable("id") id: String): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(eventService.getEventByUSvcId(id))
        } catch (e: Exception) {
            LOGGER.error(e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(ApiConstants.RESULT.FAIL_MESSAGE))
        }
    }

    @GetMapping("type")
    fun getType(
        @RequestHeader("token") token: String,
    ): ResponseEntity<Any> {

        val useEventAuth = systemProperties.getValue("useEventAuth").toBoolean()

        val omsApi = if (useEventAuth) {//권한 필터를 사용하는지 유무에 따라 OMS 에 요청을하고 말고를 결정한다.
            omsService.fetchUserInfo(token)
        } else {
            null
        }
        return try {

            if (useEventAuth) { //필터를 사용하는경우
                if (omsApi != null) {
                    ResponseEntity.ok(eventItemService.getTypeList(useEventAuth, omsApi.data.eventAuthList))
                } else {
                    LOGGER.error("there's something wrong")
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
                }
            } else {
                ResponseEntity.ok(eventItemService.getTypeList(useEventAuth, null))
            }

        } catch (e: Exception) {
            LOGGER.error(e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(ApiConstants.RESULT.FAIL_MESSAGE))
        }

    }


    /**
     * 지자체 정보 zn_cd 로 사용자의 권한에서 뽑아서 전달하게 한다.
     */
    @GetMapping("location")
    fun getLocation(
        @RequestHeader("token") token: String,
    ): ResponseEntity<Any> {
        val useEventAuth = systemProperties.getValue("useEventAuth").toBoolean()
        val omsApi = if (useEventAuth) {//권한 필터를 사용하는지 유무에 따라 OMS 에 요청을하고 말고를 결정한다.
            omsService.fetchUserInfo(token)
        } else {
            null
        }
        return try {

            if (useEventAuth) {
                if (omsApi != null) {
                    ResponseEntity.ok(eventItemService.getZnCdList(useEventAuth, omsApi.data.eventAuthList))
                } else {
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
                }
            } else {
                ResponseEntity.ok(eventItemService.getZnCdList(useEventAuth, null))
            }

        } catch (e: Exception) {
            LOGGER.error(e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(ApiConstants.RESULT.FAIL_MESSAGE))
        }

    }

    @GetMapping("heatmap/clfy")
    fun getHeatmapClfy(
        @RequestHeader("token") token: String
    ): ResponseEntity<Any> {
        val useEventAuth = systemProperties.getValue("useEventAuth").toBoolean()

        val omsApi = if (useEventAuth) {//권한 필터를 사용하는지 유무에 따라 OMS 에 요청을하고 말고를 결정한다.
            omsService.fetchUserInfo(token)
        } else {
            null
        }

        return try {

            if (useEventAuth) {
                if (omsApi != null) {
                    ResponseEntity.ok(eventItemService.getHeatmapClfy(useEventAuth, omsApi.data.eventAuthList))
                } else {
                    LOGGER.error("there's something wrong")
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
                }
            } else {
                ResponseEntity.ok(eventItemService.getHeatmapClfy(useEventAuth, null))
            }

        } catch (e: Exception) {
            LOGGER.error(e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(ApiConstants.RESULT.FAIL_MESSAGE))
        }
    }

    @GetMapping("heatmap/count")
    fun getHeatmapCount(
        @RequestHeader("token") token: String,
        @RequestParam("svcThemeCd") svcThemeCd: String,
        @RequestParam("unitCd") unitCd: String,
        @RequestParam(value = "startDtm", required = false, defaultValue = "") startDtm: String?,
        @RequestParam(value = "endDtm", required = false, defaultValue = "") endDtm: String?
    ): ResponseEntity<Any> {

        val useEventAuth = systemProperties.getValue("useEventAuth").toBoolean()
        val omsApi = if (useEventAuth) {//권한 필터를 사용하는지 유무에 따라 OMS 에 요청을하고 말고를 결정한다.
            omsService.fetchUserInfo(token)
        } else {
            null
        }
        return try {
            if (useEventAuth) {
                if (omsApi != null) {
                    val result = eventService.getHeatmapCount(omsApi, svcThemeCd, unitCd, startDtm, endDtm)
                    ResponseEntity.ok(result)
                } else {
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
                }
            } else {
                val result = eventService.getHeatmapCount(omsApi, svcThemeCd, unitCd, startDtm, endDtm)
                ResponseEntity.ok(result)
            }


        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(ApiConstants.RESULT.FAIL_MESSAGE))
        }
    }

    /**
     * 상황전파애서 사용하는 이벤트 조회 부분
     */
    @GetMapping("dstr")
    fun getDstrEvents(
        @RequestHeader(name = "token") token: String,
        @RequestParam(name = "rows", required = false, defaultValue = "30") rows: Int,
        @RequestParam(name = "startDtm", required = false, defaultValue = "") startDtm: String,
        @RequestParam(name = "endDtm", required = false, defaultValue = "") endDtm: String,
        reqeust: HttpServletRequest
    ): ResponseEntity<Any> {

        val omsApi = omsService.fetchUserInfo(token)

        return if (omsApi != null) {
            try {
                if (omsApi.result == ApiConstants.RESULT.OMS_SUCCESS_CODE) {
                    ResponseEntity.ok(eventService.getDstrEvents(rows, startDtm, endDtm, null))
                } else {
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
                }
            } catch (error: Exception) {
                LOGGER.error(error.message, error)
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(error.message!!))
            }
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
        }
    }

    @GetMapping("dstr/{uSvcOutbId}")
    fun getDstrEvent(
        @RequestHeader(name = "token") token: String,
        @PathVariable(name = "uSvcOutbId") uSvcOutbId: String
    ): ResponseEntity<Any> {

        val omsApi = omsService.fetchUserInfo(token)

        return if (omsApi != null) {
            try {
                if (omsApi.result == ApiConstants.RESULT.OMS_SUCCESS_CODE) {
                    ResponseEntity.ok(eventService.getDstrEvent(uSvcOutbId))
                } else {
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
                }
            } catch (error: Exception) {
                LOGGER.error(error.message, error)
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(error.message!!))
            }
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
        }
    }


    /**
     * 이벤트 seqn 값을 받아서 이벤트를 종료시키는 controller
     * mrs로 요청을 보내어 이벤트를 종료시킨다.
     */
    @PostMapping(value = ["close"])
    fun closeEvent(@RequestParam(name = "statEvetOutbSeqn") statEvetOutbSeqn: String): ResponseEntity<Any> {

        return try {
            val isClose = eventService.closeEvent(statEvetOutbSeqn)

            if (isClose) {
                var headers = HttpHeaders()
                headers.add("Location", statEvetOutbSeqn)
                ResponseEntity(Message(statEvetOutbSeqn), headers, HttpStatus.OK)
            } else {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(ApiConstants.RESULT.FAIL_MESSAGE))
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(ApiConstants.RESULT.FAIL_MESSAGE))
        }

    }
}