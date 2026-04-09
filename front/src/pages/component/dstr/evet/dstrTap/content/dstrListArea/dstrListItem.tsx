import React, {MutableRefObject, useEffect, useRef, useState} from "react";
import {EventListItem, VwEvent} from "../../../../../../../data/interface/event/eventInterface";
import {useDispatch, useSelector} from "react-redux";
import {
    getActiveEventType,
    getActiveItem,
    getActiveModalItem
} from "../../../../../../store/view/dstr/dstrViewEventStore";
import {RootState} from "../../../../../../store/rootStore";
import * as eventActions from "../../../../../../saga/action/dstr/evet/evet"
import {
    changeMapMove,
    updateCustomEventLatLng,
    updateMapClickFn,
    updateMapDraw
} from "../../../../../../store/view/dstr/mapStore";
import {MapDrawEnum, MapMoveEnum} from "../../../../../../../data/const/mapConst";

const DstrListItem = (props: { nowSelect: VwEvent, item: EventListItem, index: number | null, isModal: boolean }) => {

    const dispatch = useDispatch()
    const addrRef: MutableRefObject<any> = useRef()
    const [showTooltip, setShowTooltip] = useState(false)

    const activeItem = useSelector((state: RootState) => state.view.dstrMain.event.activeItem)
    const activeModalItem = useSelector((state: RootState) => state.view.dstrMain.event.activeModalItem)

    useEffect(() => {
        // tooltip 설정
        setShowTooltip(false)
    }, [])

    //todo : 무조건 리팩토링 해주기!!! 코드 잘못되서 임시로 고친 상태니 꼭 코드 정리 필수로 해줘야함!!!!!!

    // 리스트를 눌렀을 때 상세정보 가져오기
    const getDetail = (event: any) => {

        dispatch(updateMapDraw(MapDrawEnum.none))
        dispatch(updateCustomEventLatLng(null))

        //아무것도 활성화 되어있지 않은 상태라면
        if ((props.isModal ? activeModalItem === null : activeItem === null)) {
            //active 활성화 시켜주고, active Item에 해당 item id 저장해주기
            if (props.isModal) {
                //modal 일 경우 아래 코드
                dispatch(getActiveModalItem(props.item.statEvetOutbSeqn))
                dispatch(eventActions.actions.requestModalDetail(activeModalItem))
                dispatch(eventActions.actions.requestModalDetail(props.item.statEvetOutbSeqn))
            } else {
                //modal 이 아닐 경우 아래 코드
                dispatch(getActiveEventType(props.item.eventId?.substring(3, 6)!!))
                dispatch(getActiveItem(props.item.statEvetOutbSeqn))
                dispatch(eventActions.actions.requestEventDetail(props.item.statEvetOutbSeqn))

                dispatch(changeMapMove({
                    latlng: [parseFloat(props.nowSelect.lat!), parseFloat(props.nowSelect.lng!)],
                    mapMoveType: MapMoveEnum.event
                }))
            }

        } else {
            // 활성화 되어있는 상태에서 같은 것을 눌렀을 경우 ===> 해제
            if (props.nowSelect.statEvetOutbSeqn === (props.isModal ? activeModalItem : activeItem)) {
                if (props.isModal) {
                    //modal 일 경우 아래 코드
                    dispatch(getActiveModalItem(null))
                    dispatch(eventActions.actions.requestModalDetail(null))
                } else {
                    //modal 이 아닐 경우 아래 코드
                    dispatch(getActiveEventType(null))
                    dispatch(getActiveItem(null))
                    dispatch(eventActions.actions.requestEventDetail(null))
                }
            } else {
                // 활성화 되어있는 상태에서 다른 것을 눌렀을 경우 ===> 해당 item으로 active 변경해주기
                if (props.isModal) {
                    //modal 일 경우 아래 코드
                    dispatch(getActiveModalItem(props.item.statEvetOutbSeqn))
                    dispatch(eventActions.actions.requestModalDetail(props.item.statEvetOutbSeqn))
                } else {
                    //modal 이 아닐 경우 아래 코드
                    dispatch(getActiveEventType(props.item.eventId?.substring(3, 6)!!))
                    dispatch(getActiveItem(props.item.statEvetOutbSeqn))
                    dispatch(eventActions.actions.requestEventDetail(props.item.statEvetOutbSeqn))

                    dispatch(changeMapMove({latlng: [parseFloat(props.nowSelect.lat!), parseFloat(props.nowSelect.lng!)], mapMoveType: MapMoveEnum.event}))

                }
            }
        }
    }

    const setDtm = (outbDtm: string | null) => {
        if (outbDtm !== null) {
            let newDtm = outbDtm!!.substring(4, outbDtm.length)
            let month = newDtm.substring(0, 2)
            let date = newDtm.substring(2, 4)
            let hour = newDtm.substring(4, 6)
            let min = newDtm.substring(6, 8)
            let second = newDtm.substring(8, 10)
            return `${month}-${date} ${hour}:${min}:${second}`
        } else {
            return ''
        }
    }

    return (
        <li>
            <ul className={`search_list_item ${((props.isModal?activeModalItem:activeItem)===props.item.statEvetOutbSeqn)?"active":""} ${props.isModal?"modal":""}`}
                onClick={event => getDetail(event)}>
                <li>{props.item.locationNm}</li>
                <li>{props.item.eventNm}</li>
                <li
                    ref={addrRef}
                    onMouseEnter={() => {
                        const isOver = addrRef.current.scrollWidth > addrRef.current.clientWidth
                        setShowTooltip(isOver)
                    }}
                    onMouseLeave={() => {
                        setShowTooltip(false)
                    }}
                >{props.item.addr}</li>
                <li>{setDtm(props.item.outbDtm)}</li>
                {showTooltip&&<div className="gis tooltip_text" style={{"left": "149px"}}>{props.item.addr}</div>}
                <div className="hams tooltip_text" style={{"left": "149px"}}>{props.item.addr}</div>
            </ul>
        </li>
    )
}

export default DstrListItem