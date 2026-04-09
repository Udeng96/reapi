import {call, put, takeLatest} from "redux-saga/effects";
import {PayloadAction} from "@reduxjs/toolkit";
import {types} from "../../action/dstr/evet/heatMapActions"
import {fetchHeatMapClfy, fetchHeatmapEventList} from "../../apis/event/eventApis";
import {updateClfyList, updateCountList} from "../../../store/server/dstr/heatMapServerStore";
import {Count, HeatmapClfy} from "../../../../data/interface/event/heatMapInterface";

const fetchClfy = function* (action:PayloadAction){


    try{
        const data:HeatmapClfy[] | null = yield call(fetchHeatMapClfy)
        if (data){

            yield put(updateClfyList(data))
        }

    }catch(error){
        console.log(error)
    }
}


function* requestHeatMapInfoList(action:PayloadAction<{svcThemeCd:string,unitCd:string,startDtm:string,endDtm:string}>) {
    try {
        const data:Count[]|null = yield call(fetchHeatmapEventList, action.payload);

        yield put(updateCountList(data));
    } catch (error) {
        console.log(error);
    }
}


const HeatMapSaga = function*(){
    yield takeLatest(types.FETCH_CLFY,fetchClfy)
    yield takeLatest(types.FETCH_COUNT,requestHeatMapInfoList)
}


export default HeatMapSaga