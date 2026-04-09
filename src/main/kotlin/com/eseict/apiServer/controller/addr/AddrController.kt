package com.eseict.apiServer.controller.addr

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.common.Message
import com.eseict.apiServer.service.vworld.VWorldService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(value = ["*"])
@RestController
@RequestMapping(value = ["addr"], produces = [ApiConstants.API_PRODUCES])
class AddrController(val vWorldService: VWorldService) {

    private final val LOGGER = LoggerFactory.getLogger(AddrController::class.java)


    @GetMapping("juso")
    fun getJuso(@RequestParam word: String, @RequestParam type: String): ResponseEntity<Any> {

        return try {
            ResponseEntity.ok(vWorldService.searchPositionName(word, type))
        } catch (e: Exception) {
            LOGGER.error(e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(e.message!!))
        }

    }


}