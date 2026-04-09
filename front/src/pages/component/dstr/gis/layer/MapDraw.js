import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {Circle, Polygon, Polyline, Tooltip, useMap} from "react-leaflet";
import * as L from "leaflet";
import 'leaflet-draw'
import 'leaflet-draw/dist/leaflet.draw.css'
import {MapDrawEnum} from "../../../../../data/const/mapConst"
import {updateMapDraw} from "../../../../store/view/dstr/mapStore";

const MapDraw = () => {
    const dispatch = useDispatch()
    const map = useMap()
    const [features, setFeatures] = useState([])

    const [layers ,setLayers ] = useState([])

    useEffect(()=>{
        L.drawLocal.draw.handlers.polygon = {
            tooltip: {
                start: '지도의 면적을 구하고자하는 곳을 선택하십시오.',
                cont: '다음 지점을 선택하십시오.',
                end: '마지막 지점을 선택하십시오.'
            }
        }
        L.drawLocal.draw.handlers.polyline = {
            tooltip: {
                start: '지도의 길이를 구하고자하는 곳을 선택하십시오.',
                cont: '다음 지점을 선택하십시오.',
                end: '마지막 지점을 선택하십시오.'
            }
        }
    },[])

    useEffect(() => {
        map.on(L.Draw.Event.CREATED, feature => {
            setFeatures(features => [...features, feature])
        })
        return () => {
            map.off(L.Draw.Event.CREATED)
        }
    }, [map])



    const convertToComponent = (feature, index) => {
        const key = `feature_${index}`
        if (feature.layerType === 'polyline') {
            const positions = feature.layer._latlngs.map(latlng => ([latlng.lat, latlng.lng]))
            return <Polyline key={key} positions={positions}/>
        } else if (feature.layerType === 'polygon') {
            const positions = feature.layer._latlngs[0].map(latlng => ([latlng.lat, latlng.lng]))
            const rePositions = feature.layer._latlngs[0]

            let area = L.GeometryUtil.geodesicArea(rePositions);
            let readableArea = L.GeometryUtil.readableArea(area, 'm');
            readableArea = readableArea.split(' ')[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") + readableArea.split(' ')[1]

            return <Polygon key={key} positions={positions}>
                <Tooltip permanent  className={'firstShow'}>{readableArea}</Tooltip>
            </Polygon>
        } else if (feature.layerType === 'circle') {
            const positions = [feature.layer._latlng.lat, feature.layer._latlng.lng]
            const radius = feature.layer._mRadius
            return <Circle key={key} center={positions} radius={radius}/>
        }
    }

    const mapDraw = useSelector((state) => state.view.map.mapDraw)

    useEffect(() => {
        const drawingLayer =
            mapDraw === MapDrawEnum.polygon ?
                new L.Draw.Polygon(map , {
            repeatMode: true,
        }) : mapDraw === MapDrawEnum.polyline ?
            new L.Draw.Polyline(map , {
            repeatMode: true,
        }) : mapDraw === MapDrawEnum.circle ?
            new L.Draw.Circle(map , {
            repeatMode: true
        }) : null
        drawingLayer &&
            ( drawingLayer.enable(), setLayers([...layers,drawingLayer]))

        if (mapDraw === MapDrawEnum.none || mapDraw === MapDrawEnum.net){
            console.log("layers:",layers);
            layers.length >0 ? layers.forEach((layer)=>{layer.disable()}) : null
        }

        if (mapDraw === MapDrawEnum.clear){
            console.log("layers::",layers);
            setFeatures([])
            layers.length >0 ? layers.forEach((layer)=>{layer.disable()}) : null
            dispatch(updateMapDraw(MapDrawEnum.none))
        }

        // return () => {
        //     drawingLayer && drawingLayer.disable()
        //     setFeatures([])
        // }
    }, [mapDraw])

    return <>
        {features && features.map((feature, index) => convertToComponent(feature, index))}
    </>
}

export default MapDraw