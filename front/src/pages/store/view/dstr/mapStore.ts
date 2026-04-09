import {createSlice, PayloadAction} from "@reduxjs/toolkit";

import _ from "underscore"
import {LatLngExpression, LatLngTuple} from "leaflet";
import {MapDrawEnum, MapMoveEnum} from "../../../../data/const/mapConst";
import {DaumAddrInfo, MapTypeProps} from "../../../../data/interface/map/mapInterface";
import {AddrInfo} from "../../../../data/interface/etc/Addr";
import {HeatmapClfy} from "../../../../data/interface/event/heatMapInterface";
import {CctvInfo} from "../../../../data/interface/cctv/cctvInterface";
import {TileMapData} from "../../../../data/config/Map";

type MapMove = {
    latlng: LatLngTuple,
    mapMoveType: MapMoveEnum
}


interface MapState {
    mapMove: MapMove | null;
    minZoom: number,
    maxZoom: number,
    mapType: string;
    mapTile: string;
    zoomLevel: number;
    mapDraw: string;
    customLayerList: Array<MapTypeProps>;
    selectAddrInfo: AddrInfo | null;
    selectHeatMapClfy: HeatmapClfy | null
    selectHeatMapPeriod: string;
    mapClickFn: Function;
    customEventLatLng : LatLngTuple | null
    playCctvLatLngList : Array<LatLngExpression>;
}

const initialState: MapState = {
    mapMove: null,
    //FIXME 나중에 다음지도 이슈 처리 후 변경
    // minZoom: 0,
    // maxZoom: 13,
    minZoom: 6,
    maxZoom: 19,
    //FIXME 나중에 다음지도 이슈 처리 후 변경
    // mapType: 'daum',
    // mapTile: 'normal',
    mapType: 'vworld',
    mapTile: 'normal',
    mapDraw: MapDrawEnum.none,
    zoomLevel: 13,
    customLayerList: [],
    selectAddrInfo: null,
    selectHeatMapClfy: null,
    selectHeatMapPeriod: '전체',
    mapClickFn: () => {
    },
    customEventLatLng : null,
    playCctvLatLngList : []
}

const mapReducer = createSlice({
    name: 'map',
    initialState,
    reducers: {

        setZoomLevel: (state: MapState, action: PayloadAction<number>) => {
            return {...state, zoomLevel: action.payload}
        },
        setMinMaxZoom: (state: MapState, action: PayloadAction<[number, number]>) => {
            return {...state, minZoom: action.payload[0], maxZoom: action.payload[1]}
        },
        changeBaseMap: (state: MapState, action: PayloadAction<{ mapType: string, mapTile: string }>) => {
            //지도의 타입이 변경되면 기존의 custom layer는 삭제한다.
            let customLayerList = [...state.customLayerList]
            if (state.mapType !== action.payload.mapType) {
                customLayerList = [];
            }

            return {
                ...state,
                mapType: action.payload.mapType,
                mapTile: action.payload.mapTile,
                customLayerList: customLayerList
            }
        },
        changeMapMove: (state: MapState, action: PayloadAction<MapMove>) => {

            let newState = {...state, mapMove: action.payload}

            if (action.payload.mapMoveType === MapMoveEnum.max_zoom) {
                newState.zoomLevel = TileMapData[state.mapType].options.maxZoom
            } else if (action.payload.mapMoveType === MapMoveEnum.plus_zoom) {
                if (newState.zoomLevel < TileMapData[state.mapType].options.maxZoom ){
                    newState.zoomLevel = newState.zoomLevel + 1
                }
            }else if (action.payload.mapMoveType === MapMoveEnum.event){
                newState.zoomLevel = TileMapData[state.mapType].options.maxZoom -2
            }
            return newState
        },
        addCustomLayer(state: MapState, action: PayloadAction<MapTypeProps>) {

            if (state.mapType !== action.payload.mapNm) {
                //다른 타입의 지도로 변경하는 경우 지도를 타입도 강제로 변환시킨다.
                state.mapType = action.payload.mapNm
                state.mapTile = `normal`
                state.customLayerList = [];
            }
            state.customLayerList.push(action.payload)
        },
        removeCustomLayer(state: MapState, action: PayloadAction<MapTypeProps>) {
            let findIndex = _.findIndex(state.customLayerList, {
                type: action.payload.type,
                mapNm: action.payload.mapNm
            })
            let resultList = [...state.customLayerList]
            if (findIndex > -1) {
                resultList.splice(findIndex, 1);
            }
            return {
                ...state, customLayerList: resultList
            }
        },
        changeSearchResult(state: MapState, action: PayloadAction<Array<DaumAddrInfo>>) {
            return {...state, searchResult: action.payload}
        },
        updateSelectAddrInfo: (state: MapState, action: PayloadAction<AddrInfo | null>) => {
            return {...state, selectAddrInfo: action.payload}
        },
        updateSelectHeatmapClfy: (state: MapState, action: PayloadAction<HeatmapClfy | null>) => {
            return {...state, selectHeatMapClfy: action.payload}
        },
        updateSelectHeatMapPeriod: (state: MapState = initialState, action: PayloadAction<string>) => {
            return {...state, selectHeatMapPeriod: action.payload}
        },
        updateMapClickFn: (state: MapState = initialState, action: PayloadAction<Function>) => {
            return {...state, mapClickFn: action.payload}

        },
        updateMapDraw: (state: MapState = initialState, action: PayloadAction<string>) => {
            return {...state, mapDraw: action.payload}
        },

        changeToMaxZoom: (state: MapState = initialState, action: PayloadAction<string>) => {
            return {...state, zoomLevel: TileMapData[action.payload].options.maxZoom}
        },
        updateCustomEventLatLng : (state:MapState = initialState , action :PayloadAction<LatLngTuple|null>)=>{
            return {...state ,customEventLatLng : action.payload }
        },
        updatePlayCctvLatLngList : (state:MapState = initialState , action :PayloadAction<Array<LatLngExpression>>)=>{
            return {...state ,playCctvLatLngList : action.payload }
        }

    }
})

export const {
    setZoomLevel,
    setMinMaxZoom,
    changeMapMove,
    changeBaseMap,
    addCustomLayer,
    removeCustomLayer,
    changeSearchResult,
    updateSelectAddrInfo,
    updateSelectHeatmapClfy,
    updateSelectHeatMapPeriod,
    updateMapClickFn,
    updateMapDraw,
    changeToMaxZoom,
    updateCustomEventLatLng,
    updatePlayCctvLatLngList

} = mapReducer.actions
export default mapReducer.reducer