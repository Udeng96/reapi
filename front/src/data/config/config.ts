
export const domain = (window.location.host)


const API_URL:string = process.env.NODE_ENV === 'production' ? `http://${domain}/api` :`http://localhost:22222/api`
const API_WS_URL:string =  process.env.NODE_ENV === 'production' ? `ws://${domain}/api-ws` : `ws://localhost:26000/websocket`
const OMS_URL:string = process.env.NODE_ENV === 'production' ? `http://${domain}/oms` : `http://localhost:11910/oms`

export const USE_AUTH = true
export const MANAGER_TYPE_CD = '001'

export const isDev = process.env.NODE_ENV !== 'production';
const URL_USER = {
    INFO : `${API_URL}/user`
}
const URL_EVENT ={
    EVENTS : `${API_URL}/event`,
    EVENTS_BY_USVC_ID : `${API_URL}/event/uSvcOutbId`,
    LOCATIONS : `${API_URL}/event/location`,
    EVENT_TYPES : `${API_URL}/event/type`,
    FIRE_CAR_INFO : `${API_URL}/standard/system`,
}
const URL_ADDRESS = {
    SEARCH : `${API_URL}/addr/juso`
}

const URL_HEATMAP = {
    CLFY : `${API_URL}/event/heatmap/clfy`,
    COUNT : `${API_URL}/event/heatmap/count`
}
const URL_CCTV = {
    LIST : `${API_URL}/cctv`,
    DETAIL : `${API_URL}/cctv/{id}`,
    CLFY : `${API_URL}/cctv/clfy`,
}

const URL_MOBILE = {
    PAST_LIST : `${API_URL}/mobile/past`,
    NOW_LIST : `${API_URL}/mobile/live`
}

const URL_HAMS = {
    HAMS_LIST : `${API_URL}/hams/api`,
    HAMS_DETAIL :`${API_URL}/hams/api/{id}`,
    HAMS_STAT : `${API_URL}/hams/api/{id}/stat`,
    HAMS_CCTV : `${API_URL}/hams/api/cctv`,
    DOOR_LOCK : `${API_URL}/hams/api/{id}/lock`,
    DOOR_UNLOCK : `${API_URL}/hams/api/{id}/unlock`
}

const URL_MONITOR = {
    CCTV_MONITOR_LIST : `${API_URL}/cctv/monitor`,
    CCTV_CONTROL : `${API_URL}/cctv/control/{cctvId}`
}


const SYSTEM_KEY:string =  process.env.NODE_ENV === `production` ? `HWAPI` : `API`
const SYSTEM_SECRET:string =  process.env.NODE_ENV === `production` ? `fsVxi4BrQR081F9H81RUp0` :  `1YVJLPanQVs95PEJYklYyC`

const API = {
    SYSTEM_KEY,
    SYSTEM_SECRET,
    API_WS_URL,
    URL : {
        USER : {
            URL_USER
        },
        EVENTS : {
            URL_EVENT,
            URL_HEATMAP,
            URL_MOBILE,
        },
        ADDRESS : {
            URL_ADDRESS
        },
        CCTV :{
            URL_CCTV
        },
        HAMS : {
            URL_HAMS,
        },
        MONITOR : {
            URL_MONITOR,
        }

    }
}

const OMS = {
    OMS_URL,
    sysSelView: OMS_URL + '/login/goSysSelView',
    logOut: OMS_URL + '/api/user/logout'
}


//영상 호출 //nginx 에서도 받아서 처리해주어야한다,
export const STREAM_URL_CRIME = `ws://${domain}/xeus-gate-1/stream`;
export const STREAM_URL_TRAFFIC = `ws://${domain}/xeus-gate-2/stream`;
export const STREAM_URL_FIRETRUCK = `ws://${domain}/xeus-gate-2/stream`;
export const STREAM_URL_MOBILE = `ws://${domain}/xeus-gate-1/stream`
export const STREAM_URL_HAMS = `ws://${domain}/xeus-gate-1/stream`;
export const STREAM_URL_WIDE = `ws://${domain}/xeus-gate-1/stream`;

export default {
    PLATFORM:{
        OMS
    },
    API,
    XEUS : {
        STREAM_URL_CRIME,STREAM_URL_TRAFFIC,STREAM_URL_FIRETRUCK,STREAM_URL_MOBILE,STREAM_URL_HAMS,STREAM_URL_WIDE
    }
}
