import {useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import ReactDOMServer from "react-dom/server";
import {divIcon, LatLng, point} from "leaflet";
import {Marker, Pane} from "react-leaflet";

const MobileTabLiveActiveMarkerLayer = () => {
    const activeLiveMobileInfo = useSelector((state:RootState)=>state.view.dstrMain.mobile.activeLiveList)

    let domHtml = ReactDOMServer.renderToString(
        <div className={`location_icon type_mobile_live`}/>
    )

    let icon = divIcon({
        html:domHtml,
        className: `${'active live_mobile'}-marker`,
        iconSize : point(45,62,true),
        iconAnchor: [15,40],
    })

    function setMarker(){
        if(activeLiveMobileInfo){
            return <Marker
                icon={icon} position={new LatLng(activeLiveMobileInfo.lat!!,activeLiveMobileInfo.lng!!)}/>
        }else{
            return null
        }
    }

    return(
        <Pane name={'MobileTabLiveActiveMarkerLayer'} style={{zIndex:1004}}>
            {setMarker()}
        </Pane>
    )

}

export default MobileTabLiveActiveMarkerLayer