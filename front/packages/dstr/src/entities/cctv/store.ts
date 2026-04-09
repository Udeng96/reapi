import { create } from 'zustand';
import type { CctvInfo, CctvClfy } from './types';

const MAX_PLAY_CNT = 4;

interface CctvViewState {
  selectClfy: CctvClfy | null;
  word: string;
  activeCctv: CctvInfo | null;
  activeClusterLatLon: [number, number] | null;
  activeClusterMarkerList: CctvInfo[];

  /** GIS 팝업에서 재생 중인 CCTV 목록 (최대 4개) */
  playGisCctvList: CctvInfo[];

  /** 우측 패널 재생 목록 */
  playCctvList: CctvInfo[];

  /** 전체화면 CCTV ID */
  fullScreenId: string;

  // Actions
  setSelectClfy: (clfy: CctvClfy | null) => void;
  setWord: (word: string) => void;
  setActiveCctv: (cctv: CctvInfo | null) => void;
  setActiveClusterLatLon: (latlng: [number, number] | null) => void;
  setActiveClusterMarkerList: (list: CctvInfo[]) => void;
  setPlayGisCctvList: (list: CctvInfo[]) => void;
  setPlayCctvList: (list: CctvInfo[]) => void;
  setFullScreenId: (id: string) => void;
}

export const useCctvStore = create<CctvViewState>((set) => ({
  selectClfy: { id: '', nm: '전체' },
  word: '',
  activeCctv: null,
  activeClusterLatLon: null,
  activeClusterMarkerList: [],
  playGisCctvList: [],
  playCctvList: [],
  fullScreenId: '',

  setSelectClfy: (clfy) => set({ selectClfy: clfy }),
  setWord: (word) => set({ word }),
  setActiveCctv: (cctv) => set({ activeCctv: cctv }),
  setActiveClusterLatLon: (latlng) => set({ activeClusterLatLon: latlng }),
  setActiveClusterMarkerList: (list) => set({ activeClusterMarkerList: list }),
  setPlayGisCctvList: (list) =>
    set({ playGisCctvList: list.length <= MAX_PLAY_CNT ? list : [] }),
  setPlayCctvList: (list) => set({ playCctvList: list }),
  setFullScreenId: (id) => set({ fullScreenId: id }),
}));
