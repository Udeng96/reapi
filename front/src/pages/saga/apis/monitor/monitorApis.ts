import conf from "../../../../data/config/config"
import {xhr} from "../xhr";
import {CctvControlParam, MenuListParam} from "../../../../data/interface/monitor/monitorInterface";

export const requestMonitor = async() => {

    let url = conf.API.URL.MONITOR.URL_MONITOR.CCTV_MONITOR_LIST;
    const{data} = await xhr.get(url);

    return data;
}

export const requestFirstMonitor = async(payload:string|null) => {
    let url = `${conf.API.URL.MONITOR.URL_MONITOR.CCTV_MONITOR_LIST}?clfyId=${payload}`
    const{data} =await xhr.get(url);

    return data;
}

export const doCctvControl= async(payload : CctvControlParam)=>{

    let url  = `${conf.API.URL.MONITOR.URL_MONITOR.CCTV_CONTROL.replace("{cctvId}",payload.cctvId)}`
    const {data} = await xhr.post(url,payload)
    return data
}