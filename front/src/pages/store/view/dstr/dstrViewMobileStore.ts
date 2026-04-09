import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {
    FullPopupInfo,
    MobileInfo,
    MobilePopupSize,
    MobileViewStore
} from "../../../../data/interface/event/mobileInterface";
import {DTM_OPTION} from "../../../../data/const/mobileTapConsts";


const initialState:MobileViewStore = {
    activeLiveList : null,
    activeLiveBtnList: [],
    activePastItem : null,
    activePastBtnList: [],
    activeMobileClusterLatLon : null,
    activeMobileClusterMarkerList: [],
    fullPopupInfo : {isOpen:false, dtm:"", userDept:"", userNm:"", id : "", rtsp:""},
    mobilePopupSize : { height:0, width:0},


    startDtm:DTM_OPTION[3].startDtm,
    endDtm:DTM_OPTION[3].endDtm,
    doFetchPastCount : 1

}

const DstrViewMobileStore = createSlice({
    name: 'DstrViewMobileStore',
    initialState,
    reducers:{
        updateActiveLiveList : (state:MobileViewStore, action:PayloadAction<MobileInfo|null>)=>{
            return{
                ...state,
                activeLiveList : action.payload? action.payload :null
            }
        },
        updateActiveLiveBtnList : (state:MobileViewStore, action:PayloadAction<Array<MobileInfo>|null>)=>{


            return{
                ...state,
                activeLiveBtnList : action.payload?action.payload:[]
            }
        },
        updateActivePastList : (state:MobileViewStore, action:PayloadAction<MobileInfo|null>)=>{
            return{
                ...state,
                activePastItem : action.payload?action.payload:null
            }
        },
        updateActivePastBtnList : (state:MobileViewStore, action:PayloadAction<Array<MobileInfo>|null>)=>{
            return{
                ...state,
                activePastBtnList : action.payload?action.payload:[]
            }
        },

        updateFullPopupInfo : (state:MobileViewStore,action:PayloadAction<FullPopupInfo>)=>{
            return{
                ...state,
                fullPopupInfo : action.payload
            }
        },

        updateMobilePopupSize : (state:MobileViewStore, action:PayloadAction<MobilePopupSize>)=>{
            return{
                ...state,
                mobilePopupSize : action.payload
            }
        },
        updateStartDtm : (state:MobileViewStore, action:PayloadAction<string>)=>{
            return{
                ...state,
                startDtm : action.payload
            }
        },
        updateEndDtm : (state:MobileViewStore, action:PayloadAction<string>)=>{
            return{
                ...state,
                endDtm : action.payload
            }
        },
        plusDoFetchPastCount : (state:MobileViewStore,action:PayloadAction)=>{
            return {
                ...state, doFetchPastCount :state.doFetchPastCount+1
            }
        }
    }
})

export const {
    updateActiveLiveList,
    updateActivePastList,
    updateActiveLiveBtnList,
    updateActivePastBtnList,
    updateFullPopupInfo,
    updateMobilePopupSize,
    updateStartDtm,
    updateEndDtm,
    plusDoFetchPastCount
} = DstrViewMobileStore.actions
export default DstrViewMobileStore.reducer