import React, { useEffect, useRef, useState} from "react";
import { useMap} from "react-leaflet";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import L from "leaflet";
import {updateActiveLiveBtnList, updateActiveLiveList} from "../../../../store/view/dstr/dstrViewMobileStore";
import ReactDOMServer from "react-dom/server";
import {MobileInfo} from "../../../../../data/interface/event/mobileInterface";


const MobileLiveMarkers = () => {

    const ActiveLayerEditEnum = {
        add:"add",del:"del",none:"none"
    }

    const mobileLiveList = useSelector((state: RootState) => state.server.dstrMain.mobile.nowList)
    const playMobileLiveList = useSelector((state: RootState) => state.view.dstrMain.mobile.activeLiveBtnList)
    const [activeMobileLiveLayerList, setActiveMobileLiveLayerList] = useState<any[]>([])
    const dispatch = useDispatch()
    const map = useMap()

    useEffect(()=>{

        map.eachLayer((l)=>{
            // @ts-ignore
            if(l.options.mobileLiveData){
                let findIndex = -1
                playMobileLiveList.map((item,index)=>{
                    // @ts-ignore
                    if(item._leaflet_id === l._leaflet_id) findIndex = index
                })

                // @ts-ignore
                l.setIcon(renderIcon(l.options.mobileLiveData,findIndex))
                l.off('click')
                l.on('click',markerClick)
            }
        })
    },[playMobileLiveList])

    useEffect(()=>{
        let newActiveLayerList : any[] = []
        map.eachLayer((l)=>{

            // @ts-ignore
            if(l.options.mobileLiveData){

                let findIndex =-1
                playMobileLiveList.map((item,index)=>{
                    // @ts-ignore
                    if(item.id=== l.options.mobileLiveData.id) findIndex = index
                })
                // @ts-ignore
                l.setIcon(renderIcon(l.options.mobileLiveData,findIndex))
                if(findIndex>-1) newActiveLayerList.push(l)
            }
        })
        setActiveMobileLiveLayerList(newActiveLayerList)
    },[playMobileLiveList])


    function renderIcon(info : MobileInfo, findIndex : number = -1){
        return  L.divIcon({
            className : 'mobile-live-custom-icon',
            html: ReactDOMServer.renderToString(
                <div className={`mobile_icon type_live_video ${(findIndex>-1) ? 'active' : ''} ${info.id}`}/>
            ),
            iconAnchor: [30,30]
        })


    }

    function markerClick(event: any) {
        let info = event.target.options.mobileLiveData
        const editResult = playBtnClickEvent(info)
        let addList = [...activeMobileLiveLayerList]
        if (editResult === ActiveLayerEditEnum.add) {
            addList.push(event.target)
            setActiveMobileLiveLayerList(addList)
        } else if (editResult === ActiveLayerEditEnum.del) {

            let findIndex = -1
            addList.map((item, index) => {
                if (item._leaflet_id === event.target._leaflet_id) findIndex = index
            })

            if (findIndex !== -1) {
                addList.splice(findIndex, 1)
                setActiveMobileLiveLayerList(addList)
            }
        }

        // 버튼을 클릭한 경우 리스트 클릭한 것 초기화 되도록
        dispatch(updateActiveLiveList(null))
    }

    const playBtnClickEvent = (info: MobileInfo) => {


        let addResult = ActiveLayerEditEnum.none

        // 해당 버튼이 active 되어 있는지 확인
        let checkInfo = checkActive(info.id!!)

        // 해당 버튼이 active 되어있지 않으면
        if (checkInfo.findIndex === -1) {
            // 현재 active 한 버튼 추가
            checkInfo.mobileLiveList.push(info)
            dispatch(updateActiveLiveBtnList(checkInfo.mobileLiveList as MobileInfo[] | null))
            // 그리고 추가된 것으로 반환
            addResult = ActiveLayerEditEnum.add
        } else {
            // 해당 버튼이 이미 active 되어 있는 경우
            // active 제외
            checkInfo.mobileLiveList.splice(checkInfo.findIndex, 1)
            dispatch(updateActiveLiveBtnList(checkInfo.mobileLiveList as MobileInfo[] | null))
            // 삭제된 것으로 반환
            addResult = ActiveLayerEditEnum.del
        }

        return addResult

    }

    function makeMarker(mobile: any, findIndex: number = -1) {

        let newFindIndex = findIndex
        playMobileLiveList.map((item,index)=>{
            item.id===mobile.id? newFindIndex = index : newFindIndex = -1
        })
        const mobileLiveMarker = L.marker([mobile.lat, mobile.lng], {
            icon: renderIcon(mobile,newFindIndex),
            pane: 'mobileLiveMarkers',
            // @ts-ignore
            mobileLiveData: mobile,
            active: false
        })
        mobileLiveMarker.off('click')
        mobileLiveMarker.on('click', markerClick)
        return mobileLiveMarker
    }

    const checkActive = (id: string): { findIndex: number, mobileLiveList: MobileInfo[] } => {

        // 이미 실행중인 모바일 영상이 있으면 복사 없으면 초기화
        let newPlayMobileLiveList: MobileInfo[] = []

        let findIndex = -1

        playMobileLiveList.map((item, index) => {
            newPlayMobileLiveList.push(item)
            if(item.id === id) findIndex = index
        })


        return {findIndex:findIndex, mobileLiveList: newPlayMobileLiveList}

    }

    useEffect(()=>{
        if(mobileLiveList){
            mobileLiveList.map((mobile)=>{
                let marker = makeMarker(mobile)
                map.addLayer(marker)
            })
        }
    },[mobileLiveList])


    return null
}

export default MobileLiveMarkers;