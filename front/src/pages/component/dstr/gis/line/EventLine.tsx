import React, {useEffect, useState} from "react";
// import {Event} from "../../../../../data/interface/eventInterface";
import {Polyline, useMap} from "react-leaflet";
import L, {LatLng, LatLngTuple, Layer} from "leaflet";
import "leaflet-geometryutil"
import {useDispatch, useSelector} from "react-redux";
import {VwEvent} from "../../../../../data/interface/event/eventInterface";
import {RootState} from "../../../../store/rootStore";
import {getClosestClusterMarker} from "../../../../util/LeafletUtil";
import {updatePlayCctvLatLngList} from "../../../../store/view/dstr/mapStore";
import {updatePlayCctvList} from "../../../../store/view/dstr/dstrViewCctvStore";
import {MapEventMarkerType} from "../../../../../data/const/mapConst";
// import {RootState} from "../../../../../reducer/rootReducer";
// import {getClosestClusterMarker} from "../../../../../../../static/util/LeafletUtil";
// import {changeEventClusterLatLonList, changePlayCctvList} from "../../../../../reducer/event/cctvReducer";

interface Coordinate {
    lat: number;
    lng: number;
}


const EventLine = (props:{eventLatLng :{ latLng: LatLngTuple, type: MapEventMarkerType }|null, cluster:any}) => {

    const dispatch = useDispatch();

    const map = useMap();
    const [lineList, setLineList] = useState<Array<Coordinate>>([]);
    const mapMove = useSelector((state: RootState) => state.view.map.mapMove);
    const zoomLevel = useSelector((state: RootState) => state.view.map.zoomLevel);
    const mapType = useSelector((state: RootState) => state.view.map.mapType);




    useEffect(() => {
        if (props.cluster) {
            if (props.eventLatLng){
                let sortedCctvsByDistance = props.cluster.getLayers().sort((_a:Layer, _b:Layer) => {


                    //@ts-ignore
                    const aDistance = L.GeometryUtil.distance(map, new L.LatLng(props.eventLatLng.latLng![0], props.eventLatLng.latLng![1]), _a._latlng)
                    //@ts-ignore
                    const bDistance = L.GeometryUtil.distance(map, new L.LatLng(props.eventLatLng.latLng![0], props.eventLatLng.latLng![1]), _b._latlng)

                    return aDistance - bDistance
                })

                let filterDataList : any[] = [];

                //중복제거  //다른 경우에만 삽입을 실시
                sortedCctvsByDistance.forEach((info:Layer, index:number) => {
                    //@ts-ignore
                    let findIndex = filterDataList.map(info2 => info2.options.data.facNm).indexOf(info.options.data.facNm);

                    if (filterDataList.length <= 6) {
                        if (findIndex === -1) {  //중복된값이 없는경우에만 삽입한다.
                            filterDataList.push(info);
                        }
                    }
                })


                //가장가까운 6개를 축출
                const selectedMarkers = filterDataList.slice(0, Math.min(filterDataList.length, 6))

                let smList = selectedMarkers.map((sm) => {
                    const target = getClosestClusterMarker(sm, zoomLevel);
                    return {lat: target.lat, lng: target.lng}
                }).filter(sm => {
                    // return (L.GeometryUtil.length([new L.LatLng(props.event![0], props.event![1]), new L.LatLng(sm.lat, sm.lng)])) < 2000
                    return true
                })

                setLineList(smList); //라인을 먼저그리고 그린 라인이 없으면 cctv도 비워버린다. 기존의 cctv 정보가 남아있을수 있음
                if (smList.length > 0) {
                    const playCctvList = selectedMarkers.map(info => info.options.data)
                    dispatch(updatePlayCctvList(playCctvList))
                }else {
                    dispatch(updatePlayCctvList([]))
                }
                dispatch(updatePlayCctvLatLngList(smList.map(sm => new L.LatLng(sm.lat, sm.lng))))
            }else {
                dispatch(updatePlayCctvList([]))
            }
        }

    }, [props.eventLatLng, mapMove, zoomLevel, mapType]);




    const getColor = ()=>{
        if (props.eventLatLng){
            if (props.eventLatLng.type === MapEventMarkerType.event){
                return "red"
            } else {
                return "orange"
            }
        }else {
            return ''
        }

    }


    return (
        <>
            {
                props.eventLatLng &&
                lineList.map((line, index) => {
                    return <Polyline key={`PolyLine__${index}`} positions={[[line.lat, line.lng], [props.eventLatLng!.latLng![0], props.eventLatLng!.latLng![1]]]}
                                     color={getColor()}
                                     fill={true}
                                     pathOptions={{color:getColor()}}
                                     fillColor={getColor()}
                                     renderer={L.canvas()}


                    />
                })
            }
        </>
    )
}

export default EventLine;