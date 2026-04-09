package com.eseict.apiServer.dto.standardSystem

data class FireCarInfo(
    var orcIdx: String? = null,
    val orcNo: String? = null,
    val sndYmdHms: String? = null,
    val userId: String? = null,
    val truckNo: String? = null,
    val lon: Double? = null,
    val lat: Double? = null,
    val truckLcYmdHms: String? = null,
    val truckPsitn: String? = null,
    val truckTy: String? = null,
    val callNm_telNo: String? = null,
    val routeId: String? = null,
    val videoUrl: String? = null
)
