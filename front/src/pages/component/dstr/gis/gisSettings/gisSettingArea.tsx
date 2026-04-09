import GisHeatMapConfArea from "./gisHeatMap/gisHeatMapConfArea";
import React from "react";
import GisMapConfArea from "./gisMapConf/gisMapConfArea";
import styled from "styled-components"
import {useDispatch} from "react-redux";
import {updateEvetDetail, updateFireCarList} from "../../../../store/server/dstr/dstrServerEventStore";
import {getActiveEventType, getActiveItem} from "../../../../store/view/dstr/dstrViewEventStore";
import {
    updateCustomEventLatLng,
    updateMapDraw,
    updateSelectHeatmapClfy,
    updateSelectHeatMapPeriod
} from "../../../../store/view/dstr/mapStore";
import {MapDrawEnum} from "../../../../../data/const/mapConst";
import {updateCountList} from "../../../../store/server/dstr/heatMapServerStore";
import {updateActiveCctv, updatePlayGisCctvList} from "../../../../store/view/dstr/dstrViewCctvStore";
import {CctvInfo} from "../../../../../data/interface/cctv/cctvInterface";
import {
    updateActiveLiveBtnList,
    updateActiveLiveList, updateActivePastBtnList,
    updateActivePastList
} from "../../../../store/view/dstr/dstrViewMobileStore";
import {useMap} from "react-leaflet";


const StyledResetBtn = styled.button<{ openRightTab: boolean }>`
  width: 86px;
  height: 36px;
  font-family: "NotoSansCJKR";
  font-size: 12px;
  color: #e1e2e3;
  text-align: left;
  padding-left: 35.3px;
  border-radius: 6px;
  border: solid 1px #000;
  background-color: #1b1d2b;
  padding-top: 2px;
  -webkit-transition: all .25s;
  transition: all .25s;
  position: absolute;
  top: 78px;
  left: ${({openRightTab}) => openRightTab ? '1085px' : '1568px'};
  z-index: 3001;
  transition: left 0.8s ease;
`



const GisSettingArea = (props: { openRightTab: boolean }) => {


    const dispatch = useDispatch()

    const resetBtnEvent = ()=>{
        console.log("reset btn click")

        //event clear
        dispatch(updateEvetDetail(null)) //server data clear
        dispatch(getActiveItem(null)) //active Event seqn clear

        //fire car data clear
        dispatch(updateFireCarList(null))
        dispatch(getActiveEventType(null)) //right video template reset

        //custom event clear
        dispatch(updateCustomEventLatLng(null))

        //map draw type clear
        // dispatch(updateMapDraw(MapDrawEnum.none))

        //heatmap clear
        dispatch(updateCountList([])) //data clear
        dispatch(updateSelectHeatmapClfy(null)) //clfy type clear
        dispatch(updateSelectHeatMapPeriod('전체')) //period reset


        //cctv tab
        dispatch(updateActiveCctv(null)) //active cctv clear

        //gis cctv player clear
        dispatch(updatePlayGisCctvList([]))

        //mobile tab
        dispatch(updateActiveLiveList(null)) // active live list clear
        dispatch(updateActiveLiveBtnList(null)) // play live video(active buttons) reset
        dispatch(updateActivePastList(null)) // active past list clear
        dispatch(updateActivePastBtnList(null)) // play past video(active button) reset

        //gis
        dispatch(updateMapDraw(MapDrawEnum.clear))

    }


    return (
        <>

            {/*heatmapSettingArea*/}
            <GisHeatMapConfArea openRightTab={props.openRightTab}/>

            {/*mapSettingArea*/}
            <GisMapConfArea openRightTab={props.openRightTab}/>

            <StyledResetBtn openRightTab={props.openRightTab} type="button"
                            className="btn_resfresh"
                            onClick={resetBtnEvent}
            >초기화</StyledResetBtn>

        </>
    )
}

export default GisSettingArea