import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {ClfyInterface} from "../../../../data/interface/common/CommonInterface";
import {CctvInfo} from "../../../../data/interface/cctv/cctvInterface";
import {MAX_CCTV_PLAYER_CNT} from "../../../../data/const/mapConst";


interface Props {
    selectClfy: ClfyInterface | null,
    word: string,
    activeCctv: CctvInfo | null;
    activeClusterLatLon: [number, number] | null;
    activeClusterMarkerList: CctvInfo[] | [],
    playGisCctvList : CctvInfo[] | null,
    playCctvList : CctvInfo[],
    fullScreenId : string
}

const initialState: Props = {
    selectClfy: {id: '', nm: '전체'},
    word: '',
    activeCctv: null,
    activeClusterLatLon: null,
    activeClusterMarkerList: [],
    playGisCctvList : [],
    playCctvList : [],
    fullScreenId : ''
}


const DstrViewCctvStore = createSlice({
    name: 'DstrViewCctvStore',
    initialState,
    reducers: {
        updateSelectClfy: (state: Props = initialState, action: PayloadAction<ClfyInterface | null>) => {
            return {
                ...state,
                selectClfy: action.payload
            }
        },
        updateWord: (state: Props = initialState, action: PayloadAction<string>) => {
            return {
                ...state,
                word: action.payload
            }
        },
        updateActiveCctv: (state: Props = initialState, action: PayloadAction<CctvInfo | null>) => {
            return {...state, activeCctv: action.payload}
        },
        updateActiveClusterLatLon: (state: Props = initialState, action: PayloadAction<[number, number] | null>) => {
            return {...state, activeClusterLatLon: action.payload}
        },
        updateActiveClusterMarkerList: (state: Props = initialState, action: PayloadAction<CctvInfo[]>) => {
            return {...state, activeClusterMarkerList: action.payload}
        },
        updatePlayGisCctvList: (state:Props = initialState, action :PayloadAction<CctvInfo[]|null>) =>{

            if (MAX_CCTV_PLAYER_CNT >= (action.payload ? action.payload?.length : 0)){ //최대 개수만큼까지만 표출
                return {...state , playGisCctvList : action.payload}
            }else {
                return {...state}
            }
        },
        updatePlayCctvList: (state: Props = initialState, action: PayloadAction<CctvInfo[]>) => {
            return {...state, playCctvList: action.payload}
        },
        updateFullScreenId :(state: Props = initialState, action: PayloadAction<string>) => {
            return {...state, fullScreenId: action.payload}
        },
    }
})

export const {
    updateSelectClfy, updateWord,
    updateActiveCctv,
    updateActiveClusterLatLon,
    updateActiveClusterMarkerList,
    updatePlayGisCctvList,
    updatePlayCctvList,
    updateFullScreenId
} = DstrViewCctvStore.actions
export default DstrViewCctvStore.reducer