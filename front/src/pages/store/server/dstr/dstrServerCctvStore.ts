import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {ClfyInterface, Paging} from "../../../../data/interface/common/CommonInterface";
import {CctvInfo} from "../../../../data/interface/cctv/cctvInterface";

interface Props {
    cctvTabCctvList: Paging<CctvInfo> | null
    clfyList :ClfyInterface[] |null,
    cctvGistCctvList : CctvInfo[],
}

const initialState = {
    cctvTabCctvList: {total:0, data:null},
    clfyList :null,
    cctvGistCctvList : [],
}

const DstrServerCctvStore = createSlice({
    name: 'DstrServerCctvStore',
    initialState,
    reducers: {
        updateCctvTabCctvList(state: Props, action: PayloadAction<Paging<CctvInfo>>) {
            state.cctvTabCctvList = action.payload
        },
        updateClfyList(state: Props, action: PayloadAction<ClfyInterface[]>) {
            state.clfyList = action.payload
        },
        updateGisCctvList(state:Props = initialState ,action :PayloadAction<CctvInfo[]>){
            state.cctvGistCctvList = action.payload.filter(item=>item.lat !== 0.0)
        }
    }
})

export const {updateCctvTabCctvList,updateClfyList,updateGisCctvList} = DstrServerCctvStore.actions
export default DstrServerCctvStore.reducer