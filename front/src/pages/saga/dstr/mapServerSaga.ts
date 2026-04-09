import {all, call, put, takeLatest} from "redux-saga/effects";
import * as cctvActions from "../action/dstr/cctv/cctvActions";
import {PayloadAction} from "@reduxjs/toolkit";
import {CctvInfo, SearchOption} from "../../../data/interface/cctv/cctvInterface";
import {fetchCctvs} from "../apis/dstr/cctv/cctvApis";
import {Paging} from "../../../data/interface/common/CommonInterface";
import {updateGisCctvList} from "../../store/server/dstr/dstrServerCctvStore";

const fetchCctvList = function* (action:PayloadAction<SearchOption>){
    try {
        let payload = action.payload
        const data:Paging<CctvInfo> = yield call(fetchCctvs,payload)

        if (data){
            yield put(updateGisCctvList(data.data as CctvInfo[]))
        }

    } catch (error) {
        console.log("error")
    }
}

function* watchRoot(){
    yield takeLatest(cctvActions.types.FETCH_GIS_CCTV_LIST, fetchCctvList)
}

export default function* MapServerSaga(){
    yield all([watchRoot()])
}

