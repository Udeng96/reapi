import { useEffect } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { useHamsStore } from './entities/hams/store';
import { connectHamsWs, disconnectHamsWs } from './entities/hams/ws';
import { MenuPanel } from './features/hams-menu/MenuPanel';
import { GisMap } from './features/hams-gis/GisMap';
import { DashInfo } from './features/hams-dash/DashInfo';
import { RealtimePanel } from './features/hams-dash/RealtimePanel';
import { CctvPanel, CctvPopup } from './features/hams-dash/CctvPanel';
import { DoorLockModal } from './features/hams-dash/DoorLockModal';
import { DoorStatModal } from './features/hams-dash/DoorStatModal';

export default function App() {
  const { selectedHams, setWsConnected } = useHamsStore();
  const queryClient = useQueryClient();

  // DOE WebSocket 연결 — 앱 마운트 시 1회 연결, 언마운트 시 해제
  useEffect(() => {
    connectHamsWs(
      queryClient,
      () => setWsConnected(true),
      () => setWsConnected(false),
    );
    return () => {
      disconnectHamsWs();
      setWsConnected(false);
    };
  }, [queryClient, setWsConnected]);

  const isConnected = selectedHams?.status?.isConnect ?? false;

  return (
    <div className="hams dashWrap">
      {/* 메뉴 */}
      <MenuPanel />

      {/* GIS 지도 */}
      <GisMap />

      {/* 대시보드 */}
      <section className="hams dash_container">
        <h2 className={`dash_title state_${isConnected ? 'connected' : 'disconnected'}`}>
          {selectedHams?.facNm ?? ''}
        </h2>
        <DashInfo />
        <RealtimePanel />
        <CctvPanel />
      </section>

      {/* 팝업 & 모달 */}
      <CctvPopup />
      <DoorLockModal />
      <DoorStatModal />
    </div>
  );
}
