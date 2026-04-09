import {all, fork} from "redux-saga/effects"
import userSaga from "./user/userSaga";
import etcSaga from "./etc/etcSaga";
import EventTapSaga from "./dstr/event/eventTapSaga";
import HeatMapSaga from "./dstr/event/heatMapSaga";
import CctvTapSaga from "./dstr/cctv/cctvTapSaga";
import MobileTapSaga from "./dstr/event/mobileTapSaga";
import MapServerSaga from "./dstr/mapServerSaga";
import fireCarSaga from "./dstr/event/fireCarSaga";
import WebSocketEventSaga from "./dstr/event/WebSocketEventSaga";
import hamsSaga from "./hams/hamsSaga"
import MonitorSaga from "./monitor/monitorSaga";


export default function* rootSaga(){
    yield all([
        fork(userSaga),
        fork(EventTapSaga),
        fork(etcSaga),
        fork(etcSaga),
        fork(HeatMapSaga),
        fork(CctvTapSaga),
        fork(MapServerSaga),
        fork(CctvTapSaga),
        fork(MobileTapSaga),
        fork(fireCarSaga),
        fork(WebSocketEventSaga),
        fork(hamsSaga),
        fork(MonitorSaga)
    ])
}