package com.eseict.apiServer.dto.mobile

import java.math.BigDecimal

data class MobileInfo(

    var id: String? = null,//전화번호
    var seq: Int? = null, //table seq
    var userId: String? = null, //유저 ID
    var userNm: String? = null, //유저 명칭
    var userDept: String? = null, //부서 명칭
    var userDtm: String? = null, //발생 시각 혹은 종료시각 format - yyyy-MM-DD hh:mm:ss
    var lat: BigDecimal? = BigDecimal.ZERO,
    var lng: BigDecimal? = BigDecimal.ZERO,
    var mbChk: Int? = 3,           //1 입샐행, 2 촬영시작, 3 촬영중, 4 촬영종료, 5 앱종료
    var mbUrl: String? = null,     //rtsp 주소 정보
) {


    constructor(
        id: String,
        seq: Int,
        userId: String,
        userNm: String,
        userDept: String,
        userDtm: String,
        lat: Double,
        lng: Double,
        mbChk: Int,
        mbUrl: String
    ) : this(id, seq, userId, userNm, userDept, userDtm, lat.toBigDecimal(), lng.toBigDecimal(), mbChk, mbUrl)


}
