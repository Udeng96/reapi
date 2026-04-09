import {CctvInfo} from "../../../../data/interface/cctv/cctvInterface";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";

interface ServerRightStore{
    activeCctvList: CctvInfo[]
}

const initialState : ServerRightStore = {
    activeCctvList : []
}

const serverRightStore = createSlice({
    name:'serverRightStore',
    initialState,
    reducers:{
        updateActiveCctvList : (state,action:PayloadAction<CctvInfo[]|null>) => {
           return{
               ...state,
               activeCctvList: action.payload? action.payload : []
           }
        }
    }
})

export const {updateActiveCctvList} = serverRightStore.actions
export default serverRightStore.reducer