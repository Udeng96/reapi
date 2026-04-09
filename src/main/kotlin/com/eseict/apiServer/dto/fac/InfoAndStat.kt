package com.eseict.apiServer.dto.fac

data class InfoAndStat(
    var fac: GisInfo,
    var data: List<Stat>?
) {
}