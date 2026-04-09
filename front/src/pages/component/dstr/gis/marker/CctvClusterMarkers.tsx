import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {useMap} from "react-leaflet";
import L from "leaflet";
import 'leaflet.markercluster';
import ReactDOMServer from "react-dom/server";
import {RootState} from "../../../../store/rootStore";
import {
    updateActiveCctv,
    updateActiveClusterLatLon,
    updateActiveClusterMarkerList,
    updatePlayGisCctvList
} from "../../../../store/view/dstr/dstrViewCctvStore";
import {CctvInfo} from "../../../../../data/interface/cctv/cctvInterface";
import _ from "underscore";
import {MapMoveEnum, MAX_CCTV_PLAYER_CNT} from "../../../../../data/const/mapConst";
import {TileMapData} from "../../../../../data/config/Map";
import {changeMapMove} from "../../../../store/view/dstr/mapStore";


interface Props {
    setMapCluster: Function
}

const CctvClusterMarkers: React.FC<Props> = ({setMapCluster}) => {

    const ActiveLayerEditEnum = {
        add: "add", del: "del", none: "none"
    }


    const map = useMap();
    const dispatch = useDispatch();
    const cctvList = useSelector((state: RootState) => state.server.dstrMain.cctv.cctvGistCctvList)
    const playGisCctvList = useSelector((state: RootState) => state.view.dstrMain.cctv.playGisCctvList)

    const zoomLevel = useSelector((state: RootState) => state.view.map.zoomLevel);
    const mapType = useSelector((state: RootState) => state.view.map.mapType)
    const [activeLayerList, setActiveLayerList] = useState<any[]>([]);
    const [cluster, initCluster] = useState<any>(null)


    const playCctvList = useSelector((state: RootState) => state.view.dstrMain.cctv.playCctvList)


    useEffect(() => {
        if (cluster) {
            cluster.off('clusterclick')
            cluster.on('clusterclick', clusterClick)

            map.addLayer(cluster)
            cluster.refreshClusters();
            setMapCluster(cluster)
        } else {
            let data = new L.MarkerClusterGroup({
                iconCreateFunction: clusterIconFn,
                maxClusterRadius: 60,
                zoomToBoundsOnClick: false,
                clusterPane: 'makers',
                pane: 'makers',
                animate: true,
                removeOutsideVisibleBounds: true,
                spiderfyOnMaxZoom: false,
                showCoverageOnHover: false
            })
            initCluster(data)

        }
        return () => {
            if (cluster) {
                cluster.off('clusterclick', clusterClick)
                map.removeLayer(cluster)
            }
        }
    }, [cluster,zoomLevel,mapType])



    function clusterClick(_cluster: any) {

        //일정 깊이 혹은 cluster가 가지는 갯수가 30개가될때까지



        // map.closePopup();
        // let clusterCctvList = _cluster.layer.getAllChildMarkers().map((item: any) => item.options.data)
        // dispatch(updateActiveClusterMarkerList(clusterCctvList))

        if (_cluster.layer._childCount <= 30) {
            dispatch(updateActiveClusterLatLon([_cluster.latlng.lat, _cluster.latlng.lng])) //layer의 id 값이 cluster의 값인지 확인이 안됨
            cluster.options.iconCreateFunction = (c: any) => clusterIconFn(c, [_cluster.latlng.lat, _cluster.latlng.lng])
            cluster.refreshClusters();
            let clusterCctvList = _cluster.layer.getAllChildMarkers().map((item: any) => item.options.data)
            dispatch(updateActiveClusterMarkerList(clusterCctvList))
            return
        } else {
            let zoom = zoomLevel + 1;
            if (zoom < TileMapData[mapType].options.maxZoom) {
                dispatch(changeMapMove({latlng: [_cluster.latlng.lat,_cluster.latlng.lng] , mapMoveType: MapMoveEnum.plus_zoom}))
            }
            cluster.refreshClusters();
            return
        }

    }

    const activeClusterLatLon = useSelector((state: RootState) => state.view.dstrMain.cctv.activeClusterLatLon);
    useEffect(() => {
        if (!activeClusterLatLon) {
            if (cluster) {
                cluster.options.iconCreateFunction = (c: any) => clusterIconFn(c, null)
                cluster.refreshClusters();
            }
        }

    }, [activeClusterLatLon]);

    function clusterIconFn(cluster: any, clickClusterLatLon: number[] | null = [0, 0]) {

        return L.divIcon({
            className: `cluster-icon`,
            html: ReactDOMServer.renderToString(
                //@ts-ignore
                <div
                    className={`gis_icon cluster_icon ${clickClusterLatLon && clickClusterLatLon[0] === cluster._latlng.lat && clickClusterLatLon[1] === cluster._latlng.lng ? 'active' : ''}`}>
                    <div className="count_number">{cluster.getChildCount()}</div>
                </div>
            ),
            iconAnchor: [24, 24]
        });
    }


    function renderIcon(info: CctvInfo, findIndex: number = -1) {

        return L.divIcon({

            className: 'custom-icon',
            html: ReactDOMServer.renderToString(
                <div className={`gis_icon cctv_icon ${(findIndex > -1) ? 'active' : ''}   ${info.facId}`}
                     onMouseLeave={() => {
                     }}
                />
            ),
            iconAnchor: [20, 30],

        });
    }


    useEffect(() => {


        map.eachLayer((l) => {
            // @ts-ignore
            if (l.options.data) {
                // @ts-ignore
                let findIndex = _.findIndex(activeLayerList, {_leaflet_id: l._leaflet_id})
                // @ts-ignore
                let findIndex2 = _.findIndex(playGisCctvList, {facId : l.options.data.facId})

                if (findIndex ==-1){
                    findIndex = findIndex2
                }
                // @ts-ignore
                l.setIcon(renderIcon(l.options.data, findIndex))
                l.off('click')
                l.on('click', markerClick)
            }

        })
    }, [activeLayerList, zoomLevel ]);


    useEffect(() => {
        let newActiveLayerList: any[] = []


        map.eachLayer((l) => {
            //@ts-ignore
            if (l.options.data) {
                //@ts-ignore
                const findIndex = _.findIndex(playGisCctvList ? playGisCctvList : [], {facId: l.options.data.facId})

                // @ts-ignore
                l.setIcon(renderIcon(l.options.data, findIndex))
                l.off('click')
                l.on('click', markerClick)
                if (findIndex > -1) newActiveLayerList.push(l)
            }
        })
        setActiveLayerList(newActiveLayerList)

    }, [playGisCctvList,playCctvList])

    /**
     * marker click event
     *
     * 현재 투망중인 CCTV는 GIS 상에서 플레이를 하지못하게 막는다.
     *
     * @param event
     */
    function markerClick(event: any) {
        let info = event.target.options.data
        const editResult = playBtnClickEvent(info)

        let addList = [...activeLayerList]
        if (editResult === ActiveLayerEditEnum.add) { //추가된 경우
            //@ts-ignore
            addList.push(event.target)
            setActiveLayerList(addList)

        } else if (editResult === ActiveLayerEditEnum.del) { //삭제하는경우

            // @ts-ignore
            const findIndex = _.findIndex(addList, {_leaflet_id: event.target._leaflet_id})
            if (findIndex !== -1) {//있으면
                addList.splice(findIndex, 1)
                setActiveLayerList(addList)
            }
        } else { //아무것도 아닌경우

        }

        dispatch(updateActiveCctv(null))
    }


    const playBtnClickEvent = (info: CctvInfo) => {

        let addResult = ActiveLayerEditEnum.none

        //현재 투망중인지 체크한다.
        const playerIndex = _.findIndex(playCctvList, {
            facId: info.facId
        })

        if (playerIndex >-1){ //현재 투망으로 잡혀서 플레이중인 CCTV가 존재함
            return addResult
        }
        let checkInfo = checkActive(info.facId)
        if (checkInfo.index === -1) {
            checkInfo.list.push(info)
            dispatch(updatePlayGisCctvList(checkInfo.list as CctvInfo[] | null))

            if (MAX_CCTV_PLAYER_CNT >= checkInfo.list.length) {
                addResult = ActiveLayerEditEnum.add
            }

        } else {
            checkInfo.list.splice(checkInfo.index, 1)
            dispatch(updatePlayGisCctvList(checkInfo.list as CctvInfo[] | null))
            addResult = ActiveLayerEditEnum.del
        }

        return addResult
    }


    //현재 플레이 list로 비교하네
    const checkActive = (facId: string): { index: number, list: CctvInfo[] } => {
        let newPlayCctvList = playGisCctvList ? [...playGisCctvList] : []


        const index = _.findIndex(newPlayCctvList, {
            facId: facId
        })

        return {index, list: newPlayCctvList}
    }


    function makeMarker(cctv: any, findIndex: number = -1) {
        const marker = L.marker([cctv.lat, cctv.lng], {
            icon: renderIcon(cctv, findIndex),
            pane: 'makers',
            // @ts-ignore
            data: cctv,
            active: false

        })

        marker.on('click', markerClick)
        return marker
    }


    useEffect(() => {
        if (cctvList) {
            if (cluster) {
                cluster.addLayers(cctvList.map(cctv => makeMarker(cctv)))
            }

        }

        return () => {
            cluster && cluster.clearLayers()
        }
    }, [cluster, cctvList])


    return null

}

export default CctvClusterMarkers;