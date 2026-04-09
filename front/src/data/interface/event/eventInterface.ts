export interface VwIocEvet{
    msgTypCd : string | null
    znCd : string | null
    svcThemeCd : string | null
    unitSvcCd : string | null
    evetCd : string | null
    evetId: string | null
    evetNm: string | null
    znNm: string | null
    svcThemeNm: string | null
    unitSvcNm: string | null
}


export interface EventServerStore {
    result : string,
    evetList : Array<VwEvent>,
    listTotal : number,
    evetListModal : Array<VwEvent>,
    listTotalModal : number ,
    evetDetail : VwEvent |null,
    evetDetailModal : VwEvent |null ,
    evetLoca : Array<Search>,
    evetType : Array<Search>,
    fireCarInfoList : Array<FireCarInfo> | null
}

export interface EventViewStore {
    checkedLocaList : Array<Search>,
    checkedTypeList : Array<Search>,
    checkedModalLocaList : Array<Search>,
    checkedModalTypeList : Array<Search>,
    activeSearchBox : string,
    activeModal : boolean,
    activeItem : string|null,
    activeModalItem : string|null,
    eventPayload : EventsPayload,
    modalPayload : EventsPayload,
    activeEventType : string|null,

    wsEventReceive : boolean
}


//todo : 개발 진행하면서 계속 바꿔줄 예정

//-----------------Event List---------------------

export interface EventsPayload{
    eventIdList:Array<string>|null
    locationCdList : Array<string> | null
    startDtm : string | null
    endDtm : string | null
    page : number | null
    rows : number | null
}

export interface EventResult {
    data : Array<VwEvent> | null,
    total : number | null,
}

export interface VwEvent {
    addr : string | null,
    clsDtm : string | null,
    cntn : string|null,
    eventId : string | null,
    eventNm : string | null,
    lat : string | null,
    lng : string | null,
    locationCd : string | null,
    locationNm : string | null,
    outbDtm : string | null,
    statEvetOutbSeqn : string,
    usvcOutbId : string
}

export interface EventListItem {
    addr : string |null,
    eventId:string|null,
    eventNm : string | null,
    cntn : string |null,
    locationNm : string |null,
    outbDtm : string| null,
    statEvetOutbSeqn : string,
}

//-----------------Event Search---------------------

export interface Search {
    id : string
    nm : string
}

export interface EvetOptionTypeProps {
    title : string
    type : string
}

export interface DetailInfoProps{
    title : string
    value : string|null
}



//----------------Event Last Modal-------------------

export interface SearchDtm {

    id : string
    name : string
    startDtm : string
    endDtm : string
}



//----------------- FIRE EVENT TYPES -----------------

export type ChannelInfo =  {
    id : string;
    nm : string;
    rtsp : string;
}

export type FireCarResult = {
    fireCar : ChannelInfo[],
    liveChannel : ChannelInfo[],
    fireCarAll : string[]
}

export type FireCarInfo= {
   orcIdx: string,
   orcNo: string,
   sndYmdHms: string,
   userId: string,
   truckNo: string
   lon: string
   lat: string
   truckLcYmdHms: string
   truckPsitn: string
   truckTy:string
   callNm_telNo: string
   routeId: string
   videoUrl : string
}


