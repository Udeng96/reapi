package com.eseict.apiServer.dto.fac

import com.eseict.apiServer.conf.ApiConstants
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


data class Stat(
    var areaCode: String?,
    var date: String,
    var value: Double
) {

    constructor(date: String, value: Int) : this(null, date, value.toDouble()) {
    }

    constructor(date: Timestamp, value: Int) : this(
        date.toLocalDateTime().format(DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_14)), value
    ) {

    }

    constructor(date: Date, value: Int) : this(SimpleDateFormat(ApiConstants.DATE.FORMAT_14).format(date), value)
    constructor(areaCode: String, date: Date, value: Int) : this(
        areaCode,
        SimpleDateFormat(ApiConstants.DATE.FORMAT_14).format(date),
        value.toDouble()
    )

}