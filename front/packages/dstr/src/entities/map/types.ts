import type { LatLngExpression, LatLngTuple } from 'leaflet';

export type MapMoveType = 'default' | 'event' | 'plus_zoom' | 'max_zoom';

export interface MapMove {
  latlng: LatLngTuple;
  type: MapMoveType;
}

export interface MapTypeProps {
  mapNm: string;
  type: string;
  label: string;
}

export interface AddrInfo {
  addr: string;
  lat: number;
  lng: number;
}

export type MapDrawMode = 'none' | 'marker' | 'polygon' | 'circle';

export interface HeatmapClfy {
  id: string;
  nm: string;
  svcThemeCd: string;
  unitCd: string;
}
