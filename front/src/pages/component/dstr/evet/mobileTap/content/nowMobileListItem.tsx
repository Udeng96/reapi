import React from "react";
import {RootState} from "../../../../../store/rootStore";
import {useDispatch, useSelector} from "react-redux";
import {MobileInfo} from "../../../../../../data/interface/event/mobileInterface";
import {updateActiveLiveBtnList, updateActiveLiveList} from "../../../../../store/view/dstr/dstrViewMobileStore";
import {changeMapMove} from "../../../../../store/view/dstr/mapStore";
import {MapMoveEnum} from "../../../../../../data/const/mapConst";
import _ from "underscore";

const NowMobileListItem = (props: { item: MobileInfo, index: number }) => {

    const dispatch = useDispatch()
    let activeLiveItem: MobileInfo | null = useSelector((state: RootState) => state.view.dstrMain.mobile.activeLiveList)
    let activeLiveBtnList: Array<MobileInfo> = useSelector((state: RootState) => state.view.dstrMain.mobile.activeLiveBtnList)

    const setDateForm = (dtm: string) => {
        let year = dtm.substring(0, 4)
        let month = dtm.substring(4, 6)
        let date = dtm.substring(6, 8)
        let hour = dtm.substring(8, 10)
        let min = dtm.substring(10, 12)
        let second = dtm.substring(12, 14)
        return `${year}-${month}-${date} ${hour}:${min}:${second}`
    }

    const onClickItem = (e: any) => {

        if (activeLiveItem === null) {
            dispatch(updateActiveLiveList(props.item))
            dispatch(changeMapMove({latlng:[props.item.lat!!, props.item.lng!!], mapMoveType:MapMoveEnum.max_zoom}))
        } else {
            if (props.item.id === activeLiveItem.id) {
                dispatch(updateActiveLiveList(null))
            } else {
                dispatch(updateActiveLiveList(props.item))
                dispatch(changeMapMove({latlng:[props.item.lat!!, props.item.lng!!], mapMoveType:MapMoveEnum.max_zoom}))
            }
        }
    }

    const playBtnClickEvent = () => {

        if(props.item.lng === 0.0){
            return
        }

        let checkInfo = checkActive()

        // 클릭이 되어있지 않은 경우
        if(checkInfo.index === -1){
            // 해당 리스트에 item 추가
            checkInfo.list.push(props.item)
            dispatch(updateActiveLiveBtnList(checkInfo.list as MobileInfo[] | null))
        }else{
            // 클릭이 되어 있는 경우
            // 해당 클릭된 버튼에 대한 정보 삭제
            checkInfo.list.splice(checkInfo.index, 1)
            dispatch(updateActiveLiveBtnList(checkInfo.list as MobileInfo[] | null))
        }

        dispatch(updateActiveLiveList(null))

    }

    const checkActive = () : {index:number, list:MobileInfo[]} => {

        let newActiveLiveBtnList = activeLiveBtnList ? [...activeLiveBtnList] : []

        // 클릭한 버튼의 id 가 있으면 해당 id의 index가 반환
        // 클릭한 버튼의 id 가 없으면 -1 반환
        const index = _.findIndex(newActiveLiveBtnList,{
            id : props.item.id
        })

        return { index, list : newActiveLiveBtnList}
    }

    return (
        <li>
            <ul className={`live_list_item ${activeLiveItem!==null && activeLiveItem.id===props.item.id? "active":""}`} onClick={e => onClickItem(e)}>
                <li>{props.index}</li>
                <li>{props.item.userDtm!!}</li>
                <li>{props.item.userDept} - {props.item.userNm}</li>
                <li>{props.item.mbChk===3 && "촬영중"}</li>
                <li>
                    <button type="button" id={`liveBtn${props.item.id}`} className={`btn_live_play ${checkActive().index!==-1 && 'active'}`}
                            onClick={event => {
                                event.stopPropagation()
                                dispatch(updateActiveLiveBtnList(null))
                                dispatch(changeMapMove({latlng:[props.item.lat!!,props.item.lng!!],mapMoveType:MapMoveEnum.max_zoom}))
                                playBtnClickEvent()
                            }}
                    />
                </li>
            </ul>
        </li>
    )
}
export default NowMobileListItem