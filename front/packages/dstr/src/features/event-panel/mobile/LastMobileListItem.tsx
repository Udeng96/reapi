import { useMobileStore } from '../../../entities/mobile/store';
import { useMapStore } from '../../../entities/map/store';
import type { MobileInfo } from '../../../entities/mobile/types';

const MAX_PAST_PLAYER_CNT = 4;

interface Props {
  item: MobileInfo;
  index: number;
}

export function LastMobileListItem({ item, index }: Props) {
  const activePastItem = useMobileStore((s) => s.activePastItem);
  const setActivePastItem = useMobileStore((s) => s.setActivePastItem);
  const activePastBtnList = useMobileStore((s) => s.activePastBtnList);
  const setActivePastBtnList = useMobileStore((s) => s.setActivePastBtnList);

  const setMapMove = useMapStore((s) => s.setMapMove);

  const isActive = activePastItem?.id === item.id;
  const isPlaying = activePastBtnList.some((m) => m.id === item.id);

  const handleRowClick = () => {
    if (isActive) {
      setActivePastItem(null);
    } else {
      setActivePastItem(item);
    }
  };

  const handlePlayClick = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();
    if (isPlaying) {
      setActivePastBtnList(activePastBtnList.filter((m) => m.id !== item.id));
    } else {
      // MAX 초과 시 가장 오래된 항목 제거 (FIFO)
      const newList = [...activePastBtnList];
      if (newList.length >= MAX_PAST_PLAYER_CNT) {
        newList.splice(0, 1);
      }
      newList.push(item);
      setActivePastBtnList(newList);
      if (item.lat && item.lng) {
        setMapMove({ latlng: [item.lat, item.lng], type: 'max_zoom' });
      }
    }
  };

  const formatDtm = (dtm: string | null) => {
    if (!dtm) return '';
    return dtm.replace('T', ' ').substring(0, 19);
  };

  return (
    <li
      className={`past_list_item ${isActive ? 'active' : ''}`}
      onClick={handleRowClick}
    >
      <span className="list_no">{index}</span>
      <span className="list_dtm">{formatDtm(item.userDtm)}</span>
      <span className="list_user">
        {item.userDept}-{item.userNm}
      </span>
      <button
        type="button"
        className={`btn_play ${isPlaying ? 'active' : ''}`}
        onClick={handlePlayClick}
      />
    </li>
  );
}
