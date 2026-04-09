package com.eseict.apiServer.dto.common

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.util.changeStrDateToLocalDate
import com.eseict.apiServer.util.getDateFormat
import com.eseict.apiServer.util.getStatDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface StatInfo {
    var date: String
    var cnt: Int

    fun convertDate(dateType: Char) {
        val format = getStatDateFormat(dateType)
        this.date = changeStrDateToLocalDate(date, dateType).format(format)
    }
}