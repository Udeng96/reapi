package com.eseict.apiServer.dto.hams

import com.eseict.apiServer.dto.GisFacInfo
import com.eseict.apiServer.dto.fac.GisInfo

class HamsGisFacInfo : GisInfo {

    //필요한 정보를 삽입한다.
    //ip,port 정보
    var hamsId: String? = null
    var groupNm: String? = null
    var ip: String? = null
    var port: Int? = null

    //하위 시설물 정보
    var lowFacInfos: List<GisFacInfo>? = null

    var status: HamsStatus = HamsStatus()

    constructor() : super(null, null, null, null, null, null, null, null, null, null)
    constructor(
        facId: String?,
        hamsId: String?,
        facNm: String?,
        addr: String?,
        lat: Double?,
        lng: Double?,
        clfyCd: String?,
        clfyNm: String?,
        useYn: Int?,
        updDtm: String?,
        ip: String?,
        port: String?,
        groupNm: String? = ""
    ) : super(facId, facNm, addr, lat, lng, clfyCd, clfyNm, useYn, updDtm, null) {
        this.hamsId = hamsId
        this.ip = ip
        this.groupNm = groupNm
        try {
            this.port = port?.toInt()
        } catch (e: NumberFormatException) {

        }
    }

}