import  {changeDomainUrl} from "../../../util/CommonUtil";
import {xhr} from "../xhr";

import config from "../../../../data/config/config"


export const fetchDispatchFireCar = async (statEvetOutbSeqn:string)=>{
    // const {data} = await xhr.get(changeDomainUrl(config.API.URL.EVENTS.URL_EVENT.FIRE_CAR_INFO) , {
    const {data} = await xhr.get(config.API.URL.EVENTS.URL_EVENT.FIRE_CAR_INFO , {
        params : {statEvetOutbSeqn : statEvetOutbSeqn}
    })
    return data
}