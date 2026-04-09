import { useEffect } from 'react';
import { useMap } from 'react-leaflet';
import L from 'leaflet';
import ReactDOMServer from 'react-dom/server';
import { useHamsList } from '../../entities/hams/queries';
import { useHamsStore } from '../../entities/hams/store';
import type { HamsGisFacInfo } from '../../entities/hams/types';

function makeDoorIconClass(info: HamsGisFacInfo): string {
  if (info.status?.frontDoor === '2') return 'state_open';
  if (info.status?.frontDoor === '3') return 'state_close';
  return 'state_nodata';
}

function renderIcon(info: HamsGisFacInfo, isActive: boolean) {
  return L.divIcon({
    className: 'hams-custom-icon',
    html: ReactDOMServer.renderToString(
      <div className="map_area">
        <div className="gis_icon">
          <div
            className={`gis_connect_icon ${isActive ? 'active' : ''} ${
              info.status?.isConnect ? 'state_connected' : 'state_disconnected'
            }`}
          />
          <div className={`gis_door_icon ${makeDoorIconClass(info)}`} />
        </div>
      </div>
    ),
    iconAnchor: [540, 340],
  });
}

export function HamsMarker() {
  const map = useMap();
  const { data: hamsList } = useHamsList();
  const { selectedHams, setSelectedHams } = useHamsStore();

  useEffect(() => {
    map.eachLayer((l) => map.removeLayer(l));

    if (!hamsList) return;

    hamsList.forEach((hams) => {
      if (!hams.lat || !hams.lng) return;

      const isActive = selectedHams?.hamsId === hams.hamsId;
      const marker = L.marker([hams.lat, hams.lng], {
        icon: renderIcon(hams, isActive),
        pane: 'hamsMarkers',
        // @ts-ignore
        hamsData: hams,
        hamsId: hams.hamsId,
      });

      marker.on('click', () => {
        setSelectedHams(hams);
      });

      map.addLayer(marker);
    });
  }, [hamsList, selectedHams]);

  return null;
}
