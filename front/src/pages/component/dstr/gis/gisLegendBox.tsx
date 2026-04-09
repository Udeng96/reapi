import React from "react";
import styled from "styled-components"

import img from "../../../../public/static/images/dark/page/img/img_heatmap_legend_prism.png"
import {useSelector} from "react-redux";
import {RootState} from "../../../store/rootStore";


const StyledHeatmapLegendBox = styled.div<{ openRightTab: boolean }>`
  width: 320px;
  height: 80px;
  border-radius: 8px;
  border: solid 1px #000;
  background-color: #171821;
  background-image: url(${img});
  background-repeat: no-repeat;
  background-position: center 40px;
  padding: 12px 15.7px 50px 16.3px;
  position: absolute;
  bottom: 22px;
  right: ${({openRightTab}) => openRightTab ? '505px' : '18px'};
  z-index: 3001;
  transition: right 0.8s ease;

  &:before {
    content: "min";
    display: block;
    font-family: "NotoSansCJKL";
    font-size: 10px;
    letter-spacing: 0.5px;
    color: #00adff;
    position: absolute;
    top: 53px;
    left: 16.3px;
  }

`


const GisLegendBox = (props: { openRightTab: boolean }) => {

    const selectHeatMapClfy = useSelector((state: RootState) => state.view.map.selectHeatMapClfy)
    const selectHeatMapPeriod = useSelector((state: RootState) => state.view.map.selectHeatMapPeriod)

    return (
        <>{
            selectHeatMapClfy &&
            <StyledHeatmapLegendBox openRightTab={props.openRightTab} className="heatmap_legend_box">
                <div className="heatmap_legend_info">
                    <p className="heatmap_legend_title"><span>{selectHeatMapClfy!.item!.unitNm}</span>이벤트 히트맵 기준표</p>
                    <p className="heatmap_term_text">[기간:<span>{selectHeatMapPeriod}</span>]</p>
                </div>
            </StyledHeatmapLegendBox>
        }

        </>
    )
}


GisLegendBox.defaultProps = {
    openRightTab: true
}
export default GisLegendBox