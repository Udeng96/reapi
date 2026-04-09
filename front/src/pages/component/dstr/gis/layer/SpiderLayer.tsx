import React, {useEffect, useState} from "react";
import {useSelector} from "react-redux";
import ReactDOMServer from "react-dom/server";
import L, {LatLng, LatLngExpression, LatLngTuple} from "leaflet";
import {Marker, Pane} from "react-leaflet";
import EventLine from "../line/EventLine";
import {VwEvent} from "../../../../../data/interface/event/eventInterface";
import {RootState} from "../../../../store/rootStore";
import {MapEventMarkerType} from "../../../../../data/const/mapConst";

const SpiderLayer =  (props:{eventLatLng : { latLng: LatLngTuple, type: MapEventMarkerType }|null, cluster: any})=>{

    const playCctvLatLngList = useSelector((state:RootState)=>state.view.map.playCctvLatLngList)


    function renderIcon(index :number){
        //icon 이미지는 아래와 같이 className을 할당한다
        //type_0_1 event
        //type_1_1 fire

        return L.divIcon({
            className : '',
            html : ReactDOMServer.renderToString(
                <div className={`numbering_img type_${props.eventLatLng!.type}_${index}`}></div>
            ),
            // iconAnchor : [65,58],
            iconAnchor : [55,50],
        })
    }



    return (
        <>

            <Pane name={'spider'} style={{zIndex: 1000}}>
                <EventLine eventLatLng={props.eventLatLng} cluster={props.cluster}/>
            </Pane>

            <Pane name={'numberMarker'} style={{zIndex: 1000}}>
                {
                    (props.eventLatLng &&playCctvLatLngList.length > 0) &&
                    playCctvLatLngList.map((position:LatLngExpression,index:number)=>{
                        return <Marker key={`spider_${index}`} position = {position} icon={renderIcon(index+1)}/>
                    })
                }
            </Pane>
        </>
    )
}

export default SpiderLayer