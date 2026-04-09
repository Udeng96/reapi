import React, {MutableRefObject, useEffect, useRef, useState} from "react";
import styled from "styled-components"
import {StyledTooltip} from "../../common/tooltip";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import {AddrInfo} from "../../../../../data/interface/etc/Addr";
import {dispatch} from "jest-circus/build/state";
import {updateShowSearchList} from "../../../../store/view/etc/etcViewStore";
import {useIsOverflow, useIsOverflow2Item} from "../../../../hook/useIsOverFlow";
import {changeMapMove, updateSelectAddrInfo} from "../../../../store/view/dstr/mapStore";
import {MapMoveEnum} from "../../../../../data/const/mapConst";

const StyledSearchListBox = styled.div<{ isActive: boolean }>`
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  -ms-flex-direction: column;
  flex-direction: column;
  width: 100%;
  height: 40px;
  padding: 5px 0 0 23.7px;
  cursor: pointer;
  position: relative;
  -webkit-transition: all .25s;
  transition: all .25s;

  ${(props) => props.isActive ? 'background-color: #452019;' : ''}
  &:hover {
    background-color: #282a38;
  }

  &:active {
    background-color: #383a47;
  }
`

const StyledSearchListName = styled.p`
  width: 90%;
  font-family: "NotoSansCJKR";
  font-size: 12px;
  letter-spacing: -0.24px;
  color: #f7f7f7;
  margin-bottom: -2px;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
  line-height: 1.42857143;

  &:hover {
    text-decoration: underline;
  }
`

const StyledKeyWordSpan = styled.span`
  color: #ff6f26;
`

const StyledSearchListLoc = styled.p`
  width: 90%;
  font-family: "NotoSansCJKL";
  font-size: 11px;
  letter-spacing: 0.28px;
  color: #aeafb0;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
  line-height: 1.42857143;
`

const StyledTooltipText = styled(StyledTooltip)`
  left: 58px;
`


const AddrSearchListItem = (props: { activeId: string, changeActiveId: Function, item: AddrInfo }) => {

    const nameRef: MutableRefObject<any> = useRef()
    const dispatch = useDispatch()

    const word: string = useSelector((state: RootState) => state.view.etc.word)
    const [isOver, setOver] = useState(false)
    const [showTooltip, setShowTooltip] = useState(false)

    function setAddrKey(): string {
        let listName: string = props.item.title
        listName = listName.replaceAll(word, `<span style="color: #ff6f26">${word}</span>`)
        return listName
    }

    const clickEvent = () => {
        props.changeActiveId(props.item.id)
        dispatch(updateShowSearchList(false))
        //TODO 좌표 이동
        dispatch(changeMapMove({latlng: [parseFloat(props.item.point.y), parseFloat(props.item.point.x)], mapMoveType: MapMoveEnum.max_zoom}))
        dispatch(updateSelectAddrInfo(props.item))
    }

    useEffect(() => {
        const isOver = nameRef.current.scrollWidth > nameRef.current.clientWidth
        if(isOver){
            setShowTooltip(true)
        }
    }, [])



    return (
        <>
            <StyledSearchListBox isActive={props.activeId === props.item.id}
                                 onClick={() => {
                                     clickEvent()
                                 }}>
                <StyledSearchListName onMouseEnter={() => setOver(true)}
                                      onMouseLeave={() => setOver(false)}
                                      ref={nameRef}
                                      dangerouslySetInnerHTML={{__html: setAddrKey()}}
                />
                <StyledSearchListLoc>{props.item.address.road}</StyledSearchListLoc>
                {/*TODO tooltip은 너무 긴 애들만 생긴다, ... 처리하고*/}
                {(showTooltip && isOver) && <StyledTooltipText>{props.item.title}</StyledTooltipText>}

            </StyledSearchListBox>
        </>
    )
}


export default AddrSearchListItem