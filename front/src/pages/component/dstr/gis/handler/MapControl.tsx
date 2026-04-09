import {useMap, useMapEvents} from "react-leaflet";
import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import L, {LatLng, LatLngBounds, Map, Proj, CRS, LatLngTuple} from "leaflet";
import {RootState} from "../../../../store/rootStore";
import {TileMapData} from "../../../../../data/config/Map";
import {MapMoveEnum} from "../../../../../data/const/mapConst";
import {setZoomLevel} from "../../../../store/view/dstr/mapStore";
import {updateActiveCctv, updateActiveClusterLatLon} from "../../../../store/view/dstr/dstrViewCctvStore";
import dstrSaga from "../../../../saga/dstr/dstrSaga";

const MapControl = ({cluster}:any) => {
    const dispatch = useDispatch()
    const map:Map = useMap()

    const clickFn = useSelector((state:RootState)=>state.view.map.mapClickFn)

    const zoomLevel = useSelector((state: RootState) => state.view.map.zoomLevel)
    useEffect(() => {
        map.setView(map.getCenter(), zoomLevel, {
            animate: false,
            duration : 1,
            easeLinearity : 1,
            noMoveStart : true
        })

    }, [zoomLevel])

    const maxZoom = useSelector((state: RootState) => state.view.map.maxZoom)
    const mapMove = useSelector((state: RootState) => state.view.map.mapMove)

    useEffect(() => {

        if (mapMove) {


            let moveZoomLevel = zoomLevel;

            if (mapMove.mapMoveType === MapMoveEnum.none ||
                mapMove.mapMoveType === MapMoveEnum.plus_zoom
            ) {
                moveZoomLevel = zoomLevel;
            }else if (mapMove.mapMoveType === MapMoveEnum.max_zoom){
                moveZoomLevel = maxZoom;
            } else{
                moveZoomLevel = maxZoom - 2;
            }


            map.setView(new LatLng(mapMove.latlng[0],mapMove.latlng[1]), moveZoomLevel, {
                animate: false,
                duration : 1,
                easeLinearity : 1,
                noMoveStart : true
            })

        }


    }, [mapMove])


    const getMaxBounds = (crs:any) => {
        const {bounds} = crs.projection;
        return new LatLngBounds(
            crs.unproject(bounds.min),
            crs.unproject(bounds.max),
        );
    }

    const mapType = useSelector((state: RootState) => state.view.map.mapType)
    useEffect(() => {
        const zoomLevel = (map.options.maxZoom || 13) - map.getZoom()
        const latlng = map.getCenter()
        const bounds = map.getBounds();

        const crs:CRS = TileMapData[mapType].crs
        map.options.crs = crs
        map.options.minZoom = TileMapData[mapType].options.minZoom
        map.options.maxZoom = TileMapData[mapType].options.maxZoom
        // dispatch(setMinMaxZoom([map.options.minZoom, map.options.maxZoom]))

        map.options.zoomSnap = 0
        map.fitBounds(bounds)
        map.setMaxBounds(getMaxBounds(crs))
        map.options.zoomSnap = 1

        map.project(latlng, map.options.maxZoom! - zoomLevel)
        map.setView(latlng, map.options.maxZoom! - zoomLevel, {
            animate: true
        })

        if (cluster) {
            const layers = cluster.getLayers();
            cluster.clearLayers();
            cluster.addLayers(layers);

        }
    }, [mapType])

    useMapEvents({
        click: event => {
            clickFn(event)
            dispatch(updateActiveClusterLatLon(null))//click 된 cluster, 기본 cctv 전부 초기화
        },
        zoomend: () => {
            //Map 의 초기 zoom level 이 실수값으로 나옴 (6.9966986978)
            dispatch(setZoomLevel(Math.round(map.getZoom())))
            dispatch(updateActiveClusterLatLon(null))//click 된 cluster, 기본 cctv 전부 초기화
        },
        moveend: _ => {
            dispatch(setZoomLevel(Math.round(map.getZoom())))
        },

    });


    return null
}

export default MapControl