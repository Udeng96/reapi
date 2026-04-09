import { useRef, useState } from 'react';
import { useEventStore } from '../../../entities/event/store';
import { useMapStore } from '../../../entities/map/store';
import type { VwEvent } from '../../../entities/event/types';

/** outbDtm(yyyyMMddHHmmss) → "MM-dd HH:mm:ss" 형식 변환 */
function formatDtm(outbDtm: string | null): string {
  if (!outbDtm) return '';
  const s = outbDtm.substring(4);
  return `${s.substring(0, 2)}-${s.substring(2, 4)} ${s.substring(4, 6)}:${s.substring(6, 8)}:${s.substring(8, 10)}`;
}

interface Props {
  item: VwEvent;
  isModal: boolean;
}

export function DstrListItem({ item, isModal }: Props) {
  const addrRef = useRef<HTMLLIElement>(null);
  const [showTooltip, setShowTooltip] = useState(false);

  const activeItem = useEventStore((s) => s.activeItem);
  const setActiveItem = useEventStore((s) => s.setActiveItem);
  const activeModalItem = useEventStore((s) => s.activeModalItem);
  const setActiveModalItem = useEventStore((s) => s.setActiveModalItem);
  const setMapDraw = useMapStore((s) => s.setMapDraw);
  const setCustomEventLatLng = useMapStore((s) => s.setCustomEventLatLng);
  const setMapMove = useMapStore((s) => s.setMapMove);

  const currentActive = isModal ? activeModalItem : activeItem;
  const isActive = currentActive === item.statEvetOutbSeqn;

  const handleClick = () => {
    setMapDraw('none');
    setCustomEventLatLng(null);

    if (isModal) {
      if (isActive) {
        setActiveModalItem(null);
      } else {
        setActiveModalItem(item.statEvetOutbSeqn);
      }
    } else {
      if (isActive) {
        setActiveItem(null);
      } else {
        setActiveItem(item.statEvetOutbSeqn);
        if (item.lat && item.lng) {
          setMapMove({
            latlng: [parseFloat(item.lat), parseFloat(item.lng)],
            type: 'event',
          });
        }
      }
    }
  };

  return (
    <li>
      <ul
        className={`search_list_item ${isActive ? 'active' : ''} ${isModal ? 'modal' : ''}`}
        onClick={handleClick}
      >
        <li>{item.locationNm}</li>
        <li>{item.eventNm}</li>
        <li
          ref={addrRef}
          onMouseEnter={() => {
            if (addrRef.current) {
              setShowTooltip(addrRef.current.scrollWidth > addrRef.current.clientWidth);
            }
          }}
          onMouseLeave={() => setShowTooltip(false)}
        >
          {item.addr}
        </li>
        <li>{formatDtm(item.outbDtm)}</li>
        {showTooltip && (
          <div className="gis tooltip_text" style={{ left: '149px' }}>
            {item.addr}
          </div>
        )}
      </ul>
    </li>
  );
}
