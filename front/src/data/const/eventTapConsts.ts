import {EvetOptionTypeProps, SearchDtm} from "../interface/event/eventInterface";
import moment from "moment";

export const EVENT_TAP_BOX_TYPE: Array<EvetOptionTypeProps> = [
    {
        title: "발생 지역",
        type: "loca"
    },
    {
        title: "이벤트 타입",
        type: "type"
    }
]

export const MODAL_EVENT_TAP_BOX_TYPE: Array<EvetOptionTypeProps> = [
    {
        title: "발생 지역",
        type: "modal_loca"
    },
    {
        title: "이벤트 타입",
        type: "modal_type"
    }
]

export const SEARCH_BOX_TYPE = {
    LOCATION: "loca",
    EVENT_TYPE: "type",
    MODAL_LOCATION: "modal_loca",
    MODAL_EVENT_TYPE: "modal_type",
    NONE: "none"
}

export const DTM_OPTION: Array<SearchDtm> = [
    {
        id: "oneYear",
        name: "1년",
        startDtm: moment().subtract(1, 'years').format("YYYY-MM-DD"),
        endDtm: moment().format("YYYY-MM-DD")
    },
    {
        id: "sixMonths",
        name: "6개월",
        startDtm: moment().subtract(6, 'months').format("YYYY-MM-DD"),
        endDtm: moment().format("YYYY-MM-DD")
    },
    {
        id: "threeMonths",
        name: "3개월",
        startDtm: moment().subtract(3, 'months').format("YYYY-MM-DD"),
        endDtm: moment().format("YYYY-MM-DD")
    },
    {
        id: "oneMonth",
        name: "1개월",
        startDtm: moment().subtract(1, 'months').format("YYYY-MM-DD"),
        endDtm: moment().format("YYYY-MM-DD")
    },
    {
        id: "oneWeek",
        name: "1주일",
        startDtm: moment().subtract(1, 'weeks').format("YYYY-MM-DD"),
        endDtm: moment().format("YYYY-MM-DD")
    }
]


export const DUMMY_EVENT_LIST = [
    {
        addr: "경기도 화성시 반월동(반월동) 734-1",
        clsDtm: null,
        cntn: "C2[교통불편]",
        eventId: "055POL003E01",
        eventNm: "5대연계 112",
        lat: "37.230225",
        lng: "127.05393",
        locationCd: "055",
        locationNm: "반월동",
        outbDtm: "20220722185127",
        statEvetOutbSeqn: "E_fBV4ZIt4kyL8TewMDqrE-A",
        usvcOutbId: "15202233612700",
    },

    {
        addr: "경기도 화성시 석우동 큰재봉길",
        clsDtm: null,
        cntn: "C1[순찰차영상제공]",
        eventId: "061POL002E01",
        eventNm: "5대연계 112",
        lat: "37.216515",
        lng: "127.07995",
        locationCd: "061",
        locationNm: "동탄1동",
        outbDtm: "20220708183949",
        statEvetOutbSeqn: "E_aHUj-jolA7D9oXhL6W73jo",
        usvcOutbId: "15202233612542-112CAR",
    },

    {
        addr: "경기도 화성시 비봉면 주석로701번길",
        clsDtm: null,
        cntn: "C1[순찰차영상제공]",
        eventId: "033POL002E01",
        eventNm: "5대연계 112",
        lat: "37.20118",
        lng: "126.9008",
        locationCd: "033",
        locationNm: "비봉면",
        outbDtm: "20220708183553",
        statEvetOutbSeqn: "E_b5I5az2GAME9sMBfJITFzC",
        usvcOutbId: "15202233612460-112CAR",

    },
    {
        addr: "경기도 화성시 반월동(반월동) 734-1",
        clsDtm: null,
        cntn: "C2[교통불편]",
        eventId: "055POL003E01",
        eventNm: "5대연계 112",
        lat: "37.230225",
        lng: "127.05393",
        locationCd: "055",
        locationNm: "반월동",
        outbDtm: "20220722185127",
        statEvetOutbSeqn: "E_fBV4ZIt4kyL8TewMDqrE-A",
        usvcOutbId: "15202233612700",
    },

    {
        addr: "경기도 화성시 석우동 큰재봉길",
        clsDtm: null,
        cntn: "C1[순찰차영상제공]",
        eventId: "061POL002E01",
        eventNm: "5대연계 112",
        lat: "37.216515",
        lng: "127.07995",
        locationCd: "061",
        locationNm: "동탄1동",
        outbDtm: "20220708183949",
        statEvetOutbSeqn: "E_aHUj-jolA7D9oXhL6W73jo",
        usvcOutbId: "15202233612542-112CAR",
    },

    {
        addr: "경기도 화성시 비봉면 주석로701번길",
        clsDtm: null,
        cntn: "C1[순찰차영상제공]",
        eventId: "033POL002E01",
        eventNm: "5대연계 112",
        lat: "37.20118",
        lng: "126.9008",
        locationCd: "033",
        locationNm: "비봉면",
        outbDtm: "20220708183553",
        statEvetOutbSeqn: "E_b5I5az2GAME9sMBfJITFzC",
        usvcOutbId: "15202233612460-112CAR",

    },
    {
        addr: "경기도 화성시 반월동(반월동) 734-1",
        clsDtm: null,
        cntn: "C2[교통불편]",
        eventId: "055POL003E01",
        eventNm: "5대연계 112",
        lat: "37.230225",
        lng: "127.05393",
        locationCd: "055",
        locationNm: "반월동",
        outbDtm: "20220722185127",
        statEvetOutbSeqn: "E_fBV4ZIt4kyL8TewMDqrE-A",
        usvcOutbId: "15202233612700",
    },

    {
        addr: "경기도 화성시 석우동 큰재봉길",
        clsDtm: null,
        cntn: "C1[순찰차영상제공]",
        eventId: "061POL002E01",
        eventNm: "5대연계 112",
        lat: "37.216515",
        lng: "127.07995",
        locationCd: "061",
        locationNm: "동탄1동",
        outbDtm: "20220708183949",
        statEvetOutbSeqn: "E_aHUj-jolA7D9oXhL6W73jo",
        usvcOutbId: "15202233612542-112CAR",
    },

    {
        addr: "경기도 화성시 비봉면 주석로701번길",
        clsDtm: null,
        cntn: "C1[순찰차영상제공]",
        eventId: "033POL002E01",
        eventNm: "5대연계 112",
        lat: "37.20118",
        lng: "126.9008",
        locationCd: "033",
        locationNm: "비봉면",
        outbDtm: "20220708183553",
        statEvetOutbSeqn: "E_b5I5az2GAME9sMBfJITFzC",
        usvcOutbId: "15202233612460-112CAR",

    }


]



