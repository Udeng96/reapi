import {combineReducers} from "redux";
import serverStore from "./server/serverRootStore";
import viewRootStore from "./view/viewRootStore";


const rootStore = combineReducers({
    server : serverStore,
    view : viewRootStore
})


export type RootState = ReturnType<typeof rootStore>
export default rootStore