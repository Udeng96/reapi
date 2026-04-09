package com.eseict.apiServer.util

import com.eseict.apiServer.conf.ApiConstants
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpEntity
import org.apache.hc.core5.http.io.entity.ByteArrayEntity
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.io.entity.StringEntity
import org.apache.hc.core5.http.message.BasicHeader
import org.apache.hc.core5.http.message.BasicNameValuePair
import org.apache.hc.core5.net.URIBuilder
import org.apache.hc.core5.util.Timeout
import java.io.IOException
import java.net.URISyntaxException

// 지극히 개인적인 의견을 남겨보고자 합니다(안성진 의견)
// kotlin이 제공하는 it은 지양하고! 가독성이 좋도록, 임시 명칭으로 명명하는 것으로 의견드립니당. 임시 명칭은... 언더바로 하는거 추천합니당
// 그리고 또.. 이건 정말정말 개인적인 의견을 덧붙이자면 코틀린은 한 줄로 적는 게 더 가독성이 좋아 보입니다!!!(이건 정말정말 개인취향인 의견임당)
/*
 1. persons.map{
   NewPerson(it.name, it.age)
 } (X)
 2. persons.map{ _person -> NewPerson(_person.name, _person.age) } (O)  그리고 NewObject부분이 길어질 때만 enter 한-두번 의견드립니다.
 3. persons.map{ _person ->
   NewPerson(_person.name, _person.age, _person.address, _person.number, _person.company, _person.email, _person.gender, _person.etc)
 }
 */
fun httpGet(
    reqUrl: String,
    parameter: List<Pair<String, Any>>? = null,
    header: Map<String, Any>? = null,
    timeout: Long = 3000,
    contentTypeJson: Boolean = true,
    addParamtoUrl: Boolean? = false
): String? {
    var result: String? = null
    val httpClient = HttpClients.createDefault()
    var url = URIBuilder(reqUrl)

    if (addParamtoUrl == true) {
        var newUrl = reqUrl
        for (index in parameter?.indices!!) {
            val param = "${parameter.get(index).first}=${parameter.get(index).second}"
            newUrl = newUrl.plus(if (index == 0) "?" else "&").plus(param)
        }
        url = URIBuilder(newUrl)
    } else {
        parameter?.map { _param -> BasicNameValuePair(_param.first, _param.second.toString()) }
            ?.let { _params -> url.addParameters(_params) }
    }

    try {
//        ApiConstants.HTTP_GET_LOGGER.info("request url is [{}]",reqUrl);
        val httpGet = HttpGet(url.build())
        httpGet.apply { config = RequestConfig.custom().setConnectTimeout(Timeout.ofMilliseconds(timeout)).build() }
        if (contentTypeJson) httpGet.setHeader("Content-Type", "application/json")
        header?.entries?.map { _header -> BasicHeader(_header.key, _header.value) }
            ?.forEach { _header -> httpGet.setHeader(_header) }
        val response = httpClient.execute(httpGet)
        val entity: HttpEntity = response.entity
        result = EntityUtils.toString(entity)
        //ApiConstants.HTTP_GET_LOGGER.info("response is [{}]",result);
        EntityUtils.consume(entity)
    } catch (error: URISyntaxException) {
        error.printStackTrace()
    } catch (error: IOException) {
        error.printStackTrace()
    }
    return result
}

fun httpPost(
    reqUrl: String,
    parameter: List<Pair<String, Any>>? = null,
    header: Map<String, Any>? = null,
    body: Any? = null,
    timeout: Long = 3000,
    contentTypeJson: Boolean = true
): String? {
    var result: String? = null
    val httpClient = HttpClients.createDefault()
    val url = URIBuilder(reqUrl)
    parameter?.map { _param -> BasicNameValuePair(_param.first, _param.second.toString()) }
        ?.let { _params -> url.setParameters(_params) }
    try {
        //ApiConstants.HTTP_POST_LOGGER.info("request url is [{}]",reqUrl);
        val httpPost = HttpPost(url.build())

        // 1. bytearray body 요청해야 하는 상황이 아니고 gson, jackson 의존성 둘 다 없는 상황이라면 이거 사용!
        // bytearray body 요청을 해야하는 상황이라면 2번 또는 3번을 사용
//        httpPost.entity = StringEntity(body.toString())

        // 2. bytearray body 요청해야 하는 상황이라면, gson 은 없고 jackson 이 있다면 jackson 사용(부트는 디폴트라서 있음)
        httpPost.entity = ByteArrayEntity(ObjectMapper().writeValueAsBytes(body), ContentType.APPLICATION_JSON)

        // 3. bytearray body 요청해야 하는 상황이라면, jackson 은 없고 gson 이 있다면 이거 사용
//        httpPost.entity = ByteArrayEntity(Gson().toJson(body).toByteArray(Charsets.UTF_8), ContentType.APPLICATION_JSON)

        httpPost.apply { config = RequestConfig.custom().setConnectTimeout(Timeout.ofMilliseconds(timeout)).build() }
        if (contentTypeJson) httpPost.setHeader("Content-Type", "application/json")
        header?.entries?.map { _header -> BasicHeader(_header.key, _header.value) }
            ?.forEach { _header -> httpPost.setHeader(_header) }
        val response = httpClient.execute(httpPost)
        val entity: HttpEntity = response.entity
        result = EntityUtils.toString(entity)
        //ApiConstants.HTTP_POST_LOGGER.info("response is [{}]",result);
        EntityUtils.consume(entity)
    } catch (error: URISyntaxException) {
        error.printStackTrace()
    } catch (error: IOException) {
        error.printStackTrace()
    }
    return result
}