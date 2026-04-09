package com.eseict.apiServer.dto.event

interface EventInfo {
    val statEvetOutbSeqn: String
    val eventId: String
    val eventNm: String
    val locationCd: String
    val locationNm: String
    val lat: String
    val lng: String
    val addr: String
    val cntn: String
    val outbDtm: String
    val clsDtm: String?
    val uSvcOutbId : String
}