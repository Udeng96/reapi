import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {CctvInfo, TransCycleOptionType} from "../../../../data/interface/monitor/monitorInterface";
import {PageTransType, TransCycleOptions} from "../../../../data/const/monitorMenuConst";
import {NodeModel} from "@minoru/react-dnd-treeview";

interface ViewMonitorStore{
    gridRatio: Array<number>,
    isMenuOpen : boolean,
    isPageTrans : PageTransType,
    pageTransCycle : TransCycleOptionType,
    // 현재 화면에 나타는 cctv List
    currentPagePlayList : Array<CctvInfo|null>,
    // 카메라 전환할 때 필요한 총 cctv list
    allPagePlayList : Array<Array<CctvInfo|null>>
    // drag하는 cctv 및 분류에 관한 data
    dragTargetInfo : NodeModel|null,
    searchWord : string,
    searchResultMenuList : Array<string|number>,
    menuUpdateFn : Function;
    searchMenuUpdateFn :Function;
}

const initialState:ViewMonitorStore = {
    gridRatio : [3,3],
    isMenuOpen : true,
    isPageTrans : PageTransType.NONE,
    pageTransCycle : TransCycleOptions[2],
    currentPagePlayList : [],
    allPagePlayList :[],
    dragTargetInfo : null,
    searchWord : '',
    searchResultMenuList : [],
    menuUpdateFn : () => {

    },
    searchMenuUpdateFn : () => {

    }
}

const monitorViewStore = createSlice({
    name:'monitorViewStore',
    initialState,
    reducers : {
        updateGridAmount : (state:ViewMonitorStore=initialState,action:PayloadAction<Array<number>>)=>{
            return{
                ...state,
                gridRatio : action.payload
            }
        },
        updateMenuState : (state:ViewMonitorStore=initialState,action:PayloadAction<boolean>) => {
            return{
                ...state,
                isMenuOpen : action.payload
            }
        },
        updateCycleState : (state:ViewMonitorStore=initialState,action:PayloadAction<PageTransType>)=>{
            return{
                ...state,
                isPageTrans : action.payload
            }
        },
        updatePageTransCycle : (state:ViewMonitorStore=initialState,action:PayloadAction<TransCycleOptionType>)=>{
            return{
                ...state,
                pageTransCycle : action.payload
            }
        },
        updateNowPagePlayList : (state:ViewMonitorStore=initialState,action:PayloadAction<Array<CctvInfo|null>>)=>{
            return{
                ...state,
                currentPagePlayList : action.payload
            }
        },
        updateAllPagePlayList : (state:ViewMonitorStore=initialState, action:PayloadAction<Array<Array<CctvInfo|null>>>)=>{
            return{
                ...state,
                allPagePlayList : action.payload
            }
        },
        updateDragTarget : (state:ViewMonitorStore=initialState,action:PayloadAction<NodeModel|null>)=>{
            return{
                ...state,
                dragTargetInfo : action.payload
            }
        },
        updateSearchWord : (state:ViewMonitorStore = initialState,action:PayloadAction<string>)=>{
            return{
                ...state,
                searchWord: action.payload
            }
        },
        updateSearchResultMenuList : (state:ViewMonitorStore=initialState,action:PayloadAction<Array<string|number>>)=>{
            return{
                ...state,
                searchResultMenuList : action.payload
            }
        },
        updateMenuNodesFn : (state:ViewMonitorStore=initialState,action:PayloadAction<Function>)=>{
            return{
                ...state,
                menuUpdateFn : action.payload
            }
        },
        updateSearchMenuNodesFn : (state:ViewMonitorStore=initialState,action:PayloadAction<Function>)=>{
            return{
                ...state,
                searchMenuUpdateFn : action.payload
            }
        }
    }
})

export const {
    updateGridAmount,
    updateMenuState,
    updateCycleState,
    updatePageTransCycle,
    updateNowPagePlayList,
    updateAllPagePlayList,
    updateDragTarget,
    updateSearchWord,
    updateSearchResultMenuList,
    updateMenuNodesFn,
    updateSearchMenuNodesFn} = monitorViewStore.actions
export default monitorViewStore.reducer