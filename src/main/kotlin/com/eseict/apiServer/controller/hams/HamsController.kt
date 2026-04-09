package com.eseict.apiServer.controller.hams

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.hams.HamsGisFacInfo
import com.eseict.apiServer.dto.openapi.ResultResponse
import com.eseict.apiServer.proc.hams.client.HamsSendClient
import com.eseict.apiServer.service.hams.HamsService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin("*")
@RestController
@RequestMapping(value = ["hams/api"], produces = [ApiConstants.API_PRODUCES])
class HamsController(val hamsService: HamsService) {

    private final val logger = LoggerFactory.getLogger(HamsController::class.java)


    @GetMapping(value = [""])
    fun getList(): ResponseEntity<Any> {

        return try {
            ResponseEntity.ok(hamsService.getList())
        } catch (e: Exception) {
            logger.error(e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResultResponse(
                    ApiConstants.RESULT.FAIL_CODE, ApiConstants.RESULT.FAIL_MESSAGE, null
                )
            )
        }

    }

    @GetMapping(value = ["cctv"])
    fun getHamsCctvList(
        @RequestParam(value = "hamsName") hamsName:String
    ) : ResponseEntity<Any>{
        return try {
            ResponseEntity.ok(hamsService.getCctvList(hamsName));
        }catch (e: Exception){
            logger.error(e.message, e);
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResultResponse(
                    ApiConstants.RESULT.FAIL_CODE, ApiConstants.RESULT.FAIL_MESSAGE, null
                )
            )
        }
    }

    @GetMapping(value = ["{id}"])
    fun getList(@PathVariable(value = "id") id: String): ResponseEntity<Any> {

        return try {
            val result = hamsService.get(id)
            if (result != null) {
                ResponseEntity.ok(result)
            } else {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResultResponse(
                        ApiConstants.RESULT.FAIL_CODE, ApiConstants.RESULT.FAIL_MESSAGE, null
                    )
                )
            }


        } catch (e: Exception) {
            logger.error(e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResultResponse(
                    ApiConstants.RESULT.FAIL_CODE, ApiConstants.RESULT.FAIL_MESSAGE, null
                )
            )
        }

    }

    /**
     * 도어락 잠금
     *
     * 받은 정보가 있다면 업데이트하고 전달,
     * 전달받은것이 없다면 현재 상태를 전달
     *
     *
     * @param id 장비 id
     * @param target 앞문 - 1 뒷문 -2 default 1
     */
    @PostMapping(value = ["{id}/lock"])
    @CrossOrigin
    fun doLock(@PathVariable(name = "id") id: String,
                @RequestBody(required = false) fac:HamsGisFacInfo?,
                @RequestParam(name = "target", required = false , defaultValue = "1") target:Int
    ): ResponseEntity<ResultResponse> {

        return try {
            val result = hamsService.doLock(id,target,2)
            var data = fac ?: result
            if (result!=null) {
                data?.status?.frontDoor = result.status.frontDoor ?: data?.status?.frontDoor
                data?.status?.frontDoorLock = result.status.frontDoorLock ?: data?.status?.frontDoorLock
                data?.status?.backDoor = result.status.backDoor ?: data?.status?.backDoor
                data?.status?.backDoorLock = result.status.backDoorLock ?: data?.status?.backDoorLock
                ResponseEntity.ok(
                    ResultResponse(
                        ApiConstants.RESULT.SUCCESS_MESSAGE, ApiConstants.RESULT.SUCCESS_MESSAGE,data
                    )
                )
            } else {
                ResponseEntity.status(HttpStatus.OK).body(
                    ResultResponse(
                        ApiConstants.RESULT.FAIL_CODE, ApiConstants.RESULT.FAIL_MESSAGE, data
                    )
                )
            }
        } catch (e: Exception) {
            logger.error(e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResultResponse(
                    ApiConstants.RESULT.FAIL_CODE, ApiConstants.RESULT.FAIL_MESSAGE, null
                )
            )
        }


    }

    /**
     * 도어락 잠금해제
     */
    @PostMapping(value = ["{id}/unlock"])
    @CrossOrigin
    fun doUnlock(@PathVariable(name = "id") id: String,
                 @RequestBody(required = false) fac:HamsGisFacInfo?,
                 @RequestParam(name = "target", required = false , defaultValue = "1") target:Int
    ): ResponseEntity<ResultResponse> {
        return try {
            val result = hamsService.doLock(id,target,1)
            var data = fac ?: result
            if (result!=null) {
                data?.status?.frontDoor = result.status.frontDoor ?: data?.status?.frontDoor
                data?.status?.frontDoorLock = result.status.frontDoorLock ?: data?.status?.frontDoorLock
                data?.status?.backDoor = result.status.backDoor ?: data?.status?.backDoor
                data?.status?.backDoorLock = result.status.backDoorLock ?: data?.status?.backDoorLock
                ResponseEntity.ok(
                    ResultResponse(
                        ApiConstants.RESULT.SUCCESS_MESSAGE, ApiConstants.RESULT.SUCCESS_MESSAGE, data
                    )
                )
            } else {
                ResponseEntity.status(HttpStatus.OK).body(
                    ResultResponse(
                        ApiConstants.RESULT.FAIL_CODE, ApiConstants.RESULT.FAIL_MESSAGE, data
                    )
                )
            }
        } catch (e: Exception) {
            logger.error(e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResultResponse(
                    ApiConstants.RESULT.FAIL_CODE, ApiConstants.RESULT.FAIL_MESSAGE, null
                )
            )
        }
    }


    /**
     *
     * 무인국사 통계 조회 API
     *
     * @name : doorStat
     * @author : meiday
     * @since : 2022/08/17 9:57 AM
     * @param eqId FMS의 관리번호를 받아서 HAMS_(FMS 관리번호 uniq를 위한 prefix)를 제거하고 조회한다.
     * @param doorType 도어 도어락을 구분짓는 값 1 - 도어 , 2 - 도어락
     * @param frontBack  1 - 앞문 , 2 - 뒷문
     * @param dateType  Y - 년 , M - 월 , D - 일자
     * @param startDtm   조회 시작일자 yyyyMMdd format 이외의 것은 허용하지않는다.
     * @param endDtm  조회 종료일자 yyyyMMdd format 이외의 것은 허용하지않는다.
     * @return
     * @throws
     */
    @GetMapping("/{id}/stat")
    fun doorStat(
        @PathVariable(name="id") eqId: String,
        @RequestParam(name="doorType",required = false,defaultValue = "1") doorType:Char,
        @RequestParam(name="frontBack",required = false, defaultValue = "1") frontBack:Char,
        @RequestParam(name="dateType",required = false, defaultValue = "Y") dateType : Char,
        @RequestParam(name="startDtm",required = false, defaultValue = "") startDtm : String,
        @RequestParam(name="endDtm",required = false, defaultValue = "") endDtm : String
    ): ResponseEntity<Any>? {

        return try {
            val result = hamsService.getStat(eqId.replaceFirst("HAMS_",""), frontBack, doorType,dateType, startDtm, endDtm)
            if (result != null) {
                ResponseEntity.ok(result)
            } else {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResultResponse(
                        ApiConstants.RESULT.FAIL_CODE, ApiConstants.RESULT.FAIL_MESSAGE, null
                    )
                )
            }


        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResultResponse(
                    ApiConstants.RESULT.FAIL_CODE, ApiConstants.RESULT.FAIL_MESSAGE, null
                )
            )
        }
    }


    /**
     * hams 서버로의 최초 접속!
     * 단 한번만 한다.
     */
    @PostMapping("doConnectHams")
    fun firstTimeConnectHams(@RequestHeader(name = "id") id:String,
                             @RequestHeader(name = "pw") pw:String):ResponseEntity<ResultResponse>{

        return if (id == "rino" && pw == "rino1234"){
            val isConnect = hamsService.connectHams(false)
            if (isConnect){
                ResponseEntity.ok(ResultResponse(ApiConstants.RESULT.SUCCESS_CODE, ApiConstants.RESULT.SUCCESS_MESSAGE, null))
            }else {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultResponse(ApiConstants.RESULT.FAIL_CODE, ApiConstants.RESULT.FAIL_MESSAGE, null))
            }
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultResponse(ApiConstants.RESULT.FAIL_NO_AUTH, ApiConstants.RESULT.FAIL_NO_AUTH, null))
        }

    }

    @GetMapping(value = ["client/isConnect"])
    fun isConnect(): ResponseEntity<Any> {

        return ResponseEntity.ok(ResultResponse(ApiConstants.RESULT.SUCCESS_CODE,
            ApiConstants.RESULT.SUCCESS_MESSAGE,HamsSendClient.getInstance().isConnect()))
    }

    @GetMapping(value = ["updateFms"])
    fun updateFms(): ResponseEntity<Any> {
        logger.info("START HAMS UPDATE!!!")
        hamsService.updateFmsData()
        logger.info("END HAMS UPDATE!!!")
        return ResponseEntity.ok(ResultResponse(ApiConstants.RESULT.SUCCESS_CODE,
            ApiConstants.RESULT.SUCCESS_MESSAGE,HamsSendClient.getInstance().isConnect()))
    }

}