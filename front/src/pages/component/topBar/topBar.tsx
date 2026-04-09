import React from "react";
import {useSelector} from "react-redux";
import {RootState} from "../../store/rootStore";
import {UserData} from "../../../data/interface/user/userInterface";
import {doLogOut} from "../../saga/apis/user/userApis";

import config from "../../../data/config/config"

const TopBar: React.FC = () => {


    const userInfo:UserData | null = useSelector((state:RootState)=>state.server.user.data)
    const isMonitorMenuOpen = useSelector((state:RootState)=>state.view.monitor.isMenuOpen)

    const moveToOms = ()=>{
        window.location.href = config.PLATFORM.OMS.sysSelView
    }

    return (
        <>

            {/*topBar*/}
            <section className="top_bar" style={{"top":`${isMonitorMenuOpen? "0px" : "-56px"}`}}>
                <div className="logo_img" onClick={moveToOms}/>
                {/*btn logout-*/}
                <button className="btn_logout" onClick={doLogOut}>로그아웃</button>
                {/*user txt info*/}
                <div className="user_txt">
                    <span>{userInfo?userInfo.userName:"관리자"}</span>님 환영합니다
                </div>
            </section>

        </>
    )
}

export default TopBar