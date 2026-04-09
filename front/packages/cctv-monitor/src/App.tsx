import { useEffect, useRef } from 'react';
import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import { useMonitorStore } from './entities/monitor/store';
import { useMonitorList } from './entities/monitor/queries';
import { getAllPagePlayList } from './entities/monitor/getAllCctvList';
import { CctvGrid } from './features/cctv-grid/CctvGrid';
import { MenuPanel } from './features/cctv-menu/MenuPanel';
import { TopMenu } from './features/cctv-menu/TopMenu';
import { TopBar } from './features/cctv-menu/TopBar';
import { PageTransType } from './entities/monitor/types';

export default function App() {
  const {
    gridRatio,
    setGridRatio,
    setCurrentPagePlayList,
    setAllPagePlayList,
    setCurrentPageIndex,
    setPageTrans,
    allPagePlayList,
    currentPageIndex,
    isPageTrans,
    pageTransCycle,
  } = useMonitorStore();

  const { data: menuList } = useMonitorList();
  const intervalRef = useRef<ReturnType<typeof setInterval> | null>(null);

  // URL params로 그리드 사이즈 설정
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const x = Math.min(Number(params.get('x')) || 3, 5);
    const y = Math.min(Number(params.get('y')) || 3, 5);
    setGridRatio([x, y]);
  }, []);

  // 초기 로딩 시 빈 그리드로 시작 — allPagePlayList만 세팅, 화면은 비워둠
  useEffect(() => {
    if (!menuList || menuList.length === 0) return;
    const gridAmount = gridRatio[0] * gridRatio[1];
    const pages = getAllPagePlayList(menuList, gridAmount);
    setAllPagePlayList(pages);
    setCurrentPagePlayList(Array(gridAmount).fill(null));
    setCurrentPageIndex(0);
  }, [menuList, gridRatio]);

  // 자동전환 사이클 관리
  useEffect(() => {
    if (intervalRef.current) {
      clearInterval(intervalRef.current);
      intervalRef.current = null;
    }

    if (isPageTrans === PageTransType.PLAY && allPagePlayList.length > 1) {
      const cycle = Number(pageTransCycle.value) || 300000;
      intervalRef.current = setInterval(() => {
        const store = useMonitorStore.getState();
        const nextIndex = (store.currentPageIndex + 1) % store.allPagePlayList.length;
        store.setCurrentPageIndex(nextIndex);
        store.setCurrentPagePlayList(store.allPagePlayList[nextIndex]);
      }, cycle);
    }

    return () => {
      if (intervalRef.current) clearInterval(intervalRef.current);
    };
  }, [isPageTrans, allPagePlayList.length, pageTransCycle]);

  return (
    <DndProvider backend={HTML5Backend}>
      <div className="monitor dashWrap">
        <TopBar />
        <CctvGrid />
        <MenuPanel />
        <TopMenu />
      </div>
    </DndProvider>
  );
}
