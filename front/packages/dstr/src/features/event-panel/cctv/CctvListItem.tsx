import { useRef, useState, useEffect } from 'react';
import { useCctvStore } from '../../../entities/cctv/store';
import { useMapStore } from '../../../entities/map/store';
import type { CctvInfo } from '../../../entities/cctv/types';

interface Props {
  item: CctvInfo;
  no: number;
}

export function CctvListItem({ item, no }: Props) {
  const nameRef = useRef<HTMLLIElement>(null);
  const [showTooltip, setShowTooltip] = useState(false);
  const [isOver, setOver] = useState(false);

  const activeCctv = useCctvStore((s) => s.activeCctv);
  const setActiveCctv = useCctvStore((s) => s.setActiveCctv);
  const playGisCctvList = useCctvStore((s) => s.playGisCctvList);
  const setPlayGisCctvList = useCctvStore((s) => s.setPlayGisCctvList);
  const playCctvList = useCctvStore((s) => s.playCctvList);

  const setMapMove = useMapStore((s) => s.setMapMove);

  useEffect(() => {
    if (nameRef.current) {
      setShowTooltip(nameRef.current.scrollWidth > nameRef.current.clientWidth);
    }
  }, []);

  const isPlayActive = playGisCctvList.some((c) => c.facId === item.facId);
  const isInPlayList = playCctvList.some((c) => c.facId === item.facId);

  const togglePlayGisCctv = () => {
    if (isPlayActive) {
      setPlayGisCctvList(playGisCctvList.filter((c) => c.facId !== item.facId));
    } else {
      setPlayGisCctvList([...playGisCctvList, item]);
    }
  };

  const doPlay = (e: React.MouseEvent<HTMLButtonElement>) => {
    if (!item.lat || !item.lng) {
      alert('해당 CCTV는 좌표가 없습니다.');
      return;
    }
    if (isInPlayList) return;

    e.stopPropagation();
    setMapMove({ latlng: [item.lat!, item.lng!], type: 'max_zoom' });
    setActiveCctv(null);
    setTimeout(togglePlayGisCctv, 300);
  };

  const handleRowClick = () => {
    if (activeCctv?.facId === item.facId) {
      setActiveCctv(null);
    } else {
      setActiveCctv(item);
      if (item.lat !== 0 && item.lng !== 0) {
        setMapMove({ latlng: [item.lat!, item.lng!], type: 'max_zoom' });
      }
    }
  };

  return (
    <li>
      <ul
        className={`search_list_item ${activeCctv?.facId === item.facId ? 'active' : ''}`}
        onMouseEnter={() => setOver(true)}
        onMouseLeave={() => setOver(false)}
        onClick={handleRowClick}
      >
        <li>{no}</li>
        <li>{item.facClfyNm}</li>
        <li ref={nameRef}>{item.facNm}</li>
        <li>
          <button
            type="button"
            className={`btn_play ${isPlayActive ? 'active' : ''}`}
            onClick={doPlay}
          />
        </li>
        {isOver && showTooltip && (
          <div className="gis tooltip_text" style={{ left: '170px' }}>
            {item.facNm}
          </div>
        )}
      </ul>
    </li>
  );
}
