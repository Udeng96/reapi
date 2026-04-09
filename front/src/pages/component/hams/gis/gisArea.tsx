import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../store/rootStore";

import 'leaflet/dist/leaflet.css';
import 'react-leaflet-markercluster/dist/styles.min.css';
import {GeoJSON, MapContainer, Pane, TileLayer} from "react-leaflet";
import React, {MutableRefObject, useEffect, useRef, useState} from "react";
import L from "leaflet";
import GisHamsMarker from "./gisHamsMarker";
import moment from "moment";
import * as hamsActions from "../../../saga/action/hams/hams";

const GisArea = () => {


    const dispatch = useDispatch()
    const geojson = require('./data/hwaseong.json')


    const hamsList = useSelector((state: RootState) => state.server.hams.hamsList)
    const [nowDateTime, setNowDateTime] = useState(moment().format("YYYY-MM-DD HH:mm:ss"))
    const canvasRef:MutableRefObject<any> = useRef()

    // geojson layer 크기 조정
    // 다른 방법을 찾지 못해서 일단 dom 에 직접 접근
    useEffect(() => {
        // console.log("canvas",document.querySelector("canvas"))
        // console.log("json",document.querySelector(".jsonPane"))
        //
        // if (document.querySelector("canvas") !== null) {
        //
        //     console.log('캔버스의 크기를 조정')
        //     //
        //     document.querySelector("canvas")!!.style.width = "2859px"
        //     document.querySelector("canvas")!!.style.height = "1582px"
        //     document.querySelector("canvas")!!.style.transform = "translate3d(-666px,-360px,0px)"

        // }
    }, [hamsList,document.querySelector("canvas") === null])

    useEffect(()=>{
        setNowDateTime(moment().format("YYYY-MM-DD HH:mm:ss"))
    },[])

    const reload = () => {
        setNowDateTime(moment().format("YYYY-MM-DD HH:mm:ss"))

        dispatch(hamsActions.actions.requestHamsList())
    }

    return (
        <>
            {/*gisArea*/}
            <section className="hams gis_area">
                {/*<div className={"map_area"}>*/}

                    <MapContainer
                        attributionControl={false}
                        preferCanvas={true}
                        renderer={L.canvas()}
                        zoomControl={false}
                        doubleClickZoom={false}
                        scrollWheelZoom={false}
                        dragging={false}
                        center={[37.178782, 126.900000]}
                        zoom={11}
                        style={{width: '100%', height: '100%', zIndex: '0', borderRadius: '10px', backgroundColor:"#15161f"}}>
                        <div className={"map_area"}>
                            {/*<Pane className={'jsonPane'} name={'geoJson'} style={{width:"100%",height:"100%",'zIndex':' 300', 'opacity': '1'}}>*/}
                            {/*    <GeoJSON data={geojson}/>*/}
                            {/*</Pane>*/}
                        </div>
                        <Pane name={'hamsMarkers'} style={{zIndex: 1001}}>
                            <GisHamsMarker/>
                        </Pane>
                    </MapContainer>

                {/*</div>*/}


                <div className="gis_legend_img"/>
                <button type="button" className="btn_reload" onClick={event => reload()}/>
                <p className="update_time_value">최종 업데이트 일시 : {nowDateTime}</p>
            </section>
        </>
    )

}

export default GisArea