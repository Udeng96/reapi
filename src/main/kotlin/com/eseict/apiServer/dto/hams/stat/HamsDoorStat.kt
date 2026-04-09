package com.eseict.apiServer.dto.hams.stat

import com.eseict.apiServer.dto.common.StatInfo

data class HamsDoorStat(
    override var date: String,
    override var cnt: Int = 0,
    var openCnt: Int = 0,
    var clsCnt: Int = 0,
    var noneCnt: Int = 0
) : StatInfo {
    constructor(date: String, openCnt: Long, clsCnt: Long) : this(
            date,
            0,
            openCnt.toInt(),
            clsCnt.toInt()
    )

    constructor(date: String, openCnt: Long, clsCnt: Long, noneCnt: Long) : this(
        date,
        0,
        openCnt.toInt(),
        clsCnt.toInt(),
        noneCnt.toInt()
    )

}