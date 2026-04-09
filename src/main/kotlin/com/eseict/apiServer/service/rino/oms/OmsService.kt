package com.eseict.apiServer.service.rino.oms

import com.eseict.apiServer.dto.oms.UserInfo
import com.eseict.apiServer.util.httpPost
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class OmsService() {

    private final val LOGGER = LoggerFactory.getLogger(OmsService::class.java)

    private val gson: Gson = Gson()

    @Value("\${platform.oms.url}")
    private lateinit var omsUrl: String

    @Value("\${platform.oms.subUrls.getUserInfo}")
    private lateinit var getUserUrl: String

    @Value("\${system.values.secret}")
    private lateinit var secret: String

    @Value("\${system.values.key}")
    private lateinit var key: String

    @Value("\${platform.fms.url}")
    lateinit var fmsUrl: String

    @Value("\${platform.fms.subUrls.icon}")
    lateinit var fmsIconUrl: String

    /**
     * oms로 유저정보를 요청, 반환
     */
    fun fetchUserInfo(token: String): UserInfo? {
        val type = object : TypeToken<UserInfo>() {}.type
        val response = httpPost(omsUrl.plus(getUserUrl), getUserParam(token), getHeader())
        return gson.fromJson<UserInfo>(response, type)
    }

    fun getUserParam(token: String): List<Pair<String, Any>> {
        return mutableListOf(Pair("token", token))
    }

    fun getHeader(): Map<String, Any> {
        return hashMapOf<String, Any>("systemKey" to key, "secret" to secret)
    }


}