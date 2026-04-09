package com.eseict.apiServer.components

import com.eseict.apiServer.conf.ApSystemProperties
import com.eseict.apiServer.conf.ApiProperties
import com.eseict.apiServer.dto.openapi.vworld.VWorldPositionInfo
import com.eseict.apiServer.util.JsonParserUtil
import com.eseict.apiServer.util.ProxyUtil
import com.eseict.apiServer.util.httpGet
import com.eseict.common.base.SystemProperties
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.net.URLEncoder

@Component
class VWorldParser(
    val apiProperties: ApiProperties,
    val gson: Gson,
    val systemProperties: ApSystemProperties,
) {

    private final val LOGGER = LoggerFactory.getLogger(VWorldParser::class.java)
    private val isDev: Boolean = SystemProperties.getProperty("IS_DEV").toBoolean()

    fun setUrl(addr: String, type: String): String {
        val vWorldApi = apiProperties.getApi("vworld")
        val url = vWorldApi.url

        try {
            val encodeQuery = URLEncoder.encode(addr, "UTF-8")
            val key = vWorldApi.params["key"]!!

            return when (type) {

                "place" ->
                    url + vWorldApi.subUrls["searchPos"].orEmpty()
                        .replace("{type}", type)
                        .replace("{key}", key)
                        .replace("&category={category}", "")
                        .plus("&query=$encodeQuery")

                "address" ->
                    url + vWorldApi.subUrls["searchPos"].orEmpty()
                        .replace("{type}", type)
                        .replace("{key}", key)
                        .replace("{category}", "parcel")
                        .plus("&query=$encodeQuery")

                "road" ->
                    url + vWorldApi.subUrls["searchPos"].orEmpty()
                        .replace("{type}", "address")
                        .replace("{key}", key)
                        .replace("{category}", "road")
                        .plus("&query=$encodeQuery")

                else ->
                    url + vWorldApi.subUrls["searchPos"].orEmpty()
                        .replace("{type}", "address")
                        .replace("{key}", key)
                        .replace("&category={category}", "parcel,road")
                        .plus("&query=$encodeQuery")
            }

        } catch (e: Exception) {
            LOGGER.error(e.message, e)
        }

        return ""
    }

    /* --------------------------------------------------------
       PLACE RESULT — title을 고유하게 구성 (title + 좌표)
    --------------------------------------------------------- */
    fun setPlaceResponseList(response: String): List<VWorldPositionInfo> {
        val resultList = mutableListOf<VWorldPositionInfo>()

        if (response.isBlank()) return emptyList()

        val placeResponses = try {
            JsonParserUtil.getJsonArray(
                response,
                arrayOf("response", "result", "items"),
                VWorldPositionInfo::class.java
            )
        } catch (e: Exception) {
            LOGGER.error("setPlaceResponseList parsing failed: ${e.message}")
            return emptyList()
        }

        placeResponses?.forEach { item ->

            val lat = item.point?.y ?: ""
            val lon = item.point?.x ?: ""

            // 🔥 place 고유 ID 생성
            val uniqueId = "${item.id}_${(item.title ?: "").hashCode()}_${lon}_${lat}"

            resultList += VWorldPositionInfo(
                uniqueId,
                item.title ?: "",
                item.address,
                item.point
            )
        }

        return resultList
    }


    /* ============================================
       ADDRESS — parcel + 좌표 기반 고유 ID 생성
    ============================================ */
    fun setAddressResponseList(response: String): List<VWorldPositionInfo> {
        val resultList = mutableListOf<VWorldPositionInfo>()

        if (response.isBlank()) return emptyList()

        val addressResponses = try {
            JsonParserUtil.getJsonArray(
                response,
                arrayOf("response", "result", "items"),
                VWorldPositionInfo::class.java
            )
        } catch (e: Exception) {
            LOGGER.error("setAddressResponseList parsing failed: ${e.message}")
            return emptyList()
        }

        addressResponses?.forEach { item ->
            val parcel = item.address?.parcel ?: ""
            val lat = item.point?.y ?: ""
            val lon = item.point?.x ?: ""

            // 🔥 address 고유 ID 생성
            val uniqueId = "${item.id}_${parcel}_${lon}_${lat}"

            resultList += VWorldPositionInfo(
                uniqueId,
                parcel,
                item.address,
                item.point
            )
        }

        return resultList
    }


    /* ============================================
       ROAD — bldnmdc + parcel + 좌표 기반 고유 ID
    ============================================ */
    fun setRoadResponseList(response: String): List<VWorldPositionInfo> {
        val resultList = mutableListOf<VWorldPositionInfo>()

        if (response.isBlank()) return emptyList()

        val roadResponses = try {
            JsonParserUtil.getJsonArray(
                response,
                arrayOf("response", "result", "items"),
                VWorldPositionInfo::class.java
            )
        } catch (e: Exception) {
            LOGGER.error("setRoadResponseList parsing failed: ${e.message}")
            return emptyList()
        }

        roadResponses?.forEach { item ->

            val address = item.address
            val parcel = address?.parcel ?: ""
            val bldnmdc = address?.bldnmdc ?: ""
            val lat = item.point?.y ?: ""
            val lon = item.point?.x ?: ""

            // 🔥 road 고유 ID 생성
            val uniqueId = "${item.id}_${bldnmdc}_${lon}_${lat}"

            val title = buildString {
                append("[동] $bldnmdc")
                if (parcel.isNotBlank()) append(" | $parcel")
                append(" | ($lon, $lat)")
            }

            resultList += VWorldPositionInfo(
                uniqueId,     // 🔥 id 고유값으로 완전 변경됨
                title,
                address,
                item.point
            )
        }

        return resultList
    }


    /* --------------------------------------------------------
       공통 검색 함수
    --------------------------------------------------------- */
    fun getSearchList(addr: String, type: String): List<VWorldPositionInfo>? {
        if (addr.trim().isEmpty()) return emptyList()

        if (type == "all") {
            val allList = mutableListOf<VWorldPositionInfo>()
            searchByAddress(addr, "place", setUrl(addr, "place"))?.let { allList.addAll(it) }
            searchByAddress(addr, "address", setUrl(addr, "address"))?.let { allList.addAll(it) }
            searchByAddress(addr, "road", setUrl(addr, "road"))?.let { allList.addAll(it) }
            return allList
        }

        return searchByAddress(addr, type, setUrl(addr, type))
    }

    fun searchByAddress(addr: String, type: String, reqUrl: String): List<VWorldPositionInfo>? {

        val response = try {
            httpGet(reqUrl, null, mutableMapOf<String, Any>()) as? String
        } catch (e: Exception) {
            LOGGER.error("VWorld API request failed: ${e.message}")
            null
        }

        LOGGER.info("type:{}, response: {}", type, response)

        if (response.isNullOrBlank()) return emptyList()

        return when (type) {
            "place" -> setPlaceResponseList(response)
            "address" -> setAddressResponseList(response)
            "road" -> setRoadResponseList(response)
            else -> emptyList()
        }
    }
    /**
     * 좌표 값으로 주소정보를 조회하는
     */
    fun getPositionNameByLatLng(lat: Double, lng: Double): String {
        val proxyUtil = ProxyUtil(systemProperties.getValue("proxy")!!)
        var result: String = ""

        val type = object : TypeToken<List<VWorldPositionInfo>>() {}.type

        val vWorldApi = apiProperties.getApi("vworld")
        val _url = if (isDev) vWorldApi.url else proxyUtil.getUrlWithProxy(vWorldApi.url)
        val _subUrl = vWorldApi.subUrls["getPos"]
        val _params = vWorldApi.params

        val _serviceKey = _params["key"]!!
        val _service = _params["serviceGet"]!!
        val _request = _params["requestGet"]!!
        val _version = _params["version"]!!
        val _crs = _params["crs"]!!
        val _point = lng.toString().plus(",").plus(lat.toString())
        val _type = _params["typeGet"]!!
        val _format = _params["format"]!!
        val _zipcode = _params["zipcode"]!!
        val _simple = _params["simple"]!!


        val url = if (isDev) vWorldApi.url else proxyUtil.getUrlWithProxy(vWorldApi.url)

        try {
            val response = httpGet(
                "$url$_subUrl",
                listOf(
                    "service" to _service,
                    "request" to _request,
                    "version" to _version,
                    "crs" to _crs,
                    "point" to _point,
                    "zipcode" to _zipcode.toBoolean(),
                    "simple" to _simple.toBoolean(),
                    "type" to _type,
                    "format" to _format,
                    "key" to _serviceKey
                )
            )

            result =
                JsonParser.parseString(response).asJsonObject.get("response").asJsonObject.get("result").asJsonArray.get(
                    0
                ).asJsonObject.get("text").toString()

            result = result.replace("\"".toRegex(), "")

        } catch (e: Exception) {
            LOGGER.error(e.message, e)
        }
        return result
    }


}