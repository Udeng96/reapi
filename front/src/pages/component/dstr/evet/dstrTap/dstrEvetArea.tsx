import React, {useEffect, useState} from "react";
import DstrListArea from "./content/dstrListArea/dstrListArea";
import DstrDetailArea from "./content/dstrDetailArea/dstrDetailArea";
import {useDispatch, useSelector} from "react-redux";
import {Search} from "../../../../../data/interface/event/eventInterface";
import DstrSearchArea from "./content/dstrSearchArea/dstrSearchArea";
import {RootState} from "../../../../store/rootStore";

// @ts-ignore
import alarm from "../../../../../public/static/alarm/alarm.mp3";


const DstrEvetArea = () => {

    const dispatch = useDispatch()
    const checkedEventList = useSelector((state: RootState) => state.view.dstrMain.event.checkedTypeList)
    const checkedLocaList = useSelector((state: RootState) => state.view.dstrMain.event.checkedLocaList)
    const payload = useSelector((state: RootState) => state.view.dstrMain.event.eventPayload)

    let [chkEvent, setChkEvent] = useState(checkedEventList)
    let [chkLoca, setChkLoca] = useState(checkedLocaList)


    const setChkList = (chkEvent: Array<Search>, chkLoca: Array<Search>) => {

        setChkEvent(chkEvent)
        setChkLoca(chkLoca)



    }

    const [aPlay, setAPlay] = useState(false);
    useEffect(() => {
        setAPlay(true)
    }, [chkEvent])


    return (
        <>
            {/*<ReactPlayer*/}
            {/*    url={alarm}*/}
            {/*    playing={aPlay}*/}
            {/*    muted={true}*/}
            {/*    controls={true}*/}
            {/*    width={"400px"}*/}
            {/*    height={"50px"}*/}
            {/*/>*/}
            <div className="dash_wrap disaster_event_wrap" id="tab1">
                <DstrSearchArea chkEventList={checkedEventList} chkLocaList={checkedLocaList} setChkList={setChkList}/>
                <DstrListArea chkEventList={checkedEventList} chkLocaList={checkedLocaList}/>
                <DstrDetailArea/>
            </div>
        </>

)
}
export default DstrEvetArea