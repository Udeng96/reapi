import {combineReducers} from "redux";
import DstrViewCctvStore from "./dstrViewCctvStore";
import DstrViewMobileStore from "./dstrViewMobileStore"
import DstrViewEventStore from "./dstrViewEventStore"


const dstrViewRootStore = combineReducers({
    cctv:DstrViewCctvStore,
    mobile: DstrViewMobileStore,
    event:DstrViewEventStore,
})

export default dstrViewRootStore