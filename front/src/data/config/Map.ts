import L, {CRS} from "leaflet";
import "proj4leaflet";

export enum TileMapEnum {
    daum = `daum`,
    // naver = `naver`,
    // google = `google`,
    vworld = `vworld`,
    // tmap = `tmap`,
    // osm = `osm`,
}

export interface ITileMapData {
    [key: string]: {
        name: string,
        options: {
            [key: string]: any
        },
        tiles: {
            [key: string]: {
                name: string,
                urls: string[]
            }
        },
        crs: CRS
    }
}

const location = window.location.host
const domain = process.env.NODE_ENV === 'production' ? `http://${location}/proxy?`:``;


export const TileMapData: ITileMapData = {
    [TileMapEnum.daum]: {
        name: `다음`,
        options: {
            subdomains: `0123`,
            zoomReverse: true,
            zoomOffset: 1,
            minZoom: 0,
            maxZoom: 13,
            tms: true,
            continuousWorld: true,
        },
        tiles: {
            normal: {
                name: `일반지도`,
                urls: [`${domain}http://map{s}.daumcdn.net/map_2d/1807hsm/L{z}/{y}/{x}.png`]
            },
            satellite: {
                name: `위성지도`,
                urls: [`${domain}http://map{s}.daumcdn.net/map_skyview/L{z}/{y}/{x}.jpg?v=160114`,
                    `${domain}http://map{s}.daumcdn.net/map_hybrid/1807hsm/L{z}/{y}/{x}.png`]
            },
            satelliteLine: {
                name: `위성지도라벨`,
                urls: [`${domain}http://map{s}.daumcdn.net/map_hybrid/1807hsm/L{z}/{y}/{x}.png`]
            },
            cadastral: {
                name: `지적편집도`,
                urls: [`${domain}http://map{s}.daumcdn.net/map_usedistrict/1807hsm/L{z}/{y}/{x}.png`]
            },
            bBoundary: {
                name: `법정동경계지도`,
                urls: [
                    `${domain}http://boundary.map.daum.net/mapserver/db/BBOUN_L/L{z}/{y}/{x}.png`]
            },
            hBoundary: {
                name: `행정동경계지도`,
                urls: [
                    `${domain}http://boundary.map.daum.net/mapserver/db/HBOUN_L/L{z}/{y}/{x}.png`]
            },
            physical: {
                name: `지형도`,
                urls: [
                    `${domain}http://map{s}.daumcdn.net/map_shaded_relief/3.00/L{z}/{y}/{x}.png`]
            },
            bicycle: {
                name: `자전거도로지도`,
                urls: [
                    `${domain}http://map{s}.daumcdn.net/map_bicycle/2d/6.00/L{z}/{y}/{x}.png`]
            },
            traffic: {
                name: `교통상황지도`,
                urls: [
                    `${domain}http://r{s}.maps.daum-img.net/mapserver/file/realtimeroad/L{z}/{y}/{x}.png`]
            },
            cai: {
                name: `통합대기지수지도`,
                urls: [
                    `${domain}http://airinfo.map.kakao.com/mapserver/file/airinfo_khai/T/L{z}/{y}/{x}.png`]
            },
            pm10: {
                name: `미세먼지지도`,
                urls: [
                    `${domain}http://airinfo.map.kakao.com/mapserver/file/airinfo_pm10/T/L{z}/{y}/{x}.png`]
            },
            pm25: {
                name: `초미세먼지지도`,
                urls: [
                    `${domain}http://airinfo.map.kakao.com/mapserver/file/airinfo_pm25/T/L{z}/{y}/{x}.png`]
            },
            yellowDust: {
                name: `황사지도`,
                urls: [
                    `${domain}http://airinfo.map.kakao.com/mapserver/file/airinfo_ysnd/T/L{z}/{y}/{x}.png`]
            },
            no2: {
                name: `이산화질소지도`,
                urls: [
                    `${domain}http://airinfo.map.kakao.com/mapserver/file/airinfo_no2/T/L{z}/{y}/{x}.png`]
            },
            so2: {
                name: `아황산가스지도`,
                urls: [
                    `${domain}http://airinfo.map.kakao.com/mapserver/file/airinfo_so2/T/L{z}/{y}/{x}.png`]
            },
            co: {
                name: `일산화탄소지도`,
                urls: [
                    `${domain}http://airinfo.map.kakao.com/mapserver/file/airinfo_co/T/L{z}/{y}/{x}.png`]
            },
            o3: {
                name: `오존지도`,
                urls: [
                    `${domain}http://airinfo.map.kakao.com/mapserver/file/airinfo_o3/T/L{z}/{y}/{x}.png`]
            },
        },
        crs: new L.Proj.CRS(
            `EPSG:5181`,
            `+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs`,
            {
                resolutions: [2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1, 0.5, 0.25],
                origin: [-30000, -60000],
                bounds: L.bounds([-30000 - Math.pow(2, 19) * 4, -60000], [-30000 + Math.pow(2, 19) * 5, -60000 + Math.pow(2, 19) * 5])
            }
        )
    }, [TileMapEnum.vworld]: {
        name: `VWorld`,
        options: {
            minZoom: 6,
            maxZoom: 18,
        },
        tiles: {
            normal: {
                name: `일반지도`,
                urls: [`${domain}https://xdworld.vworld.kr/2d/Base/service/{z}/{x}/{y}.png`] //DEPLOY 이거는 실서버
                // urls: [`http://xdworld.vworld.kr:8080/2d/Base/service/{z}/{x}/{y}.png`]
            },
            satellite: {
                name: `위성지도`, urls: [
                    `${domain}https://xdworld.vworld.kr/2d/Satellite/service/{z}/{x}/{y}.jpeg`,//DEPLOY 이거는 실서버
                    // `${domain}https://xdworld.vworld.kr/2d/Hybrid/service/{z}/{x}/{y}.png`,//DEPLOY 이거는 실서버
                    // `http://xdworld.vworld.kr:8080/2d/Satellite/service/{z}/{x}/{y}.jpeg`,
                    // `http://xdworld.vworld.kr:8080/2d/Hybrid/service/{z}/{x}/{y}.png`,
                ]
            },
            satelliteLine: {
                name: `위성지도 poi`, urls: [
                    `${domain}https://xdworld.vworld.kr/2d/Hybrid/service/{z}/{x}/{y}.png`,//DEPLOY 이거는 실서버
                    // `http://xdworld.vworld.kr:8080/2d/Hybrid/service/{z}/{x}/{y}.png`,
                ]
            },
            gray: {
                name: `흑백지도`,
                urls: [`${domain}https://xdworld.vworld.kr/2d/gray/service/{z}/{x}/{y}.png`]//DEPLOY 이거는 실서버
                // urls: [`http://xdworld.vworld.kr:8080/2d/gray/service/{z}/{x}/{y}.png`]
            },
            midnight: {
                name: `남청지도`,
                urls: [`${domain}https://xdworld.vworld.kr/2d/midnight/service/{z}/{x}/{y}.png`]//DEPLOY 이거는 실서버
                // urls: [`http://xdworld.vworld.kr:8080/2d/midnight/service/{z}/{x}/{y}.png`]
            },
        },
        crs: L.CRS.EPSG3857,
    },
    // [TileMapEnum.tmap]: {
    //     name: `Tmap`,
    //     options: {
    //         minZoom: 6,
    //         maxZoom: 19,
    //         subdomains: `123`,
    //         continuousWorld: false,
    //     },
    //     tiles: {
    //         normal: {name: `일반지도`, urls: [`http://topopentile{s}.tmap.co.kr/tms/1.0.0/hd_tile/{z}/{x}/{-y}.png`]},
    //     },
    //     crs: L.CRS.EPSG3857,
    // },
    //
    // [TileMapEnum.osm]: {
    //     name: `OpenStreetMap`,
    //     options: {
    //         minZoom: 6,
    //         maxZoom: 19
    //     },
    //     tiles: {
    //         normal: {name: `일반지도`, urls: [`https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png`]},
    //         satellite: {
    //             name: `위성지도`, urls: [
    //                 `http://xdworld.vworld.kr:8080/2d/Satellite/service/{z}/{x}/{y}.jpeg`,
    //                 `http://xdworld.vworld.kr:8080/2d/Hybrid/service/{z}/{x}/{y}.png`,
    //             ]
    //         },
    //         gray: {
    //             name: `흑백지도`,
    //             urls: [`https://cartodb-basemaps-{s}.global.ssl.fastly.net/light_all/{z}/{x}/{y}.png`]
    //         },
    //         dark: {
    //             name: `암흑지도`, urls: [
    //                 `https://cartodb-basemaps-{s}.global.ssl.fastly.net/dark_nolabels/{z}/{x}/{y}.png`,
    //                 `http://xdworld.vworld.kr:8080/2d/Hybrid/service/{z}/{x}/{y}.png`,
    //             ]
    //         },
    //     },
    //     crs: L.CRS.EPSG3857,
    // },
}