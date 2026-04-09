package com.eseict.apiServer.controller.mobile

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.domain.eas.LiveViewMobileInfo
import com.eseict.apiServer.dto.common.CommonResponse
import com.eseict.apiServer.dto.mobile.LiveViewMobileResponse
import com.eseict.apiServer.dto.mobile.XeusAssetCctv
import com.eseict.apiServer.proc.rino.msgProc.SocketMsgQueue
import com.eseict.apiServer.service.mobile.XeusLiveViewEventService
import com.google.gson.Gson
import org.json.simple.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.Charset

@RestController
@RequestMapping(value = ["xeus/cctv"], produces = [ApiConstants.API_PRODUCES])
class XeusCctvRcvController(val gson: Gson) {

    private final val logger: Logger = LoggerFactory.getLogger(XeusCctvRcvController::class.java)

    @Autowired
    lateinit var service: XeusLiveViewEventService

    /**
     * 화성시 기준으로 변경을 진행함
     * mgr_no 값이 전화번호보다 작게 길이가 설정되어 개발 디비에서는 늘려놓음
     *
     * geometry 변환 함수가 없어서 null 처리
     * _gid 값을 무엇으로 셋팅해야하는지 몰라 고정 값으로 처리
     *
     * 2022.12.07
     * addCctv를 해서 현황 테이블에 장비를 등록한다.
     * 만약 같은 값이 있는경우에는 no action
     */
    @RequestMapping(value = ["addCctv.json"])
    fun addCctv(
        @RequestParam(value = "userId", defaultValue = "", required = false) userId: String?,  //계정 ID
        @RequestParam(value = "mgrNo", defaultValue = "", required = false) mgrNo: String,  //계정 전화번호
        @RequestParam(value = "orgMgrNo", defaultValue = "000001", required = false) orgMgrNo: String?,  //고정값
        @RequestParam(value = "mdMgrNo", defaultValue = "MDL0000014", required = false) mdMgrNo: String?,  //고정값
        @RequestParam(value = "vmsMgrNo", defaultValue = "VMS0000002", required = false) vmsMgrNo: String?,  //고정값
        @RequestParam(value = "siteMgrNo", defaultValue = "", required = false) siteMgrNo: String?,  //계정 이름
        @RequestParam(value = "cctvNm", defaultValue = "", required = false) cctvNm: String?,  //계정 이름
        @RequestParam(value = "deviceId", defaultValue = "", required = false) deviceId: String?,  //영상주소 RTSP
        @RequestParam(value = "chnlNo", defaultValue = "0", required = false) chnlNo: String?,  //고정값
        @RequestParam(value = "gbnCd", defaultValue = "99", required = false) gbnCd: String?,  //고정값
        @RequestParam(value = "useYn", defaultValue = "Y", required = false) useYn: String?,  //고정값
        @RequestParam(value = "lightYn", defaultValue = "N", required = false) lightYn: String?,  //고정값
        @RequestParam(value = "infrdYn", defaultValue = "N", required = false) infrdYn: String?,  //고정값
        @RequestParam(value = "panYn", defaultValue = "N", required = false) panYn: String?,  //고정값
        @RequestParam(value = "tiltYn", defaultValue = "N", required = false) tiltYn: String?,  //고정값
        @RequestParam(value = "zoomYn", defaultValue = "N", required = false) zoomYn: String?,  //고정값
        @RequestParam(value = "talkYn", defaultValue = "N", required = false) talkYn: String?,  //고정값
        @RequestParam(value = "tourYn", defaultValue = "N", required = false) tourYn: String?,  //고정값
        @RequestParam(value = "locDesc", defaultValue = "", required = false) locDesc: String?,  //계정이름
        @RequestParam(value = "lng", defaultValue = "", required = false) lng: String?,  //경도
        @RequestParam(value = "lat", defaultValue = "", required = false) lat: String? //위도
    ): String? {
        val assetCctv = XeusAssetCctv(
            userId,
            mgrNo.replace("-".toRegex(), ""),
            orgMgrNo,
            mdMgrNo,
            vmsMgrNo,
            siteMgrNo,
            cctvNm,
            deviceId,
            chnlNo,
            gbnCd,
            useYn,
            lightYn,
            infrdYn,
            panYn,
            tiltYn,
            zoomYn,
            talkYn,
            tourYn,
            locDesc,
            lng,
            lat
        )
        logger.info("addCctv {}",assetCctv.toString())
        val phoneNumberList: MutableList<String> = ArrayList()
        phoneNumberList.add(assetCctv.mgrNo!!)
        //로직 변경 됨
        val response = CommonResponse(service.handleAddCctvApi(phoneNumberList, assetCctv))

        Thread.sleep(3000)

        var json = JSONObject()
        json["data"] = "mobile"
        SocketMsgQueue.getInstance()
            .put(String(json.toJSONString().toByteArray(), Charset.forName(ApiConstants.ENCODING)))

        return gson.toJson(response)
    }


    /**
     * 촬영 중, 촬영 종료에 대한 정보가 들어온다.
     *
     */
    @RequestMapping(value = ["addMobile.json"])
    fun addMobile(
        @RequestParam(value = "userId", defaultValue = "", required = false) userId: String?,  //계정 ID
        @RequestParam(value = "userDtm", defaultValue = "", required = false) userDtm: String?,  //시간이지만 null로고정
        @RequestParam(value = "mgrNo", defaultValue = "", required = false) mgrNo: String,  //계정 전화번호
        @RequestParam(value = "lon", defaultValue = "0.0", required = false) lng: String,  //위도
        @RequestParam(value = "lat", defaultValue = "0.0", required = false) lat: String,  //경도
        @RequestParam(
            value = "mobileChk",
            defaultValue = "",
            required = false
        ) mobileChk: String?,  //현재상태 1:미실행 2:촬영시작 3:촬영중 4:촬영종료
        @RequestParam(value = "mbUrl", defaultValue = "", required = false) mbUrl: String?,  //영상주소
        @RequestParam(value = "title", defaultValue = "", required = false) title: String? //영상 시작시 제목
    ): String? {

        logger.info("addMobile values -  userId:{} , userDtm:{} ,mgrNo:{} , lng:{} ,lat:{}, mobileChk:{} , mbUrl:{}, title:{} ",
            userId,userDtm,mgrNo,lng,lat,mobileChk,mbUrl,title)
        val gson = Gson()
        val info = LiveViewMobileInfo(
            mgrNo.replace("-".toRegex(), ""), lat.toDouble(), lng.toDouble(), mbUrl, mobileChk, title, userDtm, userId
        )
        val result: String = service.handleLiveViewMobile(info)
        logger.info("addMobile Result : [{}]", result)
        val response: LiveViewMobileResponse = service.getLiveViewMobileResponse()


        Thread.sleep(3000)

        var json = JSONObject()
        json["data"] = "mobile"
        SocketMsgQueue.getInstance()
            .put(String(json.toJSONString().toByteArray(), Charset.forName(ApiConstants.ENCODING)))


        return gson.toJson(response)
    }

    @RequestMapping(value = ["getMobileList.json"])
    fun getMobile(): String? {
        val gson = Gson()
        val response: LiveViewMobileResponse = service.getLiveViewMobileResponse()
        return gson.toJson(response)
    }

}