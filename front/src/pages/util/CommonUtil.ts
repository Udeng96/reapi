import {isDev} from "../../data/config/config";


export const changeDomainUrl = (originUrl:string) => {
    if (!isDev) {
        let array = originUrl.split('/')
        let DOMAIN = window.location.host
        let str = ''
        for (let i = 0; i < array.length; i++) {
            if (i == 2) {//삭제해야하는 url
                str += DOMAIN + ":" + array[i].split(":")[1]
            } else if (i == 0) {
                str += array[i] + '/'
            } else {
                str += '/' + array[i]
            }
        }
        return str
    } else {
        return originUrl
    }
}