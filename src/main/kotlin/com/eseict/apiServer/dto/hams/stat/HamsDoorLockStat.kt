package com.eseict.apiServer.dto.hams.stat

import com.eseict.apiServer.dto.common.StatInfo

data class HamsDoorLockStat(
    override var date: String,
    override var cnt: Int,
    var noneCnt: Int? = 0,
    var unlockCnt: Int? = 0,
    var lockCnt: Int? = 0,
    var noRecogCnt: Int? = 0,
    var lockIssueCnt: Int? = 0
) : StatInfo {

    constructor(
        date: String,
        noneCnt: Long,
        unlockCnt: Long,
        lockCnt: Long,
        noRecogCnt: Long,
        lockIssueCnt: Long
    ) : this(
        date,
        0,
        noneCnt.toInt(),
        unlockCnt.toInt(),
        lockCnt.toInt(),
        noRecogCnt.toInt(),
        lockIssueCnt.toInt()
    )

}