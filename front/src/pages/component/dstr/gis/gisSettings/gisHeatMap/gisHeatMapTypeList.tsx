import React, {useEffect} from "react";
import GisHeatMapTypeItem from "./gisHeatMapTypeItem";
import {HeatmapClfy} from "../../../../../../data/interface/event/heatMapInterface";

const GisHeatMapTypeList = (props: { item: HeatmapClfy, clickFn:Function }) => {

    return(
        <>
            <p className="event_type_title">{props.item.svcThemeNm}</p>
            <div className="event_type_frame">

                {
                    props.item.itemList.map((item,index)=>{
                        return <GisHeatMapTypeItem key={`${item.unitCd}_${item.unitNm}`} item={ item} svcTheme={props.item} clickFn={props.clickFn}/>
                    })
                }
            </div>
        </>
    )
}

export default GisHeatMapTypeList