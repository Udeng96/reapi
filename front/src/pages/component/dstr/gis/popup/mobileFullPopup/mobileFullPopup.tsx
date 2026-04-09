import React, {useEffect, useState} from "react";
import styled from "styled-components";

import fullPopupCloseBtn from "../../../../../../public/static/images/dark/page/btn/btn_fullScreen_close_normal.png"
import fullPopupVideoIn from "../../../../../../public/static/images/dark/page/img/img_sample.png"
import anotherVideoIn from "../../../../../../public/static/images/dark/page/img/KakaoTalk_20211010_152152321.png"
import fullPopupCloseHoverBtn from "../../../../../../public/static/images/dark/page/btn/btn_fullScreen_close_hover.png"

import MobileFullPopupInfo from "./mobileFullPopupInfo";
import {useDispatch, useSelector} from "react-redux";
import {updateFullPopupInfo} from "../../../../../store/view/dstr/dstrViewMobileStore";
import {VIDEO_TYPE} from "../../../../../../data/const/const";
import Player from "../../../player/Player";
import {RootState} from "../../../../../store/rootStore";

//height: 100%;
//padding: ${({isDefault})=>isDefault? '0 711px;':'97px 0;'}
const StyledMobileFullPopup = styled.div<{isDefault:boolean, isFullOpen:boolean}>`
  width: 100%;
  background-color: #000;
  position: absolute;
  top: 0;
  left: 0;
  z-index: 5000;
  display: ${({isFullOpen})=>isFullOpen? 'block;' : 'none;'}
`

// display: ${({isFullOpen})=>isFullOpen? 'block;' : 'none;'}
//height: 100%;
const StyledMobileFullPopupFrame = styled.div<{isDefault:boolean}>`
  width: 100%;
  height : 100%;
  position: relative;
  overflow: hidden;
`

const StyledMobileFullPopupCloseBtn = styled.div`
  width: 40px;
  height: 40px;
  background: url(${fullPopupCloseBtn}) no-repeat;
  position: absolute;
  top: 16px;
  right: 16px;
  cursor : pointer;
  z-index :5001;
  
  &:hover{
  background: url(${fullPopupCloseHoverBtn}) no-repeat;
  }
`

// background: url(${fullPopupVideoIn}) no-repeat;
const StyledMobileFullPopupVideoIn = styled.div`
  width: 100%;
  height: 100%;

`

const MobileFullPopup = (props:{isFullOpen:boolean, mobileId:string ,rtsp:string}) => {

    const dispatch = useDispatch()

    const playerSize = useSelector((state:RootState)=>state.view.dstrMain.mobile.mobilePopupSize)
    const playerHeight = playerSize.height
    const playerWidth = playerSize.width
    const [isDefault, setIsDefault] = useState<boolean>(true)


    const clickFullCloseBtn = (e:any) => {
        e.stopPropagation()
        dispatch(updateFullPopupInfo({isOpen:false, dtm: null, userDept: null, userNm : null, id:"", rtsp:""}))
    }

    useEffect(()=>{
        if(playerHeight === 0 || playerWidth ===0){
            setIsDefault(true)
        }else{
            setIsDefault(false)
        }
    },[playerSize])

    return(
        <>
            <StyledMobileFullPopup isDefault={isDefault} isFullOpen={props.isFullOpen}>
                <StyledMobileFullPopupFrame isDefault={isDefault}>
                    <StyledMobileFullPopupCloseBtn onClick={e=>clickFullCloseBtn(e)}/>
                    <StyledMobileFullPopupVideoIn>
                        <Player
                            cctvId={'FULL_'+props.mobileId!!}
                            type={VIDEO_TYPE.MOBILE}
                            rtsp={props.rtsp}/>
                    </StyledMobileFullPopupVideoIn>
                    <MobileFullPopupInfo isDefault={isDefault} />

                </StyledMobileFullPopupFrame>
            </StyledMobileFullPopup>
        </>



    )
}

export default MobileFullPopup