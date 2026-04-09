import {useEffect, useState} from "react";
import {useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import MobileFullPopup from "../popup/mobileFullPopup/mobileFullPopup";

const MobileFullPopupLayer = () => {

    const [isFullOpen,setIsFullOpen] = useState(false)
    const fullPopupInfo = useSelector((state:RootState) => state.view.dstrMain.mobile.fullPopupInfo)

    useEffect(()=>{
        setIsFullOpen(fullPopupInfo!!.isOpen)
    },[fullPopupInfo])

    return(
        <MobileFullPopup isFullOpen={isFullOpen} mobileId={fullPopupInfo!!.id!!} rtsp={fullPopupInfo!!.rtsp ?? ""}/>
    )
}
export default MobileFullPopupLayer