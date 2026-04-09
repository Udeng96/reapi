package com.eseict.apiServer.dto.mobile

data class XeusAssetCctv(
    var userId //계정 ID
    : String? = null,
    var mgrNo //계정 전화번호
    : String? = null,
    var orgMgrNo //고정값
    : String? = null,
    var mdMgrNo //고정값
    : String? = null,
    var vmsMgrNo //고정값
    : String? = null,
    var siteMgrNo //계정 이름
    : String? = null,
    var cctvNm //계정 이름
    : String? = null,
    var deviceId //영상주소 RTSP
    : String? = null,
    var chnlNo //고정값
    : String? = null,
    var gbnCd //고정값
    : String? = null,
    var useYn //고정값
    : String? = null,
    var lightYn //고정값
    : String? = null,
    var infrdYn //고정값
    : String? = null,
    var panYn //고정값
    : String? = null,
    var tiltYn //고정값
    : String? = null,
    var zoomYn //고정값
    : String? = null,
    var talkYn //고정값
    : String? = null,
    var tourYn //고정값
    : String? = null,
    var locDesc //계정이름
    : String? = null,
    var lng //경도
    : String? = null,
    var lat //위도
    : String? = null
)
