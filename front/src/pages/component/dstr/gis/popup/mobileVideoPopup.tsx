import {MobileInfo} from "../../../../../data/interface/event/mobileInterface";
import {Popup} from "react-leaflet";
import {LatLng} from "leaflet";
import {useEffect, useState} from "react";
import GisMobilePopup from "./gisMobilePopup";

const MobileVideoPopup = (props: { item: MobileInfo, isLive: boolean }) => {

    const [popupPosition, setPopupPosition] = useState(new LatLng(0, 0))

    useEffect(() => {
        if (props.item !== null) {
            setPopupPosition(new LatLng(props.item.lat!!, props.item.lng!!))
        }
    }, [props.item])


    return (
        <Popup
            position={popupPosition}
            autoPan={false}
            closeButton={false}
            closeOnClick={false}
            keepInView={false}
            autoClose={false}>
            <GisMobilePopup isLive={props.isLive} item={props.item!!}/>
        </Popup>

    )

}
export default MobileVideoPopup