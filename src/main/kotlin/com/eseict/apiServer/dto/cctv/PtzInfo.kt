package com.eseict.apiServer.dto.cctv

data class PtzInfo(
    var cctvId : String? = null,
    val action:String,
    val code : String,
)
