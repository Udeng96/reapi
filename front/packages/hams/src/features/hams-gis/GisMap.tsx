import { useState } from 'react';
import { MapContainer, Pane } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { HamsMarker } from './HamsMarker';
import { useHamsList } from '../../entities/hams/queries';

export function GisMap() {
  const { refetch } = useHamsList();
  const [lastUpdated, setLastUpdated] = useState(() =>
    new Date().toLocaleString('ko-KR')
  );

  const handleReload = () => {
    refetch();
    setLastUpdated(new Date().toLocaleString('ko-KR'));
  };

  return (
    <section className="hams gis_area">
      <MapContainer
        attributionControl={false}
        preferCanvas={true}
        renderer={L.canvas()}
        zoomControl={false}
        doubleClickZoom={false}
        scrollWheelZoom={false}
        dragging={false}
        center={[37.178782, 126.9]}
        zoom={11}
        style={{
          width: '100%',
          height: '100%',
          zIndex: 0,
          borderRadius: '10px',
          backgroundColor: '#15161f',
        }}
      >
        {/* 배경 지도 그래픽 (gis.css .map_area 적용 대상) */}
        <div className="map_area" />
        <Pane name="hamsMarkers" style={{ zIndex: 1001 }}>
          <HamsMarker />
        </Pane>
      </MapContainer>

      <div className="gis_legend_img" />
      <button type="button" className="btn_reload" onClick={handleReload} />
      <p className="update_time_value">최종 업데이트 일시 : {lastUpdated}</p>
    </section>
  );
}
