package com.eseict.apiServer.controller.byPos

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.common.Message
import com.eseict.apiServer.dto.oms.UserInfo
import com.eseict.apiServer.service.rino.fms.FacClfyService
import com.eseict.apiServer.service.rino.oms.OmsService
import com.eseict.apiServer.service.vworld.VWorldService
import com.google.common.base.Strings
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin("*")
@RestController
@RequestMapping(value = ["byPos"], produces = [ApiConstants.API_PRODUCES])
class ByPosController(
    val vWorldService: VWorldService,
    val omsService: OmsService,
    val facClfyService: FacClfyService
) {

    private final val LOGGER = LoggerFactory.getLogger(ByPosController::class.java)


    @GetMapping("posNm")
    fun getPosNm(@RequestParam lat: Double, @RequestParam lng: Double): ResponseEntity<Any> {

        return try {
            ResponseEntity.ok(Message(vWorldService.getPositionNameByLatLng(lat, lng)))
        } catch (e: Exception) {
            LOGGER.error(e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(e.message!!))
        }
    }

}