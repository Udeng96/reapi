import React from "react";
import DstrSearchBoxTag from "./dstrSearchBoxTag";
import DstrSearchChkBox from "./dstrSearchChkBox";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../../../store/rootStore";
import {Search} from "../../../../../../../data/interface/event/eventInterface";
import {
    getActiveSearch,
    getCheckedLoca,
    getCheckedModalLoca,
    getCheckedModalType,
    getCheckedType
} from "../../../../../../store/view/dstr/dstrViewEventStore";
import {SEARCH_BOX_TYPE} from "../../../../../../../data/const/eventTapConsts";

const DstrSearchBox = (props: {
    searchBoxType: string,
    searchOptions:Array<Search>|null,
    chkOptionsList:Array<Search>,
}) => {

    const dispatch = useDispatch()

    // active된 box을 string 으로 받아온다.
    const activeSearchBox = useSelector((state: RootState) => state.view.dstrMain.event.activeSearchBox)

    // 자신이 active 된 box인지 확인한다.
    let isActive = (props.searchBoxType === activeSearchBox)


    // 검색 조건 box 활성화 및 비활성화
    const setSearchBoxActive = () => {
        if(activeSearchBox===SEARCH_BOX_TYPE.NONE){
            dispatch(getActiveSearch(props.searchBoxType))
        }else{
            dispatch(getActiveSearch(SEARCH_BOX_TYPE.NONE))
        }
    }

    // 태그 전체 삭제
    const delAllTag = () => {
        let newChkList:Array<Search> = []
        switch(props.searchBoxType){
            case SEARCH_BOX_TYPE.LOCATION : dispatch(getCheckedLoca(newChkList));break;
            case SEARCH_BOX_TYPE.EVENT_TYPE : dispatch(getCheckedType(newChkList));break;
            case SEARCH_BOX_TYPE.MODAL_LOCATION : dispatch(getCheckedModalLoca(newChkList));break;
            default : dispatch(getCheckedModalType(newChkList))
        }
    }


    return (
        <>
            {/*자신이 active 된 box이면 active 효과를 표출한다.*/}
            <div className={`search_select_frame ${isActive? "active":''}`} >
                <header className="search_select_header" onClick={event => setSearchBoxActive()}>
                    {
                        props.chkOptionsList.length===0?
                            <input className="placeholder_text"
                                   placeholder={(props.searchBoxType===SEARCH_BOX_TYPE.LOCATION||props.searchBoxType===SEARCH_BOX_TYPE.MODAL_LOCATION)?
                                       "발생 지역을 선택해주세요":"이벤트 타입을 선택해주세요"}/>:""
                    }

                    <div className="search_tag_area" >
                        <div className="search_tag_box_frame" >
                            {
                                props.chkOptionsList.length===0?
                                    '':props.chkOptionsList.map((item,index)=>(
                                        <DstrSearchBoxTag
                                            key ={`DSTR_SEARCH_BOX_TAG_${index}`}
                                            name={item.nm}
                                            id={item.id}
                                            boxType={props.searchBoxType}
                                            checkedOptions={props.chkOptionsList}/>
                                    ))
                            }
                        </div>
                        {
                            (props.chkOptionsList.length>3)&&
                            <div className="tag_func_area">
                                <div className="tag_more_text">...</div>
                                <div className="tag_count_num">{`+${props.chkOptionsList.length-3}`}</div>
                            </div>
                        }
                    </div>
                    <button type="button"
                            className={`btn_search_reset ${props.chkOptionsList.length===0? "dimmed":""}`}
                            onClick={event => {
                                delAllTag()
                                event.stopPropagation()
                            }}/>
                    <button type="button"
                            className="btn_search_arrow"
                            onClick={event => setSearchBoxActive()}/>
                </header>

                {/*자신이 active 된 box이면 select_body 표출한다.*/}
                <div className={"search_select_body"} style={{"display":`${isActive? "block":"none"}`}}>
                    f<div className="total_check_area">
                        <DstrSearchChkBox
                            name={(activeSearchBox===SEARCH_BOX_TYPE.LOCATION||activeSearchBox===SEARCH_BOX_TYPE.MODAL_LOCATION)?"화성시_전체":"전체"}
                            id={"all"}
                            boxType={props.searchBoxType}
                            checkedOptions={props.chkOptionsList}/>
                    </div>
                    <div className="each_check_area">
                        {
                            (props.searchOptions && props.searchOptions.length>0)?
                                props.searchOptions.map((item)=>(
                                    <DstrSearchChkBox
                                        key = {item.id}
                                        name={item.nm}
                                        id={item.id}
                                        boxType={props.searchBoxType}
                                        checkedOptions={props.chkOptionsList}
                                    />
                                ))
                                // todo : 해당 경우일때 고치기
                            : "reload 해주세요"
                                
                        }
                    </div>
                </div>
            </div>
        </>
    )

}

export default DstrSearchBox