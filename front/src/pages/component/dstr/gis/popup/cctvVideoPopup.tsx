import {Popup} from "react-leaflet";
import PlayerBox from "../../player/PlayerBox";
import {useState} from "react";
import {CctvInfo} from "../../../../../data/interface/cctv/cctvInterface";
import {LatLng} from "leaflet";
import {VIDEO_TYPE} from "../../../../../data/const/const";


const CctvVideoPopup = (props : {item : CctvInfo})=>{

    const [popupPosition, setPopupPosition] = useState(new LatLng(props.item.lat, props.item.lng));

    return (
        <Popup
            position={popupPosition}
            autoPan={ false}
            closeButton={false}
            closeOnClick={false}
            keepInView={false}
            // ref={popupRef}
            autoClose={false}
            // style={{zIndex: 700}}
        >
            <PlayerBox type={props.item.clfyNm.includes("방범")?VIDEO_TYPE.CRIME:VIDEO_TYPE.TRAFFIC} isGisPopup={true} cctvId={props.item.mgrNo} cctvName={props.item.facNm} />
        </Popup>
    )

}

export default CctvVideoPopup