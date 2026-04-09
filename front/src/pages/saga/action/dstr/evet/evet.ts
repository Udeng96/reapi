import {createAction} from "@reduxjs/toolkit";

const types = {
    REQUEST_EVENTS : `REQUEST_EVENTS`,
    REQUEST_MODAL_EVENTS : `REQUEST_MODAL_EVENTS`,
    REQUEST_LOCATIONS : `REQUEST_LOCATIONS`,
    REQUEST_EVENT_TYPES  : `REQUEST_EVENT_TYPES`,
    REQUEST_EVENT_DETAIL : `REQUEST_EVENT_DETAIL`,
    REQUEST_MODAL_DETAIL : `REQUEST_MODAL_DETAIL`,
    REQUEST_MODAL_DETAIL_BY_USVC_ID : `REQUEST_MODAL_DETAIL_BY_USVC_ID`
}

const actions = {
    requestEvents : createAction<any,string>(types.REQUEST_EVENTS),
    requestModalEvents : createAction<any,string>(types.REQUEST_MODAL_EVENTS),
    requestLocations : createAction(types.REQUEST_LOCATIONS),
    requestEventTypes : createAction(types.REQUEST_EVENT_TYPES),
    requestEventDetail : createAction<any,string>(types.REQUEST_EVENT_DETAIL),
    requestModalDetail : createAction<any,string>(types.REQUEST_MODAL_DETAIL),
    requestModalDetailByUSvcId : createAction<any,string>(types.REQUEST_MODAL_DETAIL_BY_USVC_ID),
}

export {types, actions}