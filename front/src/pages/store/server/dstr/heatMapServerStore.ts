import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {Count, HeatmapClfy} from "../../../../data/interface/event/heatMapInterface";

interface Props {
    clfyList: HeatmapClfy[],
    countList:Count[]|null
}

const initialState:Props = {
    clfyList: [],
    countList:[]
}

const HeatMapServerStore = createSlice({
    name: 'HeatMapServerStore',
    initialState,
    reducers: {
        updateClfyList: (state: Props = initialState, action: PayloadAction<HeatmapClfy[]>) => {
            return {...state, clfyList: action.payload}
        },
        updateCountList: (state: Props = initialState, action: PayloadAction<Count[]|null>) => {
            return {...state, countList: action.payload}
        }
    }
})


export const {updateClfyList,updateCountList} = HeatMapServerStore.actions
export default HeatMapServerStore.reducer