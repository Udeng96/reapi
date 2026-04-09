import {xhr} from "../xhr"
import conf from "../../../../data/config/config"
import {AddrInfo} from "../../../../data/interface/etc/Addr";


export const fetchAddress = async ({word, type}: {word: string, type: string}):Promise<AddrInfo[]>=>{

    const {data} = await xhr.get(conf.API.URL.ADDRESS.URL_ADDRESS.SEARCH,{
        params : {word : word, type: type}
    })
    return data
}