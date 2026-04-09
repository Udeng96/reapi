package com.eseict.apiServer.dto.openapi

data class LocationInfo(
    val admNm: String,
    val admSubNm: String,
    val admEnNm: String,
    val esri_pk: Int,
    val admCd: String,
    val nx: Int,
    val ny: Int
)