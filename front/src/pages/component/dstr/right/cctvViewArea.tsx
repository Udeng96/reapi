import React, {useEffect, useState} from "react";
import NormalCctvArea from ".//content/normalCctvArea";
import FireCctvArea from "./content/fireCctvArea";

import CctvPopup from "./Modal/cctvPopup";

import styled from "styled-components"
import TruckPlayerArea from "./content/truckPlayerArea";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../store/rootStore";
import {updateEvetDetail, updateFireCarList} from "../../../store/server/dstr/dstrServerEventStore";
import {getActiveEventType, getActiveItem} from "../../../store/view/dstr/dstrViewEventStore";
import {updateCustomEventLatLng, updateMapDraw} from "../../../store/view/dstr/mapStore";
import {MapDrawEnum} from "../../../../data/const/mapConst";
import {increaseVideoReloadCnt} from "../../../store/view/etc/etcViewStore";

const StyledCctvContainer = styled.section<{ isOpen: boolean }>`
  width: 483px;
  height: 1024px;
  background-color: #070914;
  border-left: 1px solid #41434c;
  position: absolute;
  bottom: 0;
  right: ${(props) => props.isOpen ? '0' : '-483px'};
  -webkit-transition: right 0.8s ease;
  transition: right 0.8s ease;
  z-index: 3000;
`


const CctvViewArea = (props: { isRightOpen: boolean, setRightOpen: Function }) => {


    const dispatch = useDispatch()

    const eventType = useSelector((state: RootState) => state.view.dstrMain.event.activeEventType)
    const detailInfo = useSelector((state: RootState) => state.server.dstrMain.event.evetDetail)
    const [selectAddr, setSelectAddr] = useState<string | null>(null)

    const [isFireFrame , setIsFireFrame] = useState(false);


    useEffect(() => {
        setSelectAddr(null)
    }, [])

    useEffect(() => {


        (detailInfo?.eventId?.includes('FFT') && eventType) ?
            dispatch(getActiveEventType('FFT')) : dispatch(getActiveEventType(null))

        setSelectAddr(detailInfo?.addr ? detailInfo.addr : null)

        const isFire:boolean = eventType === 'FFT' && (detailInfo?.usvcOutbId.startsWith('WZ') ?? false)

        //todo : 출동차량이 아직표출이 안돼서
        // setIsFireFrame(isFire)

    }, [detailInfo])

    useEffect(() => {
        const isFire:boolean = eventType === 'FFT' && (detailInfo?.usvcOutbId.startsWith('WZ') ?? false)
        //todo : 출동차량이 아직표출이 안돼서
        // setIsFireFrame(isFire)

    }, [eventType]);



    const clearMonitoring = () => {
        dispatch(updateEvetDetail(null))
        dispatch(updateFireCarList(null))
        dispatch(getActiveItem(null))
        dispatch(getActiveEventType(null))
        dispatch(updateCustomEventLatLng(null))
        dispatch(updateMapDraw(MapDrawEnum.none))
    }

    const reloadMonitoring = () => {
        console.log('do reload monitoring')
        dispatch(increaseVideoReloadCnt())
    }


    return (
        <>
            {/*cctvContainer*/}
            <StyledCctvContainer isOpen={props.isRightOpen} className="cctv_container">
                <button type="button" className={`btn_tab   ${props.isRightOpen ? '' : 'clicked open_tab'} `}
                        onClick={() => props.setRightOpen(!props.isRightOpen)}
                />

                {/*cctvHeader*/}
                <header className="cctv_header">
                    <h1 className="cctv_title">선택 위치 주변<span>CCTV</span></h1>
                    <div className="func_btn_area">
                        <button type="button" className="btn_reload"
                                onClick={reloadMonitoring}
                        />
                        <button type="button" className="btn_event_end" onClick={clearMonitoring}>
                            투망 종료
                        </button>
                    </div>
                    <div
                        className={`cctv_loc_text ${selectAddr ? "" : "state_no_event"}`}>{selectAddr ? selectAddr : "선택된이벤트가 없습니다."}</div>
                </header>

                {/*cctvBody*/}
                <div className="cctv_body">
                    {/*typeNormal*/}
                    <div className={`cctv_box_wrap ${'type_event'}`} style={{display: "block"}}>
                        {
                            // (eventType !== "FFT" && (!detailInfo?.usvcOutbId.startsWith("WZ")  ?? true))  ?
                            // !isFireFrame ?
                                <NormalCctvArea/>
                            // :
                                // <>
                                //     <FireCctvArea/>
                                //     <TruckPlayerArea/>
                                // </>
                        }
                    </div>
                </div>
            </StyledCctvContainer>

            {/*cctvFullPopup*/}
            <CctvPopup/>

        </>
    )
}

export default CctvViewArea