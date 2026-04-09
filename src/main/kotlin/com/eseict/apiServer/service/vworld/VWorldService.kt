package com.eseict.apiServer.service.vworld

import com.eseict.apiServer.components.VWorldParser
import com.eseict.apiServer.dto.openapi.vworld.VWorldPositionInfo
import com.google.gson.Gson
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class VWorldService(val vWorldParser: VWorldParser) {

    private final val LOGGER = LoggerFactory.getLogger(VWorldService::class.java)

    fun searchPositionName(word: String, type:String): List<VWorldPositionInfo>? {
        return vWorldParser.getSearchList(word, type)
    }

    fun getPositionNameByLatLng(lat: Double, lng: Double): String {
        return vWorldParser.getPositionNameByLatLng(lat, lng)
    }


}