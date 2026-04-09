import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {useMap} from "react-leaflet";
import * as L from "leaflet";
import {DrawMap, Map} from "leaflet";
import {MapDrawEnum} from "../../../../../data/const/mapConst"
import 'leaflet-draw'
import 'leaflet-draw/dist/leaflet.draw.css'
import {updateMapDraw} from "../../../../store/view/dstr/mapStore";
import {RootState} from "../../../../store/rootStore";


const MapDrawLayer = () => {
    const dispatch = useDispatch()
    const map: Map = useMap()
    const [layers, setLayers] = useState<L.Draw.Feature[]>([])
    const [layerList, setLayerList] = useState<Array<any>>([])

    const mapDraw = useSelector((state: RootState) => state.view.map.mapDraw)


    useEffect(() => {

        L.drawLocal.draw.handlers.polygon = {
            tooltip: {
                start: '지도의 면적을 구하고자하는 곳을 선택하십시오.',
                cont: '다음 지점을 선택하십시오.',
                end: '마지막 지점을 선택하십시오.'
            }
        }
        //@ts-ignore
        L.drawLocal.draw.handlers.polyline = {
            tooltip: {
                start: '지도의 길이를 구하고자하는 곳을 선택하십시오.',
                cont: '다음 지점을 선택하십시오.',
                end: '마지막 지점을 선택하십시오.'
            }
        }

    }, [])

    useEffect(() => {

        // draw 레이어 (create)
        // 마커는 필요없으니 제외
        const drawingLayer =
            mapDraw === MapDrawEnum.polygon ?
                new L.Draw.Polygon(map as DrawMap, {
                    repeatMode: true,
                    showArea: true
                }) : mapDraw === MapDrawEnum.polyline ?
                    new L.Draw.Polyline(map as DrawMap, {repeatMode: true}) : mapDraw === MapDrawEnum.circle ?
                        new L.Draw.Circle(map as DrawMap, {repeatMode: true}) : null

        if (drawingLayer) {
            drawingLayer.enable();
            setLayers([...layers, drawingLayer])
        }


        // 활성화 되어있는게 없으면 draw layer가 create 되지 않는다.
        if (mapDraw === MapDrawEnum.none|| mapDraw === MapDrawEnum.net){
            if (layers.length > 0){
                layers.forEach((layer) => {
                    layer.disable()
                })
            }
        }
        if (mapDraw === MapDrawEnum.clear) {

            layerList.map((l) => {
                if (layerList.includes(l)) {
                    map.removeLayer(l)
                }
            })

            dispatch(updateMapDraw(MapDrawEnum.none))

        }

        return () => {
            drawingLayer && drawingLayer.disable()
        }

    }, [mapDraw,layerList])

    useEffect(() => {

        // 실제로 그리는 부분
        if (mapDraw === MapDrawEnum.polyline || mapDraw === MapDrawEnum.polygon) {

            map.on(L.Draw.Event.CREATED, e => {
                let layer = e.layer;

                let newLayers = [...layerList]

                // map에 그린 레이어 추가
                map.addLayer(layer)

                newLayers.push(layer)

                setLayerList(newLayers)

                // polygon 면적
                let seeArea = L.GeometryUtil.geodesicArea(layer.getLatLngs()[0]);

                // polyline 길이
                let length = 0;


                if (mapDraw === MapDrawEnum.polygon) {
                    //@ts-ignore
                    let readableArea = L.GeometryUtil.readableArea(seeArea, 'm');
                    readableArea = readableArea.split(' ')[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") + readableArea.split(' ')[1]

                    layer.bindTooltip(readableArea, {permanent: true, direction: 'center'}).openTooltip();
                } else if (mapDraw === MapDrawEnum.polyline) {

                    let coords = layer.getLatLngs();

                    for (let i = 0; i < coords.length - 1; i++) {
                        length += coords[i].distanceTo(coords[i + 1]);
                    }

                    let strLength = length < 1000 ? length.toFixed(1).toString() + "m" : (length / 1000).toFixed(2).toString() + "km"
                    layer.bindTooltip(strLength, {permanent: true, direction: 'center'}).openTooltip();
                }
            })
            return () => {
                map.off(L.Draw.Event.CREATED)
            }
        }

    }, [map, mapDraw, layerList])

    return <>

    </>

}

export default MapDrawLayer