import {all, call, delay, put, takeLatest} from "redux-saga/effects";
import {PayloadAction} from "@reduxjs/toolkit";
import * as standardSystemApi from "../../apis/event/standardSystem"
import {FireCarInfo, FireCarResult} from "../../../../data/interface/event/eventInterface";
import {updateFireCarList} from "../../../store/server/dstr/dstrServerEventStore";
import {types} from "../../action/dstr/evet/fireCarACtions";



function* fetchDispatchFireCar(action:PayloadAction<string>){
    try{
        const data : Array<FireCarInfo>|[] = yield call(standardSystemApi.fetchDispatchFireCar,action.payload)

        if (data){
            yield put(updateFireCarList(data))
        }

    }catch(error){
        console.log(error)
    }
}

export default function* fireCarSaga(){
    yield takeLatest(types.FETCH_DISPATCH_FIRE_CAR,fetchDispatchFireCar)
}