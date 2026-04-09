import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {AddrInfo} from "../../../../data/interface/etc/Addr";

interface initialStateIF  {
    word : string,
    showSearchList : boolean
    videoReloadCnt : number
}

const initialState :initialStateIF= {
    word : '',
    showSearchList : false,
    videoReloadCnt : 0
}

const etcViewStore = createSlice({
    name : 'etcStore',
    initialState,
    reducers:{
        updateWord (state,action:PayloadAction<string>){
            state.word = action.payload
        },
        updateShowSearchList (state,action:PayloadAction<boolean>){
            state.showSearchList = action.payload
        },
        increaseVideoReloadCnt : (state:initialStateIF = initialState ,action:PayloadAction<void>)=>{
            return {
                ...state,
                videoReloadCnt : state.videoReloadCnt +1
            }
        }
    }
})

export const {updateWord,updateShowSearchList,increaseVideoReloadCnt} = etcViewStore.actions
export default etcViewStore.reducer