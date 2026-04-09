package com.eseict.reapi.dstr.mobile.dto;

public record MobileInfoDto(
    String userId,
    String userNm,
    String userDtm,
    Double lat,
    Double lon,
    String mbUrl,
    String title,
    String dptNm,
    String mgrNo,
    Integer mobileChk
) {}
