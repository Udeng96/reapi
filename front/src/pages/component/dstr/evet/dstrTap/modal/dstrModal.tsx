import React, {useEffect, useState} from "react";
import LastDstrDetailModal from "./lastDstr/lastDstrDetailModal";
import LastDstrListModal from "./lastDstr/lastDstrListModal";
import LastDstrSearchModal from "./lastDstr/lastDstrSearchModal";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../../store/rootStore";
import {
    getActiveItem,
    getActiveModal, getActiveModalItem,
    getCheckedModalLoca,
    getCheckedModalType,
} from "../../../../../store/view/dstr/dstrViewEventStore";
import {DTM_OPTION} from "../../../../../../data/const/eventTapConsts";
import * as eventActions from "../../../../../saga/action/dstr/evet/evet";

const DstrModal = () => {

    const dispatch = useDispatch()
    const isOpen = useSelector((state:RootState)=> state.view.dstrMain.event.activeModal)
    const eventSize = useSelector((state:RootState)=> state.server.dstrMain.event.listTotalModal)
    const payload = useSelector((state:RootState) => state.view.dstrMain.event.modalPayload);

    const eventType = useSelector((state:RootState)=>state.server.dstrMain.event.evetType)
    const eventLoca = useSelector((state:RootState)=>state.server.dstrMain.event.evetLoca)


    const closeModal = () => {
        dispatch(getActiveModal(false));
        dispatch(getCheckedModalLoca([]));
        dispatch(getCheckedModalType([]));
        dispatch(eventActions.actions.requestModalDetail(null))
        dispatch(getActiveModalItem(null))
    }

    const setDateForm = (dtm:string) =>{
        let year = dtm.substring(0,4)
        let month = dtm.substring(4,6)
        let date = dtm.substring(6,8)

        return `${year}-${month}-${date}`
    }

    const setLocaCdNm = (id:string) => {

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

    useEffect(()=> {
        if(isOpen){
            let firstPayload = {
                eventIdList : null,
                locationCdList : null,
                startDtm : DTM_OPTION[3].startDtm.replaceAll("-",""),
                endDtm: DTM_OPTION[3].endDtm.replaceAll("-",""),
                page: 1,
                rows: 10}
            dispatch(eventActions.actions.requestModalEvents(firstPayload))
        }
    },[isOpen])

    return (
        <>
            {/*pastEvtHistoryPopup*/}
            <section className={ "gis modal_popup past_evt_history_popup"} style={{"display":`${isOpen?"block": "none"}` }}>
                <div className="modal_popup_frame">
                    {/*modalPopupHeader*/}
                    <header className="modal_popup_header">
                        <div className="popup_title">지난 이벤트 내역</div>
                        <button type="button" className="btn_popup_close" onClick={event => closeModal()}/>
                    </header>
                    {/*modalPopupBody*/}
                    <div className="modal_popup_body">
                        {/*evtSearchArea*/}
                        <div className="evt_search_area">
                            <LastDstrSearchModal isOpen={isOpen}/>
                        </div>
                        {/*pastEvtHistoryArea*/}
                        <div className="past_evt_history_area">
                            <div className="evt_search_result_area">
                                <p className="evt_search_result_text">
                                    <span> {setDateForm(payload.startDtm!!)} ~ {setDateForm(payload.endDtm!!)} 까지
                                        {((payload.locationCdList!==null)&&(payload.locationCdList.length>0))&&` #${setLocaCdNm(payload.locationCdList[0])} ${payload.locationCdList.length>1? ` 외${payload.locationCdList.length-1}개` : ""}`}
                                        {((payload.eventIdList!==null)&&(payload.eventIdList.length>0))&&` / #${setTypeNm(payload.eventIdList[0])} ${payload.eventIdList.length>1? ` 외${payload.eventIdList.length-1}개` : ""}`}
                                    </span>
                                    검색결과 [{eventSize}]
                                </p>
                            </div>
                            <div className="past_evt_info_wrap">
                                -{/*pastEvtInfo*/}-
                               <LastDstrListModal isOpen={isOpen}/>

                                {/*evtDetailInfo*/}
                                <LastDstrDetailModal isOpen={isOpen}/>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </>
    )
}

export default DstrModal