import {createAction} from "@reduxjs/toolkit";
import {CctvControlParam, MenuListParam} from "../../../../data/interface/monitor/monitorInterface";

const types = {
    REQUEST_CCTV_MONITOR_LIST : `REQUEST_CCTV_MONITOR_LIST`,
    REQUEST_FIRST_PLAY_PAGE : `REQUEST_FIRST_PLAY_PAGE`,
    REQUEST_DO_CCTV_CONTROL : `REQUEST_DO_CCTV_CONTROL`
}

const actions = {
    requestCctvMonitorList : createAction(types.REQUEST_CCTV_MONITOR_LIST),
    requestFirstPlayPage : createAction<string>(types.REQUEST_FIRST_PLAY_PAGE),
    requestDoCctvControl : createAction<CctvControlParam>(types.REQUEST_DO_CCTV_CONTROL)
}

export {types,actions}