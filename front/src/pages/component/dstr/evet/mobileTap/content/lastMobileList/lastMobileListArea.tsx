import React, {useEffect, useState} from "react";
import LastMobileListItem from "./lastMobileListItem";
import {RootState} from "../../../../../../store/rootStore";
import {useDispatch, useSelector} from "react-redux";
import Pagination from "../../../../paginagtion/pagination";
import {Paging} from "../../../../../../../data/interface/common/CommonInterface";
import {MobileInfo} from "../../../../../../../data/interface/event/mobileInterface";
import * as mobileActions from "../../../../../../saga/action/dstr/evet/mobileActions";

const LastMobileListArea = (props:{startDtm:string, endDtm:string}) => {

    const dispatch = useDispatch()
    const pastLists = useSelector((state: RootState) => state.server.dstrMain.mobile.pastList) as Paging<MobileInfo>
    const pastListTotal = useSelector((state: RootState) => state.server.dstrMain.mobile.pastListTotal)


    // 페이지 옵션
    const pageCnt = 8;
    const rows = 10;
    const [currentPage,setCurrentPage] = useState(1)

    const viewStartDtm = useSelector((state: RootState) => state.view.dstrMain.mobile.startDtm)
    const viewEndDtm = useSelector((state: RootState) => state.view.dstrMain.mobile.endDtm)

    const doFetchPastCount = useSelector((state: RootState) => state.view.dstrMain.mobile.doFetchPastCount)

    useEffect(() => {

        if (doFetchPastCount>1){
            dispatch(mobileActions.actions.requestMobilePastList({
                startDtm : props.startDtm,
                endDtm : props.endDtm,
                rows : 10,
                page : currentPage}))
        }

    }, [currentPage,doFetchPastCount])

    return (
        <>
            <div className="search_result_area">
                <p className="search_result_text"><span
                    className="result_value">{viewStartDtm} ~ {viewEndDtm} 까지</span>검색결과 [{pastListTotal}]</p>
            </div>
            <div className="search_list_frame">
                <ul className="search_list_head">
                    <li>No</li>
                    <li>영상 촬영 시작 일시</li>
                    <li>담당자 소속-이름</li>
                    <li/>
                </ul>
                <ul className="search_list_body">
                    {
                        pastLists ?
                            pastLists.data!.length !== 0 ?
                                pastLists.data!.map((item, index) => (
                                    <LastMobileListItem item={item} index={(index + 1) + ((currentPage - 1) * rows)}/>
                                )) :
                                // noResultArea
                            <div className="no_result_area">
                                    <p className="no_result_message">검색 조건에 해당하는 <span> 과거 출동영상이 없습니다.</span>
                                    </p>
                                </div> :
                            // eventLoadErrorArea
                            <div className="event_load_error_area">
                                <button type="button" className="btn_reload"/>
                                <p className="event_load_error_message">출동영상 로드에 문제가 발생하였습니다.<br/>
                                    <span>Reload 또는 재검색을 시도해주세요.</span>
                                </p>
                            </div>
                    }
                </ul>
                {/*pagination*/}
                <Pagination dataList={pastLists.data!} rows={rows} pageCnt={pageCnt} setCurrentPage={setCurrentPage}
                            maxDataCnt={pastListTotal} currentPage={currentPage}/>
            </div>
        </>
    )
}

export default LastMobileListArea