import * as mobileActions from "../../action/dstr/evet/mobileActions";
import {all, call, put, takeLatest} from "redux-saga/effects";
import {requestNowList, requestPastList} from "../../apis/dstr/event/mobileApis";
import {MobileInfo} from "../../../../data/interface/event/mobileInterface";
import {updateMobileNowList, updateMobilePastList} from "../../../store/server/dstr/dstrServerMobileStore";
import {Paging, SearchDefaultOption} from "../../../../data/interface/common/CommonInterface";
import {PayloadAction} from "@reduxjs/toolkit";

const requestPastMobileList = function* (action:PayloadAction<SearchDefaultOption>){
    try{
        const pastMobileList :  Paging<MobileInfo> = yield call(requestPastList, action.payload)
        yield put(updateMobilePastList(pastMobileList))

    }catch(error){
        console.log("error")
    }
}

const requestNowMobileList = function* () {
    try{
        const nowMobileList : Array<MobileInfo> = yield call(requestNowList)
        yield put(updateMobileNowList(nowMobileList))
    }catch (error){
        console.log("error")
    }
}

function* watchRoot(){
    yield takeLatest(mobileActions.types.REQUEST_MOBILE_PAST_LIST,requestPastMobileList);
    yield takeLatest(mobileActions.types.REQUEST_MOBILE_NOW_LIST,requestNowMobileList);
}

export default function* MobileTapSaga(){
    yield all([watchRoot()])
}


