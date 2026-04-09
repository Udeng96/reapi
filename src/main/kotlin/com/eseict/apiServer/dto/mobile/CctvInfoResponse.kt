package com.eseict.apiServer.dto.mobile

import java.io.Serializable

data class CctvInfoResponse(
    var mgrNo: String? = null,
    var cctvNm: String? = null,
    var xCrdnt: Double? = null,
    var yCrdnt: Double? = null,
    var gbnCd: String? = null,
    var gbnCdNm: String? = null,
    var rtspUrl: String? = null,
) {


    constructor() : this(null)


    constructor(mgrNo: String, cctvNm: String, xCrdnt: Double, yCrdnt: Double, gbnCd: String, gbnCdNm: String)
            : this(mgrNo, cctvNm, xCrdnt, yCrdnt, gbnCd, gbnCdNm, null)

    constructor(mgrNo: String, cctvNm: String, xCrdnt: Double, yCrdnt: Double, rtspUrl: String)
            : this(mgrNo, cctvNm, xCrdnt, yCrdnt, null, null, rtspUrl = rtspUrl)

    fun getPropertiesMap(): Map<String, Serializable?>? {
        val properties: MutableMap<String, Serializable?> = HashMap()
        properties["mgrNo"] = mgrNo
        properties["cctvNm"] = cctvNm
        properties["xCrdnt"] = xCrdnt
        properties["yCrdnt"] = yCrdnt
        properties["gbnCd"] = gbnCd
        properties["gbnCdNm"] = gbnCdNm
        return properties
    }


}
