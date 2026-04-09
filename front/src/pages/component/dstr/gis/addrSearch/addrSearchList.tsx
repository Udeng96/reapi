import React, {useEffect, useState} from "react";
import styled from "styled-components"
import AddrSearchListItem from "./addrSearchListItem";
import {AddrInfo} from "../../../../../data/interface/etc/Addr";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import {dispatch} from "jest-circus/build/state";
import {updateShowSearchList} from "../../../../store/view/etc/etcViewStore";
import {changeMapMove, updateSelectAddrInfo} from "../../../../store/view/dstr/mapStore";
import {MapMoveEnum} from "../../../../../data/const/mapConst";


const StyledSearchList = styled.div<{ isShow: boolean }>`
    width: 270px;
    height: auto;
    -webkit-box-shadow: 0px 10px 20px 0 rgba(0, 0, 0, 0.3);
    box-shadow: 0px 10px 20px 0 rgba(0, 0, 0, 0.3);
    border: solid 1px #ff6f26;
    background-color: #13151f;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    border-bottom-left-radius: 8px;
    border-bottom-right-radius: 8px;
    position: absolute;
    top: 39px;
    left: 170px;
    display: ${(props) => props.isShow ? 'block' : 'none'};
`

const StyledSearchListHeader = styled.header`
    width: 94%;
    height: 34px;
    padding: 11px 0 0 16.7px;
    margin: 0 8px;
    border-bottom: 1px solid #36363d;
    position: relative;
`
const StyledSearchListTitle = styled.p`
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    width: 100%;
    font-family: "NotoSansCJKL";
    font-size: 11px;
    color: #c7c8c9;
    letter-spacing: 0.28px;
`

const StyledSearchSpan = styled.span`
    max-width: 130px;
    color: #ff6f26;
    font-size: 12px;
    letter-spacing: 0.3px;
    margin-right: 2px;
`

const StyledSearchSpanWord = styled.span`
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
    max-width: 130px;
    color: #ff6f26;
    font-size: 12px;
    letter-spacing: 0.3px;
    margin-right: 0;
`

const StyledSearchListArea = styled.div`
    width: 100%;
    height: 204px;
    margin: 10px 0 12px 0;
    overflow-y: scroll;

    &::-webkit-scrollbar {
        background-color: transparent;
        width: 4px;
    }

    &::-webkit-scrollbar-track {
        background-color: rgba(235, 242, 245, 0.1);
        border-radius: 2px;
    }

    &::-webkit-scrollbar-thumb {
        background-color: #747a8a;
        border-radius: 2px;
    }

    &::-webkit-scrollbar-thumb:hover {
        background-color: #9197a8;
    }
`

const AddrSearchList: React.FC = () => {

    //TODO 나중에 검색값으로 useSelector로 변경하여 리스트가 size가 1개 이상일 경우 active 처리
    // IS OPEN 처리도 따로 해줘야한다.
    const dispatch = useDispatch()

    const word: string = useSelector((state: RootState) => state.view.etc.word)
    const showSearchList: boolean = useSelector((state: RootState) => state.view.etc.showSearchList)
    const addrList: AddrInfo[] = useSelector((state: RootState) => state.server.etc.addrList)

    const selectAddrInfo = useSelector((state: RootState) => state.view.map.selectAddrInfo)


    const [activeId, setActiveId] = useState('')

    useEffect(() => {
        if (addrList.length > 0) {
            dispatch(updateShowSearchList(true))
            //TODO 좌표 이동
            dispatch(changeMapMove({
                latlng: [parseFloat(addrList[0].point.y), parseFloat(addrList[0].point.x)],
                mapMoveType: MapMoveEnum.max_zoom
            }))
            setActiveId(addrList[0].id);
            dispatch(updateSelectAddrInfo(addrList[0]))
        } else {
            dispatch(updateShowSearchList(false))
            dispatch(updateSelectAddrInfo(null))
            setActiveId("");
        }
    }, [addrList])

    useEffect(() => {
        if (selectAddrInfo === null) {
            setActiveId('')
        }

    }, [selectAddrInfo])


    return (
        <>
            <StyledSearchList isShow={addrList.length > 0 && showSearchList}>
                <StyledSearchListHeader>
                    <StyledSearchListTitle>
                        <StyledSearchSpan>'</StyledSearchSpan>
                        <StyledSearchSpanWord>{word}</StyledSearchSpanWord>
                        <StyledSearchSpan>'</StyledSearchSpan>
                        검색결과
                    </StyledSearchListTitle>
                </StyledSearchListHeader>
                <StyledSearchListArea>
                    {
                        addrList.length > 0 &&
                        addrList.map((item) => {
                            return <AddrSearchListItem activeId={activeId} key={item.id} changeActiveId={setActiveId}
                                                       item={item}/>
                        })
                    }

                </StyledSearchListArea>
            </StyledSearchList>
        </>
    )
}

export default AddrSearchList