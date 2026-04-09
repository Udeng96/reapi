import React, {MutableRefObject, useEffect, useRef, useState} from "react";
import {CctvInfo} from "../../../../../../../data/interface/cctv/cctvInterface";
import {StyledTooltip} from "../../../../common/tooltip";
import styled from "styled-components";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../../../store/rootStore";
import {updateActiveCctv, updatePlayGisCctvList} from "../../../../../../store/view/dstr/dstrViewCctvStore";
import _ from "underscore"
import {changeMapMove} from "../../../../../../store/view/dstr/mapStore";
import {MapMoveEnum} from "../../../../../../../data/const/mapConst";


const StyledTooltipTextLeft170 = styled(StyledTooltip)`
  left: 170px;
`


const CctvListItem = (props: { item: CctvInfo, no: number }) => {

    const dispatch = useDispatch()

    const nameRef: MutableRefObject<any> = useRef()
    const [showTooltip, setShowTooltip] = useState(false)
    const [isOver, setOver] = useState(false)

    const playGisCctvList = useSelector((state: RootState) => state.view.dstrMain.cctv.playGisCctvList)
    const activeCctv = useSelector((state:RootState)=>state.view.dstrMain.cctv.activeCctv)

    //현재 투망중인 CCTV를 player가 열리지않게 한다.
    const playCctvList = useSelector((state: RootState) => state.view.dstrMain.cctv.playCctvList)


    useEffect(() => {
        const isOver = nameRef.current.scrollWidth > nameRef.current.clientWidth
        if (isOver) {
            setShowTooltip(true)
        }
    }, [])


    const playBtnClickEvent = () => {
        //현재 투망중인지 체크한다.
        const playerIndex = _.findIndex(playCctvList, {
            facId: props.item.facId
        })
        if (props.item.lng === 0.0 || playerIndex > -1) {
            return
        }
        let checkInfo = checkActive()

        if (checkInfo.index === -1) {
            checkInfo.list.push(props.item)
            dispatch(updatePlayGisCctvList(checkInfo.list as CctvInfo[] | null))
        } else {
            checkInfo.list.splice(checkInfo.index, 1)
            dispatch(updatePlayGisCctvList(checkInfo.list as CctvInfo[] | null))
        }
    }

    const checkActive = (): { index: number, list: CctvInfo[] } => {
        let newPlayCctvList = playGisCctvList ? [...playGisCctvList] : []

        const index = _.findIndex(newPlayCctvList, {
            facId: props.item.facId
        })


        return {index, list: newPlayCctvList}
    }

    const doPlay = (e:React.MouseEvent<HTMLButtonElement>)=>{

        if (!(props.item.lat == 0 || props.item.lng == 0)){
            e.stopPropagation()
            dispatch(changeMapMove({latlng: [props.item.lat, props.item.lng], mapMoveType: MapMoveEnum.max_zoom}))
            dispatch(updateActiveCctv(null))
            setTimeout(playBtnClickEvent,300)
        }else {
            alert("해당 CCTV는 좌표가 없습니다.")
        }
    }

    return (
        <>
            <li>
                <ul className={`search_list_item ${activeCctv?.facId === props.item.facId ? 'active' : ''}`}
                    onMouseEnter={() => setOver(true)}
                    onMouseLeave={() => setOver(false)}
                    onClick={
                        e => {
                            if (activeCctv?.facId === props.item.facId) {
                                dispatch(updateActiveCctv(null))
                            } else {
                                dispatch(updateActiveCctv(props.item))

                                if (!(props.item.lat == 0 || props.item.lng == 0)){
                                    dispatch(changeMapMove({latlng: [props.item.lat, props.item.lng], mapMoveType: MapMoveEnum.max_zoom}))
                                }
                            }
                        }
                    }>
                    <li>{props.no}</li>
                    <li>{props.item.clfyNm}</li>
                    <li ref={nameRef}>{props.item.facNm}</li>
                    <li>
                        <button type="button" className={`btn_play ${checkActive().index !==-1 && 'active'}`}
                                onClick={e => doPlay(e)}/>
                    </li>
                    {
                        (isOver && showTooltip) &&
                        <StyledTooltipTextLeft170>{props.item.facNm}</StyledTooltipTextLeft170>
                    }

                </ul>
            </li>
        </>
    )
}
export default CctvListItem