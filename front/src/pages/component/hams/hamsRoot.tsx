import React, {useEffect, useState} from "react";
import MenuArea from "./menu/menuArea";
import GisArea from "./gis/gisArea";
import DashRoot from "./dash/dashRoot";
import {useSelector} from "react-redux";
import {RootState} from "../../store/rootStore";

const HamsRoot = () => {

    const loadingPopup = useSelector((state:RootState)=>state.view.hams.isLoading) //  첫 렌더링 로딩
    const doorLockLoadingPopup = useSelector((state:RootState)=>state.view.hams.isDoorLockLoading) // Lock 및 unLock 작업 시 로딩
    const menuLoadingPopup = useSelector((state:RootState)=>state.view.hams.isHamsMenuLoading); // 메뉴 클릭 시 로딩 화면
    const [isLoading, setIsLoading] = useState(true);
    const [isDoorLockLoading,setIsDoorLockLoading] = useState(false);
    useEffect(()=>{
        setIsLoading(loadingPopup);
    },[loadingPopup])

    useEffect(()=>{
        setIsDoorLockLoading(doorLockLoadingPopup)
    },[doorLockLoadingPopup])

    return(
        <div className={"hams dashWrap"}>
            <div className="loading_img" style={{"display":`${isLoading?"inline-block":"none"}`}}/>
            <div className="loading_img" style={{"display":`${isDoorLockLoading?"inline-block":"none"}`}}/>
            <div className={"loading_img"} style={{"display": `${menuLoadingPopup?"inline-block":"none"}`}}/>
            <MenuArea/>
            <GisArea/>
            <DashRoot/>
        </div>
    )
}

export default HamsRoot