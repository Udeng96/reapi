import React, {useEffect, useState} from "react";

import CctvTabArea from "./cctvTap/cctvArea";
import DstrEvetArea from "./dstrTap/dstrEvetArea";
import MobileEvetArea from "./mobileTap/mobileEvetArea";

import styled from "styled-components"
import * as eventActions from "../../../saga/action/dstr/evet/evet";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../store/rootStore";
import {updateWsEventReceive} from "../../../store/view/dstr/dstrViewEventStore";
import {UserData} from "../../../../data/interface/user/userInterface";
import {MANAGER_TYPE_CD} from "../../../../data/config/config";


const StyledDashContainer = styled.section<{ isOpen: boolean }>`
  width: 533px;
  height: 1024px;
  background-color: #070914;
  border-right: 1px solid #41434c;
  position: absolute;
  bottom: 0;
  left: ${(props) => props.isOpen ? '0' : '-533px'};
  -webkit-transition: left 0.8s ease;
  transition: left 0.8s ease;
  z-index: 3000;
`


const EvetArea = (props: { isLeftOpen: boolean, setLeftOpen: Function }) => {

    const dispatch = useDispatch()
    const payload = useSelector((state:RootState)=>state.view.dstrMain.event.eventPayload)
    const wsEventReceive = useSelector((state:RootState)=>state.view.dstrMain.event.wsEventReceive)

    const userInfo = useSelector((state:RootState)=>state.server.user.data) as UserData | null

    const [tabType, setTabType] = useState('evet')
    useEffect(()=>{
        dispatch(eventActions.actions.requestEvents(payload))
    },[])

    useEffect(() => {
       console.log('userInfo',userInfo)
    }, [userInfo]);


    const changeWsReceive = ()=>{
        dispatch(updateWsEventReceive(!wsEventReceive))
    }


    return (
        <StyledDashContainer isOpen={props.isLeftOpen} className="dash_container">
            <button type="button" className={`btn_tab ${props.isLeftOpen ? '' : 'clicked open_tab'}`}
                    onClick={() => {
                        props.setLeftOpen(!props.isLeftOpen)
                    }}
            />
            {/*dashHeader*/}
            <header className="dash_header">
                <h1 className="dash_title">재난관제<span>Dashboard</span></h1>
                <button type="button" className={`btn_event_receive ${wsEventReceive && 'active'}`} onClick={changeWsReceive}/>
                <div className="btn_menu_tab_group">
                    <button type="button" className={`btn_menu_tab btn_event ${(tabType === 'evet' )&&'active'}`}
                        onClick={e=>setTabType('evet')}>재난이벤트</button>
                    <button type="button" className={`btn_menu_tab btn_cctv ${(tabType === 'cctv' )&&'active'}`}
                            onClick={e=>setTabType('cctv')}>CCTV 검색</button>
                    <button type="button" className={`btn_menu_tab btn_mobile state_video_on ${(tabType === 'mobile' )&&'active'}`}
                            onClick={e=>setTabType('mobile')}>모바일 출동영상</button>
                    {/*{*/}
                    {/*    userInfo && (userInfo.userType === MANAGER_TYPE_CD &&*/}
                    {/*    <button type="button" className={`btn_menu_tab btn_mobile state_video_on ${(tabType === 'mobile' )&&'active'}`}*/}
                    {/*            onClick={e=>setTabType('mobile')}>모바일 출동영상</button>)*/}
                    {/*}*/}
                </div>
            </header>
            {/*dashBody*/}
            <div className="dash_body" >
                {
                    tabType === 'evet' ? <DstrEvetArea /> :
                        tabType === 'cctv' ? <CctvTabArea/> :
                        tabType === 'mobile' ? <MobileEvetArea/> : null
                }
            </div>
        </StyledDashContainer>
    )
}

export default EvetArea