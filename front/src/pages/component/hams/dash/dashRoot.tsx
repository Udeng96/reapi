import RealTimeArea from "./realTime/realTimeArea";
import DashInfoArea from "./dashInfo/dashInfoArea";
import CctvArea from "./cctv/cctvArea";
import {useSelector} from "react-redux";
import {RootState} from "../../../store/rootStore";
import CctvPopup from "./cctv/cctvPopup";
import React, {useEffect, useState} from "react";
import DoorLockModal from "./realTime/modal/doorLockModal";
import ChartDoorStateModal from "./realTime/modal/chart/chartDoorStateModal";
import {WsDoeInfo} from "../../../../data/interface/hams/hamsInterface";

const DashRoot = () => {

    const [isLock,setIsLock] = useState(false)

    // const activeHamsInfo = useSelector((state:RootState) => state.view.hams.activeHamsInfo)
    const activeHamsInfo = useSelector((state:RootState) => state.server.hams.activeHamsInfo)
    //이벤트로 받은 업데이트 정보
    const wsDoeInfo : WsDoeInfo | null = useSelector((state: RootState) => state.server.hams.wsDoeInfo)


    useEffect(()=>{

        let lock = true
        if (activeHamsInfo){
            if (activeHamsInfo?.status!!.frontDoorLock==="2"){
                lock = false
            }else {
                lock=true
            }
        }else {
            lock=true
        }

        if (wsDoeInfo){
            if (activeHamsInfo?.hamsId ==='HAMS_'+parseInt(wsDoeInfo?.EQ_ID.substring(4))){
                if(wsDoeInfo.FRONT_DOOR_LOCK_S ==='2' ){
                    lock = false
                }else if(wsDoeInfo.FRONT_DOOR_LOCK_S ==='3' ){
                    lock = true
                }else{
                    lock = true
                }
            }
        }

        setIsLock(lock)

        // activeHamsInfo?activeHamsInfo.status!!.frontDoorLock==="2"?setIsLock(false):setIsLock(true):setIsLock(true)
    },[activeHamsInfo, wsDoeInfo])



    return (
        <>
            {/*dashContainer*/}
            <section className="hams dash_container">
                <h2 className={`dash_title state_${activeHamsInfo&&activeHamsInfo!!.status!!.isConnect?'connected':'disconnected'}`}>{activeHamsInfo&&activeHamsInfo.facNm}</h2>
                <DashInfoArea activeItem={activeHamsInfo}/>
                <RealTimeArea/>
                <CctvArea/>
            </section>
            {/*popup & modal*/}
            <CctvPopup item={activeHamsInfo}/>
            <DoorLockModal isLockPopup={!isLock} hamsItem = {activeHamsInfo}/>
            <ChartDoorStateModal/>
        </>
    )

}

export default DashRoot
