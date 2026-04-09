import React, {useEffect, useState} from "react";
import GisHeatMapTypeList from "./gisHeatMapTypeList";
import styled from "styled-components";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../../store/rootStore";
import {actions} from "../../../../../saga/action/dstr/evet/heatMapActions"
import {updateSelectHeatMapPeriod} from "../../../../../store/view/dstr/mapStore";
import moment from "moment";
import {HeatmapClfy} from "../../../../../../data/interface/event/heatMapInterface";

const StyledHeatmapSettingArea = styled.div<{ openRightTab: boolean }>`
  width: 345px;
  height: auto;
  position: absolute;
  top: 78px;
  left:  ${({openRightTab}) => openRightTab ? '1179px' : '1662px'};
  z-index: 3001;
  transition: left 0.8s ease;
`


const StyledLabel = styled.label`
  cursor: pointer;
  display: inline-block;
  font-family: "NotoSansCJKL";
  font-size: 12px;
  text-align: left;
  color: #f7f7f7;`

const GisHeatMapConfArea = (props: { openRightTab: boolean }) => {

    const dispatch = useDispatch()
    const [isOver, setOver] = useState(false)


    const clfyList = useSelector((state: RootState) => state.server.heatMap.clfyList)
    const selectHeatMapClfy = useSelector((state: RootState) => state.view.map.selectHeatMapClfy)

    const selectHeatMapPeriod = useSelector((state: RootState) => state.view.map.selectHeatMapPeriod)
    useEffect(() => {
        dispatch(actions.fetchClfy())
    }, [])


    const getPeriodDate = () => {
        let result = {startDtm: "", endDtm: ""}
        let now = moment().format("YYYYMMDD")
        switch (selectHeatMapPeriod) {
            case "전체" :
                break
            case "오늘": {
                result.startDtm = now;
                result.endDtm = now
                break
            }
            case "당월": {
                result.startDtm = moment().add(-30, 'd').format("YYYYMMDD");
                result.endDtm = now
                break
            }
            case "3개월": {
                result.startDtm = moment().add(-90, 'd').format("YYYYMMDD");
                result.endDtm = now
                break
            }
            default:
                break
        }
        return result
    }


    function toggleClfy(item: HeatmapClfy) {
        const payload = getPeriodDate()

        //지정한결과로 전달
        dispatch(actions.fetchCount({
            unitCd: item.item?.unitCd!,
            svcThemeCd: item.svcThemeCd!,
            startDtm: payload.startDtm!,
            endDtm: payload.endDtm!
        }))
    }


    return (
        <StyledHeatmapSettingArea className="heatmap_setting_area"
                                  openRightTab={props.openRightTab}
                                  onMouseEnter={() => setOver(true)}
                                  onMouseLeave={() => setOver(false)}
        >
            <button type="button" className={`btn_heatmap_setting ${selectHeatMapClfy ? 'active' : ''}`}>히트 맵</button>
            {
                isOver &&
                <div className="heatmap_setting_frame">
                    <header className="heatmap_setting_header term_setting">기간 설정</header>
                    <div className="term_setting_area">
                        <div className="radio_button_box">
                            <input id="radio1" name="select" type="radio" className="radio" value="전체"
                                   checked={selectHeatMapPeriod === "전체"}
                                   onChange={e => dispatch(updateSelectHeatMapPeriod(e.target.value))}/>
                            <StyledLabel
                                htmlFor="radio1"
                                className="radio_label">전체</StyledLabel>
                        </div>
                        <div className="radio_button_box">
                            <input id="radio2" name="select" type="radio" className="radio" value="오늘"
                                   checked={selectHeatMapPeriod === "오늘"}
                                   onChange={e => dispatch(updateSelectHeatMapPeriod(e.target.value))}/>
                            <StyledLabel
                            htmlFor="radio2"
                            className="radio_label">오늘</StyledLabel>
                        </div>
                        <div className="radio_button_box">
                            <input id="radio3" name="select" type="radio" className="radio" value="당월"
                                   checked={selectHeatMapPeriod === "당월"}
                                   onChange={e => dispatch(updateSelectHeatMapPeriod(e.target.value))}/><StyledLabel
                            htmlFor="radio3"
                            className="radio_label">당월</StyledLabel>
                        </div>
                        <div className="radio_button_box">
                            <input id="radio4" name="select" type="radio" className="radio" value="3개월"
                                   checked={selectHeatMapPeriod === "3개월"}
                                   onChange={e => dispatch(updateSelectHeatMapPeriod(e.target.value))}
                            /><StyledLabel
                            htmlFor="radio4"
                            className="radio_label">3개월</StyledLabel>
                        </div>
                    </div>
                    <header className="heatmap_setting_header event_select">이벤트 선택</header>
                    <div className="event_select_area">
                        <div className="event_select_list">
                            {
                                clfyList.map((item) => {
                                    return <GisHeatMapTypeList item={item} key={item.svcThemeCd} clickFn={toggleClfy}/>
                                })
                            }
                        </div>
                    </div>
                </div>
            }

        </StyledHeatmapSettingArea>
    )
}

export default GisHeatMapConfArea