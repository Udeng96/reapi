import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {
    EventResult,
    EventServerStore, FireCarInfo,
    FireCarResult,
    Search,
    VwEvent
} from "../../../../data/interface/event/eventInterface"

const initialState: EventServerStore = {
    result: '',
    evetList: [],
    listTotal: 0,
    evetListModal : [],
    listTotalModal : 0,
    evetDetail : null,
    evetDetailModal:null,
    evetLoca: [],
    evetType : [],


    fireCarInfoList : []
}

const dstrServerEventStore = createSlice({
    name: 'dstrServerEventStore',
    initialState,
    reducers: {
        updateEvetList: (state: EventServerStore, action: PayloadAction<EventResult>) => {
            console.log("dummy:",action.payload.data);

            return{
                ...state,
                evetList : action.payload.data ?action.payload.data: [] ,
                listTotal : action.payload.total ?action.payload.total: 0,
            }
        },
        updateModalEvetList: (state: EventServerStore, action: PayloadAction<EventResult>) => {

            return{
                ...state,
                evetListModal : action.payload.data ? action.payload.data: [] ,
                listTotalModal : action.payload.total ?action.payload.total: 0,
            }
        },
        updateEvetLoca: (state: EventServerStore, action: PayloadAction<Array<Search>>) => {

            return {
                ...state,
                evetLoca: action.payload ?action.payload: []
            }
        },

        updateEvetType: (state: EventServerStore, action:PayloadAction<Array<Search>>) => {

            return{
                ...state,
                evetType : action.payload ? action.payload: []
            }
        },

        updateEvetDetail : (state: EventServerStore,action:PayloadAction<VwEvent|null>) => {

            return{
                ...state,
                evetDetail : action.payload!==null? action.payload : null
            }

        },

        updateModalEvetDetail : (state: EventServerStore,action:PayloadAction<VwEvent|null>) => {

            return{
                ...state,
                evetDetailModal : action.payload!==null? action.payload : null
            }

        },
        updateFireCarList : (state:EventServerStore = initialState , action:PayloadAction<Array<FireCarInfo>|null>)=>{
            return {
                ...state , fireCarInfoList : action.payload
            }
        },
        unshiftEvetList : (state: EventServerStore, action: PayloadAction<VwEvent>) => {

            let list = [...state.evetList]
            list.unshift(action.payload)
            return{
                ...state,
                evetList : list,
                listTotal : state.listTotal+1
            }
        },

    }
})

export const {updateEvetList,
    updateModalEvetList,
    updateEvetLoca,
    updateEvetType,
    updateEvetDetail,
    updateModalEvetDetail,
    updateFireCarList,
    unshiftEvetList
} = dstrServerEventStore.actions
export default dstrServerEventStore.reducer