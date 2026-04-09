package com.eseict.reapi.common.config;

public final class CommonConstants {

    private CommonConstants() {}

    public static final String ENCODING = "UTF-8";
    public static final String API_PRODUCES = "application/json;charset=utf8";
    public static final String TIME_ZONE = "Asia/Seoul";
    public static final String STR_SPLIT = "_";

    public static final class DateType {
        public static final String YEAR = "Y";
        public static final String MONTH = "M";
        public static final String DATE = "D";
        public static final String HOUR = "H";
        public static final String MINUTE = "M";
    }

    public static final class State {
        public static final String ON = "ON";
        public static final String OFF = "OFF";
        public static final String ERROR = "ERROR";
    }

    public static final class OmsUserGrade {
        public static final String PLATFORM = "001";
        public static final String MANAGER = "002";
        public static final String LOCAL = "003";
        public static final String NORMAL = "004";
    }
}
