import ChartDoorStateFrame from "./chartDoorStateFrame";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../../../store/rootStore";
import {updateChartPopupOpen} from "../../../../../../store/view/hams/hamsViewStore";
import {useEffect, useState} from "react";

const ChartDoorStateModal = () => {

    const dispatch = useDispatch()
    const isPopupOpen = useSelector((state: RootState) => state.view.hams.chartPopupOpen)
    // const activeHamsInfo = useSelector((state: RootState) => state.view.hams.activeHamsInfo)
    const activeHamsInfo = useSelector((state: RootState) => state.server.hams.activeHamsInfo)


    const [hamsId,setHamsId] = useState<string>("")


    useEffect(()=>{
        activeHamsInfo?
            setHamsId(activeHamsInfo.hamsId!!)
            :setHamsId("HAMS")
    },[activeHamsInfo,isPopupOpen])

    const closePopup = () => {
        dispatch(updateChartPopupOpen(false))
    }

    return (
        <>
            {/*historyPopup*/}
            <section className="hams modal_popup history_popup" id="historyPopup"
                     style={{"display": `${isPopupOpen ? "block" : "none"}`}}>
                <div className="modal_popup_frame">
                    <header className="modal_popup_header">
                        <p className="modal_title">앞문 개폐 이력</p>
                        <button type="button" className="btn_popup_close" onClick={event => closePopup()}/>
                    </header>
                    <div className="modal_popup_body">
                        <ChartDoorStateFrame type={"D"} hamsId={hamsId!!} isOpen={isPopupOpen}/>
                        <ChartDoorStateFrame type={"M"} hamsId={hamsId!!} isOpen={isPopupOpen}/>
                        <ChartDoorStateFrame type={"Y"} hamsId={hamsId!!} isOpen={isPopupOpen}/>
                    </div>
                </div>
            </section>
        </>
    )

}
export default ChartDoorStateModal