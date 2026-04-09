package com.eseict.apiServer.controller.fac.clfy

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.common.Message
import com.eseict.apiServer.dto.oms.UserInfo
import com.eseict.apiServer.service.rino.fms.FacClfyService
import com.eseict.apiServer.service.rino.fms.FacService
import com.eseict.apiServer.service.rino.oms.OmsService
import com.google.common.base.Strings
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin(value = ["*"])
@RestController
@RequestMapping(value = ["/fac/clfy"], produces = [ApiConstants.API_PRODUCES])
class FacClfyController(
    val omsService: OmsService,
    val facClfyService: FacClfyService,
    val facService: FacService
) {

    private final val LOGGER = LoggerFactory.getLogger(FacClfyController::class.java)


    @GetMapping("")
    fun getFacClfyList(@RequestHeader token: String): ResponseEntity<Any> {

        if (!Strings.isNullOrEmpty(token)) {
            val user: UserInfo? = omsService.fetchUserInfo(token)

            if (user != null) {
                try {
                    return ResponseEntity.ok(facClfyService.getClfyListWithIconByToken(user))
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


    /**
     * TODO
     *   gis 상에서 등록은 하지않고 아이콘 변경도 하지않고
     *      등록되어있는 시설물을 변경하는것으로 되어있음 그런데 시설물을 clfy에서 빼버리면, 시설물 표출이 어려운 문제가 있음
     *      기존의 시설물을 강제로 변경하는 경우에는 가능함, fac_clfy_id 만을 변경하는것이라서 가능
     *       그러나 존재하는 시설물을 제외시키는경우에는 문제가 발생함 , fac_clfy_id 가 와래키로 잡혀있으며 delete option이 cascade로 잡혀있기때문임
     *      하여 시설물 분류에서 삭제한 시설물은 실제로 삭제되어야함
     *
     *      일단 대기이지만, fac useYnd을 false로 변경하는 방향으로 갈듯
     *      목록을 전달을 받을테니 , 그 목록을 대조하여, 빠지는것들은 useYn을 fasle로 병경을 하고, 추가되는것들은 clfyId 값을 변경하는 것으로 개발을 진행한다.
     *
     */
    @GetMapping("{clfyCd}")
    fun getFacClfy(@RequestHeader token: String, @PathVariable clfyCd: String): ResponseEntity<Any> {

        if (!Strings.isNullOrEmpty(token)) {
            val user: UserInfo? = omsService.fetchUserInfo(token)

            if (user != null) {
                try {

                    return ResponseEntity.ok(facClfyService.getClfyWithIconByToken(user, clfyCd))
                } catch (e: Exception) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mutableListOf<String>())
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(ApiConstants.RESULT.FAIL_NO_AUTH))
        }
    }

}