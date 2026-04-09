package com.eseict.apiServer.controller.fac

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.common.Message
import com.eseict.apiServer.dto.oms.UserInfo
import com.eseict.apiServer.service.facAll.FacAllService
import com.eseict.apiServer.service.rino.fms.FacClfyService
import com.eseict.apiServer.service.rino.fms.FacService
import com.eseict.apiServer.service.rino.oms.OmsService
import com.google.common.base.Strings
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.system.exitProcess

@CrossOrigin
@RestController
@RequestMapping(value = ["facAll"], produces = [ApiConstants.API_PRODUCES])
class FacAllController(
    val omsService: OmsService,
    val facAllService: FacAllService
) {

    private val logger = LoggerFactory.getLogger(FacAllController::class.java)


    /**
     * 전체 검색
     */
    @GetMapping("")
    fun getList(
        @RequestHeader token: String,
        @RequestParam(name = "locationCd", required = false, defaultValue = "") siteCd: String,
        @RequestParam(name = "type", required = false, defaultValue = "") clfyId: String,
        @RequestParam(name = "facNm", required = false, defaultValue = "") facNm: String,
        @RequestParam(name = "page", required = false) page: Int?,
        @RequestParam(name = "rows", required = false) rows: Int?
    ): ResponseEntity<Any> {

        if (!Strings.isNullOrEmpty(token)) {
            val user: UserInfo? = omsService.fetchUserInfo(token)

            if (user != null) {
                try {
                    //fms 정보
                    var result = facAllService.getFacAllList(clfyId, facNm, page ?: -1, rows ?: 10)
                    return ResponseEntity.ok(result)
                } catch (e: Exception) {
                    logger.error(e.message, e)
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(e.message!!))
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
        }
    }

//    /**
//     * 시설물 분류 그룹 가져오기
//     */
//    @GetMapping("clfy")
//    fun getFacClfyList(@RequestHeader token: String): ResponseEntity<Any> {
//
//        if (!Strings.isNullOrEmpty(token)) {
//            val user: UserInfo? = omsService.fetchUserInfo(token)
//
//            if (user != null) {
//                try {
//                    return ResponseEntity.ok(facAllService.getAllClfy(user))
//                } catch (e: Exception) {
//                    logger.error(e.message , e)
//                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(e.message!!))
//                }
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
//        }
//
//    }
}