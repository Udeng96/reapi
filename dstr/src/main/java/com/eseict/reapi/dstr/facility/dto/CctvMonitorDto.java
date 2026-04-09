package com.eseict.reapi.dstr.facility.dto;

import java.util.List;

public record CctvMonitorDto(
    String clfyNm,
    String clfyId,
    int cctvCnt,
    List<GisFacInfoDto> cctvList
) {}
