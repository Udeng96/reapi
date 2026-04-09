package com.eseict.apiServer.controller.user

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.common.Message
import com.eseict.apiServer.service.rino.oms.OmsService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin
@RestController
@RequestMapping(value = ["user"], produces = [ApiConstants.API_PRODUCES])
class UserController(val omsService: OmsService) {

    private final val LOGGER = LoggerFactory.getLogger(UserController::class.java)


    @GetMapping(value = [""])
    fun getUser(@RequestHeader token: String): ResponseEntity<Any> {

        return try {
            ResponseEntity.ok(omsService.fetchUserInfo(token))
        } catch (e: Exception) {
            LOGGER.error(e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(e.message!!))
        }

    }

}