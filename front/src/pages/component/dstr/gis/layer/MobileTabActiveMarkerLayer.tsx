import {useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import ReactDOMServer from "react-dom/server";
import {divIcon, LatLng, point} from "leaflet";
import {Marker, Pane} from "react-leaflet";

const MobileTabActiveMarkerLayer = () => {
    const activePastMobileInfo = useSelector((state:RootState)=>state.view.dstrMain.mobile.activePastItem)

    let domHtml = ReactDOMServer.renderToString(
        <div className={`location_icon type_common`}/>
    )

    let icon = divIcon({
        html: domHtml,
        className: `${'active past_mobile'}-marker`,
        iconSize: point(45,62,true),
        iconAnchor: [15,40],
    })

    function setMarker(){
        if(activePastMobileInfo){
            return <Marker
                icon={icon} position={new LatLng(activePastMobileInfo!.lat!!, activePastMobileInfo!.lng!!)}/>
        }else{
            return null
        }
    }

    return(
        <Pane name={'MobileTabActiveMarkerLayer'} style={{zIndex:1004}}>
            {setMarker()}
        </Pane>
    )
}

export default MobileTabActiveMarkerLayer