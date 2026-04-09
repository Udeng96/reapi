

import fullPopupInfoOpenBtn from "../../../../../../public/static/images/dark/page/btn/btn_fullScreen_up_normal.png"
import fullPopupInfoOpenHoverBtn from "../../../../../../public/static/images/dark/page/btn/btn_fullScreen_up_hover.png"
import fullPopupInfoCloseBtn  from "../../../../../../public/static/images/dark/page/btn/btn_fullScreen_down_normal.png"
import fullPopupInfoCloseHoverBtn from "../../../../../../public/static/images/dark/page/btn/btn_fullScreen_down_hover.png"
import styled from "styled-components";
import {useEffect, useState} from "react";
import {useSelector} from "react-redux";
import {RootState} from "../../../../../store/rootStore";

const StyledMobileFullPopupInfoArea  = styled.div<{isDefault:boolean, fullInfoOpen:boolean}>`
  width: 100%;
  height: ${({isDefault})=>isDefault?'164px;':'112px;'}
  position: absolute;
  bottom: ${({fullInfoOpen, isDefault})=>fullInfoOpen? '0px;' :isDefault?'-142px;':'-90px;'}
  left: 0;
  -webkit-transition: bottom 0.2s ease;
  transition: bottom 0.2s ease;
`

const StyledMobileFullPopupInfoFrame = styled.div<{isDefault:boolean}>`
  width: 100%;
  height: ${({isDefault})=>isDefault?'142px;':'90px;'}
  background-color: #171821;
  padding: ${({isDefault})=>isDefault?'19px 30px 0 30px;':'21px 46.5px 0 46.5px;'}
`

const StyledMobileFullPopupInfoBlock = styled.div<{isDefault:boolean}>`
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  width: 100%;
  height: ${({isDefault}) => isDefault ? 'auto;' : '17px;'}
  margin-bottom: ${({isDefault}) => isDefault ? '17px;' : '15px;'}
   ${({isDefault}) => isDefault && `
        -webkit-box-orient: vertical;
        -webkit-box-direction: normal;
            -ms-flex-direction: column;
                flex-direction: column;
        `}
   &:last-child{ margin-bottom : 15px; } 
`

const StyledMobileFullPopupTitle = styled.div<{isDefault:boolean}>`
  width: ${({isDefault}) => isDefault ? '100%' : '162.5px;'}
  font-family: "NotoSansCJKL";
  font-size: 17px;
  letter-spacing: 0.43px;
  color: #c7c8c9;
  line-height: 1;
  ${({isDefault}) => isDefault && `
  margin-bottom: 6px;
  `}
`

const StyledMobileFullPopupValue = styled.div<{isDefault:boolean}>`
  width: ${({isDefault}) => isDefault ? '100%' : 'calc(100% - 162.5px);'}
  font-family: "NotoSansCJKR";
  font-size: 17px;
  letter-spacing: 0.43px;
  color: #f7f7f7;
  line-height: 1;
    ${({isDefault}) => isDefault && `
    text-overflow : ellipsis;
    white-space : nowrap;
    overflow: hidden;
  `}
`

const StyledMobileFullPopupInfoOpenBtn = styled.div<{isDefault:boolean, isFullInfoOpen:boolean}>`
  width: 100%;
  height: 22px;
  -webkit-box-shadow: inset 0px 1px 0 0 rgba(0, 0, 0, 0.75);
          box-shadow: inset 0px 1px 0 0 rgba(0, 0, 0, 0.75);
  background-color: rgba(23, 24, 33, 0.75);
  background-image: ${({isFullInfoOpen})=>isFullInfoOpen?`url("${fullPopupInfoCloseBtn}")`:`url("${fullPopupInfoOpenBtn}")`};
  background-repeat: no-repeat;
  background-position: center;
  border-top: 1px solid black;
  border-top-left-radius: 16px;
  border-top-right-radius: 16px;
  cursor : pointer;

  &:hover{
      background-image : ${({isFullInfoOpen})=> isFullInfoOpen ? `url("${fullPopupInfoCloseHoverBtn}")` : `url("${fullPopupInfoOpenHoverBtn}")` }
  }

`

const MobileFullPopupInfo = (props:{isDefault:boolean}) => {

    const [isFullInfoOpen,setIsFullInfoOpen] = useState(false)
    const fullPopupInfo = useSelector((state:RootState)=>state.view.dstrMain.mobile.fullPopupInfo)

    useEffect(()=>{
        setIsFullInfoOpen(false)
    },[fullPopupInfo])

    return (


        <StyledMobileFullPopupInfoArea isDefault={props.isDefault} fullInfoOpen={isFullInfoOpen}>
            <StyledMobileFullPopupInfoOpenBtn isFullInfoOpen={isFullInfoOpen} isDefault={props.isDefault} onClick={event => setIsFullInfoOpen(!isFullInfoOpen)}/>
            <StyledMobileFullPopupInfoFrame isDefault={props.isDefault}>
                <StyledMobileFullPopupInfoBlock isDefault={props.isDefault}>
                    <StyledMobileFullPopupTitle isDefault={props.isDefault}>영상 촬영 시작 일시</StyledMobileFullPopupTitle>
                    <StyledMobileFullPopupValue isDefault={props.isDefault}>{fullPopupInfo!!.dtm}</StyledMobileFullPopupValue>
                </StyledMobileFullPopupInfoBlock>
                <StyledMobileFullPopupInfoBlock isDefault={props.isDefault}>
                    <StyledMobileFullPopupTitle isDefault={props.isDefault}>담당자 소속-이름</StyledMobileFullPopupTitle>
                    <StyledMobileFullPopupValue isDefault={props.isDefault}>{fullPopupInfo!!.userDept}-{fullPopupInfo!!.userNm}</StyledMobileFullPopupValue>
                </StyledMobileFullPopupInfoBlock>
            </StyledMobileFullPopupInfoFrame>
        </StyledMobileFullPopupInfoArea>

    )
}

export default MobileFullPopupInfo