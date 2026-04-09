import { useState } from 'react';
import { useDrop } from 'react-dnd';
import type { CctvInfo } from '../../entities/monitor/types';
import type { DndCctvItem } from '../../entities/monitor/dndTypes';
import { DND_ITEM_TYPE } from '../../entities/monitor/dndTypes';
import { useMonitorStore } from '../../entities/monitor/store';
import { PtzControlBox } from '../ptz-control/PtzControlBox';

interface Props {
  cctv: CctvInfo | null;
  index: number;
  isFull: boolean;
  gridRatio: [number, number];
}

export function CctvFrame({ cctv, index, isFull, gridRatio }: Props) {
  const [showPtz, setShowPtz] = useState(false);
  const { replaceCctvAtIndex } = useMonitorStore();

  // 개별 CCTV 드롭
  const [{ isOver }, drop] = useDrop(() => ({
    accept: DND_ITEM_TYPE.CCTV,
    drop: (item: DndCctvItem) => {
      replaceCctvAtIndex(index, item.cctv);
    },
    collect: (monitor) => ({ isOver: monitor.isOver() }),
  }), [index]);

  const width = isFull
    ? Math.floor(1922 / gridRatio[0] - 5)
    : Math.floor(1657 / gridRatio[0] - 5);
  const height = isFull
    ? Math.floor(1080 / gridRatio[1])
    : Math.floor(936 / gridRatio[1]);

  return (
    <div
      ref={drop}
      className="cctv_frame"
      style={{
        width: `${width}px`,
        height: `${height}px`,
        outline: isOver ? '2px solid #5d73f0' : 'none',
      }}
    >
      <header className="cctv_header">
        {cctv ? cctv.facNm : ''}
      </header>
      <div
        className={`cctv_body ${cctv?.ptzYn ? 'ptz_in' : ''}`}
        onMouseOver={() => setShowPtz(true)}
        onMouseLeave={() => setShowPtz(false)}
      >
        {cctv === null ? (
          <div className="cctv_default_area">
            <p className="cctv_default_message">
              CCTV 목록에서 드래그&드롭하여<br />영상을 재생시켜주세요.
            </p>
          </div>
        ) : (
          <>
            <div className="cctv_in">
              {/* TODO: RTSP Player 연동 */}
              <div style={{
                width: '100%',
                height: '100%',
                backgroundColor: '#000',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                color: '#666',
              }}>
                {cctv.mgrNo || 'No Stream'}
              </div>
            </div>
            {cctv.ptzYn && (
              <PtzControlBox isShow={showPtz} cctvId={cctv.mgrNo} />
            )}
          </>
        )}
      </div>
    </div>
  );
}
