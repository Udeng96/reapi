import {MenuListResultType} from "../../../../data/interface/monitor/monitorInterface";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";


interface ServerMonitorStore{
    menuList : Array<MenuListResultType>,
    firstPlayPage : Array<MenuListResultType>
}

const initialState : ServerMonitorStore = {
    menuList : [],
    firstPlayPage : []
}

const monitorServerStore = createSlice({
    name:'monitorServerStore',
    initialState,
    reducers:{
        updateCctvMenuList : (state:ServerMonitorStore,action:PayloadAction<Array<MenuListResultType>>)=>{
            return{
                ...state,
                menuList : action.payload ? action.payload: []
            }
        },
        updateFirstPlayPage : (state:ServerMonitorStore,action:PayloadAction<Array<MenuListResultType>>)=>{
            return{
                ...state,
                firstPlayPage : action.payload ? action.payload :  []
            }
        }
    }
})

export const {
    updateCctvMenuList,
    updateFirstPlayPage
} = monitorServerStore.actions
export default monitorServerStore.reducer