package com.eseict.apiServer.dto.fac

import com.eseict.apiServer.dto.GisFacInfo

class GisFloodInfo : GisFacInfo {
    var flood: Boolean = false

    constructor(
        facId: Double,
        facNm: String?,
        addr: String?,
        lat: Double,
        lng: Double,
        useYn: Int,
        updDtm: String?,
        clfyCd: String,
        clfyNm: String,
        flood: Boolean = false
    ) : super(facId.toString(), facNm, addr, lat.toString(), lng.toString(), clfyCd, clfyNm, useYn, updDtm) {
        this.flood = flood
    }
}