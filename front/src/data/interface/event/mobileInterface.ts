import {Paging} from "../common/CommonInterface";

export interface MobileServerStore {
    pastList : Paging<MobileInfo> | null,
    pastListTotal : number,
    nowList : Array<MobileInfo>,
    nowListTotal : number
}

export interface MobileInfo{
    id : string|null,
    seq: number,
    userId : string|null,
    userNm : string|null,
    userDept:string|null,
    userDtm: string|null,
    lat: number|null,
    lng: number|null,
    mbChk: number,
    mbUrl : string|null
}

export interface FullPopupInfo {
    "isOpen" : boolean,
    "dtm" : string|null,
    "userDept" :string|null,
    "userNm" : string|null,
    "id" : string,
    rtsp : string|null,
}

export interface MobilePopupSize {
    "height" : number,
    "width" : number,
}

export interface MobileViewStore{
    activeLiveList : MobileInfo|null,
    activeLiveBtnList : Array<MobileInfo>,
    activePastItem : MobileInfo|null,
    activePastBtnList : Array<MobileInfo>,
    activeMobileClusterLatLon : [number,number]|null,
    activeMobileClusterMarkerList: MobileInfo[] | [],
    fullPopupInfo : FullPopupInfo | null,
    mobilePopupSize : MobilePopupSize,
    startDtm:string,
    endDtm:string,
    doFetchPastCount:number,
}



