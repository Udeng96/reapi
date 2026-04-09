package com.eseict.apiServer.service.standardSsytem

import com.eseict.apiServer.dto.standardSystem.FireCarInfo
import com.eseict.apiServer.util.httpGet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service("standardSystemService")
class StandardSystemService(val gson: Gson) {

    private final val logger = LoggerFactory.getLogger(StandardSystemService::class.java)

    @Value("\${platform.ocs.url}")
    lateinit var ocsUrl:String

    @Value("\${platform.ocs.subUrls.outTruck}")
    lateinit var getOutTruckList:String

    fun getStandardSystemFireCar(uSvcOutbId:String):List<FireCarInfo>{

        val response = httpGet("$ocsUrl${getOutTruckList.replace("{orcNo}",uSvcOutbId)}")

        return  try{
            gson.fromJson<List<FireCarInfo>>(response, object : TypeToken<List<FireCarInfo>>(){}.type)
        }catch(e : java.lang.NullPointerException){
            listOf<FireCarInfo>()
        }

    }
}