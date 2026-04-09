import React, {useEffect, useState} from "react";
import DstrSearchBox from "./dstrSearchBox";
import {useDispatch, useSelector} from "react-redux";
import * as eventActions from "../../../../../../saga/action/dstr/evet/evet"
import {RootState} from "../../../../../../store/rootStore";
import {EventsPayload, Search} from "../../../../../../../data/interface/event/eventInterface";
import {EVENT_TAP_BOX_TYPE, SEARCH_BOX_TYPE} from "../../../../../../../data/const/eventTapConsts";
import moment from "moment";
import {getActiveItem, getActiveModal, getEventPayload} from "../../../../../../store/view/dstr/dstrViewEventStore";

const DstrSearchArea = (props:{chkEventList:Array<Search> ,chkLocaList:Array<Search>, setChkList:Function})=>{

    const dispatch = useDispatch()
    const getNowTime = moment().format("YYYY-MM-DD HH:mm:ss")
    const eventPayload = useSelector((state:RootState)=> state.view.dstrMain.event.eventPayload)

    const storeEvetLoca =  useSelector((state:RootState) => state.server.dstrMain.event.evetLoca)
    const storeEvetType =  useSelector((state:RootState) => state.server.dstrMain.event.evetType)

    const storeCheckedLocaList =  useSelector((state:RootState) => state.view.dstrMain.event.checkedLocaList)
    const storeCheckedTypeList =  useSelector((state:RootState) => state.view.dstrMain.event.checkedTypeList)

    const [updateTime,setUpdateTime]= useState<string>(getNowTime)

    useEffect(()=>{
        dispatch(eventActions.actions.requestLocations())
        dispatch(eventActions.actions.requestEventTypes())
    },[])

    // 옵션들 리스트 가져오기
    const getSearchOptions = (boxType:string) => {
        let searchOptions : Array<Search> | null = (boxType === SEARCH_BOX_TYPE.LOCATION) ? storeEvetLoca :storeEvetType
        return searchOptions
    }

    //체크된 옵션들 리스트 가져오기
    const getChkOptionsList = (boxType:string) => {
        let ChkOptionsList : Array<Search> | null = (boxType === SEARCH_BOX_TYPE.LOCATION) ? storeCheckedLocaList : storeCheckedTypeList

        return ChkOptionsList
    }

    const clickSearchBtn = () => {

        let eventIdList:Array<string> = []
        let locationCdList:Array<string> = []

        for(let item of props.chkEventList){
            item.id==="all"?  eventIdList=[] : eventIdList.push(item.id)
        }
        for(let item of props.chkLocaList){
            item.id==="all"? locationCdList=[] : locationCdList.push(item.id)
        }

        props.setChkList(props.chkEventList, props.chkLocaList)
        setUpdateTime(getNowTime)

        let payload : EventsPayload = {
            ...eventPayload,
            eventIdList: eventIdList,
            locationCdList: locationCdList,
            rows: 3000
        }

        dispatch(eventActions.actions.requestEvents(payload))
        dispatch(eventActions.actions.requestEventDetail(null))
        dispatch(getEventPayload(payload))
        dispatch(getActiveItem(null))

    }

    const openLastSearchPopup = () => {
        dispatch(getActiveModal(true))
    }

    return(
        <>

            <div className="search_setting_area">
                <h2 className="dash_sub_title event_search_title">이벤트 검색 조건 설정</h2>
                <button type="button" className="btn_event_history" onClick={event => openLastSearchPopup()}>지난 이벤트 내역</button>
                {
                    EVENT_TAP_BOX_TYPE.map((item)=> (
                        <div className="select_block" key ={item.type}>
                            <p className="select_title"><span>&middot;</span>{item.title}</p>
                            <DstrSearchBox
                                searchBoxType={item.type}
                                searchOptions={getSearchOptions(item.type)}
                                chkOptionsList={getChkOptionsList(item.type)}
                            />
                        </div>
                    ))
                }
                <button type="button" className="btn_search" onClick={event=>clickSearchBtn()}>검색<span>{`(업데이트 일시 : ${updateTime})`}</span></button>
            </div>

        </>


    )
}
export default DstrSearchArea

