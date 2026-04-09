package com.eseict.apiServer.interceptor

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.oms.UserInfo
import com.eseict.apiServer.service.rino.oms.OmsService
import com.google.common.base.Strings
import com.google.gson.Gson
import org.apache.http.HttpStatus
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenInterceptor() : HandlerInterceptor {

    private final val LOGGER = LoggerFactory.getLogger(TokenInterceptor::class.java)
    val gson = Gson()

    @Autowired
    lateinit var omsSerivce: OmsService

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        val reqUri: String = request.requestURI
        var session = request.session
        /**
         * 먼저 재난상황쪽만 일시적으로 삽입하여 사용
         */
        return if (reqUri.contains("rino") || reqUri.contains("smsgroups")) { //토큰을 검사해야하는 부분인경우
            val token = request.getHeader("token")

            if (!Strings.isNullOrEmpty(token)) {//토큰이 있는경우
                val user: UserInfo? = omsSerivce.fetchUserInfo(token)
                if (user != null) {
                    session.setAttribute("user", user)
                    true
                } else {
                    false
                }

            } else {
                writeAndClose(ApiConstants.RESULT.FAIL_NO_AUTH, response, HttpStatus.SC_UNAUTHORIZED)
                false
            }


        } else { //토큰 검사가 불필요한경우
            true
        }
    }


    private fun writeAndClose(message: String, response: HttpServletResponse, status: Int) {
        response.setHeader("Content-Type", ApiConstants.API_PRODUCES)
        response.status(status)
        response.writer.write(gson.toJson(com.eseict.apiServer.dto.common.Message(message)))
    }
}

private fun HttpServletResponse.status(status: Int) {
    this.status(status)
}

