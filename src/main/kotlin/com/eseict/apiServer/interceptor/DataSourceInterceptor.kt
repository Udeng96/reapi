package com.eseict.apiServer.interceptor

import com.eseict.apiServer.conf.DataContextEnum
import com.eseict.apiServer.conf.db.DataContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class DataSourceInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val dataContext: String? = request.getHeader("dataContext")
        if (DataContextEnum.FIRE_STATION.toString().equals(dataContext, ignoreCase = true)) {
            DataContextHolder.setDataContext(DataContextEnum.FIRE_STATION)
        } else {
            DataContextHolder.setDataContext(DataContextEnum.RINO)
        }
        return super.preHandle(request, response, handler)
    }
}