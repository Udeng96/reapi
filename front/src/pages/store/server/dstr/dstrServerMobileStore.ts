import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {MobileInfo, MobileServerStore} from "../../../../data/interface/event/mobileInterface";
import {Paging} from "../../../../data/interface/common/CommonInterface";

const initialState: MobileServerStore = {
    pastList: {total:0, data:[]},
    pastListTotal: 0,
    nowList: [],
    nowListTotal: 0,
}

const dstrServerMobileStore = createSlice({
    name: 'dstrServerMobileStore',
    initialState,
    reducers: {
        updateMobilePastList: (state: MobileServerStore, action: PayloadAction<Paging<MobileInfo>>) => {
            return {
                ...state,
                pastList: action.payload ? action.payload : {total:0, data:[]},
                pastListTotal: action.payload.total ? action.payload.total : 0
            }
        },
        updateMobileNowList: (state: MobileServerStore, action: PayloadAction<Array<MobileInfo>>) => {

            return {
                ...state,
                nowList: action.payload ? action.payload: [],
                nowListTotal: action.payload.length ? action.payload.length: 0
            }
        }
    }
})

export const {updateMobilePastList, updateMobileNowList} = dstrServerMobileStore.actions
export default dstrServerMobileStore.reducer