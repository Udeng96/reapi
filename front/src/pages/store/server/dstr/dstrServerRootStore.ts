import {combineReducers} from "redux";
import DstrServerCctvStore from "./dstrServerCctvStore";
import dstrServerMobileStore from "./dstrServerMobileStore";
import DstrServerEventStore from "./dstrServerEventStore"

export const DstrServerRootStore = combineReducers({
    cctv :DstrServerCctvStore,
    mobile : dstrServerMobileStore,
    event : DstrServerEventStore,
})

