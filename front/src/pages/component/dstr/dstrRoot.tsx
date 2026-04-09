import React, {useEffect, useState} from "react";
import CctvViewArea from "./right/cctvViewArea";
import EvetArea from "./evet/evetArea";
import GisArea from "./gis/gisArea";
import DstrModal from "./evet/dstrTap/modal/dstrModal";
import * as mobileActions from "../../saga/action/dstr/evet/mobileActions";
import {useDispatch, useSelector} from "react-redux";
import {DTM_OPTION} from "../../../data/const/eventTapConsts";
import {RootState} from "../../store/rootStore";
import {UserData} from "../../../data/interface/user/userInterface";
import {MANAGER_TYPE_CD} from "../../../data/config/config";
import {plusDoFetchPastCount} from "../../store/view/dstr/dstrViewMobileStore";


const DstrRoot:React.FC = () => {


    const dispatch = useDispatch()
    const [isLeftOpen , setLeftOpen] = useState(true)
    const [isRightOpen , setRightOpen] = useState(true)

    const userInfo = useSelector((state:RootState)=>state.server.user.data) as UserData | null


    useEffect(() => {
        if (userInfo){
            if (userInfo.userType === MANAGER_TYPE_CD){
                dispatch(mobileActions.actions.requestMobilePastList({
                    startDtm : DTM_OPTION[3].startDtm,
                    endDtm : DTM_OPTION[3].endDtm,
                    rows : 10,
                    page : 1}))
                dispatch(mobileActions.actions.requestMobileNowList())
            }
            dispatch(plusDoFetchPastCount())
        }


    }, [userInfo]);




    return(
        <section className={"dashWrap"}>
            <DstrModal/>
            <CctvViewArea isRightOpen={isRightOpen} setRightOpen = {setRightOpen}/>
            <EvetArea isLeftOpen={isLeftOpen} setLeftOpen ={setLeftOpen}/>
            <GisArea isLeftOpen={isLeftOpen} isRightOpen ={isRightOpen}/>

        </section>
    )
}

export default DstrRoot