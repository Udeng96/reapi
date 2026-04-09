import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../store/rootStore";
import {useMap} from "react-leaflet";
import {useEffect} from "react";
import L from "leaflet";
import {HamsGisFacInfo, WsDoeInfo} from "../../../../data/interface/hams/hamsInterface";
import ReactDOMServer from "react-dom/server";
import {updateActiveHamsInfo} from "../../../store/view/hams/hamsViewStore";
import * as actionHams from "../../../saga/action/hams/hams";

const GisHamsMarker = () => {
    // const activeHamsItem = useSelector((state: RootState) => state.view.hams.activeHamsInfo)
    const activeHamsItem = useSelector((state: RootState) => state.server.hams.activeHamsInfo)
    const hamsList = useSelector((state: RootState) => state.server.hams.hamsList)

    //이벤트로 받은 업데이트 정보
    const wsDoeInfo : WsDoeInfo | null = useSelector((state: RootState) => state.server.hams.wsDoeInfo)
    // const hamsList = HAMS_LIST_EXAMPLE;
    const hoverHamsId = useSelector((state:RootState)=>state.view.hams.hoverMenuHamsId)
    const map = useMap()
    const dispatch = useDispatch()


    const makeDoorIcon = (info: HamsGisFacInfo)=>{

        let iconClassNm = `state_nodata`

        if (info.status?.frontDoor === '2'){//열림
            iconClassNm = `state_open`
        } else if (info.status?.frontDoor === '3') {//닫힘
            iconClassNm = `state_close`
        }else{
            iconClassNm = 'state_nodata'
        }

        if (wsDoeInfo != null){
            if ( info.hamsId ==='HAMS_'+parseInt(wsDoeInfo?.EQ_ID.substring(4))){
                if(wsDoeInfo.FRONT_DOOR_S ==='2' ){
                    iconClassNm = `state_open`
                }else if(wsDoeInfo.FRONT_DOOR_S ==='3' ){
                    iconClassNm = `state_close`
                }else{
                    iconClassNm = `state_nodata`
                }
            }
        }
        return iconClassNm
    }




    function renderIcon(info: HamsGisFacInfo, findIndex: number = -1) {
        return L.divIcon({
            className: 'hams-custom-icon',
            html: ReactDOMServer.renderToString(
                <div className={"map_area"}>
                    <div className={"gis_icon"}>
                        <div className="gis_floor_icon" style={{"display": `${info.hamsId === hoverHamsId ? "block" : "none"}`}}/>
                        <div className={`gis_connect_icon ${findIndex > -1 ? "active" : ""}  ${info.status? info.status?.isConnect ? 'state_connected' : 'state_disconnected' : 'state_disconnected'}`}/>
                        <div className={`gis_door_icon ${makeDoorIcon(info)}` }/>
                    </div>
                </div>


            ),
            iconAnchor: [540, 340]
        })

    }

    const markerClick = (e:any) => {
        let info = e.target.options.hamsData

        //실제 코드
        if(activeHamsItem!!.hamsId !== info.hamsId){
            dispatch(updateActiveHamsInfo(info))
            dispatch(actionHams.actions.requestActiveHamsInfo(info.hamsId));
        }
    }

    function makeMarker(hams: HamsGisFacInfo, findIndex: number = -1) {

        let newFindIndex = findIndex
        hamsList?.forEach((item,index)=>{
            activeHamsItem && activeHamsItem.hamsId === hams.hamsId ? newFindIndex = index : newFindIndex = -1
        })

        // const hamsMarker = L.marker([hams!!.lng!!, hams!!.lat!!], {
        const hamsMarker = L.marker([hams!!.lat!, hams!!.lng!!], {
            icon: renderIcon(hams, newFindIndex),
            pane: 'hamsMarkers',
            // @ts-ignore
            hamsData: hams,
            hamsId : hams.hamsId,
            active: false
        })

        hamsMarker.off('click')
        hamsMarker.on('click',markerClick)

        return hamsMarker
    }

    useEffect(() => {

        map.eachLayer((l) => {

            //@ts-ignore
            if (l.options.hamsId) {

                //@ts-ignore
                 if (l.options.hamsId === 'HAMS_'+parseInt(wsDoeInfo?.EQ_ID.substring(4))){

                     let newFindIndex = -1
                     hamsList?.forEach((item,index)=>{
                         //@ts-ignore
                         activeHamsItem && activeHamsItem.hamsId === l.options.hamsId ? newFindIndex = index : newFindIndex = -1
                     })

                     // @ts-ignore
                     l.setIcon(renderIcon(l.options.hamsData, newFindIndex))
                     l.off('click')
                     l.on('click', markerClick)
                 }
            }
        })


    }, [wsDoeInfo]);




    useEffect(() => {

        map.eachLayer((l)=>{
            map.removeLayer(l)
        })

        if (hamsList) {
            hamsList.map((hams) => {
                let marker = makeMarker(hams)
                map.addLayer(marker)
            })
        }
    },[hamsList,activeHamsItem,hoverHamsId])

    return null
}

export default GisHamsMarker;