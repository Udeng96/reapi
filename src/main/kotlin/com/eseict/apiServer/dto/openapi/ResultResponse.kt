package com.eseict.apiServer.dto.openapi

data class ResultResponse(
    val resultType: String,
    val message: String,
    val data: Any? = null
)