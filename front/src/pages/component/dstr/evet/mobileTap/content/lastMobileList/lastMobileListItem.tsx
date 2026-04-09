import React, {useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../../../store/rootStore";
import {updateActivePastBtnList, updateActivePastList} from "../../../../../../store/view/dstr/dstrViewMobileStore";
import {MobileInfo} from "../../../../../../../data/interface/event/mobileInterface";
import {changeMapMove} from "../../../../../../store/view/dstr/mapStore";
import {MapMoveEnum, MAX_MOBILE_PAST_PLAYER_CNT} from "../../../../../../../data/const/mapConst";
import _ from "underscore";

const LastMobileListItem = (props: { item: MobileInfo, index: number }) => {

    const dispatch = useDispatch();
    let activePastItem: MobileInfo | null = useSelector((state: RootState) => state.view.dstrMain.mobile.activePastItem)
    let activePastBtnList: Array<MobileInfo> = useSelector((state: RootState) => state.view.dstrMain.mobile.activePastBtnList)

    const setDateForm = (outbDtm: string) => {

        let year = outbDtm.substring(0, 4)
        let month = outbDtm.substring(4, 6)
        let date = outbDtm.substring(6, 8)
        let hour = outbDtm.substring(8, 10)
        let min = outbDtm.substring(10, 12)
        let second = outbDtm.substring(12, 14)
        return `${year}-${month}-${date} ${hour}:${min}:${second}`
    }

    const onClickPastItem = (e: any) => {

        if (activePastItem === null) {
            dispatch(updateActivePastList(props.item))
            dispatch(changeMapMove({latlng:[props.item.lat!!, props.item.lng!!], mapMoveType:MapMoveEnum.max_zoom}))

            if(activePastBtnList!==null && activePastBtnList.length!==0){
                if(activePastBtnList[0].seq !== props.item.seq){
                    dispatch(updateActivePastBtnList(null))

                }
            }
        } else {
            if (props.item.seq === activePastItem.seq) {
                dispatch(updateActivePastList(null))
            } else {
                dispatch(updateActivePastList(props.item))
                dispatch(changeMapMove({latlng:[props.item.lat!!, props.item.lng!!], mapMoveType:MapMoveEnum.max_zoom}))

                if(activePastBtnList!==null && activePastBtnList.length!==0){
                    if(activePastBtnList[0].seq !== props.item.seq){
                        dispatch(updateActivePastBtnList(null))

                    }
                }
            }
        }

    }

    const playBtnClickEvent = () => {

        if(props.item.lng === 0.0){
            return
        }

        let checkInfo = checkActive()

        if(checkInfo.index===-1){
            if(MAX_MOBILE_PAST_PLAYER_CNT> (checkInfo.list?checkInfo.list:0)){
                checkInfo.list.push(props.item)
                dispatch(updateActivePastBtnList(checkInfo.list as MobileInfo[] | null))
            }else{
                checkInfo.list.splice(checkInfo.index,1)
                checkInfo.list.push(props.item)
                dispatch(updateActivePastBtnList(checkInfo.list as MobileInfo[]|null))

            }
        }else{
            checkInfo.list.splice(checkInfo.index, 1)
            dispatch(updateActivePastBtnList(checkInfo.list as MobileInfo[] | null))
        }

        dispatch(updateActivePastList(null))
    }

    const checkActive = () : {index:number, list:MobileInfo[]} => {
        let newActivePastBtnList = activePastBtnList ? [...activePastBtnList] : []
        const index = _.findIndex(newActivePastBtnList,{
            seq:props.item.seq
        })

        return {index, list: newActivePastBtnList}
    }

    return (
        <li>
            <ul className={`search_list_item ${activePastItem !== null && activePastItem.seq === props.item.seq ? "active" : ""}`}
                onClick={event => onClickPastItem(event)}>
                <li>{props.index}</li>
                {/*<li>{setDateForm(props.item.userDtm!!)}</li>*/}
                <li>{props.item.userDtm!!}</li>
                <li>{props.item.userDept} - {props.item.userNm}</li>
                <li>
                    <button className={`btn_past_play ${checkActive().index!==-1 && 'active'}`}
                            type="button" id={`pastBtn_${props.item.seq}`}
                            onClick={event => {
                                event.stopPropagation()
                                dispatch(updateActivePastList(null))
                                dispatch(changeMapMove({latlng:[props.item.lat!!,props.item.lng!!],mapMoveType:MapMoveEnum.max_zoom}))
                                playBtnClickEvent()
                            }}
                    />
                </li>
            </ul>
        </li>
    )
}

export default LastMobileListItem