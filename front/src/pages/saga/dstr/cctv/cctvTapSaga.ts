import {all, call, put, takeLatest} from "redux-saga/effects";
import * as cctvActions from "../../action/dstr/cctv/cctvActions";
import {PayloadAction} from "@reduxjs/toolkit";
import {CctvInfo, SearchOption} from "../../../../data/interface/cctv/cctvInterface";
import {fetchCctvClfy, fetchCctvDetail, fetchCctvs} from "../../apis/dstr/cctv/cctvApis";
import {ClfyInterface, Paging} from "../../../../data/interface/common/CommonInterface";
import {updateCctvTabCctvList, updateClfyList} from "../../../store/server/dstr/dstrServerCctvStore";

const fetchList = function* (action:PayloadAction<SearchOption>){
    try {
        let payload = action.payload
        const data:Paging<CctvInfo> = yield call(fetchCctvs,payload)

        if (data){
            yield put(updateCctvTabCctvList(data))
        }

    } catch (error) {
        console.log("error")
    }
}

const fetchDetail = function* (action:PayloadAction<string>){
    try{
        const data:CctvInfo = yield call(fetchCctvDetail,action.payload)
    }catch(error){
        console.log("error")
    }
}

const fetchClfy = function* (){
    try{
        const data:ClfyInterface[] = yield call(fetchCctvClfy)
        if (data){
            yield put(updateClfyList(data))
        }

    }catch(error){
        console.log("error")
    }
}

function* watchRoot(){
    yield takeLatest(cctvActions.types.FETCH_LIST, fetchList)
    yield takeLatest(cctvActions.types.FETCH_DETAIL,fetchDetail)
    yield takeLatest(cctvActions.types.FETCH_CLFY,fetchClfy)
}

export default function* CctvTapSaga(){
    yield all([watchRoot()])
}

