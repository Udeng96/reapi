package com.eseict.reapi.hams.hams.dto;

import java.math.BigDecimal;

public record HamsStatusDto(
    String frontDoor,
    String frontDoorLock,
    String backDoor,
    String backDoorLock,
    BigDecimal temp,
    BigDecimal humi,
    boolean isConnect
) {
    public static HamsStatusDto empty() {
        return new HamsStatusDto(null, null, null, null, BigDecimal.ZERO, BigDecimal.ZERO, false);
    }
}
