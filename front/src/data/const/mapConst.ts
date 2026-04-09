import {MapTypeProps} from "../interface/map/mapInterface";

export const DAUM_MAP_TYPE_LIST: Array<MapTypeProps> = [
    {
        type: 'daum_2d',
        mapNm: 'Daum',
        mapTypeNm: '일반지도',
        tileNm: 'normal',
        map: 'daum'
    },
    {
        type: 'daum_real',
        mapNm: 'Daum',
        mapTypeNm: '위성지도',
        tileNm : 'satellite',
        map : 'daum'
    }
]

export const V_WORLD_MAP_TYPE_LIST: Array<MapTypeProps> = [
    {
        type: 'vworld_2d',
        mapNm: 'V World',
        mapTypeNm: '일반지도',
        tileNm: 'normal',
        map: 'vworld'
    },
    {
        type: 'vworld_real',
        mapNm: 'V World',
        mapTypeNm: '위성지도',
        tileNm: 'satellite',
        map: 'vworld'
    },
    {
        type: 'vworld_gray',
        mapNm: 'V World',
        mapTypeNm: '회색지도',
        tileNm: 'gray',
        map: 'vworld'
    },
    {
        type: 'vworld_navy',
        mapNm: 'V World',
        mapTypeNm: '남청색지도',
        tileNm: 'midnight',
        map: 'vworld'
    }
]

export const DAUM_MAP_LAYER_LIST: Array<MapTypeProps> = [

    {
        type: 'cadastral',
        mapNm: 'daum',
        mapTypeNm: 'Daum 지적편집도'
    },
    {
        type: 'bBoundary',
        mapNm: 'daum',
        mapTypeNm: 'Daum 법정동 경계지도'
    },
    {
        type: 'hBoundary',
        mapNm: 'daum',
        mapTypeNm: 'Daum 행정동 경계지도'
    },
    {
        type: 'physical',
        mapNm: 'daum',
        mapTypeNm: 'Daum 지형도'
    },
    {
        type: 'bicycle',
        mapNm: 'daum',
        mapTypeNm: 'Daum 자전거 지도'
    },
    {
        type: 'traffic',
        mapNm: 'daum',
        mapTypeNm: 'Daum 교통상황 지도'
    },
    //FIXME 위성지도는 타입이랑 같이 들어가게 해야한다.
    // {
    //     type: 'satelliteLine',
    //     mapNm: 'daum',
    //     mapTypeNm: 'Daum 위성지도 라벨'
    // }
]

export const V_WORLD_MAP_LAYER_LIST: Array<MapTypeProps> = [
    {
        type: 'satelliteLine',
        mapNm: 'vworld',
        mapTypeNm: 'V World 라벨'
    }
]

export enum MapMoveEnum {
    none,
    normal,
    event,
    event_disaster,
    max_zoom,
    plus_zoom
}

export enum MapEventMarkerType {
    event,
    fire
}

export const MapDrawEnum = {
    none: "none",
    polygon: "polygon",
    polyline: "polyline",
    circle: "circle",
    net : "net",
    clear : "clear", //clear 후 none 처리
}


export const MAX_CCTV_PLAYER_CNT = 3

export const MAX_MOBILE_PAST_PLAYER_CNT = 1
