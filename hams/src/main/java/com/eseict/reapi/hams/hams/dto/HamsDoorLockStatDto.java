package com.eseict.reapi.hams.hams.dto;

public record HamsDoorLockStatDto(
    String date,
    long noneCount,
    long unlockCount,
    long lockCount,
    long unrecognizedCount,
    long lockErrorCount
) {
    public HamsDoorLockStatDto(String date, Long none, Long unlock, Long lock, Long unrecognized, Long lockError) {
        this(date,
            none != null ? none : 0L,
            unlock != null ? unlock : 0L,
            lock != null ? lock : 0L,
            unrecognized != null ? unrecognized : 0L,
            lockError != null ? lockError : 0L);
    }
}
