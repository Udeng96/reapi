package com.eseict.apiServer.controller.fac

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.common.Message
import com.eseict.apiServer.dto.oms.UserInfo
import com.eseict.apiServer.service.rino.fms.FacClfyService
import com.eseict.apiServer.service.rino.fms.FacService
import com.eseict.apiServer.service.rino.oms.OmsService
import com.google.common.base.Strings
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * 전체 시설물
 */
@CrossOrigin(value = ["*"])
@RestController
@RequestMapping(value = ["fac"])
class FacController(
    val omsService: OmsService,
    val facService: FacService,
    val facClfyService: FacClfyService
) {

    private val LOGGER: Logger = LoggerFactory.getLogger(FacController::class.java)

    @GetMapping("")
    fun getList(
        @RequestHeader token: String,
        @RequestParam(required = false, defaultValue = "") type: String?,
        @RequestParam(required = false, defaultValue = "") facNm: String?,
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) rows: Int?
    ): ResponseEntity<Any> {

        val omsApi = omsService.fetchUserInfo(token)
        return if (omsApi != null) {
            try {
                if (omsApi.result == ApiConstants.RESULT.OMS_SUCCESS_CODE) {
                    ResponseEntity.ok(facService.getFacList(type, facNm, page ?: -1, rows ?: 10))
                } else {
                    LOGGER.info("DETECT UNAUTHORIZED ACCESS")
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
                }
            } catch (e: Exception) {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(e.message!!))
            }
        } else {
            LOGGER.error("DETECT UNAUTHORIZE ACCESS")
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
        }
    }

    @GetMapping("{facId}")
    fun getFac(@PathVariable facId: String, @RequestHeader token: String): ResponseEntity<Any> {

        if (!Strings.isNullOrEmpty(token)) {
            val user: UserInfo? = omsService.fetchUserInfo(token)

            if (user != null) {
                try {
                    return ResponseEntity.ok(facService.getFac(facId))
                } catch (e: Exception) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(e.message!!))
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
        }
    }

    @GetMapping("/clfy")
    fun getFacClfy(
        @RequestHeader token: String,

        ): ResponseEntity<Any> {

        val omsApi = omsService.fetchUserInfo(token)

        return if (omsApi != null) {
            try {
                if (omsApi.result == ApiConstants.RESULT.OMS_SUCCESS_CODE) {
                    ResponseEntity.ok(facClfyService.getFacClfy())
                } else {
                    LOGGER.info("DETECT UNAUTHORIZED ACCESS")
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
                }
            } catch (e: Exception) {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(e.message!!))
            }
        } else {
            LOGGER.error("DETECT UNAUTHORIZED ACCESS")
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
        }

    }

    @GetMapping("/clfy/{clfyCd}")
    fun getFacClfy(
        @RequestHeader token: String,
        @PathVariable clfyCd: String
    ): ResponseEntity<Any> {

        val omsApi = omsService.fetchUserInfo(token)

        return if (omsApi != null) {
            try {
                if (omsApi.result == ApiConstants.RESULT.OMS_SUCCESS_CODE) {
                    ResponseEntity.ok(facClfyService.getFacClfyByClfyCd(clfyCd))
                } else {
                    LOGGER.info("DETECT UNAUTHORIZED ACCESS")
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
                }
            } catch (e: Exception) {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(e.message!!))
            }
        } else {
            LOGGER.error("DETECT UNAUTHORIZED ACCESS")
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
        }

    }

    @GetMapping("/clfy/{clfyCd}/facInfo")
    fun getListByClfyCd(
        @RequestHeader token: String,
        @PathVariable clfyCd: String,
    ): ResponseEntity<Any> {
        val omsApi = omsService.fetchUserInfo(token)
        return if (omsApi != null) {
            try {
                if (omsApi.result == ApiConstants.RESULT.OMS_SUCCESS_CODE) {
                    ResponseEntity.ok(facService.getFacListByClfyCd(clfyCd))
                } else {
                    LOGGER.info("DETECT UNAUTHORIZED ACCESS")
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
                }
            } catch (e: Exception) {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(e.message!!))
            }
        } else {
            LOGGER.error("DETECT UNAUTHORIZE ACCESS")
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
        }
    }


}