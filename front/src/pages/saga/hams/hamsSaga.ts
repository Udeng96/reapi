import {
    fetchHamsCctvList,
    requestHams,
    requestHamsDetail,
    requestLock,
    requestStat,
    requestUnlock
} from "../apis/hams/hamsApis";
import {all, call, delay, put, takeEvery, takeLatest} from "redux-saga/effects";
import {
    HamsCctvInfo,
    HamsDoorStat,
    HamsGisFacInfo,
    LockParam,
    LockResult,
    StatParam
} from "../../../data/interface/hams/hamsInterface";
import {
    getHamsByYearStat,
    getHamsDailyStat,
    getHamsMonthlyStat, updateHamsDoorLock, updateHamsDoorUnlock,
    updateHamsList, updateActiveHamsInfo, updateHamsCctvList
} from "../../store/server/hams/hamsServerStore";
import * as hamsActions from "../../saga/action/hams/hams";
import {PayloadAction} from "@reduxjs/toolkit";
import {
    changeLockRequestLockUnLockBtn,
    showHamsDoorLockLoading,
    showHamsLoading, showHamsMenuLoading, updateActiveHamsCctvInfo
} from "../../store/view/hams/hamsViewStore";

const requestHamsList = function*(){
    try{
        let hamsList : Array<HamsGisFacInfo>  = yield call(requestHams)

        // let dummy  = {...hamsList[0],
        //     facId : hamsList[0].facId,
        //     hamsId:"dummy",
        //     lat : hamsList[0].lat! +0.2,
        //     // lng : hamsList[0].lng! +0.2,
        // }
        // hamsList.push(dummy)

        yield put(showHamsLoading(true))
        yield put(updateHamsList(hamsList))
        yield delay(3000)
        yield put(showHamsLoading(false))
    }catch(error){
        yield put(showHamsLoading(false))
        console.log("error")
    }
}

const requestDoorStat = function*(action:PayloadAction<StatParam>){
    try{
        const data : Array<HamsDoorStat> = yield call(requestStat,action.payload)
        const dateType = action.payload.param.dateType
        if(dateType==="D"){
            yield put(getHamsDailyStat(data))
        }else if(dateType==="M"){
            yield put(getHamsMonthlyStat(data))
        }else{
            yield put(getHamsByYearStat(data))
        }
    }catch(error){
        console.log("error")
    }
}

const requestDoorLock = function*(action:PayloadAction<LockParam>){
    try{
        const data : LockResult = yield call(requestLock,action.payload)
        yield put(showHamsDoorLockLoading(true))
        yield put(updateHamsDoorLock(data))
        yield put(updateActiveHamsInfo(data.data))
        //버튼을 활성화 한다.
        yield put(changeLockRequestLockUnLockBtn())

        //버튼 비활성화 후 3초후에 닫는다.
        yield delay(3000)
        yield put(showHamsDoorLockLoading(false))

    }catch(e){
        yield put(showHamsDoorLockLoading(false))
        console.log("error")
    }
}

const requestDoorUnlock = function*(action:PayloadAction<LockParam>){
    try{
        const data : LockResult = yield call(requestUnlock,action.payload)
        //loading창 표출
        yield put(showHamsDoorLockLoading(true))

        yield put(updateHamsDoorUnlock(data))
        yield put(updateActiveHamsInfo(data.data))

        //버튼을 활성화 한다.
        yield put(changeLockRequestLockUnLockBtn())

        //버튼 비활성화 후 3초후에 닫는다.
        yield delay(3000)
        yield put(showHamsDoorLockLoading(false))

    }catch (e) {
        yield put(showHamsDoorLockLoading(false))
        console.log("error")
    }
}

const requestHamsInfo = function*(action:PayloadAction<string>){
    try{
        const data : HamsGisFacInfo = yield call(requestHamsDetail,action.payload)
        yield put(showHamsMenuLoading(true))
        yield put(updateActiveHamsInfo(data))

        // yield delay(3000);
        // yield put(showHamsMenuLoading(false))

    }catch(e){
        yield put(showHamsMenuLoading(false));
        console.log("error")
    }
}

const requestHamsCctvList = function*(action:PayloadAction<string>){
    try{
        const data : HamsCctvInfo[] = yield call(fetchHamsCctvList,action.payload);
        if (data){
            yield put(updateHamsCctvList(data));
            if (data.length>0){
                yield put(updateActiveHamsCctvInfo(data[0]));
            }
        }
        yield put(showHamsMenuLoading(false))

    }catch (e){
        console.log("error",e);
    }
}

function* watchRoot(){
    yield takeLatest(hamsActions.types.REQUEST_HAMS_LIST,requestHamsList)
    yield takeEvery(hamsActions.types.REQUEST_DOOR_STAT,requestDoorStat)
    yield takeLatest(hamsActions.types.REQUEST_DOOR_LOCK,requestDoorLock)
    yield takeLatest(hamsActions.types.REQUEST_DOOR_UNLOCK,requestDoorUnlock)
    yield takeLatest(hamsActions.types.REQUEST_ACTIVE_HAMS_INFO,requestHamsInfo)
    yield takeLatest(hamsActions.types.REQUEST_ACTIVE_HAMS_CCTV_LIST,requestHamsCctvList)
}
export default function* HamsSaga(){
    yield all([watchRoot()])
}