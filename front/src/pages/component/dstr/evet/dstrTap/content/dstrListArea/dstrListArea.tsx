import React, {MutableRefObject, useEffect, useRef, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../../../store/rootStore";
import {EventListItem, Search} from "../../../../../../../data/interface/event/eventInterface";
import DstrListItem from "./dstrListItem";
import * as eventActions from "../../../../../../saga/action/dstr/evet/evet";
import ReactPlayer from "react-player";
// @ts-ignore
import alarm from "../../../../../../../public/static/alarm/alarm.mp3";

const DstrListArea = (props: { chkEventList: Array<Search>, chkLocaList: Array<Search>}) => {


    const eventList = useSelector((state: RootState) => state.server.dstrMain.event.evetList)
    const evetSize: Number | null = useSelector((state: RootState) => state.server.dstrMain.event.listTotal)
    const eventPayload = useSelector((state:RootState)=> state.view.dstrMain.event.eventPayload)

    const eventType = useSelector((state:RootState)=>state.server.dstrMain.event.evetType)
    const eventLoca = useSelector((state:RootState)=>state.server.dstrMain.event.evetLoca)

    const dispatch = useDispatch()
    const reload = () => {
        dispatch(eventActions.actions.requestEvents(eventPayload))
    }


    const setLocaNm = (id:string) => {

        let result = ""
        eventLoca.map((item)=>{
            if(item.id===id){
                result =  item.nm
            }
        })

        return result
    }

    const setTypeNm = (id:string) => {

        let result = ""
        eventType.map((item)=>{
            if(item.id===id){
                result= item.nm
            }
        })

        return result
    }

    // const [aPlay,setAPlay] = useState(false);
    // useEffect(()=>{
    //     setAPlay(true)
    // },[eventList])



    return (
        <>
            {/*<ReactPlayer*/}
            {/*    url={alarm}*/}
            {/*    playing={aPlay}*/}
            {/*    muted={false}*/}
            {/*    controls={true}*/}
            {/*    width={"400px"}*/}
            {/*    height={"50px"}*/}
            {/*/>*/}

            <div className="search_result_area">
                <p className="search_result_text">
                    {(eventPayload.locationCdList!==null)&&(eventPayload.locationCdList.length>0)?
                        <>
                            <span className="result_value"> #{setLocaNm(eventPayload.locationCdList[0])} </span>
                            { eventPayload.locationCdList.length > 1? <span>외{eventPayload.locationCdList.length - 1}개 /</span> : <span>&nbsp;/</span>}

                        </> : ""
                    }
                    {((eventPayload.eventIdList!==null)&&(eventPayload.eventIdList.length>0))?
                        <>
                            <span className="result_value"> #{setTypeNm(eventPayload.eventIdList[0])} </span>
                            {eventPayload.eventIdList.length > 1&& <span>외{eventPayload.eventIdList.length-1}개</span>}
                        </> : ""
                    }

                    검색결과 [{(eventList !== null && eventList.length !== 0) ? evetSize!!.toString() : '0'}]
                </p>
            </div>
            <div className="search_list_frame">
                <ul className="search_list_head">
                    <li>지역 명</li>
                    <li>이벤트 타입</li>
                    <li>주소</li>
                    <li>발생 일시</li>
                </ul>
                <ul className="search_list_body">
                    {eventList !== null ? eventList.length !== 0 ? eventList.map((item: EventListItem, index) => (
                                <DstrListItem key={`DstrListItem_${index}`} nowSelect={eventList[index]} item={item} index={index} isModal={false}/>)) :
                            <div className="no_result_area">
                                <p className="no_result_message">검색 조건의 오늘 발생한 재난 이벤트가 없습니다.<br/>
                                    <span>지난 이벤트 내역은 ‘지난 이벤트 내역’ 팝업을 통해 확인해주세요.</span>
                                </p>
                            </div> :
                        <div className="event_load_error_area">
                            <button type="button" className="btn_reload" onClick={event => reload()}/>
                            <p className="event_load_error_message">이벤트 로드에 문제가 발생하였습니다.<br/>
                                <span>Reload 또는 재검색을 시도해주세요.</span>
                            </p>
                        </div>
                    }
                </ul>
            </div>
        </>
    )
}

export default DstrListArea