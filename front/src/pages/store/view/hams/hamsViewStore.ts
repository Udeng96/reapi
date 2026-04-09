import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {HamsCctvInfo, HamsGisFacInfo} from "../../../../data/interface/hams/hamsInterface";

interface ViewHamsStore {
    activeHamsInfo : HamsGisFacInfo | null,
    cctvPopupOpen : boolean,
    lockPopupOpen : boolean,
    chartPopupOpen : boolean,
    hoverMenuHamsId : string|null,
    lockRequestLockUnLockBtn : boolean,
    isLoading : boolean,
    isDoorLockLoading:boolean,
    isHamsMenuLoading: boolean,
    videoReloadCnt : number,
    activePlayHamsCctv : HamsCctvInfo | null
}

const initialState:ViewHamsStore ={
    activeHamsInfo : null,
    cctvPopupOpen:false,
    lockPopupOpen:false,
    chartPopupOpen:false,
    hoverMenuHamsId : null,
    lockRequestLockUnLockBtn : false,
    isLoading:true,
    isDoorLockLoading:false,
    isHamsMenuLoading:false,
    videoReloadCnt : 0,
    activePlayHamsCctv : null
}



const hamsViewStore = createSlice({
    name : 'hamsViewStore',
    initialState,
    reducers : {
        updateActiveHamsInfo : (state:ViewHamsStore = initialState, action : PayloadAction<HamsGisFacInfo>) =>{
            return {
                ...state,
                activeHamsInfo : action.payload
            }
        },
        updateCctvPopupOpen : (state:ViewHamsStore = initialState,action:PayloadAction<boolean>) => {
            return{
                ...state,
                cctvPopupOpen:action.payload
            }
        },
        updateLockPopupOpen : (state:ViewHamsStore = initialState,action:PayloadAction<boolean>)=>{
            return{
                ...state,
                lockPopupOpen:action.payload
            }
        },
        updateChartPopupOpen : (state:ViewHamsStore = initialState,action:PayloadAction<boolean>)=>{
            return{
                ...state,
                chartPopupOpen:action.payload
            }
        },
        updateHoverMenuHamsId : (state:ViewHamsStore = initialState,action:PayloadAction<string|null>)=>{
            return{
                ...state,
                hoverMenuHamsId : action.payload
            }
        },
        changeLockRequestLockUnLockBtn : (state:ViewHamsStore= initialState,action:PayloadAction)=>{
            return {
                ...state,
                lockRequestLockUnLockBtn : !state.lockRequestLockUnLockBtn
            }
        },
        showHamsLoading : (state:ViewHamsStore = initialState, action:PayloadAction<boolean>)=>{
            return{
                ...state,
                isLoading : action.payload
            }
        },
        showHamsDoorLockLoading : (state:ViewHamsStore = initialState, action:PayloadAction<boolean>)=>{
            return{
                ...state,
                isDoorLockLoading : action.payload
            }
        },
        showHamsMenuLoading : (state:ViewHamsStore = initialState, action:PayloadAction<boolean>)=>{
            return{
                ...state,
                isHamsMenuLoading : action.payload
            }
        },
        updateHamsVideoReloadCnt : (state:ViewHamsStore = initialState, action:PayloadAction<void>) => {
            return {
                ...state,
                videoReloadCnt : state.videoReloadCnt +1
            }
        },
        updateActiveHamsCctvInfo : (state:ViewHamsStore = initialState, action : PayloadAction<HamsCctvInfo | null>) => {
            return{
                ...state,
                activePlayHamsCctv : action.payload
            }
        }
    }

})

export const {updateActiveHamsInfo,
    updateCctvPopupOpen,
    updateLockPopupOpen,
    updateChartPopupOpen,
    updateHoverMenuHamsId,
    changeLockRequestLockUnLockBtn,
    showHamsLoading,
    showHamsDoorLockLoading,
    showHamsMenuLoading,
    updateHamsVideoReloadCnt,
    updateActiveHamsCctvInfo
} = hamsViewStore.actions
export default hamsViewStore.reducer



