import {xhr} from "../../xhr";
import conf from "../../../../../data/config/config"
import {SearchDefaultOption} from "../../../../../data/interface/common/CommonInterface";


//파라미터 넘기기
export const requestPastList = async(param:SearchDefaultOption) => {

    const {data} = await xhr.get(conf.API.URL.EVENTS.URL_MOBILE.PAST_LIST, {
        params : param
    })
    return data

}

export const requestNowList = async() => {

    const {data} = await xhr.get(conf.API.URL.EVENTS.URL_MOBILE.NOW_LIST)
    return data

}