import {xhr} from "../../xhr";
import conf from "../../../../../data/config/config"
import {EventsPayload} from "../../../../../data/interface/event/eventInterface";

export const requestEvents = async (payload: EventsPayload) =>  {
    const locaJoined = payload.locationCdList? payload.locationCdList.join(',') : payload.locationCdList
    const typeJoined = payload.eventIdList? payload.eventIdList.join(',') : payload.eventIdList
    const newPayload = {...payload, locationCdList: locaJoined, eventIdList:typeJoined}
    const {data} = await xhr.get(conf.API.URL.EVENTS.URL_EVENT.EVENTS,{params:newPayload})

    return data
}

export const requestDetail = async(payload:string|null) => {

    let url = conf.API.URL.EVENTS.URL_EVENT.EVENTS + `/${payload}`

    const {data} = await xhr.get(url)

    return data
}



export const requestDetailByUSvcId = async(payload:string|null) => {

    let url = conf.API.URL.EVENTS.URL_EVENT.EVENTS_BY_USVC_ID + `/${payload}`

    const {data} = await xhr.get(url)

    return data
}


export const requestLocations = async() => {

    const {data} = await xhr.get(conf.API.URL.EVENTS.URL_EVENT.LOCATIONS)

    return data

}

export const requestTypes = async() => {

    const {data} = await xhr.get(conf.API.URL.EVENTS.URL_EVENT.EVENT_TYPES)

    return data
}

