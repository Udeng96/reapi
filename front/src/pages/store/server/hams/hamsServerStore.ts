import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {
    HamsCctvInfo,
    HamsDoorStat,
    HamsGisFacInfo,
    LockResult,
    WsDoeInfo
} from "../../../../data/interface/hams/hamsInterface";

interface ServerHamsStore {

    hamsList: Array<HamsGisFacInfo> | null,
    hamsDailyStat: Array<HamsDoorStat>,
    hamsMonthlyStat: Array<HamsDoorStat>,
    hamsByYearStat: Array<HamsDoorStat>,
    doorLockResult: LockResult | null,
    doorUnlockResult: LockResult | null
    wsDoeInfo: WsDoeInfo | null
    activeHamsInfo: HamsGisFacInfo | null,
    hamsCctvList : HamsCctvInfo[]
}

const initialState: ServerHamsStore = {
    hamsList: [],
    hamsDailyStat: [],
    hamsMonthlyStat: [],
    hamsByYearStat: [],
    doorLockResult: null,
    doorUnlockResult: null,
    wsDoeInfo: null,
    activeHamsInfo: null,
    hamsCctvList : [],
}

const serverHamsStore = createSlice({
    name: 'serverHamsStore',
    initialState,
    reducers: {
        updateHamsList: (state: ServerHamsStore, action: PayloadAction<Array<HamsGisFacInfo>>) => {
            return {
                ...state,
                hamsList: action.payload ? action.payload : []
            }
        },
        getHamsDailyStat: (state: ServerHamsStore, action: PayloadAction<Array<HamsDoorStat>>) => {
            return {
                ...state,
                hamsDailyStat: action.payload
            }
        },
        getHamsMonthlyStat: (state: ServerHamsStore, action: PayloadAction<Array<HamsDoorStat>>) => {
            return {
                ...state,
                hamsMonthlyStat: action.payload
            }
        },
        getHamsByYearStat: (state: ServerHamsStore, action: PayloadAction<Array<HamsDoorStat>>) => {
            return {
                ...state,
                hamsByYearStat: action.payload
            }
        },
        updateHamsDoorLock: (state: ServerHamsStore, action: PayloadAction<LockResult>) => {
            return {
                ...state,
                doorLockResult: action.payload
            }
        },
        updateHamsDoorUnlock: (state: ServerHamsStore, action: PayloadAction<LockResult>) => {
            return {
                ...state,
                doorUnlockResult: action.payload
            }
        },
        updateWsDoeInfo: (state: ServerHamsStore = initialState, action: PayloadAction<WsDoeInfo | null>) => {
            return {
                ...state, wsDoeInfo: action.payload
            }
        },
        updateActiveHamsInfo : (state:ServerHamsStore=initialState, action:PayloadAction<HamsGisFacInfo>) => {
            return {
                ...state, activeHamsInfo:action.payload
            }
        },
        updateHamsCctvList : (state:ServerHamsStore = initialState, action:PayloadAction<HamsCctvInfo[]>) => {
            return{
                ...state,  hamsCctvList : action.payload
            }
        }
    }
})


export const {
    updateHamsList,
    getHamsDailyStat,
    getHamsMonthlyStat,
    getHamsByYearStat,
    updateHamsDoorLock,
    updateHamsDoorUnlock,
    updateWsDoeInfo,
    updateActiveHamsInfo,
    updateHamsCctvList
} = serverHamsStore.actions
export default serverHamsStore.reducer