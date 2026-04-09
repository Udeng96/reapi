package com.eseict.apiServer.util

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.common.base.SystemProperties
import com.google.gson.Gson
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.parser.ParseException
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.net.URLEncoder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class CommonUtil {


    /**
     * filePath를 받아 샘플데이터를 활용
     * file은 resources의 파일을 사용
     *
     * @return
     * @Method : getSampleData
     * @author : Seoung Jin Kang
     * @since : 2019.04.26
     */
    companion object {

        val logger = LoggerFactory.getLogger(CommonUtil::class.java)
        val homePath = SystemProperties.getProperty("API_HOME")

        fun getSampleData(fileName: String): String? {
            val gson = Gson()
            var array: JSONArray? = null
            val parser: JSONParser = JSONParser()
            val file: File = File(homePath + File.separator + fileName)
            try {
                try {
                    array = parser.parse(FileReader(file)) as JSONArray
                } catch (e: ClassCastException) {
                    var obj: org.json.simple.JSONObject? = null
                    obj = parser.parse(FileReader(file)) as JSONObject
                    return gson.toJson(obj)
                }
            } catch (e: ParseException) {
                logger.error(e.message, e)
                var obj: org.json.simple.JSONObject? = null
                obj = parser.parse(FileReader(file)) as JSONObject
                return gson.toJson(obj)
            } catch (e: FileNotFoundException) {
                logger.error(e.message, e)
            } catch (e: IOException) {
                logger.error(e.message, e)
            }
            return gson.toJson(array)
        }
    }

}

fun setFileNameEncoding(userAgent: String, orgFileName: String): String {

    return when {
        userAgent.contains("Edge") || userAgent.contains("Edge") -> {
            val regex = "\\+".toRegex()
            URLEncoder.encode(orgFileName, "UTF-8").replace(regex, "%20")
        }
        userAgent.contains("Edge") || userAgent.contains("Edge") || userAgent.contains("Edge") -> {
            String(orgFileName.toByteArray(Charsets.UTF_8), Charsets.ISO_8859_1)
        }
        else -> {
            String(orgFileName.toByteArray(Charsets.UTF_8), Charsets.ISO_8859_1)
        }
    }

}

fun getStatDateFormat(dateType: Char): DateTimeFormatter {
    return when (dateType) {
        'Y' -> DateTimeFormatter.ofPattern(ApiConstants.DATE.STAT_Y)
        'M' -> DateTimeFormatter.ofPattern(ApiConstants.DATE.STAT_M)
        else -> DateTimeFormatter.ofPattern(ApiConstants.DATE.STAT_D)
    }
}

fun getDateFormat(dateType: Char? = 'D') = when (dateType) {
    'Y' -> DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_4)
    'M' -> DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_6)
    else -> DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_8)
}

fun getMonthGab(beforeDate: LocalDate, nowDate: LocalDate): Int {
    val monthA: Int = beforeDate.year * 12 + beforeDate.monthValue
    val monthB: Int = nowDate.year * 12 + nowDate.monthValue
    return monthB - monthA
}

fun changeStrDateToLocalDate(strDate: String, dateType: Char? = 'D'): LocalDate =
    when (dateType) {
        'Y' -> LocalDate.of(strDate.toInt(), 1, 1)
        'M' -> LocalDate.of(strDate.substring(0, 4).toInt(), strDate.substring(4, strDate.length).toInt(), 1)
        else -> LocalDate.of(
            strDate.substring(0, 4).toInt(),
            strDate.substring(4, 6).toInt(),
            strDate.substring(6, strDate.length).toInt()
        )
    }

fun 종료일을검사하여오늘과같은지검사하고오늘자로돌려주는메소드(strDate: String, dateType: Char? = 'D'): String {
    val now = LocalDate.now()
    val date = when (dateType) {
        'Y' -> LocalDate.of(strDate.toInt(), 1, 1)
        'M' -> LocalDate.of(strDate.substring(0, 4).toInt(), strDate.substring(4, strDate.length).toInt(), 1)
        else -> LocalDate.of(
            strDate.substring(0, 4).toInt(),
            strDate.substring(4, 6).toInt(),
            strDate.substring(6, strDate.length).toInt()
        )
    }

    return when (dateType) {
        'Y' -> {
            if (now.year.equals(date.year)) date.year.toString() else now.year.toString()
        }
        'M' -> {
            if (now.year.equals(date.year) && now.monthValue.equals(date.monthValue)) date.format(getDateFormat(dateType)) else now.format(
                getDateFormat(dateType)
            )
        }
        else -> if (now.toEpochDay() == date.toEpochDay()) date.format(getDateFormat(dateType)) else now.format(
            getDateFormat(dateType)
        )
    }

}


fun getToday(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    return current.format(formatter)
}