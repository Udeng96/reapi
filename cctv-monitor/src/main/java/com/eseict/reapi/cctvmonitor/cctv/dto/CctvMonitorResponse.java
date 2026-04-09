package com.eseict.reapi.cctvmonitor.cctv.dto;

import java.util.List;

public record CctvMonitorResponse(
    String clfyNm,
    String clfyId,
    int cctvCnt,
    List<CctvItemDto> cctvList
) {
    public record CctvItemDto(
        String facId,
        String facNm,
        String mgtNo,
        Double lat,
        Double lng
    ) {}
}
