import React, {useEffect} from "react";
import styled from "styled-components"
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import {MapDrawEnum} from "../../../../../data/const/mapConst";
import {
    setZoomLevel,
    updateCustomEventLatLng,
    updateMapClickFn,
    updateMapDraw
} from "../../../../store/view/dstr/mapStore";
import {TileMapData} from "../../../../../data/config/Map";
import {updateEvetDetail} from "../../../../store/server/dstr/dstrServerEventStore";
import {getActiveEventType, getActiveItem} from "../../../../store/view/dstr/dstrViewEventStore";

const StyledMapFuncButtonGroup = styled.div<{ openRightTab: boolean }>`
  width: 32px;
  height: auto;
  position: absolute;
  top: 74px;
  right: ${({openRightTab}) => openRightTab ? '503px' : '18px'};
  z-index: 3001;
  transition: right 0.8s ease;
`


const GisFuncIconArea = (props: { openRightTab: boolean }) => {

    const dispatch = useDispatch();
    const mapDraw = useSelector((state: RootState) => state.view.map.mapDraw); // map에 그리는 도구
    const zoomLevel = useSelector((state: RootState) => state.view.map.zoomLevel);
    const mapType = useSelector((state: RootState) => state.view.map.mapType)


    useEffect(() => {
        escListen();
    }, []);

    function escListen() {
        document.addEventListener('keydown', reset, false);
    }

    const reset = (e: KeyboardEvent) => {
        if (e.key === 'Escape') {
            if (mapDraw === MapDrawEnum.net) {
                dispatch(updateMapClickFn(() => {
                }));
            }
            dispatch(updateMapDraw(MapDrawEnum.none));
        }
    };

    useEffect(() => {

        if (mapDraw === MapDrawEnum.none) {
            dispatch(updateMapClickFn(() => {}))
        }
    }, [mapDraw]);

    const zoomPlus = () => {
        let zoom = zoomLevel + 1;
        if (zoom < TileMapData[mapType].options.maxZoom) {
            dispatch(setZoomLevel(zoom));
        }

    };
    const zoomMinus = () => {
        let zoom = zoomLevel - 1;
        if (zoom > TileMapData[mapType].options.minZoom) {
            dispatch(setZoomLevel(zoom));
        }
    };


    const line = () => {
        if (mapDraw === MapDrawEnum.polyline) {
            dispatch(updateMapDraw(MapDrawEnum.none))
        } else {
            dispatch(updateMapDraw(MapDrawEnum.polyline))
            dispatch(updateMapClickFn(() => {}))
        }


    };

    const polygon = () => {

        if (mapDraw === MapDrawEnum.polygon) {
            dispatch(updateMapDraw(MapDrawEnum.none))
        } else {
            dispatch(updateMapDraw(MapDrawEnum.polygon))
            dispatch(updateMapClickFn(() => {}))
        }

    };


    const net = () => {
        const netFn = (e: L.LeafletMouseEvent) => {

            console.log('click event', e)
            //위치를 지정하고 이벤트 투망을 실시한다.
            //기존 초기화를 한번 실시하고 진행

            dispatch(updateEvetDetail(null))
            dispatch(getActiveItem(null))
            dispatch(getActiveEventType(null))
            dispatch(updateCustomEventLatLng([e.latlng.lat, e.latlng.lng]))
        }

        //맵 그리기 타입을 변경하여 style 변경
        //이벤트 리스트로 강제 변동  //이후 클릭을 하고 난 다음 크리거 수행
        //가상의 이벤트를 만들어 투망을 실시하게 함
        //TODO 현재 탭에서 강제로 발생시켜야한다,
        // 이벤트 위치 와 가까운 CCTV 리스트를 net list에 담아서 이벤트를 투망하게 한다.
        if (mapDraw === MapDrawEnum.net) {
            // 네트 비활성화
            dispatch(updateMapDraw(MapDrawEnum.none))
            dispatch(updateCustomEventLatLng(null))
            dispatch(updateMapClickFn(() => {}))
        } else {
            // net 활성화
            dispatch(updateMapDraw(MapDrawEnum.net))
            dispatch(updateMapClickFn(netFn))
        }
        //마커 이벤트 클리어
        //TODO 지정한 위치 정보 업데이트 넣어야함


    }

    const setActiveClassNm = (type: string) => {
        return type === mapDraw
    }

    return (

        <StyledMapFuncButtonGroup openRightTab={props.openRightTab} className="map_func_button_group">
            <button type="button" className="btn_map_func btn_zoom_in" onClick={zoomPlus}/>
            <button type="button" className="btn_map_func btn_zoom_out" onClick={zoomMinus}/>
            <button type="button"
                    className={`btn_map_func btn_distance ${setActiveClassNm(MapDrawEnum.polyline) ? 'active' : ''}`}
                    onClick={line}/>
            <button type="button"
                    className={`btn_map_func btn_extent ${setActiveClassNm(MapDrawEnum.polygon) ? 'active' : ''}`}
                    onClick={polygon}/>
            <button type="button"
                    className={`btn_map_func btn_castnet ${setActiveClassNm(MapDrawEnum.net) ? 'active' : ''}`}
                    onClick={net}/>
        </StyledMapFuncButtonGroup>
    )
}

GisFuncIconArea.defaultProps = {
    openRightTab: true
}

export default GisFuncIconArea