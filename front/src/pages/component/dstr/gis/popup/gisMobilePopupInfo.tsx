import React, {useEffect, useState} from "react";
import styled from "styled-components";

import infoOpenBtn from "../../../../../public/static/images/dark/page/btn/btn_thumbnailScreen_up_normal.png"
import infoOpenHoverBtn from "../../../../../public/static/images/dark/page/btn/btn_thumbnailScreen_up_hover.png"
import infoCloseBtn from "../../../../../public/static/images/dark/page/btn/btn_thumbnailScreen_down_normal.png"
import infoCloseHoverBtn from "../../../../../public/static/images/dark/page/btn/btn_thumbnailScreen_down_hover.png"
import cctvFullBtn from "../../../../../public/static/images/dark/page/btn/btn_rightPanel_video_expansion_normal.png"
import cctvFullHoverBtn from "../../../../../public/static/images/dark/page/btn/btn_rightPanel_video_expansion_hover.png"
import {MobileInfo} from "../../../../../data/interface/event/mobileInterface";
import {updateFullPopupInfo} from "../../../../store/view/dstr/dstrViewMobileStore";
import {useDispatch} from "react-redux";
import MobileFullPopup from "./mobileFullPopup/mobileFullPopup";


const StyledVideoInfoArea = styled.div<{ isDefault: boolean, infoOpen:boolean}>`
        width: 100%;
        height: ${({isDefault}) => isDefault ? '108px;' : '76px;'}
        position: absolute;
        bottom: ${({infoOpen,isDefault}) => infoOpen? '0px;' : isDefault ? '-88px;' : '-55px;'}
        left: 0;
        -webkit-transition: bottom 0.2s ease;
        transition: bottom 0.2s ease;  
`

const StyledInfoOpenBtn = styled.div<{infoOpen:boolean}>`
        width: 100%;
        height: 18px;
        -webkit-box-shadow: inset 0px 1px 0 0 rgba(0, 0, 0, 0.75);
                box-shadow: inset 0px 1px 0 0 rgba(0, 0, 0, 0.75);
        background-color: rgba(23, 24, 33, 0.75);
        background-image: ${({infoOpen})=>infoOpen?`url("${infoCloseBtn}")`:`url("${infoOpenBtn}")`};
        background-repeat: no-repeat;
        background-position: center;
        border-top: 1px solid black;
        position: relative;
        overflow: hidden;
        ${({infoOpen})=>infoOpen&&`
        border-top-left-radius: 16px;
        border-top-right-radius: 16px;
        overflow: hidden;
        `}
        
        &:hover{
        background-image : ${({infoOpen})=> infoOpen ? `url("${infoCloseHoverBtn}")` : `url("${infoOpenHoverBtn}")` }
        }
`


const StyledMobileFullBtn =  styled.div<{isDefault:boolean}>`
        width: 20px;
        height: 20px;
        background: url(${cctvFullBtn}) no-repeat;
        position: absolute;
        bottom: ${({isDefault})=>isDefault?'111px;':'79px;'}
        right: 9px;
        z-index: 3000;
        
        &:hover{ background : url(${cctvFullHoverBtn}) no-repeat}
`

const StyledVideoInfoFrame = styled.div<{isDefault:boolean}>`
        width: 100%;
        height: ${({isDefault}) => isDefault ? '88px;' : '58px;'}
        border-top: 1px solid #40424f;
        background-color: #171821;
        padding: ${({isDefault}) => isDefault ? '8px 19px 0 19px;' : '9px 19px 0 17.5px;'}
`

const StyledVideoInfoBlock = styled.div<{isDefault:boolean}>`
        display: -webkit-box;
        display: -ms-flexbox;
        display: flex;
        width: 100%;
        height: ${({isDefault}) => isDefault ? 'auto;' : '12px;'}
        margin-bottom: 8px;
        ${({isDefault}) => isDefault && `
        -webkit-box-orient: vertical;
        -webkit-box-direction: normal;
            -ms-flex-direction: column;
                flex-direction: column;
        `}
        
        &:last-child{ margin-bottom : 0px; }       
`

const StyledVideoInfoTitle = styled.div<{isDefault:boolean}>`
        width: ${({isDefault}) => isDefault ? '100%' : '108px;'}
        font-family: "NotoSansCJKL";
        font-size: 10px;
        letter-spacing: 0.5px;
        color: #aeafb0;
`

const StyledVideoInfoValue = styled.div<{isDefault:boolean}>`
        width: ${({isDefault}) => isDefault ? '100%' : 'calc(100% - 108px);'}
        font-family: "NotoSansCJKR";
        font-size: 11px;
        letter-spacing: 0.28px;
        color: #f7f7f7;
        ${({isDefault}) => isDefault && `
        text-overflow: ellipsis;
        white-space: nowrap;
        overflow: hidden;
        `}
`



const GisMobilePopupInfo = (props:{isDefault:boolean, item : MobileInfo}) => {

    const[infoOpen, setInfoOpen] = useState(false)
    const dispatch = useDispatch()

    const setDateForm = (outbDtm: string) => {

        let year = outbDtm.substring(0, 4)
        let month = outbDtm.substring(4, 6)
        let date = outbDtm.substring(6, 8)
        let hour = outbDtm.substring(8, 10)
        let min = outbDtm.substring(10, 12)
        let second = outbDtm.substring(12, 14)
        return `${year}-${month}-${date} ${hour}:${min}:${second}`
    }

    useEffect(()=>{
        if(props.item!==null){
            console.log("MobileInfo userDtm:",props.item.userDtm)
            console.log("dateForm : ", setDateForm(props.item.userDtm!!))
        }
    },[props.item])

    const clickFullPopupBtn = (item:MobileInfo) => {

        dispatch(updateFullPopupInfo(
            {isOpen:true, dtm:setDateForm(item.userDtm!!),
                userDept:item.userDept, userNm:item.userNm, id:item.id!!, rtsp : item.mbUrl}
        ))

        setInfoOpen(false)
    }

    return(

        <>
            <StyledVideoInfoArea isDefault={props.isDefault} infoOpen={infoOpen}>
                <StyledInfoOpenBtn infoOpen={infoOpen} onClick={e=>{setInfoOpen(!infoOpen)}}/>
                <StyledMobileFullBtn isDefault={props.isDefault} onClick={e=> clickFullPopupBtn(props.item) }/>
                <StyledVideoInfoFrame isDefault={props.isDefault}>
                    <StyledVideoInfoBlock isDefault={props.isDefault}>
                        <StyledVideoInfoTitle isDefault={props.isDefault}>
                            영상 촬영 시작 일시
                        </StyledVideoInfoTitle>
                        <StyledVideoInfoValue isDefault={props.isDefault}>
                            {/*{setDateForm(props.item.userDtm!!)}*/}
                            {props.item.userDtm}
                        </StyledVideoInfoValue>
                    </StyledVideoInfoBlock>
                    <StyledVideoInfoBlock isDefault={props.isDefault}>
                        <StyledVideoInfoTitle isDefault={props.isDefault}>
                            담당자 소속-이름
                        </StyledVideoInfoTitle>
                        <StyledVideoInfoValue isDefault={props.isDefault}>
                            {props.item.userDept} - {props.item.userNm!!}
                        </StyledVideoInfoValue>
                    </StyledVideoInfoBlock>
                </StyledVideoInfoFrame>
            </StyledVideoInfoArea>
        </>


    )

}

export default GisMobilePopupInfo