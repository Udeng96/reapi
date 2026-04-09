import {Marker, Pane} from 'react-leaflet';
import {divIcon, LatLng, point} from 'leaflet';
import ReactDOMServer from 'react-dom/server';
import {useSelector} from 'react-redux';
import {RootState} from "../../../../store/rootStore";

const CctvTabActiveMarkerLayer = () => {
  const cctvInfo = useSelector((state:RootState) => state.view.dstrMain.cctv.activeCctv);



  let domHtml = ReactDOMServer.renderToString(
    <div className={`location_icon type_common`}/>
  );

  let icon = divIcon({
    html: domHtml,
    className: `${'active cctv'}-marker`,
    iconSize: point(45, 62, true),
    iconAnchor: [15, 40],
  });


  function setMarker() {
    if(cctvInfo) {
      return  <Marker
        icon={icon} position={new LatLng(cctvInfo!.lat, cctvInfo!.lng )} />
    } else {
      return null
    }
  }

  return (
    <Pane name={'CctvTabActiveMarkerLayer'} style={{ zIndex: 1004 }}>
      {setMarker()}
    </Pane>
  );
};

export default CctvTabActiveMarkerLayer;
