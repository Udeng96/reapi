import React, {useState} from "react";
import styled from "styled-components";
import MapSettingBox from "./mapSettingBox";
import {
    DAUM_MAP_LAYER_LIST,
    DAUM_MAP_TYPE_LIST,
    V_WORLD_MAP_LAYER_LIST,
    V_WORLD_MAP_TYPE_LIST
} from "../../../../../../data/const/mapConst";
import {useSelector} from "react-redux";
import {RootState} from "../../../../../store/rootStore";
import {isA} from "expect/build/jasmineUtils";


const StyledMapSettingArea = styled.div<{ openRightTab: boolean }>`
  width: 345px;
  height: auto;
  position: absolute;
  top: 78px;
  left: ${({openRightTab}) => openRightTab ? '1275px' : '1758px'};
  z-index: 3003;
  transition: left 0.8s ease;
`
//FIXME 나중에 다음지도 이슈 처리 후 변경
// height: 490px;
const StyledMapSettingFrame = styled.div<{ openRightTab: boolean, isOver: boolean}>`
  width: 345px;
  height: auto;
  border-radius: 8px;
  -webkit-box-shadow: 0px 10px 20px 0 rgba(0, 0, 0, 0.3);
  box-shadow: 0px 10px 20px 0 rgba(0, 0, 0, 0.3);
  border: solid 1px #2b2d38;
  background-color: #0e101c;
  position: absolute;
  top: 39px;
  left: ${({openRightTab}) => openRightTab ? '-245px' : '-245px'};
  display: ${({isOver}) => isOver ? 'block' : 'none'};
`

const GisMapConfArea = (props: { openRightTab: boolean }) => {

    const [isOver, setOver] = useState(false)
    const customLayerList = useSelector((state:RootState)=>state.view.map.customLayerList)


    return (
        <StyledMapSettingArea openRightTab={props.openRightTab} className={`map_setting_area`}
                              onMouseEnter={() => setOver(true)}
                              onMouseLeave={() => setOver(false)}>
            <button type="button" className={`btn_map_setting ${customLayerList.length>0 ? 'active':''}`}>지도 설정</button>

            <StyledMapSettingFrame isOver={isOver} openRightTab={props.openRightTab} className="map_setting_frame">
                {/*FIXME 나중에 다음지도 이슈 처리 후 변경*/}
                {/*<MapSettingBox isFirst={true} title={'Daum'} mapTypeList={DAUM_MAP_TYPE_LIST}*/}
                {/*               mapLayerList={DAUM_MAP_LAYER_LIST}/>*/}
                <MapSettingBox isFirst={true} title={'VWorld'} mapTypeList={V_WORLD_MAP_TYPE_LIST}
                               mapLayerList={V_WORLD_MAP_LAYER_LIST}/>
            </StyledMapSettingFrame>


        </StyledMapSettingArea>
    )
}
export default GisMapConfArea