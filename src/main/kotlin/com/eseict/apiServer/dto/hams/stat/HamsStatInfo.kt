package com.eseict.apiServer.dto.hams.stat

data class HamsStatInfo<T>(
    var eqId: String,
    var dateType: Char,
    var doorType: Char,
    var data: MutableList<T>
) {

    data class Stat(
        val date: String,
        val openCnt: Int,
        val clsCnt: Int,
        val noneCnt: Int
    )

    data class Lock(
        val date: String,
        val noneCnt: Int,
        val unlockCnt: Int,
        val lockCnt: Int,
        val noRecogCnt: Int,
        val lockIssueCnt: Int
    )


}