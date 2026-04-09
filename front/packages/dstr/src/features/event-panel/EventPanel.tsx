import { useState } from 'react';
import { useEventStore } from '../../entities/event/store';
import { useEvents } from '../../entities/event/queries';
import { DstrEvetTab } from './DstrEvetTab';
import { CctvTab } from './CctvTab';
import { MobileTab } from './MobileTab';

type TabType = 'evet' | 'cctv' | 'mobile';

export function EventPanel({
  isOpen,
  onToggle,
}: {
  isOpen: boolean;
  onToggle: () => void;
}) {
  const [tabType, setTabType] = useState<TabType>('evet');

  const wsEventReceive = useEventStore((s) => s.wsEventReceive);
  const setWsEventReceive = useEventStore((s) => s.setWsEventReceive);
  const eventPayload = useEventStore((s) => s.eventPayload);

  // 최초 마운트 시 이벤트 목록 로드
  useEvents(eventPayload);

  return (
    <aside
      className="dash_container"
      style={{
        width: 533,
        height: 1024,
        backgroundColor: '#070914',
        borderRight: '1px solid #41434c',
        position: 'absolute',
        bottom: 0,
        left: isOpen ? 0 : -533,
        transition: 'left 0.8s ease',
        zIndex: 3000,
      }}
    >
      <button
        type="button"
        className={`btn_tab ${isOpen ? '' : 'clicked open_tab'}`}
        onClick={onToggle}
      />

      {/* 헤더 */}
      <header className="dash_header">
        <h1 className="dash_title">
          재난관제<span>Dashboard</span>
        </h1>
        <button
          type="button"
          className={`btn_event_receive ${wsEventReceive ? 'active' : ''}`}
          onClick={() => setWsEventReceive(!wsEventReceive)}
        />
        <div className="btn_menu_tab_group">
          <button
            type="button"
            className={`btn_menu_tab btn_event ${tabType === 'evet' ? 'active' : ''}`}
            onClick={() => setTabType('evet')}
          >
            재난이벤트
          </button>
          <button
            type="button"
            className={`btn_menu_tab btn_cctv ${tabType === 'cctv' ? 'active' : ''}`}
            onClick={() => setTabType('cctv')}
          >
            CCTV 검색
          </button>
          <button
            type="button"
            className={`btn_menu_tab btn_mobile state_video_on ${tabType === 'mobile' ? 'active' : ''}`}
            onClick={() => setTabType('mobile')}
          >
            모바일 출동영상
          </button>
        </div>
      </header>

      {/* 바디 */}
      <div className="dash_body">
        {tabType === 'evet' ? (
          <DstrEvetTab />
        ) : tabType === 'cctv' ? (
          <CctvTab />
        ) : tabType === 'mobile' ? (
          <MobileTab />
        ) : null}
      </div>
    </aside>
  );
}
