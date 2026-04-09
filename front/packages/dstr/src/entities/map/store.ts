import { create } from 'zustand';
import type { LatLngExpression, LatLngTuple } from 'leaflet';
import type { MapMove, MapTypeProps, AddrInfo, MapDrawMode, HeatmapClfy } from './types';

interface MapState {
  mapMove: MapMove | null;
  minZoom: number;
  maxZoom: number;
  mapType: string;       // 'vworld' | 'daum' 등
  mapTile: string;       // 'normal' | 'satellite' 등
  zoomLevel: number;
  mapDraw: MapDrawMode;
  customLayerList: MapTypeProps[];
  selectAddrInfo: AddrInfo | null;
  selectHeatMapClfy: HeatmapClfy | null;
  selectHeatMapPeriod: string;
  customEventLatLng: LatLngTuple | null;
  playCctvLatLngList: LatLngExpression[];

  // Actions
  setZoomLevel: (level: number) => void;
  setMinMaxZoom: (min: number, max: number) => void;
  setMapMove: (move: MapMove) => void;
  setMapType: (type: string, tile: string) => void;
  addCustomLayer: (layer: MapTypeProps) => void;
  removeCustomLayer: (layer: MapTypeProps) => void;
  setSelectAddrInfo: (info: AddrInfo | null) => void;
  setSelectHeatMapClfy: (clfy: HeatmapClfy | null) => void;
  setSelectHeatMapPeriod: (period: string) => void;
  setMapDraw: (mode: MapDrawMode) => void;
  setCustomEventLatLng: (latlng: LatLngTuple | null) => void;
  setPlayCctvLatLngList: (list: LatLngExpression[]) => void;
}

export const useMapStore = create<MapState>((set, get) => ({
  mapMove: null,
  minZoom: 6,
  maxZoom: 19,
  mapType: 'vworld',
  mapTile: 'normal',
  zoomLevel: 13,
  mapDraw: 'none',
  customLayerList: [],
  selectAddrInfo: null,
  selectHeatMapClfy: null,
  selectHeatMapPeriod: '전체',
  customEventLatLng: null,
  playCctvLatLngList: [],

  setZoomLevel: (level) => set({ zoomLevel: level }),
  setMinMaxZoom: (min, max) => set({ minZoom: min, maxZoom: max }),
  setMapMove: (move) => set({ mapMove: move }),
  setMapType: (type, tile) =>
    set((s) => ({
      mapType: type,
      mapTile: tile,
      // 다른 지도 타입으로 변경 시 커스텀 레이어 초기화
      customLayerList: s.mapType !== type ? [] : s.customLayerList,
    })),
  addCustomLayer: (layer) =>
    set((s) => {
      const base = s.mapType !== layer.mapNm
        ? { mapType: layer.mapNm, mapTile: 'normal', customLayerList: [] }
        : {};
      return { ...base, customLayerList: [...(base.customLayerList ?? s.customLayerList), layer] };
    }),
  removeCustomLayer: (layer) =>
    set((s) => ({
      customLayerList: s.customLayerList.filter(
        (l) => !(l.type === layer.type && l.mapNm === layer.mapNm),
      ),
    })),
  setSelectAddrInfo: (info) => set({ selectAddrInfo: info }),
  setSelectHeatMapClfy: (clfy) => set({ selectHeatMapClfy: clfy }),
  setSelectHeatMapPeriod: (period) => set({ selectHeatMapPeriod: period }),
  setMapDraw: (mode) => set({ mapDraw: mode }),
  setCustomEventLatLng: (latlng) => set({ customEventLatLng: latlng }),
  setPlayCctvLatLngList: (list) => set({ playCctvLatLngList: list }),
}));
