package com.eseict.apiServer.dto.mobile

data class AddEventInfo(


    val statEvetTypCd: String,
    var statMsgTypCd: String? = null,
    val outbPosNm: String,
    val statEvetNm: String,
    var statEvetClrDtm: String? = null,
    val statEvetCntn: String,
    val statEvetType: String,


    var outbPos: List<Xy>,
    val statEvetOutbDtm: String,
    var statEvetActnCntn: String? = null, // 무시
    var procSt: String = "10",
    val isTest: String = "Y", // 모의 여부. Y 고정
    var uSvcOutbId: String? = null, // 무시
    var statEvetActnMn: String? = null, // 무시
    var statEvetActnDtm: String? = null, // 무시
    var statEvetSvcTyp: String? = null, // 무시
    var etcCntn: CctvId,
    val outbPosX: String,

    val outbPosY: String,
    val tmx: String? = null,
    val tmy: String? = null


) {


    data class Xy(val x: String, val y: String)

    data class CctvId(val cctvId: String)
}