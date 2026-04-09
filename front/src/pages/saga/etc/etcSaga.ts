import {call, put, takeLatest} from "redux-saga/effects";
import * as addrActions from "../action/etc/addrActions"
import {fetchAddress} from "../apis/etc/addressApi";
import {PayloadAction} from "@reduxjs/toolkit";
import {AddrInfo} from "../../../data/interface/etc/Addr";
import {updateAddrList} from "../../store/server/etc/serverEtcStore";


const fetchAddr = function* (action:PayloadAction<{word:string,type:string}>){

    try{

        const data:AddrInfo[] = yield call(fetchAddress,action.payload);


        if (data){
            yield put(updateAddrList(data))
        }


    }catch(error){
        console.log(error)
    }




}



export default function* etcSaga(){
    yield takeLatest(addrActions.types.FETCH_ADDR,fetchAddr)
}