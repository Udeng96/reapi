import { create } from 'zustand';
import type { CctvInfo, TransCycleOption, PageTransType, MenuListResult } from './types';
import { TransCycleOptions, PageTransType as PT } from './types';

export interface DragTargetInfo {
  type: 'clfy' | 'cctv';
  clfyId?: string;
  cctv?: CctvInfo;
  cctvList?: CctvInfo[];
}

interface MonitorState {
  gridRatio: [number, number];
  isMenuOpen: boolean;
  isPageTrans: PageTransType;
  pageTransCycle: TransCycleOption;
  currentPagePlayList: (CctvInfo | null)[];
  allPagePlayList: (CctvInfo | null)[][];
  currentPageIndex: number;
  searchWord: string;
  dragTarget: DragTargetInfo | null;

  setGridRatio: (ratio: [number, number]) => void;
  setMenuOpen: (open: boolean) => void;
  setPageTrans: (type: PageTransType) => void;
  setPageTransCycle: (cycle: TransCycleOption) => void;
  setCurrentPagePlayList: (list: (CctvInfo | null)[]) => void;
  setAllPagePlayList: (list: (CctvInfo | null)[][]) => void;
  setCurrentPageIndex: (index: number) => void;
  setSearchWord: (word: string) => void;
  setDragTarget: (target: DragTargetInfo | null) => void;
  replaceCctvAtIndex: (index: number, cctv: CctvInfo) => void;
}

export const useMonitorStore = create<MonitorState>((set) => ({
  gridRatio: [3, 3],
  isMenuOpen: true,
  isPageTrans: PT.NONE,
  pageTransCycle: TransCycleOptions[2],
  currentPagePlayList: [],
  allPagePlayList: [],
  currentPageIndex: 0,
  searchWord: '',
  dragTarget: null,

  setGridRatio: (ratio) => set({ gridRatio: ratio }),
  setMenuOpen: (open) => set({ isMenuOpen: open }),
  setPageTrans: (type) => set({ isPageTrans: type }),
  setPageTransCycle: (cycle) => set({ pageTransCycle: cycle }),
  setCurrentPagePlayList: (list) => set({ currentPagePlayList: list }),
  setAllPagePlayList: (list) => set({ allPagePlayList: list }),
  setCurrentPageIndex: (index) => set({ currentPageIndex: index }),
  setSearchWord: (word) => set({ searchWord: word }),
  setDragTarget: (target) => set({ dragTarget: target }),
  replaceCctvAtIndex: (index, cctv) =>
    set((state) => {
      const newList = [...state.currentPagePlayList];
      newList[index] = cctv;
      return { currentPagePlayList: newList, isPageTrans: PT.EACH };
    }),
}));
