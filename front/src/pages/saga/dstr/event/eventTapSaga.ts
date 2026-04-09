import {all, call, put, takeLatest} from "redux-saga/effects";
import * as evetActions from "../../action/dstr/evet/evet";
import {
    requestEvents,
    requestTypes,
    requestLocations,
    requestDetail,
    requestDetailByUSvcId
} from "../../apis/dstr/event/eventApis";
import {PayloadAction} from "@reduxjs/toolkit";
import {EventResult, EventsPayload, Search, VwEvent} from "../../../../data/interface/event/eventInterface";
import {
    updateEvetDetail,
    updateEvetList,
    updateEvetLoca,
    updateEvetType, updateModalEvetDetail, updateModalEvetList
} from "../../../store/server/dstr/dstrServerEventStore";

const requestMainEventList = function* (action: PayloadAction<EventsPayload>) {
    try {
        let payload = action.payload
        const events: EventResult = yield call(requestEvents, payload)
        yield put(updateEvetList(events))
    } catch (error) {
        console.log("error")
    }
}

const requestModalEventList = function* (action: PayloadAction<EventsPayload>) {
    try {
        let payload = action.payload
        const events: EventResult = yield call(requestEvents, payload)
        yield put(updateModalEvetList(events))
    } catch (error) {
        console.log("error")
    }
}

const requestLocaList = function* () {
    try {
        const locas: Array<Search> = yield call(requestLocations)

        console.log("locas:",locas)

        yield put(updateEvetLoca(locas))


    } catch (error) {
        console.log("error")
    }
}

const requestTypeList = function* () {
    try {
        const types: Array<Search> = yield call(requestTypes)
        console.log("type:",types);
        yield put(updateEvetType(types))
    } catch (error) {
        console.log("error")
    }
}

const requestEventDetail = function* (action: PayloadAction<string | null>) {
    try {
        let payload = action.payload

        if (payload === null) {
            yield put(updateEvetDetail(null))
        } else {
            const detail: VwEvent = yield call(requestDetail, payload)
            yield put(updateEvetDetail(detail))
        }


    } catch (error) {
        console.log("error")
    }
}


const requestEventDetailByUSvcId = function* (action: PayloadAction<string | null>) {
    try {
        let payload = action.payload

        const detail: VwEvent = yield call(requestDetailByUSvcId, payload)

        if (detail){
            yield put(updateEvetDetail(detail))
        }

    } catch (error) {
        console.log("error")
    }
}

const requestModalEventDetail = function* (action: PayloadAction<string | null>) {
    try {
        let payload = action.payload


        if (payload === null) {
            yield put(updateModalEvetDetail(null))
        } else {
            const detail: VwEvent = yield call(requestDetail, payload)
            yield put(updateModalEvetDetail(detail))
        }

    } catch (error) {
        console.log("error")
    }
}

function* watchRoot() {
    yield takeLatest(evetActions.types.REQUEST_EVENTS, requestMainEventList)
    yield takeLatest(evetActions.types.REQUEST_MODAL_EVENTS, requestModalEventList)
    yield takeLatest(evetActions.types.REQUEST_LOCATIONS, requestLocaList)
    yield takeLatest(evetActions.types.REQUEST_EVENT_TYPES, requestTypeList)
    yield takeLatest(evetActions.types.REQUEST_EVENT_DETAIL, requestEventDetail)
    yield takeLatest(evetActions.types.REQUEST_MODAL_DETAIL, requestModalEventDetail)
    yield takeLatest(evetActions.types.REQUEST_MODAL_DETAIL_BY_USVC_ID, requestEventDetailByUSvcId)
}

export default function* EventTapSaga() {
    yield all([watchRoot()])
}

