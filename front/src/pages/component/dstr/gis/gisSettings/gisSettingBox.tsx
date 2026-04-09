import GisHeatMapConfArea from "./gisHeatMap/gisHeatMapConfArea";
import React from "react";
import GisMapConfArea from "./gisMapConf/gisMapConfArea";
import styled from "styled-components"


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
  left: ${({openRightTab}) => openRightTab ? '1085px' : '0px'};
  z-index: 1;
`


const GisSettingBox = (props: { openRightTab: boolean }) => {
    return (
        <>

            {/*heatmapSettingArea*/}
            <GisHeatMapConfArea openRightTab={props.openRightTab}/>

            {/*mapSettingArea*/}
            <GisMapConfArea openRightTab={props.openRightTab}/>

            <StyledResetBtn openRightTab={props.openRightTab} type="button"
                            className="btn_resfresh">초기화</StyledResetBtn>

        </>
    )
}

export default GisSettingBox