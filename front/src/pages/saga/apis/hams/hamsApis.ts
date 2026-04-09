import conf from "../../../../data/config/config"
import {xhr} from "../xhr";
import {LockParam, StatParam} from "../../../../data/interface/hams/hamsInterface";
import {async} from "q";
import {param} from "jquery";

export const requestHams = async () => {


    const {data} = await xhr.get(conf.API.URL.HAMS.URL_HAMS.HAMS_LIST)

    return data

}


export const requestHamsDetail = async(param:string)=>{
    let url = conf.API.URL.HAMS.URL_HAMS.HAMS_DETAIL.replace('{id}',`${param}`)
    const {data} = await xhr.get(url)

    return data
}

export const fetchHamsCctvList = async(param:string) =>{
    let url = conf.API.URL.HAMS.URL_HAMS.HAMS_CCTV;
    const {data} = await xhr.get(url, {
        params:{
            hamsName : param
        }
    })

    return data;
}
export const requestStat  = async(params:StatParam) => {

    let url = conf.API.URL.HAMS.URL_HAMS.HAMS_STAT.replace('{id}',`${params.hamsId}`)
    const {data} = await xhr.get(url,{params:params.param})

    return data

}

export const requestLock = async(param:LockParam) => {

    // 뒷문일 경우 target 을 2로 변경해 줘야 함. (default 1)
    let url = conf.API.URL.HAMS.URL_HAMS.DOOR_LOCK.replace('{id}',param.hamsId)
    const {data} = await xhr.post(url,param.body)

    return data

}

export const requestUnlock = async(param:LockParam) => {

    let url = conf.API.URL.HAMS.URL_HAMS.DOOR_UNLOCK.replace('{id}',param.hamsId)
    const {data} = await xhr.post(url,param.body)

    return data

}

