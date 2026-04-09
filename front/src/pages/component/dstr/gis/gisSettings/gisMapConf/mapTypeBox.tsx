import React from "react";
import {IMap} from "../../../../../../data/interface/interface";
import {MapTypeProps} from "../../../../../../data/interface/map/mapInterface";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../../store/rootStore";
import {changeBaseMap} from "../../../../../store/view/dstr/mapStore";

const MapTypeBox :React.FC<MapTypeProps> = ({type,mapNm,mapTypeNm,tileNm, map}) => {

    const dispatch = useDispatch();
    const mapType = useSelector((state:RootState)=>state.view.map.mapType)
    const mapTile = useSelector((state:RootState)=>state.view.map.mapTile)


    return(
        <>
            <div className={`map_type_box ${(map === mapType) && (mapTile=== tileNm) ? 'active' : ''}`}
                onClick={()=>{
                    dispatch(changeBaseMap({mapType : map!, mapTile : tileNm!}))
                }}

            >
                {/*className = mapFrame_+(2d(일반) / real(위성) / grap(회색) / navy(남청색))*/}
                <div className={`map_type_image ${type}`}/>
                <div className="map_type_text"><span>{mapNm}</span>{mapTypeNm}</div>
            </div>

        </>
    )
}

export default MapTypeBox