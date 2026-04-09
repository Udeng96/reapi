import L from 'leaflet';
import 'leaflet.heat';
import { useMap } from 'react-leaflet';
import {useEffect, useState} from 'react';
import {useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";

const HEAT_MAP_OPTION = {
  radius: 15,
  gradient: {
    0.1: '#00E626',
    0.5: '#FFD70D',
    1: '#FF0040',
  },
  max: 1,
  minOpacity: 0.8,
};




const HeatmapLayer = ( ) => {
  const list = useSelector((state:RootState) => state.server.heatMap.countList);
  const map = useMap();
  //@ts-ignore
  const [layer , setLayer] = useState(L.heatLayer([], HEAT_MAP_OPTION))

  const heatMapList = list && list.map ? list.map((_item) => {
          return [_item.lat, _item.lng, _item.count];}) : [];


  useEffect(() => {
    if (!map.hasLayer(layer)) {
      layer.addTo(map);
    }
    layer.setLatLngs(heatMapList);
  }, [list]);

  return null
};

export default HeatmapLayer;
