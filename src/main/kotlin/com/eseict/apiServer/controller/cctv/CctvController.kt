package com.eseict.apiServer.controller.cctv

import com.eseict.apiServer.conf.ApSystemProperties
import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.cctv.PtzInfo
import com.eseict.apiServer.dto.common.CommonResponse
import com.eseict.apiServer.dto.common.Message
import com.eseict.apiServer.dto.fac.CctvMonitor
import com.eseict.apiServer.service.cctv.CctvControlService
import com.eseict.apiServer.service.rino.fms.FacClfyService
import com.eseict.apiServer.service.rino.fms.FacService
import com.eseict.apiServer.service.rino.oms.OmsService
import com.eseict.apiServer.util.CommonUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping(value = ["cctv"], produces = [ApiConstants.API_PRODUCES])
class CctvController(
    val systemProperties: ApSystemProperties,
    val facService: FacService,
    val facClfyService: FacClfyService,
    val omsService: OmsService,
    val cctvControlService: CctvControlService,
    val gson: Gson

) {

    private final val LOGGER: Logger = LoggerFactory.getLogger(CctvController::class.java)

    //401 error 처리, 400 에러 처리 생성 필요
    /**
     * 화성은 CCTV 분륙가 depth가 구분되어있지않음
     * FAC CLFY 의 CLFY_NM으로 구분을 지어야함
     *
     * %CCTV% 로 조회하는데 , lowercase uppercase 생각해서 하기 조건절에 삽입을 해야함
     *
     * type 은 CCTV 설치 목적에 해당하는 param 임
     *
     * page 처리 jpaRepository 기능 사용
     */
    @GetMapping(value = [""])
    fun getCctvList(
        @RequestParam(required = false, defaultValue = "") type: String?, //  시설물 분류 id
        @RequestParam(required = false, defaultValue = "") cctvNm: String?, // 시설물(cctv) 이름
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) rows: Int?,
    ): ResponseEntity<Any> {

        return try {
            ResponseEntity.ok(facService.getFacList(type, cctvNm, page ?: -1, rows ?: 10))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(e.message!!))
        }
    }


    @GetMapping(value = ["/monitor"])
    fun getCctvListGrpByClfy(
        @RequestParam(required = false, defaultValue = "") clfyId: String?,
    ): ResponseEntity<Any> {
        return try {
            val isDev = System.getenv()["IS_DEV"]?.toBoolean()

            if (isDev == true) {
                val data = CommonUtil.getSampleData("/exData/monitor/cctvList.json");
                val result: List<CctvMonitor>? = if (!clfyId.isNullOrEmpty()) {
                    gson.fromJson<List<CctvMonitor>>(data, object : TypeToken<List<CctvMonitor>>() {}.type)
                        .filter { it.clfyId == clfyId }
                } else {
                    gson.fromJson<List<CctvMonitor>>(data, object : TypeToken<List<CctvMonitor>>() {}.type)
                }
                ResponseEntity.ok(result)
            } else {

                val disasterWideData = CommonUtil.getSampleData("/cctvData/disasterWideList.json");
                val disasterWideList : List<CctvMonitor>? = if(!clfyId.isNullOrEmpty()){
                    gson.fromJson<List<CctvMonitor>>(disasterWideData, object : TypeToken<List<CctvMonitor>>(){}.type).filter { it.clfyId == clfyId }
                }else{
                    gson.fromJson<List<CctvMonitor>>(disasterWideData, object : TypeToken<List<CctvMonitor>>() {}.type)
                }


                val fmsCctvList = facService.getCctvListGrpByClfy(clfyId);

                val result : MutableList<CctvMonitor> = ArrayList();


                result.addAll(fmsCctvList);
                if (disasterWideList != null) {
                    for(disaster in disasterWideList){
                        disaster.cctvList = facService.getNewCctvMonitorList(disaster);
                        result.add(disaster);
                    }
                };

//                ResponseEntity.ok(facService.getCctvListGrpByClfy(clfyId));
                ResponseEntity.ok(result);
            }


        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(e.message!!))
        }
    }


    @GetMapping(value = ["/{cctvId}"])
    fun getCctv(@PathVariable(name = "cctvId") cctvId: String): ResponseEntity<Any> {

        return try {
            ResponseEntity.ok(facService.getFac(cctvId))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(e.message!!))
        }
    }

    /**
     * erf_fac_clfy  테이블
     *
     */
    @GetMapping(value = ["/clfy"])
    fun getCctvInfo(
    ): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(facClfyService.getCctvClfy())
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(e.message!!))
        }
    }


    //PTZ 제어 및 설정 가능 값 조회
    //연속 제어
    //설정 값 조회 후 + -실시


    /**
     * 요청
     */
    @PostMapping(value = ["/control/{cctvId}"])
    fun doControlCctv(@PathVariable(name = "cctvId" ) cctvId : String,
                        @RequestBody controlInfo :PtzInfo
    ):ResponseEntity<Any>{

        return try {
            val result = cctvControlService.doControl(controlInfo.also { it.cctvId = cctvId })

            if (result.equals("true",ignoreCase = true)){ //성공한경우
                ResponseEntity.ok(CommonResponse(result))
            }else{
                ResponseEntity.ok(CommonResponse("FAIL",result))
            }


        }catch(e:Exception){
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message(e.message!!))
        }
    }


}