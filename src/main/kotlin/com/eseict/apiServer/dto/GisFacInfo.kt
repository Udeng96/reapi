package com.eseict.apiServer.dto

import com.eseict.apiServer.dto.fac.GisInfo

//GIS 전체에서 사용하는 공통적인 객체
open class GisFacInfo : GisInfo {

    var mgrNo: String? = null
    var ptzYn : Boolean? = false

    constructor() : super(null, null, null, null, null, null, null, null, null, null)


    constructor(
        facId: String?,
        facNm: String?,
        addr: String?,
        lat: String?,
        lng: String?,
        clfyCd: String?,
        clfyNm: String?,
        useYn: Int?,
        updDtm: String? = null,
        data: Any? = null,
    ) : super(facId, facNm, addr, lat!!.toDoubleOrNull(), lng!!.toDoubleOrNull(), clfyCd, clfyNm, useYn, updDtm, data)

    constructor(
        facId: String?,
        facNm: String?,
        mgrNo: String?,
        addr: String?,
        lat: String?,
        lng: String?,
        clfyCd: String?,
        clfyNm: String?,
        useYn: Int?,
        updDtm: String? = null,
        data: Any? = null,

        ) : super(facId, facNm, addr, lat!!.toDoubleOrNull(), lng!!.toDoubleOrNull(), clfyCd, clfyNm, useYn, updDtm, data) {
        this.mgrNo = mgrNo
    }


}
