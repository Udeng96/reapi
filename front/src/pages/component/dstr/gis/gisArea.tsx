import React, {useEffect, useState} from "react";
import GisSettingArea from "./gisSettings/gisSettingArea";
import GisLegendBox from "./gisLegendBox";
import AddrSearchArea from "./addrSearch/AddrSearchArea";
import GisFuncIconArea from "./funcGroup/gisFuncIconArea";
import L, {LatLng, LatLngTuple} from "leaflet"
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../store/rootStore";
import {MapContainer, Pane, TileLayer} from "react-leaflet";
import {ResizeMap} from "./handler/ResizeMap"
import MapControl from "./handler/MapControl";
import {TileMapData} from "../../../../data/config/Map";
import BaseMapLayer from "./layer/BaseLayer";


import 'leaflet/dist/leaflet.css';
import 'react-leaflet-markercluster/dist/styles.min.css';
import AddressMakerLayer from "./layer/AddressMakerLayer";
import HeatmapLayer from "./layer/HeatmapLayer";
import MapDrawLayer from "./layer/MapDrawLayer";

import * as actions from "../../../saga/action/dstr/cctv/cctvActions"
import CctvClusterMarkers from "./marker/CctvClusterMarkers";
import CctvClusterPopup from "./popup/cctvClusterPopup";
import CctvPopupLayer from "./layer/CctvPopupLayer";
import CctvTabActiveMarkerLayer from "./layer/CctvTabActiveMarkerLayer";
import MobileTabActiveMarkerLayer from "./layer/MobileTabActiveMarkerLayer";
import MobileTabLiveActiveMarkerLayer from "./layer/MobileTabLiveActiveMarkerLayer";
import MobileLiveMarkers from "./marker/MobileLiveMarkers";
import MobilePastMarkers from "./marker/MobilePastMarkers";
import MobilePopupLayer from "./layer/MobilePopupLayer";
import SpiderLayer from "./layer/SpiderLayer";
import {MapEventMarkerType} from "../../../../data/const/mapConst";
import {VwEvent} from "../../../../data/interface/event/eventInterface";
import EventMarker from "./marker/EventMarker";
import MobileFullPopupLayer from "./layer/mobileFullPopupLayer";
import MapDraw from "./layer/MapDraw";


const GisArea = (props: { isLeftOpen: boolean, isRightOpen: boolean }) => {
        //오른쪽 열린것에 대한 값을 받아서 내려줘야한다.
        const dispatch = useDispatch()


        const [cluster, setCluster] = useState(null)
        const [center, setCenter] = useState(new LatLng(37.199782, 126.830556))
        const [mobilePastCluster, setMobilePastCluster] = useState(null)



        const mapZoomLevel = useSelector((state: RootState) => state.view.map.zoomLevel)
        const addLayerList = useSelector((state: RootState) => state.view.map.customLayerList)
        const mapType = useSelector((state: RootState) => state.view.map.mapType)

        const activeEvent = useSelector((state: RootState) => state.server.dstrMain.event.evetDetail) as VwEvent | null
        const customEventLatLng = useSelector((state: RootState) => state.view.map.customEventLatLng) as LatLngTuple | null
        const [eventLatLng, setEventLatLng] = useState<{ latLng: LatLngTuple, type: MapEventMarkerType } | null>(null)


        //이벤트가 설정되거나, 사용자가 지정한 좌표가 정해지면 그에 따라 이벤트를 만들고 전달한다.
        useEffect(() => {


                if (activeEvent) {
                        setEventLatLng({
                                latLng: [parseFloat(activeEvent.lat!), parseFloat(activeEvent.lng!)],
                                type: MapEventMarkerType.event
                        })
                }

                if (customEventLatLng) {
                        setEventLatLng({
                                latLng: customEventLatLng,
                                type: MapEventMarkerType.fire
                        })
                }

                if (!activeEvent && !customEventLatLng) {
                        setEventLatLng(null)
                }

        }, [activeEvent, customEventLatLng])


        useEffect(() => {


                dispatch(actions.actions.fetchGisCctvList())

        }, [])


        return (
            <>

                    {/*gisArea*/}
                    <section className="gis_area ">
                            <MobileFullPopupLayer/>

                            {/*//오른쪽 열린것에 대한 값을 받아서 내려줘야한다.*/}
                            <GisSettingArea openRightTab={props.isRightOpen}/>
                            {/*searchArea*/}
                            <AddrSearchArea openLeftTab={props.isLeftOpen}/>
                            {/*heatmapLegendBox*/}
                            <GisLegendBox openRightTab={props.isRightOpen}/>

                            {/*mapFuncButtonGroup*/}
                            <GisFuncIconArea openRightTab={props.isRightOpen}/>
                            {/*split*/}
                    </section>
                    {/*searchArea*/}
                    <section className={`gis_icon_area`}>
                            <MapContainer
                                attributionControl={false}
                                preferCanvas={true}
                                renderer={L.canvas()}
                                zoomControl={false}
                                zoom={mapZoomLevel}
                                center={center}
                                maxZoom={TileMapData[mapType].options.maxZoom}
                                minZoom={TileMapData[mapType].options.minZoom}
                                scrollWheelZoom={true}
                                style={{width: '100%', height: '100%', zIndex: '10', borderRadius: '10px'}}>
                                    <ResizeMap/>
                                    <MapControl cluster={cluster}/>
                                    <MapControl cluster={mobilePastCluster}/>

                                    <Pane name={'baseMap'} style={{zIndex: 100}}>
                                            <BaseMapLayer/>
                                    </Pane>
                                    <Pane name={'customLayer'} style={{zIndex: 200}}>
                                            {
                                                    addLayerList.map((layer, index) => {
                                                            return <TileLayer
                                                                key={`add_${layer.mapNm}_${index}`} {...TileMapData[layer.mapNm].options}
                                                                url={TileMapData[layer.mapNm].tiles[layer.type].urls[0]}/>
                                                    })
                                            }
                                    </Pane>
                                    <HeatmapLayer/>
                                    <Pane name={'draw'} style={{zIndex: 1000}}>
                                            <MapDrawLayer/>
                                            {/*<MapDraw/>*/}
                                    </Pane>
                                    <Pane name={'makers'} style={{zIndex: 1001}}>
                                            <CctvClusterMarkers setMapCluster={setCluster}/>
                                    </Pane>

                                    <SpiderLayer eventLatLng={eventLatLng} cluster={cluster}/>
                                    <Pane name={'cctvClusterPopup'} style={{zIndex: 1005}}>
                                            <CctvClusterPopup/>
                                    </Pane>
                                    {/*화면에 찍히는 마커 (모바일-라이브)*/}
                                    <Pane name={'mobileLiveMarkers'} style={{zIndex:1004}}>
                                            <MobileLiveMarkers/>
                                    </Pane>

                                    <Pane name={'cctvPopup'} style={{zIndex: 1006}}>
                                            <CctvPopupLayer/>
                                    </Pane>
                                    <Pane name={'eventLayer'} style={{zIndex: 1001}}>
                                            {eventLatLng && <EventMarker eventLatLng={eventLatLng}/>}
                                    </Pane>
                                    <Pane name={'mobilePopup'} style={{zIndex:1006}}>
                                            <MobilePopupLayer/>
                                    </Pane>


                                    <AddressMakerLayer/>
                                    <CctvTabActiveMarkerLayer/>
                                    <MobileTabActiveMarkerLayer/>
                                    <MobileTabLiveActiveMarkerLayer/>
                                    <MobilePastMarkers/>

                            </MapContainer>
                    </section>

                    {/*gisIconArea*/}
                    {/*<section className="gis_icon_area">*/}
                    {/*    /!*cctvNetworkIcon*!/*/}
                    {/*    <div className="cctv_network_icon locate_event_icon"/>*/}
                    {/*    /!* <div className="cctv_network_icon locate_castnet_icon"></div> *!/*/}

                    {/*    /!*cctvIcon*!/*/}
                    {/*    <div className="gis_icon cctv_icon loc_1"/>*/}
                    {/*    <div className="gis_icon cctv_icon loc_2"/>*/}
                    {/*    <div className="gis_icon cctv_icon loc_3"/>*/}
                    {/*    <div className="gis_icon cctv_icon loc_4"/>*/}
                    {/*    <div className="gis_icon cctv_icon loc_5"/>*/}
                    {/*    <div className="gis_icon cctv_icon loc_6"/>*/}

                    {/*    /!*numberingImg event*!/*/}
                    {/*    <div className="numbering_img event_1 loc_01"/>*/}
                    {/*    <div className="numbering_img event_2 loc_02"/>*/}
                    {/*    <div className="numbering_img event_3 loc_03"/>*/}
                    {/*    <div className="numbering_img event_4 loc_04"/>*/}
                    {/*    <div className="numbering_img event_5 loc_05"/>*/}
                    {/*    <div className="numbering_img event_6 loc_06"/>*/}

                    {/*    /!*numberingImg castnet*!/*/}
                    {/*    /!* <div className="numbering_img castnet_1 loc_01"></div>*/}
                    {/*    <div className="numbering_img castnet_2 loc_02"></div>*/}
                    {/*    <div className="numbering_img castnet_3 loc_03"></div>*/}
                    {/*    <div className="numbering_img castnet_4 loc_04"></div>*/}
                    {/*    <div className="numbering_img castnet_5 loc_05"></div>*/}
                    {/*    <div className="numbering_img castnet_6 loc_06"></div> *!/*/}

                    {/*    /!*locationIcon*!/*/}
                    {/*    <div className="location_icon type_common loc_7"></div>*/}
                    {/*    <div className="location_icon type_mobile_live loc_8"></div>*/}

                    {/*    <div className="gis_icon cctv_icon active loc_9">*/}
                    {/*        <div className="location_icon type_common"></div>*/}
                    {/*        <div className="gis_cctv_popup">*/}
                    {/*            <header className="gis_cctv_popup_header">*/}
                    {/*                <p className="gis_cctv_popup_title">[1087] 남양뉴타운LH10단지아파트 서문 전기차충전소입니다</p>*/}
                    {/*                <button type="button" className="btn_cctv_popup_close"></button>*/}
                    {/*            </header>*/}
                    {/*            <div className="gis_cctv_popup_body">*/}
                    {/*                <div className="cctv_in"></div>*/}
                    {/*                <button type="button" className="btn_cctv_full"></button>*/}
                    {/*                <div className="cctv_load_error_area">*/}
                    {/*                    <button type="button" className="btn_reload"></button>*/}
                    {/*                    <p className="cctv_load_error_message">영상 로드에<span>문제가 발생하였습니다.</span>*/}
                    {/*                    </p>*/}
                    {/*                </div>*/}
                    {/*            </div>*/}
                    {/*        </div>*/}
                    {/*    </div>*/}
                    {/*    /!*mobileIcon*!/*/}
                    {/*    <div className="mobile_icon type_live_video loc_10">*/}
                    {/*        <div className="location_icon type_mobile_live"></div>*/}
                    {/*        /!*gisMobilePopup 가로모드*!/*/}
                    {/*        <div className="gis_mobile_popup landscape_mode">*/}
                    {/*            <button type="button" className="btn_mobile_popup_close"></button>*/}
                    {/*            /!* <div className="video_in"></div> *!/*/}
                    {/*            <div className="cctv_load_error_area">*/}
                    {/*                <button type="button" className="btn_reload"></button>*/}
                    {/*                <p className="cctv_load_error_message">영상 로드에&nbsp;<span>문제가 발생하였습니다.</span>*/}
                    {/*                </p>*/}
                    {/*            </div>*/}
                    {/*            <div className="video_info_area">*/}
                    {/*                <button type="button" className="btn_open"></button>*/}
                    {/*                <button type="button" className="btn_cctv_full"></button>*/}
                    {/*                <div className="video_info_frame">*/}
                    {/*                    <div className="video_info_block">*/}
                    {/*                        <p className="video_info_title">영상 촬영 시작 일시</p>*/}
                    {/*                        <p className="video_info_value">2022-08-01 10:42:28</p>*/}
                    {/*                    </div>*/}
                    {/*                    <div className="video_info_block">*/}
                    {/*                        <p className="video_info_title">담당자 소속-이름</p>*/}
                    {/*                        <p className="video_info_value">재난안전과-홍길동</p>*/}
                    {/*                    </div>*/}
                    {/*                </div>*/}
                    {/*            </div>*/}
                    {/*        </div>*/}
                    {/*    </div>*/}
                    {/*    <div className="mobile_icon type_past_video loc_11">*/}
                    {/*        <div className="location_icon type_common"></div>*/}
                    {/*        /!*gisMobilePopup 세로모드*!/*/}
                    {/*        <div className="gis_mobile_popup portrait_mode">*/}
                    {/*            <button type="button" className="btn_mobile_popup_close"></button>*/}
                    {/*            /!* <div className="video_in"></div> *!/*/}
                    {/*            <div className="cctv_load_error_area">*/}
                    {/*                <button type="button" className="btn_reload"></button>*/}
                    {/*                <p className="cctv_load_error_message">영상 로드에<span>문제가 발생하였습니다.</span>*/}
                    {/*                </p>*/}
                    {/*            </div>*/}
                    {/*            <div className="video_info_area">*/}
                    {/*                <button type="button" className="btn_open"></button>*/}
                    {/*                <button type="button" className="btn_cctv_full"></button>*/}
                    {/*                <div className="video_info_frame">*/}
                    {/*                    <div className="video_info_block">*/}
                    {/*                        <p className="video_info_title">영상 촬영 시작 일시</p>*/}
                    {/*                        <p className="video_info_value">2022-08-01 10:42:28</p>*/}
                    {/*                    </div>*/}
                    {/*                    <div className="video_info_block">*/}
                    {/*                        <p className="video_info_title">담당자 소속-이름</p>*/}
                    {/*                        <p className="video_info_value">재난안전과-홍길동</p>*/}
                    {/*                    </div>*/}
                    {/*                </div>*/}
                    {/*            </div>*/}
                    {/*        </div>*/}
                    {/*    </div>*/}
                    {/*</section>*/}
            </>
        )
}
export default GisArea