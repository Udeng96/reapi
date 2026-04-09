package com.eseict.reapi.hams.hams.dto;

public record HamsDoorStatDto(
    String date,
    long openCount,
    long closeCount
) {
    public HamsDoorStatDto(String date, Long openCount, Long closeCount) {
        this(date, openCount != null ? openCount : 0L, closeCount != null ? closeCount : 0L);
    }
}
