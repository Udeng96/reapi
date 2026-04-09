import { useHamsStore } from '../../entities/hams/store';
import { DOOR_LOCK_TYPE, DOOR_STATE_TYPE } from '../../entities/hams/consts';
import type { HamsFacStatus } from '../../entities/hams/types';

// ─── 도어락 박스 ──────────────────────────────────────────────────────────────

function DoorLockBox({ status }: { status: HamsFacStatus | null }) {
  const { setDoorLockModal } = useHamsStore();

  const isConnected = status?.isConnect === true;
  const isLocked = status?.frontDoorLock === DOOR_LOCK_TYPE.LOCK;
  const isUnlocked = status?.frontDoorLock === DOOR_LOCK_TYPE.UNLOCK;
  const hasData = isLocked || isUnlocked;

  const stateClass = isLocked ? 'state_lock' : isUnlocked ? 'state_unlock' : 'state_nodata';
  const valueClass = isLocked ? 'tSky' : isUnlocked ? 'tRed' : 'tGray';
  const stateText = isLocked
    ? "'잠금중' 입니다"
    : isUnlocked
    ? "'잠금해제 중' 입니다"
    : '데이터 값이 없습니다';

  return (
    <div className="realtime_data_box">
      <div className={`realtime_icon ${stateClass}`} />
      <p className="realtime_data_title w106px">앞문 도어락 상태</p>
      <p className={`realtime_state_value ${valueClass}`}>{stateText}</p>
      <div className="btn_lock_group">
        <button
          type="button"
          className="btn_lock"
          disabled={!isConnected || isLocked || !hasData}
          onClick={() => setDoorLockModal(true, 'lock')}
        >
          잠금
        </button>
        <button
          type="button"
          className="btn_lock"
          disabled={!isConnected || isUnlocked || !hasData}
          onClick={() => setDoorLockModal(true, 'unlock')}
        >
          잠금해제
        </button>
      </div>
    </div>
  );
}

// ─── 도어 개폐 박스 ───────────────────────────────────────────────────────────

function DoorStateBox({ status }: { status: HamsFacStatus | null }) {
  const { setDoorStatModal } = useHamsStore();

  const isOpen = status?.frontDoor === DOOR_STATE_TYPE.OPEN;
  const isClose = status?.frontDoor === DOOR_STATE_TYPE.CLOSE;

  const stateClass = isOpen ? 'state_door_open' : isClose ? 'state_door_close' : 'state_nodata';
  const valueClass = isOpen ? 'tRed' : isClose ? 'tSky' : 'tGray';
  const stateText = isOpen
    ? "문이 '열려'있습니다"
    : isClose
    ? "문이 '닫혀'있습니다"
    : '데이터 값이 없습니다';

  return (
    <div className="realtime_data_box">
      <div className={`realtime_icon ${stateClass}`} />
      <p className="realtime_data_title w72px">앞문 개폐 상태</p>
      <p className={`realtime_state_value ${valueClass}`}>{stateText}</p>
      <button
        type="button"
        className="btn_view_history"
        onClick={() => setDoorStatModal(true)}
      >
        앞문 개폐 이력 보기
      </button>
    </div>
  );
}

// ─── 연결 상태 박스 ───────────────────────────────────────────────────────────

function ConnectionBox({ status }: { status: HamsFacStatus | null }) {
  const isConnect = status?.isConnect ?? null;

  const stateClass =
    isConnect === true ? 'state_connected' : isConnect === false ? 'state_disconnected' : 'state_nodata';
  const valueClass = isConnect === true ? 'tMint' : isConnect === false ? 'tPurple' : 'tGray';
  const connectTextClass = isConnect === true ? 'state_connected' : 'state_unconnected';

  const stateText =
    isConnect === true
      ? "'연결' 되었습니다"
      : isConnect === false
      ? "'미연결' 되었습니다"
      : '데이터 값이 없습니다';

  return (
    <div className="realtime_data_box">
      <div className={`realtime_icon ${stateClass}`} />
      <p className="realtime_data_title w112px">연결 상태</p>
      <p className={`realtime_state_value ${valueClass}`}>{stateText}</p>
      <p className={`connect_text ${connectTextClass}`}>
        {isConnect ? '_Connected' : '_Unconnected'}
      </p>
    </div>
  );
}

// ─── 온습도 박스 ──────────────────────────────────────────────────────────────

function TempHumiBox({ status }: { status: HamsFacStatus | null }) {
  const temp = status?.temp ?? null;
  const humi = status?.humi ?? null;

  const splitVal = (v: number | null) => {
    if (v === null) return { int: 'noData', dec: '' };
    const [i, d] = String(v).split('.');
    return { int: i, dec: d ? '.' + d : '.0' };
  };

  const t = splitVal(temp);
  const h = splitVal(humi);
  const isHot = typeof temp === 'number' && temp > 30;

  return (
    <div className="realtime_data_box seperate_box">
      {/* 온도 */}
      <div className="seperate_area">
        <p className="realtime_data_title w112px">
          온도 <span>Temperature</span>
        </p>
        <p className={`temp_value ${isHot ? 'tRed' : 'tBlue'}`}>
          {t.int}
          <span>{t.dec}</span>
          <span className="unit_text">{temp !== null ? '℃' : ''}</span>
        </p>
        <p className="limit_temp_text">
          상한 온도 <span>40.0</span>℃
        </p>
      </div>
      {/* 습도 */}
      <div className="seperate_area">
        <p className="realtime_data_title w96px">
          습도 <span>Humidity</span>
        </p>
        <p className="humid_value">
          {h.int}
          <span>{h.dec}</span>
          <span className="unit_text">{humi !== null ? '%' : ''}</span>
        </p>
      </div>
    </div>
  );
}

// ─── RealtimePanel ────────────────────────────────────────────────────────────

export function RealtimePanel() {
  const { selectedHams } = useHamsStore();
  const status = selectedHams?.status ?? null;

  return (
    <>
      <p className="info_title title_realtime_data">실시간 데이터</p>
      <div className="realtime_data_frame">
        <div className="realtime_data_box_group">
          <DoorLockBox status={status} />
          <DoorStateBox status={status} />
          <ConnectionBox status={status} />
          <TempHumiBox status={status} />
        </div>
      </div>
    </>
  );
}
