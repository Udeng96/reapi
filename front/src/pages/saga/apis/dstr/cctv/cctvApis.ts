import {xhr} from "../../xhr";
import conf from "../../../../../data/config/config"
import {EventsPayload} from "../../../../../data/interface/event/eventInterface";
import {SearchOption} from "../../../../../data/interface/cctv/cctvInterface";

export const fetchCctvs = async (params : SearchOption) =>  {

    const {data} = await xhr.get(conf.API.URL.CCTV.URL_CCTV.LIST,{params:params})
    // console.log('CALLLL')
    // console.trace()
    return data
}

export const fetchCctvDetail = async(id:string) => {

    const {data} = await xhr.get(`${conf.API.URL.CCTV.URL_CCTV.DETAIL}`.replace('{id}',id), )

    return data

}

export const fetchCctvClfy = async() => {

    const {data} = await xhr.get(conf.API.URL.CCTV.URL_CCTV.CLFY)

    return data
}
