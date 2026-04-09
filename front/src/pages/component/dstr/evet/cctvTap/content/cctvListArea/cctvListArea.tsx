import React, {useEffect} from "react";
import CctvListItem from "./cctvListItem";
import Pagination from "../../../../paginagtion/pagination";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../../../store/rootStore";
import {Paging} from "../../../../../../../data/interface/common/CommonInterface";
import {CctvInfo} from "../../../../../../../data/interface/cctv/cctvInterface";
import * as actions from "../../../../../../saga/action/dstr/cctv/cctvActions"

const CctvListArea = (props:
                          {
                              currentPage: number, setCurrentPage: Function
                          }) => {

    const dispatch = useDispatch()

    const clfyInfo = useSelector((state: RootState) => state.view.dstrMain.cctv.selectClfy)
    const word = useSelector((state: RootState) => state.view.dstrMain.cctv.word)
    const cctvList = useSelector((state: RootState) => state.server.dstrMain.cctv.cctvTabCctvList) as Paging<CctvInfo>

    //페이징 옵션
    const rows = 11;


    useEffect(() => {
        //페이지가 변경이 될때마다 데이터를 다시 가져와서 그린다.
        dispatch(actions.actions.fetchList({
            page: props.currentPage,
            rows: 11,
            type: clfyInfo ? clfyInfo.id : '',
            cctvNm: word
        }))

    }, [props.currentPage])


    return (
        <>
            <div className="search_result_area">
                <p className="search_result_text">
                    {/*검색어는 열글자로 제한한다고 하던데*/}
                    <span
                        className="result_value"> {clfyInfo && '#' + clfyInfo.nm} {word === '' ? '' : '> #‘' + word + '‘'}</span>검색결과
                    [{cctvList ? cctvList!.total : 0}]</p>
            </div>
            <div className="search_list_frame">
                <ul className="search_list_head">
                    <li>No</li>
                    <li>설치 목적</li>
                    <li>명칭</li>
                    <li/>
                </ul>
                <ul className="search_list_body">
                    {
                        cctvList.data ? cctvList.data.length > 0 ?
                                cctvList.data.map((item: CctvInfo, index) => {
                                    return <CctvListItem item={item} no={(index + 1) + ((props.currentPage - 1) * rows)}
                                                         key ={`CCTV_LIST_${item.facId}`}/>
                                }) :
                                (<div className="no_result_area">
                                    <p className="no_result_message">검색 조건에 해당하는 CCTV가 없습니다.</p>
                                </div>)
                            :
                            (<div className="event_load_error_area">
                                <button type="button" className="btn_reload" onClick={() => {

                                    dispatch(actions.actions.fetchList({
                                        page: 1,
                                        rows: 11,
                                        type: clfyInfo ? clfyInfo.id : '',
                                        cctvNm: word
                                    }))

                                }}></button>
                                <p className="event_load_error_message">CCTV 로드에 문제가 발생하였습니다.<br/>Reload 또는 재검색을 시도해주세요.
                                </p>
                            </div>)
                    }
                </ul>
                {
                    cctvList.data && (
                        cctvList.data.length > 0 &&
                        <Pagination dataList={cctvList.data} rows={rows} pageCnt={8} setCurrentPage={props.setCurrentPage}
                                    currentPage={props.currentPage} maxDataCnt={cctvList.total}/>)
                }
            </div>
        </>
    )
}

export default CctvListArea