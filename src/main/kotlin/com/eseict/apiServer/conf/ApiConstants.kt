package com.eseict.apiServer.conf

class ApiConstants {


    companion object Common {
        const val ENCODING = "UTF-8"
        const val API_PRODUCES = "application/json;charset=utf8"
        const val TIME_ZONE = "Asia/Seoul"
        const val STR_SPLIT = "_"
//        val HTTP_GET_LOGGER = LoggerFactory.getLogger("HTTP_GET_FILE")
//        val HTTP_POST_LOGGER = LoggerFactory.getLogger("HTTP_POST_FILE")
    }

    object RESULT {
        const val SUCCESS_CODE = "SUCCESS"
        const val SUCCESS_STATUS = "200"
        const val OMS_SUCCESS_CODE = "OK"
        const val FAIL_CODE = "FAIL"

        const val SUCCESS_MESSAGE = "정상 처리되었습니다."
        const val FAIL_MESSAGE = "불러온 데이터가 없거나 정상적인 호출이 되지 않았습니다."
        const val FAIL_NO_AUTH = "권한이 없거나, 접속정보가 올바르지않습니다."
    }


    object DATE {
        const val FORMAT_4 = "yyyy"
        const val FORMAT_6 = "yyyyMM"
        const val FORMAT_8 = "yyyyMMdd"
        const val FORMAT_10 = "yyyyMMddHH"
        const val FORMAT_12 = "yyyyMMddHHmm"
        const val FORMAT_14 = "yyyyMMddHHmmss"
        const val FORMAT_17 = "yyyyMMddHHmmssSSS"
        const val FORMAT_FRONT = "yyyy-MM-dd HH:mm:ss"
        const val MILLISECOND_ONE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        const val YEAR_TO_MINUTE_FORMAT = "yyyy-MM-dd HH:mm"
        const val DTM_TXT_FORMAT_8 = "yyyy/MM/dd"
        const val HOUR_MINUTE_FORMAT = "HHmm"
        const val HOUR_FORMAT = "HH"

        const val STAT_D = "MM-dd"
        const val STAT_M = "yyyy-MM"
        const val STAT_Y = "yyyy"
    }

    object HEATMAPDATE {
        const val ALL_DATE = "ALL"
        const val TODAY = "TODAY"
        const val FOR_A_MONTH = "MONTH"
        const val FOR_THREE_MONTH = "THREE"
    }


    object EVENT {
        const val STAT_EVET_ROOT_NAME = "StatEvet"
        const val HEADER_TYP_CD_STAT_EVET = "A1"
        const val STAT_EVET_CD = "001"
        const val EVET_GB_CD_E = "E"
        const val EVET_GD_CD_NORMAL = "10"
        const val STAT_EVET_NM = "상황 이벤트"
        const val FAC_EVET_NM = "시설 이벤트"
        const val STAT_EVET_CNTN = "상황 이벤트"
        const val FAC_EVET_CNTN = "시설 이벤트"
    }

    object STAT_EVET_PROC_ST {
        const val OUTB = "1"
        const val ORDR = "2"
        const val ACTN = "3"
        const val CLR = "4"
        const val CLS = "5"
    }

    object TYPE {
        const val FIRE = "화재"
        const val RESCUE = "구조"
        const val RELIEF = "구급"
        const val ETC = "기타"
    }

    object DATE_TYPE {
        const val YEAR = "Y"
        const val MONTH = "M"
        const val DATE = "D"
        const val HOUR = "H"
        const val MINUTE = "M"
    }

    object STATE {
        const val ON = "ON"
        const val OFF = "OFF"
        const val ERROR = "ERROR"
    }

    object DB_SCHEMA {
        const val API = "api";
        const val XEUS = "xeus";
        const val IOC = "rino_ioc";
        const val OMS = "rino_oms";
        const val FMS = "rino_fms";
        const val HAMS = "hams";
//        const val HAMS = "hams_gl";
        const val HS_S_SERVICE = "hs_s_service";
        const val EAS = "eas";
    }

    object DB_SCHEMA_OTHER {
        const val EQ = "ggeew"
        const val DISASTER = "integration_disaster"
        const val STANDARD_SYSTEM = "docheng_app"
    }

    object OMS_USER_GD {
        const val PLATFORM = "001"
        const val MANAGER = "002"
        const val LOCAL = "003"
        const val NORMAL = "004"
    }

    object ResponseConstants {
        const val RESPONSE_SUCCESS = "SUCCESS"
        const val RESPONSE_FAIL = "FAIL"
    }

}