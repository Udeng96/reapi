import {createAction} from "@reduxjs/toolkit";
import {SearchOption} from "../../../../../data/interface/cctv/cctvInterface";


const types ={
    FETCH_LIST : `FETCH_CCTVS`,
    FETCH_DETAIL : `FETCH_CCTV_DETAIL`,
    FETCH_CLFY : `FETCH_CCTV_CLFY`,

    FETCH_GIS_CCTV_LIST : `FETCH_GIS_CCTV_LIST`
}

const actions = {
    fetchList : createAction<SearchOption|null>(types.FETCH_LIST),
    fetchDETAIL : createAction<string>(types.FETCH_DETAIL),
    fetchCLFY : createAction(types.FETCH_CLFY),
    fetchGisCctvList : createAction(types.FETCH_GIS_CCTV_LIST)
}

export {types, actions};