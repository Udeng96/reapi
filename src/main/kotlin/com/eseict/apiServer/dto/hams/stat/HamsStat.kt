package com.eseict.apiServer.dto.hams.stat

data class HamsStat(
    var eqId: String,
    var dateType: Char,
    var doorType: Char,
    var dataStat: List<Stat>?,
    var dataLock: List<Lock>?
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