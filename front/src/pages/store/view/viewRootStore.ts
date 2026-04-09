import {combineReducers} from "redux";
import hamsViewStore from "./hams/hamsViewStore";
import mapStore from "./dstr/mapStore";
import etcViewStore from "./etc/etcViewStore";
import dstrViewRootStore from "./dstr/dstrViewRootStore";
import monitorViewStore from "./monitor/monitorViewStore";


const viewRootStore = combineReducers({
    hams: hamsViewStore,
    map : mapStore,
    dstrMain :dstrViewRootStore,
    monitor : monitorViewStore,
    etc : etcViewStore
})

export default viewRootStore