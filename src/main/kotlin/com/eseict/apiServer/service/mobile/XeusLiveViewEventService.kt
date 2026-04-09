package com.eseict.apiServer.service.mobile

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.domain.eas.LiveViewMobileInfo
import com.eseict.apiServer.domain.eas.MonMbDesc
import com.eseict.apiServer.domain.eas.MonMbDescLog
import com.eseict.apiServer.domain.eas.XeusEventObj
import com.eseict.apiServer.dto.mobile.CctvInfoResponse
import com.eseict.apiServer.dto.mobile.LiveViewMobileResponse
import com.eseict.apiServer.dto.mobile.XeusAssetCctv
import com.eseict.apiServer.dto.oms.UserInfoForDecrypt
import com.eseict.apiServer.repository.eas.CctvInfoDao
import com.eseict.apiServer.repository.eas.XeusLiveViewEventDao
import com.eseict.apiServer.repository.mobile.MobileLiveRepository
import com.eseict.apiServer.repository.mobile.MobilePastRepository
import com.eseict.apiServer.repository.rino.oms.UserRepository
import com.eseict.common.net.http.HttpConnection
import com.eseict.common.net.http.responseHandler.JsonToMapHandler
import com.google.common.collect.Maps
import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.awt.SystemColor.info
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Service("xeusLiveViewEventService")
class XeusLiveViewEventService(
    val mobileLiveRepository: MobileLiveRepository,
    val mobilePastRepository: MobilePastRepository,
    val userRepository: UserRepository,
    val gson : Gson
) {

    private val logger: Logger = LoggerFactory.getLogger(XeusLiveViewEventService::class.java)

    @Autowired
    lateinit var dao: XeusLiveViewEventDao

    @Autowired
    lateinit var cctvInfoDao: CctvInfoDao


    @Value("\${platform.cryptsvr.system.key}")
    lateinit var cryptSystemKey:String

    @Value("\${platform.cryptsvr.system.secret}")
    lateinit var cryptSystemSecret:String

    @Value("\${platform.cryptsvr.crypt.type}")
    lateinit var cryptType:String

    @Value("\${platform.cryptsvr.api.address}")
    lateinit var cryptAddress:String
    @Value("\${platform.cryptsvr.decrypt}")
    lateinit var cryptDecrypt:String

    /**
     * 종료가 들어온경우 분기 처리 해야함
     */
    fun handleLiveViewMobile(info: LiveViewMobileInfo): String {
        val find: LiveViewMobileInfo? = dao.selectLiveViewMobileInfo(info.mgrNo)
        val dptNm = userRepository.findOmsUserInfoEntitiesAndDptOrgnByUserLoginId(info.userId!!)

        val findMonMbDesc: MonMbDesc? = try {
            mobileLiveRepository.findByUserId(info.userId!!)
        }catch(e:java.lang.Exception){
            null
        }

        var result = if (find == null) {
            dao.insertLiveViewMobileInfo(info)
        } else {
            dao.updateLiveViewMobileInfo(info)
        }

        var userNm = "";
        val user = userRepository.findByUserLoginId(info.userId!!)
        if (user != null){

            val handler = JsonToMapHandler()
            val httpConn = HttpConnection(handler)

            val reqHeader: Map<String, Any> = this.getReqHeaderMap()
            val reqParam: MutableMap<String, Any> = mutableMapOf()
            var decryptUser = UserInfoForDecrypt(user?.userId!!,user?.userLoginId!!, user?.userNm!!,user.cpNo,user.email,user.dptId,dptNm,user?.encryptKey!!)


            var cryptDataMapList: MutableList<Map<String, String>> = mutableListOf()
            var crypteDataMap: MutableMap<String, String> = Maps.newHashMap()
            crypteDataMap["userName"] = user.userNm!!
            crypteDataMap["cpNo"] = user.cpNo!!
            crypteDataMap["encryptKey"] = user.encryptKey!!
            cryptDataMapList.add(crypteDataMap)

            reqParam.put("data", gson.toJson(cryptDataMapList))
            reqParam.put("encType", cryptType)

            val resultMap: Map<String, Any> = httpConn.doPost(
                cryptAddress + cryptDecrypt,
                reqHeader,
                reqParam
            )
            val resultDataList: List<Map<String?, String?>> = gson.fromJson<List<Map<String?, String?>>>(
                resultMap["data"] as String,
                MutableList::class.java
            )

            if (resultDataList.size == 1){
                try {

                    decryptUser.encryptKey = resultDataList.get(0).get("encryptKey") as String
                    userNm = resultDataList.get(0).get("userName") as String
                    decryptUser.cpNo = resultDataList.get(0).get("cpNo") as String

                }catch(e:UnsupportedOperationException){
                    logger.error("user {}", user.toString())
                    logger.error(e.message,e)
                }
            }
        }


        val mobileInfo = if (findMonMbDesc == null) {
            //user 이름 이 비어있으니 불러와서 값을 셋팅해야한다.
            val live = MonMbDesc(
                info.userId!!,
                userNm,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_14)),
                info.lat, info.lng,
                mobileChk = 1, mbUrl = info.mbUrl, dptNm = dptNm, mgrNo = info.mgrNo
            )
            logger.info("addInfo {}", live.toString())
            live
        } else {
            val find = mobileLiveRepository.findByUserId(userId = info.userId!!)
            find.userNm = userNm
            find.mobileChk = info.mobileCheck?.toInt()
            find.mbUrl = info.mbUrl
            find.lat = info.lat
            find.lon = info.lng
            find.title = info.liveTitle
            find.userDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_14))
            find
        }

        result = try {
            mobileLiveRepository.save(mobileInfo)
            logger.info("SAVE SUCCESS {}",mobileInfo.toString())
            ApiConstants.ResponseConstants.RESPONSE_SUCCESS
        } catch (e: Exception) {
            logger.info("FAIL TO SAVE {}")
            logger.error(e.message, e)
            ApiConstants.ResponseConstants.RESPONSE_FAIL
        }

        if (info.mobileCheck.equals("4") ) {//촬영종료가 들어온 경우 log 테이블에도 정보를 저장한다.

            val log = MonMbDescLog(
                null,
                mobileInfo.userId,
                mobileInfo.userNm,
                mobileInfo.userDtm, mobileInfo.lat, mobileInfo.lon,null, 5,
                mobileInfo.mbUrl, "vod", mobileInfo.title, mobileInfo.dptNm, mobileInfo.mgrNo
            )

            logger.info("log {}",log)
            mobilePastRepository.save(log)
        }else {
            logger.info("IS NOT CLOSE")
        }



        return result
    }

    fun getLiveViewMobileResponse(): LiveViewMobileResponse {
        val list: List<LiveViewMobileInfo?>? = dao.selectLiveViewMobileList()
        return LiveViewMobileResponse(
            "true", "false", list
        )
    }

    fun handleAddCctvApi(phoneNumberList: List<String?>? = listOf(), assetCctv: XeusAssetCctv): String {
        var result: String = ApiConstants.ResponseConstants.RESPONSE_FAIL


        val dptNm = userRepository.findOmsUserInfoEntitiesAndDptOrgnByUserLoginId(assetCctv.userId!!) ?: null

        if (dptNm != null) {

            val find = try{
                mobileLiveRepository.findByUserId(assetCctv.userId!!)
            }catch(e:Exception){
                null
            }
            if (find == null){
                val mobileInfo = MonMbDesc(
                    assetCctv.userId!!,
                    assetCctv.siteMgrNo,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_14)),
                    assetCctv.lat?.toDouble(),
                    assetCctv.lng?.toDouble(),
                    mobileChk = 1,
                    mbUrl = assetCctv.deviceId,
                    dptNm = dptNm,
                    mgrNo = assetCctv.mgrNo
                )
                mobileLiveRepository.save(mobileInfo)
            }else {
                find.mobileChk =1
                find.userDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_14))
                find.mbUrl = assetCctv.deviceId
                find.lat = assetCctv.lat?.toDouble()
                find.lon = assetCctv.lng?.toDouble()
                mobileLiveRepository.flush()
            }

        }
//        val cctvInfoList: List<CctvInfoResponse?>? = cctvInfoDao.selectLiveViewInfoList(phoneNumberList)
//        if (cctvInfoList != null) {
//            result = if (cctvInfoList.isEmpty()) {
//                cctvInfoDao.insertLiveCctvInfo(assetCctv) ?:""
//            } else {
//                cctvInfoDao.updateLiveCctvInfo(assetCctv) ?: ""
//            }
//        }
        return ApiConstants.ResponseConstants.RESPONSE_SUCCESS
    }

    fun getLiveViewEventList(): List<XeusEventObj?>? {
        val list: List<XeusEventObj?>? = dao.selectXeusLiveViewEvnetList()
        val phoneNumberList: MutableList<String> = ArrayList()

        if (!list.isNullOrEmpty()) {
            for (event in list) {
                if (event?.phoneNumber.isNullOrEmpty()) {
                    phoneNumberList.add(event!!.phoneNumber!!)
                }
            }
            val cctvInfoList: List<CctvInfoResponse?>? = cctvInfoDao.selectLiveViewInfoList(phoneNumberList)
            for (eventObj in list) {

                try {
                    val cctv: CctvInfoResponse? = cctvInfoList?.stream()?.filter { cctvInfoResponse ->
                        cctvInfoResponse?.mgrNo.equals(eventObj?.phoneNumber)
                    }?.findFirst()?.get() ?: null

                    if (cctv != null) {
                        eventObj?.rtspUrl = cctv.rtspUrl
                        eventObj?.userName = cctv.cctvNm
                    }

                } catch (e: NoSuchElementException) {
                    logger.error("no search cctv by event")
                }

            }
        }

        return list
    }

    fun handleLiveViewEvent(eventObj: XeusEventObj): String {
        var result: String = ApiConstants.ResponseConstants.RESPONSE_FAIL
        result = if ("10" == eventObj.procSt) {
            val checkAlready: String = dao.selectLiveViewEventNow(eventObj.phoneNumber)
            if (ApiConstants.ResponseConstants.RESPONSE_SUCCESS == checkAlready) {
                dao.insertNewLiveViewEvent(eventObj)
            } else {
                ApiConstants.ResponseConstants.RESPONSE_SUCCESS
            }
        } else {
            dao.updateLiveViewEvent(eventObj)
        }

//        /**
//         * 현황테이블을 확인하여, merge를 수행, 촬영상태를 업데이트
//         */
//        val findMonMbDesc: MonMbDesc? = mobileLiveRepository.findByMgrNo(eventObj.phoneNumber!!)
//
//        val mobileInfo = if (findMonMbDesc == null) {
//            val dptNm = userRepository.findOmsUserInfoEntitiesAndDptOrgnByUserLoginId(info.userId!!)
//            MonMbDesc(
//                info.userId!!, LocalDateTime.now().format(DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_14)),
//                info.lat, info.lng,
//                mobileChk = 1, mbUrl = info.mbUrl, dptNm = dptNm, mgrNo = info.mgrNo
//            )
//
//        } else {
//            val find = mobileLiveRepository.findByUserId(userId = info.userId!!)
//            find.mobileChk = info.mobileCheck?.toInt()
//            mobileLiveRepository.save(find)
//            find
//        }
//
//        result = try {
//            mobileLiveRepository.save(mobileInfo)
//            ApiConstants.ResponseConstants.RESPONSE_SUCCESS
//        } catch (e: Exception) {
//            ApiConstants.ResponseConstants.RESPONSE_FAIL
//        }

        return result
    }

    fun clearLive(){
        var allList = mobileLiveRepository.findAll()

        val now = LocalDateTime.now()

        allList = allList?.map { data->
            if (!data.userDtm.isNullOrEmpty()){
                val userDtm = LocalDateTime.parse(data.userDtm, DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_14))
                val duration = Duration.between(userDtm, now)

                if (duration.toHours()>1){
                    data.mobileChk =4
                }
            }
            mobileLiveRepository.save(data)
        }
        mobileLiveRepository.flush()
    }


    fun getReqHeaderMap(): Map<String, Any> {
        val reqHeader: MutableMap<String, Any> = Maps.newHashMap()
        reqHeader["systemKey"] = cryptSystemKey
        reqHeader["secret"] = cryptSystemSecret
        return reqHeader
    }
}
