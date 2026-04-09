import {Marker, Pane} from "react-leaflet";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import React from "react";
import L, {LatLng} from "leaflet";
import ReactDOMServer from "react-dom/server";
import {updateActivePastBtnList} from "../../../../store/view/dstr/dstrViewMobileStore";
import {MobileInfo} from "../../../../../data/interface/event/mobileInterface";

const MobilePastMarkers = () => {


    const activePastMobileItem = useSelector((state: RootState) => state.view.dstrMain.mobile.activePastItem)
    const playPastMobileList = useSelector((state: RootState) => state.view.dstrMain.mobile.activePastBtnList)
    const dispatch = useDispatch()

    const renderIcon = (info: MobileInfo, findIndex: number = -1) => {
        return L.divIcon({
            className: 'mobile-past-custom-icon',
            html: ReactDOMServer.renderToString(
                <div className={`mobile_icon type_past_video ${(findIndex) > -1 ? 'active' : ''} ${info.id}`}/>
            ),
            // @ts-ignore
            mobilePastData : {info},
            iconAnchor: [30, 30]
        })
    }

    const markerClick = ( e:any ) =>{

        if(playPastMobileList!==null && playPastMobileList.length!==0){
            dispatch(updateActivePastBtnList(null))
        }else{

            let newActivePlayPastList:Array<MobileInfo> = []
            newActivePlayPastList.push(e.target.options.icon.options.mobilePastData.info)
            dispatch(updateActivePastBtnList(newActivePlayPastList))
        }

    }

    function setMarker() {

        if (playPastMobileList.length !== 0) {

            let marker = <Marker
                icon={renderIcon(playPastMobileList[0], 0)}
                position={new LatLng(playPastMobileList[0].lat!!, playPastMobileList[0].lng!!)}
                //@ts-ignore
                eventHandlers={{
                    click: (e) => {
                        markerClick(e)
                    },
                }}
            />
            return marker
        } else if (activePastMobileItem !== null) {
            let marker =<Marker icon={renderIcon(activePastMobileItem)}
                                position={new LatLng(activePastMobileItem.lat!!, activePastMobileItem.lng!!)}
                                //@ts-ignore
                                eventHandlers={{
                                    click: (e) => {
                                        markerClick(e)
                                    },
                                }}
            />

            // marker.props.click((e: any)=> marekrClick(e))
            return marker
        } else {
            return null
        }
    }

    return (
        <Pane name={'mobilePastMarkers'} style={{zIndex: 1003}}>
            {setMarker()}
            {/*<MobilePastMarkers />*/}
        </Pane>
    )
}

export default MobilePastMarkers