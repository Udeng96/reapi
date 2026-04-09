import React, {useEffect, useState} from "react";
import Select, { StylesConfig } from "react-select";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import {actions} from "../../../../saga/action/dstr/evet/fireCarACtions"
import {updateFireCarList} from "../../../../store/server/dstr/dstrServerEventStore";
import SelectOption from "./SelectOption";
import FireCarNameBox from "./FireCarNameBox";
import Player from "../../player/Player";
import {VIDEO_TYPE} from "../../../../../data/const/const";
import {VwEvent} from "../../../../../data/interface/event/eventInterface";

/**
 * active 이벤트의 경우 sage를 통해 정보를 받아서 처리
 * @constructor
 */
const TruckPlayerArea = () => {


    const dispatch = useDispatch()

    const activeEvent:VwEvent|null = useSelector((state:RootState)=>state.server.dstrMain.event.evetDetail)
    const [fireVideoInfo, setFireVideoInfo] = useState({value: {rtsp: '', mgrNo: ''}, label: '', nm : ''});

    useEffect(()=>{

        if (activeEvent){
            if (activeEvent.eventId!.includes('FFT')){ //소방 이벤트일 경우에만 수행

                //이벤트에 맵핑된 정보 호출
                dispatch(actions.fetchDispatchFireCar(activeEvent.usvcOutbId))
            }
        }else {
            dispatch(updateFireCarList(null))
            setFireVideoInfo({value: {rtsp: '', mgrNo: ''}, label: '', nm : ''})
        }
    },[activeEvent])




    return (
        <>
            <div className="fire_truck_video_frame">
                <p className="fire_truck_video_title">소방차 출동영상</p>

                {/*SELECT BOX*/}
                {/*<Select className="select_fire_truck" placeholder='소방차 출동영상 선택' options={options} styles={selectStyle} />*/}
                <SelectOption setVideoInfo={setFireVideoInfo}/>

                {/*VIDEO 영역*/}
                <div className="cctv_box_frame">
                    <div className="cctv_box_body">
                        <div className="cctv_in" >
                            {
                                fireVideoInfo.nm !== '' &&
                                <Player
                                    // cctvId={fireVideoInfo.value.mgrNo}
                                    cctvId={''}
                                        rtsp = {fireVideoInfo.value.rtsp}
                                        type = {VIDEO_TYPE.FIRE}
                                />
                            }


                        </div>
                        {
                            fireVideoInfo.nm === '' &&
                            <>
                                <p className="no_video_message">소방차 출동영상이 없습니다.<br /><span>소방차 출동영상 선택에서</span><span
                                    className="hint_text">다른 이벤트 소방차 출동영상</span><span>을 선택해 주세요.</span></p>
                                <button type="button" className="btn_cctv_full" />
                            </>
                        }

                    </div>
                </div>

                <FireCarNameBox/>
            </div>
        </>
    )
}

export default TruckPlayerArea