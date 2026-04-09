import { create } from 'zustand';
import type { MobileInfo, FullPopupInfo, MobilePopupSize } from './types';
import dayjs from 'dayjs';

interface MobileViewState {
  activeLiveItem: MobileInfo | null;
  activeLiveBtnList: MobileInfo[];
  activePastItem: MobileInfo | null;
  activePastBtnList: MobileInfo[];
  activeMobileClusterLatLon: [number, number] | null;
  activeMobileClusterMarkerList: MobileInfo[];
  fullPopupInfo: FullPopupInfo;
  mobilePopupSize: MobilePopupSize;
  startDtm: string;
  endDtm: string;

  // Actions
  setActiveLiveItem: (item: MobileInfo | null) => void;
  setActiveLiveBtnList: (list: MobileInfo[]) => void;
  setActivePastItem: (item: MobileInfo | null) => void;
  setActivePastBtnList: (list: MobileInfo[]) => void;
  setActiveMobileClusterLatLon: (latlng: [number, number] | null) => void;
  setActiveMobileClusterMarkerList: (list: MobileInfo[]) => void;
  setFullPopupInfo: (info: FullPopupInfo) => void;
  setMobilePopupSize: (size: MobilePopupSize) => void;
  setStartDtm: (dtm: string) => void;
  setEndDtm: (dtm: string) => void;
}

const defaultStart = dayjs().subtract(3, 'month').format('YYYY-MM-DD');
const defaultEnd = dayjs().format('YYYY-MM-DD');

export const useMobileStore = create<MobileViewState>((set) => ({
  activeLiveItem: null,
  activeLiveBtnList: [],
  activePastItem: null,
  activePastBtnList: [],
  activeMobileClusterLatLon: null,
  activeMobileClusterMarkerList: [],
  fullPopupInfo: { isOpen: false, dtm: null, userDept: null, userNm: null, id: '', rtsp: null },
  mobilePopupSize: { height: 0, width: 0 },
  startDtm: defaultStart,
  endDtm: defaultEnd,

  setActiveLiveItem: (item) => set({ activeLiveItem: item }),
  setActiveLiveBtnList: (list) => set({ activeLiveBtnList: list }),
  setActivePastItem: (item) => set({ activePastItem: item }),
  setActivePastBtnList: (list) => set({ activePastBtnList: list }),
  setActiveMobileClusterLatLon: (latlng) => set({ activeMobileClusterLatLon: latlng }),
  setActiveMobileClusterMarkerList: (list) => set({ activeMobileClusterMarkerList: list }),
  setFullPopupInfo: (info) => set({ fullPopupInfo: info }),
  setMobilePopupSize: (size) => set({ mobilePopupSize: size }),
  setStartDtm: (dtm) => set({ startDtm: dtm }),
  setEndDtm: (dtm) => set({ endDtm: dtm }),
}));
