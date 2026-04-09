package com.eseict.apiServer.dto.fac

import com.eseict.apiServer.dto.GisFacInfo

data class CctvMonitor(
    val clfyNm: String?,
    val clfyId: String?,
    val cctvCnt: Int?,
    var cctvList: List<GisFacInfo>?
) {

}