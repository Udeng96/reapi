import React, {useEffect, useState} from "react";
import Pagination from "../../../../paginagtion/pagination";
import {RootState} from "../../../../../../store/rootStore";
import {useDispatch, useSelector} from "react-redux";
import DstrListItem from "../../content/dstrListArea/dstrListItem";
import * as evetActions from "../../../../../../saga/action/dstr/evet/evet";
import {getActiveModalItem} from "../../../../../../store/view/dstr/dstrViewEventStore";


const LastDstrListModal = (props: { isOpen: boolean }) => {

    const dispatch = useDispatch()

    //페이징 옵션
    const pageCnt = 8;
    const rows = 10;
    const [currentPage, setCurrentPage] = useState(1)

    // payload
    const payload = useSelector((state: RootState) => state.view.dstrMain.event.modalPayload)
    const eventList = useSelector((state: RootState) => state.server.dstrMain.event.evetListModal)
    const eventSize = useSelector((state: RootState) => state.server.dstrMain.event.listTotalModal)


    // 처음 modal open 시, 리스트 불러오기
    // page가 바뀔 때 마다 list 재 요청
    useEffect(() => {

        let newPayload = {...payload, page: currentPage, rows: rows}

        if (props.isOpen) {
            dispatch(evetActions.actions.requestModalEvents(newPayload))
        }
    }, [currentPage])

    // 검색 버튼 누르거나, popup창 을 열때 페이지 1로 설정
    // 여기서 검색 버튼 누르거나 popup창을 열 때, 선택된 아이템도 모드 reset
    useEffect(() => {
        setCurrentPage(1)
        dispatch(getActiveModalItem(null))
    }, [props.isOpen, payload])

    const reload = () => {
        dispatch(evetActions.actions.requestModalEvents(payload))
    }

    return (
        <>
            <div className="past_evt_info">
                <ul className="search_list_head">
                    <li>지역 명</li>
                    <li>이벤트 타입</li>
                    <li>주소</li>
                    <li>발생 일시</li>
                </ul>
                <ul className="search_list_body">
                    {eventList !== null ? eventList.length !== 0 ?
                            eventList.map((item, index) => (
                                <DstrListItem key={`MODAL_ITEM_${item.statEvetOutbSeqn}`}nowSelect={eventList[index]} item={item} index={index} isModal={true}/>
                            )) :
                            <div className="no_result_area">
                                <p className="no_result_message">검색 조건의 오늘 발생한 재난 이벤트가 없습니다.<br/>
                                    <span>지난 이벤트 내역은 ‘지난 이벤트 내역’ 팝업을 통해 확인해주세요.</span>
                                </p>
                            </div> :
                        <div className="event_load_error_area">
                            <button type="button" className="btn_reload" onClick={reload}/>
                            <p className="event_load_error_message">이벤트 로드에 문제가 발생하였습니다.<br/>
                                <span>Reload 또는 재검색을 시도해주세요.</span>
                            </p>
                        </div>
                    }
                </ul>
                {/*pagination*/}
                <Pagination dataList={eventList} rows={rows} pageCnt={pageCnt} setCurrentPage={setCurrentPage}
                            maxDataCnt={eventSize} currentPage={currentPage}/>
            </div>
        </>
    )
}

export default LastDstrListModal