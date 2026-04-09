import NowMobileListArea from "./content/nowMobileListArea";
import LastMobileSearchArea from "./content/lastMobileSearchArea";
import LastMobileListArea from "./content/lastMobileList/lastMobileListArea";
import React, {useEffect, useState} from "react";
import {useDispatch} from "react-redux";
import * as mobileActions from "../../../../saga/action/dstr/evet/mobileActions";
import {DTM_OPTION} from "../../../../../data/const/mobileTapConsts";
import {end} from "@popperjs/core";

const MobileEvetArea = () => {

    const dispatch = useDispatch()

    const [startDtm, setStartDtm] = useState<string>(DTM_OPTION[3].startDtm)
    const [endDtm, setEndDtm] = useState<string>(DTM_OPTION[3].endDtm)





    /**
     * 원래는 이탭에서 실시하는것이 맞지만 ,tab 별 데이터는 유지되기때문에 처음 root에서 dispatch를 실시한다.
     */
    // useEffect(() => {
    //     dispatch(mobileActions.actions.requestMobilePastList({
    //             startDtm : startDtm,
    //             endDtm : endDtm,
    //             rows : 10,
    //             page : 1}))
    //     dispatch(mobileActions.actions.requestMobileNowList())
    // }, [])



    return (
        <div className="dash_wrap mobile_video_wrap">
            <NowMobileListArea />
            <div className="divide_area"/>
            <LastMobileSearchArea startDtm={startDtm} setStartDtm={setStartDtm} endDtm={endDtm} setEndDtm={setEndDtm}/>
            <LastMobileListArea startDtm={startDtm} endDtm={endDtm} />
        </div>
    )
}
export default MobileEvetArea