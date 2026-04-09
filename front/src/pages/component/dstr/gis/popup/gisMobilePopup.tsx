import React, {useEffect, useState} from "react";
import GisMobilePopupInfo from "./gisMobilePopupInfo";
import styled from "styled-components";

import reloadBtn from "../../../../../public/static/images/dark/page/btn/btn_evnetTab_searchList_reload_normal.png"
import reloadHoverBtn from "../../../../../public/static/images/dark/page/btn/btn_evnetTab_searchList_reload_hover.png"
import closeBtn from "../../../../../public/static/images/dark/page/btn/btn_thumbnailScreen_close_normal.png"
import closeHoverBtn from "../../../../../public/static/images/dark/page/btn/btn_thumbnailScreen_close_hover.png"
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import {MobileInfo} from "../../../../../data/interface/event/mobileInterface";
import {updateActiveLiveBtnList, updateActivePastBtnList} from "../../../../store/view/dstr/dstrViewMobileStore";
import Player from "../../player/Player";
import {VIDEO_TYPE} from "../../../../../data/const/const";

// width: ${({isDefault}) => isDefault ? '202px;' : '429px;'}
//height: ${({isDefault}) => isDefault ? '433px;' : '198px;'}
const StyledMobilePopup = styled.div<{ isDefault: boolean }>`
        width: 433px;   
        border-radius: 14px;
        background-color: #171821;
        padding: 2px 2px;
        position: absolute;
        top: 8.5px;
        left: 67.5px;
        overflow: hidden;
    `

const StyledCloseBtn = styled.div`
        width: 20px;
        height: 20px;
        background: url(${closeBtn}) no-repeat;
        position: absolute;
        top: 9px;
        right: 9px;
        z-index: 3000;
        
        &: hover{
        background : url(${closeHoverBtn}) no-repeat;
        }
`

//height: 0;
//padding-bottom: 56.25%;
const StyledVideoIn = styled.div<{ isDefault: boolean }>`
        width: 100%;
        background-color: #807e7b;
        border-radius: 11px;
`

const StyledMobileLoadErrorArea = styled.div<{ isDefault: boolean }>`
    
        width: 100%;
        height: 100%;
        padding-top: ${({isDefault}) => isDefault ? '230px;' : '121px;'}
        position: absolute;
        background-color: #171821;
        top: 0;
        left: 0;
    `

const StyledMobileReloadBtn = styled.div<{ isDefault: boolean }>`
    
        width: 36px;
        height: 36px;
        background: url(${reloadBtn}) no-repeat;
        position: absolute;
        top: ${({isDefault}) => isDefault ? '169px;' : '60.5px;'}
        left: 50%;
        -webkit-transform: translateX(-50%);
            transform: translateX(-50%);
            
        &:hover {
            background : url(${reloadHoverBtn}) no-repeat;
        }
    
    &::after {
        content: "Reload";
        display: block;
        font-family: "NotoSansCJKL";
        font-size: 10px;
         letter-spacing: 0.5px;
        color: #aeafb0;
        position: absolute;
        top: 38px;
        left: 0;
    }
    `

const StyledMobileErrorMessage = styled.div<{ isDefault: boolean }>`
    
        font-family: "NotoSansCJKL";
        font-size: 12px;
        text-align: center;
        color: #9698a3;
        ${({isDefault}) => isDefault && `
        display: -webkit-box;
        display: -ms-flexbox;
        display: flex;
        -webkit-box-orient: vertical;
        -webkit-box-direction: normal;
            -ms-flex-direction: column;
                flex-direction: column;
        `
}       
    `

const GisMobilePopup = (props: { isLive: boolean, item: MobileInfo }) => {


    const dispatch = useDispatch()
    const playLiveMobileList = useSelector((state: RootState) => state.view.dstrMain.mobile.activeLiveBtnList)
    const playPastMobileList = useSelector((state: RootState) => state.view.dstrMain.mobile.activePastBtnList)
    const playerSize = useSelector((state: RootState) => state.view.dstrMain.mobile.mobilePopupSize)
    const playerHeight = playerSize.height
    const playerWidth = playerSize.width
    const [isDefault, setIsDefault] = useState<boolean>(true)
    let isError = false


    useEffect(() => {
        if (playerHeight === 0 || playerWidth === 0) {
            setIsDefault(true)
        } else {
            setIsDefault(false)
        }
    }, [playerSize])

    useEffect(() => {

        isError = props.isLive ? playLiveMobileList === null && true : playPastMobileList === null && true

    }, [playLiveMobileList, playPastMobileList])


    const closeMobilePopup = (isLive: boolean) => {

        let newPlayLiveMobileList: Array<MobileInfo> = []
        let liveIndex = -1
        let newPlayPastMobileList: Array<MobileInfo> = []
        let pastIndex = -1

        if (isLive) {
            newPlayLiveMobileList = [...playLiveMobileList]
            playLiveMobileList &&
            playLiveMobileList.map((item, index) => {
                if (item.id === props.item.id) liveIndex = index
            })


            newPlayLiveMobileList.splice(liveIndex, 1)

            dispatch(updateActiveLiveBtnList(newPlayLiveMobileList))

        } else {
            playPastMobileList &&
            playPastMobileList.map((item, index) => {
                newPlayPastMobileList.push(item);
                if (item.seq === props.item.seq) pastIndex = index
            })

            newPlayPastMobileList.splice(pastIndex, 1)
            dispatch(updateActivePastBtnList(newPlayPastMobileList))
        }
    }


    return (
        <StyledMobilePopup isDefault={isDefault}>
            <StyledCloseBtn onClick={e => closeMobilePopup(props.isLive)}/>
            <StyledVideoIn isDefault={isDefault}>
                <Player
                        // cctvId={props.item.id!!}
                        cctvId={props.item.id!!}
                        type={VIDEO_TYPE.MOBILE}
                        rtsp={props.item.mbUrl!!}
                />
            </StyledVideoIn>
            {/*<StyledMobileLoadErrorArea isPortrait={isPortrait}>*/}
            {/*    <StyledMobileReloadBtn isPortrait={isPortrait}/>*/}
            {/*    <StyledMobileErrorMessage isPortrait={isPortrait}>*/}
            {/*        영상 로드에&nbsp;<span>문제가 발생하였습니다.</span>*/}
            {/*    </StyledMobileErrorMessage>*/}
            {/*</StyledMobileLoadErrorArea>*/}
            <GisMobilePopupInfo isDefault={isDefault} item={props.item}/>


        </StyledMobilePopup>
    )

}

GisMobilePopup.defaultProps = {
    isLive: false,
    item: {},
}

export default GisMobilePopup