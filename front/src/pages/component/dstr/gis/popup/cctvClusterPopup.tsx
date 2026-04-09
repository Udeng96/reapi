import React, {useEffect, useState} from "react";
import {Popup} from "react-leaflet";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import {updateActiveClusterLatLon} from "../../../../store/view/dstr/dstrViewCctvStore";
import {CctvInfo} from "../../../../../data/interface/cctv/cctvInterface";
import {LatLngTuple} from "leaflet";
import CctvClusterPopupItem from "./cctvClusterPopupItem";


const CctvClusterPopup = () => {

    const dispatch = useDispatch();

    const activeClusterLatLon = useSelector((state: RootState) => state.view.dstrMain.cctv.activeClusterLatLon);
    const activeClusterMarkerList = useSelector((state: RootState) => state.view.dstrMain.cctv.activeClusterMarkerList);

    const [open, changeOpen] = useState(false);
    const [cctvList, setCctvList] = useState([]);

    const playGisCctvList = useSelector((state: RootState) => state.view.dstrMain.cctv.playGisCctvList)

    useEffect(() => {

        if (activeClusterLatLon && activeClusterMarkerList.length > 0) {
            if (activeClusterLatLon[0] !== 0 && activeClusterLatLon[1] !== 0 &&
                activeClusterLatLon[0] !== 99999999 && activeClusterLatLon[1] !== 99999999) {
                changeOpen(true);
            } else {
                changeOpen(false)
            }
        } else {
            changeOpen(false)
        }

    }, [activeClusterLatLon,activeClusterMarkerList]);





    return (
        open ? <Popup
            autoClose={false}
            closeButton={false}
            position={activeClusterLatLon as LatLngTuple}>

            <div className="gis_cluster_popup">

                <header className="gis_cluster_popup_header">
                    <p className="gis_cluster_popup_title">CCTV 선택</p>
                    <button type="button" className="btn_cluster_popup_close"
                            onClick={() => {
                                dispatch(updateActiveClusterLatLon(null))//click 된 cluster, 기본 cctv 전부 초기화
                            }}
                    />
                </header>
                <div className="gis_cluster_popup_body">
                    <ul className="cluster_list">
                        {
                            activeClusterMarkerList.map((cctv: CctvInfo) => {
                                return (<CctvClusterPopupItem cctv={cctv} key={`CCTV_CLUSTER_POPUP_${cctv.facId}`}/>)
                            })
                        }
                    </ul>
                </div>
            </div>
        </Popup>
            :
            <></>
    )
}

export default CctvClusterPopup;