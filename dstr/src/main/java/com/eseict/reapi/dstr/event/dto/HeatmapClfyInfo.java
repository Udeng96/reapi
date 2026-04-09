package com.eseict.reapi.dstr.event.dto;

import java.util.List;

public record HeatmapClfyInfo(
    String svcThemeCd,
    String svcThemeNm,
    List<UnitItem> itemList
) {
    public record UnitItem(String unitCd, String unitNm) {}
}
