package com.eseict.apiServer.dto.event

data class HeatmapClfyInfo(
    val svcThemeCd: String,
    val svcThemeNm: String,
    var itemList: List<UnitItem>
) {
    data class UnitItem(
        val unitCd: String,
        val unitNm: String
    )
}