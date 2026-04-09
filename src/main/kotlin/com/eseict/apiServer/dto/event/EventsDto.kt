package com.eseict.apiServer.dto.event

import java.io.Serializable

class EventsDto : Serializable {

    var statEvetOutbSeqn: String? = null
    var eventId: String? = null
    var eventNm: String? = null
    var locationCd: String? = null
    var locationNm: String? = null
    var lat: String? = null
    var lng: String? = null
    var addr: String? = null
    var cntn: String? = null
    var outbDtm: String? = null
    var clsDtm: String? = null
    var uSvcOutbId : String? = null

    constructor(
        statEvetOutbSeqn: String? = null,
        eventId: String? = null,
        eventNm: String? = null,
        locationCd: String? = null,
        locationNm: String? = null,
        lat: String? = null,
        lng: String? = null,
        addr: String? = null,
        cntn: String? = null,
        outbDtm: String? = null,
        clsDtm: String? = null,
        uSvcOutbId : String? = null
    ) {
        this.statEvetOutbSeqn = statEvetOutbSeqn
        this.eventId = eventId
        this.eventNm = eventNm
        this.locationCd = locationCd
        this.locationNm = locationNm
        this.lat = lat
        this.lng = lng
        this.addr = addr
        this.cntn = cntn
        this.outbDtm = outbDtm
        this.clsDtm = clsDtm
        this.uSvcOutbId =uSvcOutbId
    }
}