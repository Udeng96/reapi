package com.eseict.apiServer.dto.standardSystem


data class ChannelInfo(
    val fireCar: List<FireChannel>?,
    val liveChannel: List<FireChannel>?,
    val fireCarAll: List<String>? = null
) {
}