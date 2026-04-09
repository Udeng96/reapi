import {xhr} from "../xhr";
import conf from "../../../../data/config/config"
import {EventsPayload} from "../../../../data/interface/event/eventInterface";

export const requestEvents = async (payload: EventsPayload) => {

    let url = conf.API.URL.EVENTS.URL_EVENT.EVENTS

    const {data} = await xhr.get(url, {params: payload})

    return data
}

export const requestLocations = async () => {

    const {data} = await xhr.get(conf.API.URL.EVENTS.URL_EVENT.LOCATIONS)

    return data

}

export const fetchHeatMapClfy = async () => {
    const {data} = await xhr.get(conf.API.URL.EVENTS.URL_HEATMAP.CLFY)
    return data
}


export const fetchHeatmapEventList = async (params:{svcThemeCd:string,unitCd:string,startDtm:string,endDtm:string}) => {
    const {data} = await xhr.get(conf.API.URL.EVENTS.URL_HEATMAP.COUNT, {
        params: {
            svcThemeCd: params.svcThemeCd,
            unitCd: params.unitCd,
            startDtm: params.startDtm,
            endDtm: params.endDtm,
        }});
    return data;
};