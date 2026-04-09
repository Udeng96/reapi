package com.eseict.reapi.cctvmonitor.cctv.dto;

public record PtzRequest(
    String cctvId,
    String action,
    String code
) {}
