import { create } from 'zustand';
import type { HamsGisFacInfo, HamsCctvInfo } from './types';

interface HamsStore {
  // 선택된 국사
  selectedHams: HamsGisFacInfo | null;
  setSelectedHams: (hams: HamsGisFacInfo | null) => void;

  // 선택된 CCTV
  selectedCctv: HamsCctvInfo | null;
  setSelectedCctv: (cctv: HamsCctvInfo | null) => void;

  // 모달 상태
  isDoorLockModalOpen: boolean;
  isDoorLockAction: 'lock' | 'unlock';
  setDoorLockModal: (open: boolean, action?: 'lock' | 'unlock') => void;

  isDoorStatModalOpen: boolean;
  setDoorStatModal: (open: boolean) => void;

  isCctvPopupOpen: boolean;
  setCctvPopup: (open: boolean) => void;

  // WebSocket 연결 상태
  wsConnected: boolean;
  setWsConnected: (connected: boolean) => void;
}

export const useHamsStore = create<HamsStore>((set) => ({
  selectedHams: null,
  setSelectedHams: (hams) => set({ selectedHams: hams, selectedCctv: null }),

  selectedCctv: null,
  setSelectedCctv: (cctv) => set({ selectedCctv: cctv }),

  isDoorLockModalOpen: false,
  isDoorLockAction: 'lock',
  setDoorLockModal: (open, action = 'lock') =>
    set({ isDoorLockModalOpen: open, isDoorLockAction: action }),

  isDoorStatModalOpen: false,
  setDoorStatModal: (open) => set({ isDoorStatModalOpen: open }),

  isCctvPopupOpen: false,
  setCctvPopup: (open) => set({ isCctvPopupOpen: open }),

  wsConnected: false,
  setWsConnected: (connected) => set({ wsConnected: connected }),
}));
