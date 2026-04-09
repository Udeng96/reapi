package com.eseict.reapi.cctvmonitor.cctv.dto;

public record CctvDetailResponse(
    String facId,
    String facNm,
    String mgtNo,
    String clfyNm,
    String addrShort,
    Double lat,
    Double lng,
    boolean ptzYn
) {}
