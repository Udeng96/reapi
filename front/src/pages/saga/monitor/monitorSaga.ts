import {CctvControlParam, MenuListResultType} from "../../../data/interface/monitor/monitorInterface";
import {all, call, put, takeLatest} from "redux-saga/effects";
import * as monitorActions from "../../saga/action/monitor/monitor";
import {updateCctvMenuList, updateFirstPlayPage} from "../../store/server/monitor/monitorServerStore";
import {doCctvControl, requestFirstMonitor, requestMonitor} from "../apis/monitor/monitorApis";
import {PayloadAction} from "@reduxjs/toolkit";
import {CommonResponse} from "../../../data/interface/common/CommonInterface";

const requestMonitorList = function* () {
    try {

        let cctvMonitorList: Array<MenuListResultType> = yield call(requestMonitor)
        yield put(updateCctvMenuList(cctvMonitorList))
    } catch (error) {
        console.log("error")
    }
}

const requestFirstMonitorCctv = function* (action: PayloadAction<string>) {
    try {
        let firstPlayPage: Array<MenuListResultType> = yield call(requestFirstMonitor, action.payload);
        console.log("data",firstPlayPage)
        yield put(updateFirstPlayPage(firstPlayPage));
    } catch (error) {
        console.log("error");
    }

}

const requestDoCctvControl = function* (action:PayloadAction<CctvControlParam>){
    try{
        let result :CommonResponse  = yield call(doCctvControl,action.payload)

        if (result.resultCode !== 'true'){
            console.log("error");
            alert("CCTV 제어를 실패하였습니다.")
        }

    }catch(error){
        console.log("error");
        alert("CCTV 제어를 실패하였습니다.")
    }
}

function* watchRoot() {
    yield takeLatest(monitorActions.types.REQUEST_CCTV_MONITOR_LIST, requestMonitorList)
    yield takeLatest(monitorActions.types.REQUEST_FIRST_PLAY_PAGE, requestFirstMonitorCctv)
    yield takeLatest(monitorActions.types.REQUEST_DO_CCTV_CONTROL, requestDoCctvControl)
}

export default function* MonitorSaga() {
    yield all([watchRoot()])
}