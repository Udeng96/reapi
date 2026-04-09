import React from "react";
import FullScreenIcon from "../right/Modal/fullScreenIcon";
import styled from "styled-components"


import gisPopupTitleImg from "../../../../public/static/images/dark/page/icon/icon_cctv_pop_header.png"

import closeBtn from "../../../../public/static/images/dark/page/btn/btn_gis_cctv_pop_close_normal.png"
import closeHoverBtn from "../../../../public/static/images/dark/page/btn/btn_gis_cctv_pop_close_hover.png"


import fullBtn from "../../../../public/static/images/dark/page/btn/btn_rightPanel_video_expansion_normal.png"
import fullHoverBtn from "../../../../public/static/images/dark/page/btn/btn_rightPanel_video_expansion_hover.png"
import reloadBtn from "../../../../public/static/images/dark/page/btn/btn_evnetTab_searchList_reload_normal.png"
import reloadHoverBtn from "../../../../public/static/images/dark/page/btn/btn_evnetTab_searchList_reload_hover.png"
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../store/rootStore";
import {updateFullScreenId, updatePlayGisCctvList} from "../../../store/view/dstr/dstrViewCctvStore";
import {CctvInfo} from "../../../../data/interface/cctv/cctvInterface";
import _ from "underscore";

import videoIcon from "../../../../public/static/images/dark/page/icon/icon_video_nothing.png"
import {EMPTY_PLAYER_PREFIX, PLAYER_ID_PREFIX, VIDEO_TYPE} from "../../../../data/const/const";
import Player from "./Player";

interface Props {
    isGisPopup: boolean,
    cctvName: string,
    cctvId: string,
    no: number,
    isSmall: boolean,
    type : VIDEO_TYPE
}

const StyledCctvVideoBoxFrame = styled.div<{ isGisPopup: boolean, isSmall: boolean }>`
  width: ${({isGisPopup, isSmall}) => isGisPopup ? '320px' : isSmall ? '240px' : '100%'};
  height: ${({isGisPopup, isSmall}) => isGisPopup ? '203px' : isSmall ? '158px' : '294px'};

  ${({isGisPopup}) => isGisPopup ?
    'position: absolute;  top: 6px; left: 63px;' : ''}

  ${({isGisPopup, isSmall}) => !isGisPopup ? isSmall ? 'border-bottom: transparent;  margin-bottom: 2px;' :
    'border-bottom: 1px solid #525366;  margin-bottom: 4px;' : ''}
  
  
}

`
const StyledCctvVideoHeader = styled.header<{ isGisPopup: boolean }>`
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  ${({isGisPopup}) => isGisPopup ?
    '-webkit-box-pack: justify; -ms-flex-pack: justify; justify-content: space-between;' : ''}


  width: 100%;
  height: 23px;
  background-color: #303140;
  padding: 0 ${({isGisPopup}) => isGisPopup ? '9' : '8'}px;
`


const StyledGisCctvPopupTitle = styled.p`
  width: 93%;
  font-family: "NotoSansCJKR";
  font-size: 11px;
  letter-spacing: 0.28px;
  color: #f7f7f7;
  padding-left: 19.5px;
  position: relative;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
  
  &::before{
  content: "";
  display: block;
  width: 16px;
  height: 15px;
  background: url(${gisPopupTitleImg}) no-repeat;
  position: absolute;
  top: 0;
  left: 0;
  }
`

const StyledGisCctvCloseBtn = styled.button`
  width: 17px;
  height: 17px;
  background: url(${closeBtn}) no-repeat;
  
  
  &:hover{
    background: url(${closeHoverBtn}) no-repeat;
  }
`

const StyledRightCctvTitleNumber = styled.p`
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  -webkit-box-pack: center;
  -ms-flex-pack: center;
  justify-content: center;
  width: 15px;
  height: 15px;
  border-radius: 3px;
  background-color: #f03a46;
  margin-right: 4.5px;
  font-family: "NotoSansCJKM";
  font-size: 12px;
  color: #fff;
`

const StyledRightCctvTitle = styled.p`
  width: 100%;
  font-family: "NotoSansCJKR";
  font-size: 11px;
  letter-spacing: 0.28px;
  color: #f7f7f7;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
`

const StyledVideoBody = styled.div<{isGisPopup: boolean,isSmall:boolean , isImgShow:boolean}>`
  width: 100%;
  height: calc(100% - 23px);
  background-color: #070914;
  ${({isImgShow})=>isImgShow &&`background-image: url(${ videoIcon});`}   
  background-repeat: no-repeat;
  background-position: center 114px;
  position: relative;
  
  ${({isGisPopup,isSmall}) => ((isGisPopup||isSmall) && 'background-image : none')}
  
`

const StyledCctvIn = styled.div`
  width: 100%;
  height: 100%;
  z-index: 1000;
`

const StyledCctvFullBtn = styled.button<{ isGisPopup: boolean }>`
  width: 20px;
  height: 20px;
  background: url(${fullBtn}) no-repeat;
  position: absolute;
  bottom: 7px;
  right: 7px;
  z-index: 4000;
  
  &:hover{
    background: url(${fullHoverBtn}) no-repeat;
  }
`

const StyledCctvLoadErrorArea = styled.div<{ isGisPopup: boolean, isSmall: boolean }>`
  width: 100%;
  height: 100%;
  padding-top: 121px;
  position: absolute;
  top: ${({isGisPopup, isSmall}) => isGisPopup ? '0' : isSmall ? '-1vh' : '4vh'};
  left: 0;
`

const StyledReloadBtn = styled.button`
  width: 36px;
  height: 36px;
  background: url(${reloadBtn}) no-repeat;
  position: absolute;
  top: 48px;
  left: 50%;
  -webkit-transform: translateX(-50%);
  transform: translateX(-50%);

  &:hover {
    background: url(${reloadHoverBtn}) no-repeat;
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

const StyledCctvLoadErrorMessage = styled.p<{ isGisPopup: boolean }>`
  font-family: "NotoSansCJKL";
  font-size: 12px;
  text-align: center;
  color: #9698a3;
  
  ${({isGisPopup}) => isGisPopup && 'margin : 0 !important;'}
`


const StyledNoCctvMessage = styled.p<{ isGisPopup: boolean, isSmall:boolean }>`
  width: 100%;
  font-family: "NotoSansCJKL";
  font-size: 11px;
  letter-spacing: 0.28px;
  text-align: center;
  color: #898c8f;
  position: absolute;
  top: 148px;
  
  ${({isSmall})=>isSmall && `top: 50%;
-webkit-transform: translateY(-50%);
transform: translateY(-50%);`}
  ${({isGisPopup}) => isGisPopup && 'margin : 0 !important;'}
`


const PlayerBox = (props: Props) => {

    const dispatch = useDispatch()

    const fullScreenId = useSelector((state:RootState) => state.view.dstrMain.cctv.fullScreenId)
    const playGisCctvList = useSelector((state: RootState) => state.view.dstrMain.cctv.playGisCctvList)
    /**
     * GIS 상에서만 사용될 close 버튼 event
     */
    const closeClickEvent = () => {
        let newPlayCctvList = playGisCctvList ? [...playGisCctvList] : []

        const index = _.findIndex(newPlayCctvList, {
            mgrNo: props.cctvId
        })

        if (index !== -1) {
            newPlayCctvList.splice(index, 1)
            dispatch(updatePlayGisCctvList(newPlayCctvList as CctvInfo[] | null))
        }
    }



    const fullScreenBtnEvent = ()=>{
        if (fullScreenId !== '') {//떠있는경우에만 하는것인데
            if (document.fullscreenElement === null) {
                dispatch(updateFullScreenId(`${PLAYER_ID_PREFIX}${props.cctvId}`))
                // @ts-ignore
                document.getElementById(`${PLAYER_ID_PREFIX}${props.cctvId}`).requestFullscreen()
            } else {
                dispatch(updateFullScreenId(''))
                document.exitFullscreen();
            }
        } else {
            dispatch(updateFullScreenId(`${PLAYER_ID_PREFIX}${props.cctvId}`))
            // @ts-ignore
            document.getElementById(`${PLAYER_ID_PREFIX}${props.cctvId}`).requestFullscreen()
        }
    }



    return (
        <StyledCctvVideoBoxFrame isGisPopup={props.isGisPopup} isSmall={props.isSmall}>
            <StyledCctvVideoHeader isGisPopup={props.isGisPopup}>

                {
                    props.isGisPopup ?
                        (
                            <>
                                <StyledGisCctvPopupTitle>{props.cctvName}</StyledGisCctvPopupTitle>
                                <StyledGisCctvCloseBtn onClick={e => {
                                    e.stopPropagation()
                                    closeClickEvent()
                                }}/>
                            </>
                        )
                        :
                        (
                            <>
                                <StyledRightCctvTitleNumber>{props.no}</StyledRightCctvTitleNumber>
                                <StyledRightCctvTitle>{props.cctvName}</StyledRightCctvTitle>
                            </>
                        )
                }
            </StyledCctvVideoHeader>

            {/*todo: 얘도 코드 더 줄일 수 있는지 고민하기*/}
            <StyledVideoBody isGisPopup={props.isGisPopup} isSmall={props.isSmall} isImgShow={props.cctvId.startsWith(EMPTY_PLAYER_PREFIX)}>
                {
                    props.cctvName !=='선택없음' &&
                    <StyledCctvIn>
                        <Player cctvId={props.cctvId} type={props.type}/>
                    </StyledCctvIn>
                }
                {
                    props.type === (VIDEO_TYPE.CRIME || VIDEO_TYPE.TRAFFIC) &&
                    <StyledCctvFullBtn isGisPopup={true} onClick={fullScreenBtnEvent}/>
                }
                {
                    props.isGisPopup?
                        !(playGisCctvList!==null && playGisCctvList!.length!==0)&&
                            <StyledCctvLoadErrorArea isGisPopup={props.isGisPopup} isSmall={props.isSmall}>
                                <StyledReloadBtn/>
                                <StyledCctvLoadErrorMessage isGisPopup={props.isGisPopup}>
                                    영상 로드에 문제가 발생하였습니다.
                                </StyledCctvLoadErrorMessage>
                            </StyledCctvLoadErrorArea>
                        :
                        props.cctvName === '선택없음' &&
                        <StyledNoCctvMessage isGisPopup={props.isGisPopup} isSmall={props.isSmall}>표출가능 영상이 없습니다.</StyledNoCctvMessage>
                }
                {
                    !props.cctvId.startsWith(EMPTY_PLAYER_PREFIX) &&
                    <FullScreenIcon clickFn={fullScreenBtnEvent}/>
                }
            </StyledVideoBody>
        </StyledCctvVideoBoxFrame>
    )
}


PlayerBox.defaultProps = {
    isGisPopup: false,
    cctvName: '선택없음',
    no: 1,
    isSmall: false,
    type : VIDEO_TYPE.CRIME,
    rtsp : ''
}

export default PlayerBox