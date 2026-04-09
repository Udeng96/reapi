package com.eseict.reapi.common.config;

public final class ResultCode {

    private ResultCode() {}

    public static final String SUCCESS_CODE = "SUCCESS";
    public static final String SUCCESS_STATUS = "200";
    public static final String OMS_SUCCESS_CODE = "OK";
    public static final String FAIL_CODE = "FAIL";

    public static final String SUCCESS_MESSAGE = "정상 처리되었습니다.";
    public static final String FAIL_MESSAGE = "불러온 데이터가 없거나 정상적인 호출이 되지 않았습니다.";
    public static final String FAIL_NO_AUTH = "권한이 없거나, 접속정보가 올바르지않습니다.";
}
