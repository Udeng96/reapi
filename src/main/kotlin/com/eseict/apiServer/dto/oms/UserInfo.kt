package com.eseict.apiServer.dto.oms

import com.eseict.apiServer.domain.rino.event.VwIocEvetInfo

data class UserInfo(
    val result: String,
    val data: Data
) {
    data class Data(
        val token: String,
        val userId: String,
        val userName: String,
        val loginId: String,
        val mobile: String,
        val phone: String,
        val email: String,
        val grupId: String,
        val orgnId: String,
        val dptId: String,
        val userType: String,
        val userZnCd: String,
        val eventAuthList: List<VwIocEvetInfo>?,
    )
}
