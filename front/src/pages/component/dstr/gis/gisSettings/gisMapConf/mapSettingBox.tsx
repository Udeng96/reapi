import React from "react";
import {MapTypeProps} from "../../../../../../data/interface/map/mapInterface";
import MapLayerCheckBox from "./mapLayerCheckBox";
import MapTypeBox from "./mapTypeBox";

interface SettingProps {
    isFirst?: boolean
    title: string,
    mapTypeList: Array<MapTypeProps>
    mapLayerList: Array<MapTypeProps>
}

const MapSettingBox: React.FC<SettingProps> = ({isFirst = false, title, mapTypeList, mapLayerList}) => {

    return (
        <div className={`map_setting_box ${isFirst ? 'first_map_setting' : ''}`}>
            <header className="map_setting_header">{title}</header>
            <div className="map_setting_body">
                <div className="map_setting_title">지도 타입 설정</div>
                <div className="map_type_frame">
                    {
                        mapTypeList.map((type) => {
                            return <MapTypeBox key={`${type.type}_${type.mapNm}_${type.mapTypeNm}`}
                                               type={type.type} mapNm={type.mapNm} mapTypeNm={type.mapTypeNm}
                                               tileNm={type.tileNm} map={type.map}
                            />
                        })
                    }
                </div>

                <div className="map_setting_title">레이어 설정</div>
                <div className="check_box_area">
                    {
                        mapLayerList.map((layer) => {
                            return <MapLayerCheckBox key={`${layer.type}_${layer.mapNm}_${layer.mapTypeNm}`}
                                                     type={layer.type} mapNm={layer.mapNm} mapTypeNm={layer.mapTypeNm}/>
                        })
                    }

                </div>
            </div>
        </div>
    )
}

export default MapSettingBox;