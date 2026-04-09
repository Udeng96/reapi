import {createAction} from "@reduxjs/toolkit";

const types = {
    FETCH_CLFY : `HEATMAP_FETCH_CLFY`,
    FETCH_COUNT : `FETCH_COUNT`,
}

const actions = {
    fetchClfy : createAction(types.FETCH_CLFY),
    fetchCount : createAction<{svcThemeCd:string,unitCd:string,startDtm:string,endDtm:string}>(types.FETCH_COUNT),
}

export {types, actions}