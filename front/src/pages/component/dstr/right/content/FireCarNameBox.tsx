import React, {MutableRefObject, useEffect, useRef, useState} from "react";
import {useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import {FireCarInfo, FireCarResult} from "../../../../../data/interface/event/eventInterface";
import {StyledTooltipTextLeft21} from "../../gis/gisSettings/gisHeatMap/gisHeatMapTypeItem";
import styled from "styled-components";
import {StyledTooltip} from "../../common/tooltip";


export const StyledTooltipTextLeft58 = styled(StyledTooltip)`
  left: 58px;
`

const FireCarNameBox = () => {


    const fireCarInfoList = useSelector((state: RootState) => state.server.dstrMain.event.fireCarInfoList) as Array<FireCarInfo> | null


    const [carList, setCarList] = useState<string[]>([])

    const nameRef: MutableRefObject<any> = useRef()
    const [showTooltip, setShowTooltip] = useState(false)
    const [isOver, setOver] = useState(false)


    useEffect(() => {

        if (fireCarInfoList && fireCarInfoList.length >0){
            setCarList(fireCarInfoList.map((item)=>item.truckNo))
        }else {
            setCarList([])
        }

    }, [fireCarInfoList,nameRef]);

    const makeNames = () => {

        let names = '없음'

        if (carList.length > 0) {
            names = carList.map((item, index) => item).join(',')

        }

        return names
    }


    return (
        <div className="fire_truck_info_frame">
            <div className="fire_truck_list_area">
                <p className="fire_truck_list_title">해당 이벤트 소방차 출동 목록</p>
                <div className="fire_truck_list_text" ref={nameRef}
                     onMouseEnter={() => setOver(true)}
                     onMouseLeave={() => setOver(false)}>
                    {makeNames()}
                </div>

                {
                    (isOver && showTooltip) &&
                    <StyledTooltipTextLeft58>{makeNames()}</StyledTooltipTextLeft58>
                }
                {/*<div className="tooltip_text">소방차 MTV_001, 소방차 MTV_002, 소방차 MTV_016,소방차 MTV_019소방차*/}
                {/*    MTV_001, 소방차*/}
                {/*    MTV_002, 소방차 MTV_016,소방차 MTV_019,소방차 MTV_019,소방차 MTV_019*/}
                {/*</div>*/}
                {/* <div className="no_fire_truck_list_text">해당 이벤트<br />소방차 출동 목록이 없습니다.</div> */}
            </div>
            <div className="fire_truck_legend_area">
                <p className="fire_truck_legend_title">[출동영상 기준표]</p>
                <div className="fire_truck_legend_text legend_red">해당 이벤트 소방차 출동영상</div>
                <div className="fire_truck_legend_text legend_blue">다른 이벤트 소방차 출동영상</div>
            </div>
        </div>
    )
}


export default FireCarNameBox