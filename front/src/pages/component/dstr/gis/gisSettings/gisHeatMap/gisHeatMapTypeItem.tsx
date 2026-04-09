import React, {MutableRefObject, useEffect, useRef, useState} from "react";
import {HeatmapClfy, UnitInfo} from "../../../../../../data/interface/event/heatMapInterface";
import styled from "styled-components";
import {StyledTooltip} from "../../../common/tooltip";
import {useDispatch, useSelector} from "react-redux";
import {updateSelectHeatmapClfy, updateSelectHeatMapPeriod} from "../../../../../store/view/dstr/mapStore";
import {RootState} from "../../../../../store/rootStore";
import {updateCountList} from "../../../../../store/server/dstr/heatMapServerStore";


export const StyledTooltipTextLeft21 = styled(StyledTooltip)`
  left: 21px;
`

const GisHeatMapTypeItem: React.FC<{ item: UnitInfo, svcTheme: HeatmapClfy,clickFn:Function }> = ({item, svcTheme,clickFn}) => {

    const dispatch = useDispatch()

    const nameRef: MutableRefObject<any> = useRef()
    const [showTooltip, setShowTooltip] = useState(false)
    const [isOver, setOver] = useState(false)

    const selectHeatMapClfy = useSelector((state: RootState) => state.view.map.selectHeatMapClfy)

    useEffect(() => {
        const isOver = nameRef.current.scrollWidth > nameRef.current.clientWidth
        if (isOver) {
            setShowTooltip(true)
        }
    }, [])


    const makeActiveInfo = () => {
        return {...svcTheme, item: item}
    }
    const compareActiveToThis = () => {
        if (selectHeatMapClfy) {
            return (selectHeatMapClfy.svcThemeCd === svcTheme.svcThemeCd && selectHeatMapClfy.item!.unitCd === item.unitCd)
        }
        return false
    }

    const clickEvent = () => {

        if (selectHeatMapClfy) {
            if (selectHeatMapClfy.svcThemeCd === svcTheme.svcThemeCd && selectHeatMapClfy.item!.unitCd === item.unitCd) {
                dispatch(updateSelectHeatmapClfy(null))
                dispatch(updateCountList([]))
            } else {
                dispatch(updateSelectHeatmapClfy(makeActiveInfo()))
                clickFn(makeActiveInfo())
            }
        } else {
            dispatch(updateSelectHeatmapClfy(makeActiveInfo()))
            clickFn(makeActiveInfo())
        }
    }




    return (
        <div className={`event_type_box ${compareActiveToThis() ? 'active' : ''}`}
             onMouseEnter={() => setOver(true)}
             onMouseLeave={() => setOver(false)}
             onClick={clickEvent}
        >
            <p className="event_type_text" ref={nameRef}>
                {item.unitNm}</p>
            {
                (isOver && showTooltip) &&
                <StyledTooltipTextLeft21>{item.unitNm}</StyledTooltipTextLeft21>
            }
        </div>
    )
}

export default GisHeatMapTypeItem