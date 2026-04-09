import {createAction} from "@reduxjs/toolkit";
import {LockParam, StatParam} from "../../../../data/interface/hams/hamsInterface";

const types = {
    REQUEST_HAMS_LIST : `REQUEST_HAMS_LIST`,
    REQUEST_DOOR_STAT : `REQUEST_DOOR_STAT`,
    REQUEST_DOOR_LOCK : `REQUEST_DOOR_LOCK`,
    REQUEST_DOOR_UNLOCK : `REQUEST_DOOR_UNLOCK`,
    REQUEST_ACTIVE_HAMS_INFO : `REQUEST_ACTIVE_HAMS_INFO`,
    REQUEST_ACTIVE_HAMS_CCTV_LIST : `REQUEST_ACTIVE_HAMS_CCTV_LIST`
}

const actions = {
    requestHamsList : createAction(types.REQUEST_HAMS_LIST),
    requestDoorStat : createAction<StatParam>(types.REQUEST_DOOR_STAT),
    requestDoorLock : createAction<LockParam>(types.REQUEST_DOOR_LOCK),
    requestDoorUnlock : createAction<LockParam>(types.REQUEST_DOOR_UNLOCK),
    requestActiveHamsInfo : createAction<string>(types.REQUEST_ACTIVE_HAMS_INFO),
    requestActiveHamsCctvList : createAction<string>(types.REQUEST_ACTIVE_HAMS_CCTV_LIST)

}

export {types,actions}