import { Marker, Pane } from 'react-leaflet';
import { divIcon, point, LatLng } from 'leaflet';
import ReactDOMServer from 'react-dom/server';
import { useEffect } from 'react';
import { useSelector } from 'react-redux';
import {RootState} from "../../../../store/rootStore";

const AddressMakerLayer = () => {
  const addrInfo = useSelector((state:RootState) => state.view.map.selectAddrInfo);

  //FIXME ICON 변경이 있을 예정
  let domHtml = ReactDOMServer.renderToString(
    <div className={`location_icon type_location`}/>
  );

  let icon = divIcon({
    html: domHtml,
    className: `${'address'}-marker`,
    iconSize: point(45, 62, true),
    iconAnchor: [23, 62],
  });


  function setMarker() {
    if(addrInfo) {
      return  <Marker
        icon={icon} position={new LatLng(parseFloat(addrInfo.point.y), parseFloat(addrInfo.point.x))} />
    } else {
      return null
    }
  }

  return (
    <Pane name={'addressMarkerLayer'} style={{ zIndex: 1004 }}>
      {setMarker()}
    </Pane>
  );
};

export default AddressMakerLayer;
