package com.eseict.reapi.dstr.event.dto;

public record EventsDto(
    String statEvetOutbSeqn,
    String eventId,
    String eventNm,
    String locationCd,
    String locationNm,
    String lat,
    String lng,
    String addr,
    String cntn,
    String outbDtm,
    String clsDtm,
    String uSvcOutbId
) {}
