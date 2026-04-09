export interface HeatmapClfy {
    svcThemeCd : string;
    svcThemeNm : string;
    itemList : UnitInfo[];
    item? : UnitInfo|null
}

export interface UnitInfo {
    unitCd : string;
    unitNm : string;
}

export interface Count{
    count : number;
    lat : number;
    lng : number;
}