package com.eseict.apiServer.service.cctv

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.math.pow
import kotlin.math.roundToInt

@Service
class CctvService(
) {

    private final val LOGGER = LoggerFactory.getLogger(CctvService::class.java)


    //from 이 작은 수, to 를 큰 수
    fun getRandomDouble(from: Double, to: Double, fixed: Int = 6): Double {
        val ten = 10.0
        return ((from + Math.random() * (to - from)) * ten.pow(fixed)).roundToInt() / ten.pow(fixed)
    }

}