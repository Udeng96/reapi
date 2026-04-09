package com.eseict.reapi.hams.hams.dto;

import java.math.BigDecimal;

public record HamsInfoDto(
    String facId,
    String hamsId,
    String name,
    String groupNm,
    String ip,
    Long port,
    String address,
    BigDecimal lat,
    BigDecimal lng,
    HamsStatusDto status
) {}
