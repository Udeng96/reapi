import {combineReducers} from "redux";
import serverUserStore from "./user/serverUserStore";
import etcStore from "./etc/serverEtcStore";
import HeatMapServerStore from "./dstr/heatMapServerStore";
import {DstrServerRootStore} from "./dstr/dstrServerRootStore";
import serverRightStore from "./right/serverRightStore";
import serverHamsStore from "./hams/hamsServerStore";
import monitorServerStore from "./monitor/monitorServerStore";


const serverStore = combineReducers({
    user : serverUserStore,
    right : serverRightStore,
    dstrMain :DstrServerRootStore,
    etc : etcStore,
    heatMap :HeatMapServerStore,
    hams : serverHamsStore,
    monitor : monitorServerStore,
})

export default serverStore