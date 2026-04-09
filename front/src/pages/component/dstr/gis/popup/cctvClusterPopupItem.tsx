import {StyledTooltipTextLeft21} from "../gisSettings/gisHeatMap/gisHeatMapTypeItem";
import React, {MutableRefObject, useEffect, useRef, useState} from "react";
import {CctvInfo} from "../../../../../data/interface/cctv/cctvInterface";
import _ from "underscore";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import {updateActiveClusterLatLon, updatePlayGisCctvList} from "../../../../store/view/dstr/dstrViewCctvStore";

//TODO active 작업 다시해야함.
const CctvClusterPopupItem = (props:{cctv: CctvInfo}) => {

    const dispatch = useDispatch()
    const nameRef: MutableRefObject<any> = useRef()
    const [showTooltip, setShowTooltip] = useState(false)
    const [isOver, setOver] = useState(false)
    const playGisCctvList = useSelector((state: RootState) => state.view.dstrMain.cctv.playGisCctvList)

    const playCctvList = useSelector((state: RootState) => state.view.dstrMain.cctv.playCctvList)

    useEffect(() => {
        const isOver = nameRef.current.scrollWidth > nameRef.current.clientWidth
        if (isOver) {
            setShowTooltip(true)
        }
    }, [])

    const checkActive = (facId:string): { index: number, list: CctvInfo[] } => {
        let newPlayCctvList = playGisCctvList ? [...playGisCctvList] : []
        //현재 투망중인지 체크한다.
        const playerIndex = _.findIndex(playCctvList, {
            facId: facId
        })

        if (playerIndex >-1){//현재 투망으로 잡혀서 플레이중인 CCTV가 존재함
            return {index:-2,list:[]}
        }


        const index = _.findIndex(newPlayCctvList, {
            facId: facId
        })

        return {index, list: newPlayCctvList}
    }

    /**
     * item click event
     * 현재 투망중인 CCTV는 GIS 상에서 플레이를 하지못하게 막는다.
     * @param cctv
     */
    const clickEvent = (cctv: CctvInfo | null) => {

        let checkInfo = checkActive(cctv!.facId)

        if (checkInfo.index === -1) {
            checkInfo.list.push(cctv!)
            dispatch(updatePlayGisCctvList(checkInfo.list as CctvInfo[] | null))
        } else if(checkInfo.index > -1){
            checkInfo.list.splice(checkInfo.index, 1)
            dispatch(updatePlayGisCctvList(checkInfo.list as CctvInfo[] | null))
        } else {

        }
        dispatch(updateActiveClusterLatLon(null))
    }


    return (
        <li key={props.cctv.facId}
            className={`gis_cluster_name ${checkActive(props.cctv.facId).index > -1 ? 'active' : checkActive(props.cctv.facId).index}`}
            onMouseEnter={() => setOver(true)}
            onMouseLeave={() => setOver(false)}
            onClick={() => clickEvent(props.cctv)}
        >
            <p className="cctv_name" ref={nameRef}>{props.cctv.facNm}</p>
            <button type="button" className="btn_play"/>
            {
                (isOver && showTooltip) &&
                <StyledTooltipTextLeft21>{props.cctv.facNm}</StyledTooltipTextLeft21>
            }
        </li>
    )
}

export default CctvClusterPopupItem