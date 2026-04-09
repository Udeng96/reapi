import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {AddrInfo} from "../../../../data/interface/etc/Addr";

//AddrInfo

interface initialStateIF  {
    addrList:AddrInfo[]
}

const initialState :initialStateIF= {
    addrList :[]
}

const serverEtcStore = createSlice({
    name : 'etcStore',
    initialState,
    reducers:{
        updateAddrList : (state,action:PayloadAction<AddrInfo[]>)=>{
            state.addrList = [];
            state.addrList = action.payload
        }
    }
})

export const {updateAddrList} = serverEtcStore.actions
export default serverEtcStore.reducer