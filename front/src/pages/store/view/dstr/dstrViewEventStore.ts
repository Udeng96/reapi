import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {EventsPayload, EventViewStore, Search} from "../../../../data/interface/event/eventInterface";
import {DTM_OPTION} from "../../../../data/const/eventTapConsts";

const initialState: EventViewStore = {
    checkedLocaList: [],
    checkedTypeList: [],
    checkedModalLocaList: [],
    checkedModalTypeList: [],
    activeSearchBox: "none",
    activeModal: false,
    activeItem: null,
    activeModalItem: null,
    eventPayload: {
        eventIdList: null,
        locationCdList: null,
        startDtm: "20201212",
        endDtm:null,
        page: 1,
        rows: 10
    },
    modalPayload: {
        eventIdList: null,
        locationCdList: null,
        startDtm: DTM_OPTION[3].startDtm.replaceAll("-", ""),
        endDtm: DTM_OPTION[3].endDtm.replaceAll("-", ""),
        page: 1,
        rows: 10
    },
    activeEventType: null,
    wsEventReceive : true
}

const dstrViewEventStore = createSlice({
    name: 'dstrViewEventStore',
    initialState,
    reducers: {
        getCheckedLoca: (state: EventViewStore, action: PayloadAction<Array<Search>>) => {
            return {
                ...state,
                checkedLocaList: action.payload
            }
        },
        getCheckedType: (state: EventViewStore, action: PayloadAction<Array<Search>>) => {
            return {
                ...state,
                checkedTypeList: action.payload
            }
        },
        getCheckedModalLoca: (state: EventViewStore, action: PayloadAction<Array<Search>>) => {
            return {
                ...state,
                checkedModalLocaList: action.payload
            }
        },
        getCheckedModalType: (state: EventViewStore, action: PayloadAction<Array<Search>>) => {
            return {
                ...state,
                checkedModalTypeList: action.payload
            }
        },
        getActiveSearch: (state: EventViewStore, action: PayloadAction<string>) => {
            return {
                ...state,
                activeSearchBox: action.payload
            }
        },
        getActiveModal: (state: EventViewStore, action: PayloadAction<boolean>) => {
            return {
                ...state,
                activeModal: action.payload
            }

        },
        getActiveItem: (state: EventViewStore, action: PayloadAction<string | null>) => {
            return {
                ...state,
                activeItem: action.payload
            }
        },
        getActiveModalItem: (state: EventViewStore, action: PayloadAction<string | null>) => {
            return {
                ...state,
                activeModalItem: action.payload
            }
        },
        getEventPayload : (state:EventViewStore, action: PayloadAction<EventsPayload>) => {
            return{
                ...state,
                eventPayload : action.payload
            }
        },
        getModalPayload: (state: EventViewStore, action: PayloadAction<EventsPayload>) => {
            return {
                ...state,
                modalPayload: action.payload
            }
        },
        getActiveEventType: (state: EventViewStore, action: PayloadAction<string | null>) => {
            return {
                ...state,
                activeEventType: action.payload
            }
        },
        updateWsEventReceive:(state: EventViewStore, action: PayloadAction<boolean>) => {
            return {
                ...state,
                wsEventReceive: action.payload
            }
        },
    }
})

export const {
    getCheckedLoca,
    getCheckedType,
    getCheckedModalLoca,
    getCheckedModalType,
    getActiveSearch,
    getActiveModal,
    getActiveItem,
    getActiveModalItem,
    getModalPayload,
    getActiveEventType,
    getEventPayload,
    updateWsEventReceive
} = dstrViewEventStore.actions
export default dstrViewEventStore.reducer