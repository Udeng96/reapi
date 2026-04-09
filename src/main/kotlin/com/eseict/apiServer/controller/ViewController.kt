package com.eseict.apiServer.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@Controller
@RequestMapping(value = [""])
class ViewController {

    final val logger: Logger = LoggerFactory.getLogger(ViewController::class.java)

    @RequestMapping(value = ["/gis"])
    fun gis(
        @RequestParam(required = false, defaultValue = "") token: String="",
        session: HttpSession,
        request: HttpServletRequest,
        response : HttpServletResponse
    ): String {
        val id = session.id
        logger.info("token is {}",token)
        logger.info("session id is {}",id)
        session.setAttribute("token", token)
        session.setAttribute("__TOKEN__", token)


        response.addCookie(Cookie("token",token))

        return "index";
    }



    @RequestMapping(value = ["/hams"])
    fun hams(@RequestParam(required = false, defaultValue = "") token: String = "",
             request : HttpServletRequest,
             response : HttpServletResponse,
             session: HttpSession): String {

        logger.info("token is {}",token)
        session.setAttribute("token", token)
        response.addCookie(Cookie("token",token))
        return "index";
    }

    @RequestMapping(value = ["cctvMonitor"])
    fun monitor(@RequestParam(required = false, defaultValue = "") token: String = "",
                request : HttpServletRequest,
                response : HttpServletResponse,
                session: HttpSession): String {

        logger.info("token is {}",token)

        session.setAttribute("token", token)
        response.addCookie(Cookie("token",token))
        return "index";
    }
}