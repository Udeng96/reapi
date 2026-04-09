import ReactDOMServer from 'react-dom/server';
import {divIcon, LatLng, LatLngTuple, point} from 'leaflet';
import styled from 'styled-components';
import {Marker} from 'react-leaflet';
import {MapEventMarkerType} from "../../../../../data/const/mapConst";


import eventPng from "../../../../../public/static/images/dark/page/icon/gis/icon_cctvNetwork_locating_event.png"
import firePng from "../../../../../public/static/images/dark/page/icon/gis/icon_cctvNetwork_locating_castNet.png"
import {useEffect} from "react";

const StyledEventMarker = styled.div<{ markerType: MapEventMarkerType }>`
  width: 45px;
  height: 62px;
  position: absolute;
  background: url(${({ markerType }) => markerType === MapEventMarkerType.event ? `${eventPng}` : `${firePng}`})
    no-repeat;
`;

const CustomWatchMarker = (props:{eventLatLng : { latLng: LatLngTuple, type: MapEventMarkerType }|null }) => {


  function renderMarker() {
    let domHtml = ReactDOMServer.renderToString(
      <StyledEventMarker markerType={props.eventLatLng!.type} />
    );


    let icon = divIcon({
      html: domHtml,
      className: `cctv_network_icon locate_icon_${props.eventLatLng!.type}`,
      iconSize: point(45, 62, true),
      iconAnchor: [23, 55],
    });
    return icon;
  }

  return (
      props.eventLatLng && (
      <Marker
        icon={renderMarker()}
        position={new LatLng(props.eventLatLng.latLng[0], props.eventLatLng.latLng[1])}
        key={'EVENT_WATCH_MARKER'}
      />
    )
  );
};

export default CustomWatchMarker;
