import { useMobileStore } from '../../../entities/mobile/store';
import { useMapStore } from '../../../entities/map/store';
import type { MobileInfo } from '../../../entities/mobile/types';

interface Props {
  item: MobileInfo;
  index: number;
}

export function NowMobileListItem({ item, index }: Props) {
  const activeLiveItem = useMobileStore((s) => s.activeLiveItem);
  const setActiveLiveItem = useMobileStore((s) => s.setActiveLiveItem);
  const activeLiveBtnList = useMobileStore((s) => s.activeLiveBtnList);
  const setActiveLiveBtnList = useMobileStore((s) => s.setActiveLiveBtnList);

  const setMapMove = useMapStore((s) => s.setMapMove);

  const isActive = activeLiveItem?.id === item.id;
  const isPlaying = activeLiveBtnList.some((m) => m.id === item.id);

  const handleRowClick = () => {
    if (isActive) {
      setActiveLiveItem(null);
    } else {
      setActiveLiveItem(item);
      if (item.lat && item.lng) {
        setMapMove({ latlng: [item.lat, item.lng], type: 'max_zoom' });
      }
    }
  };

  const handlePlayClick = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();
    if (isPlaying) {
      setActiveLiveBtnList(activeLiveBtnList.filter((m) => m.id !== item.id));
    } else {
      setActiveLiveBtnList([...activeLiveBtnList, item]);
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
      className={`live_list_item ${isActive ? 'active' : ''}`}
      onClick={handleRowClick}
    >
      <span className="list_no">{index}</span>
      <span className="list_dtm">{formatDtm(item.userDtm)}</span>
      <span className="list_user">{item.userDept}-{item.userNm}</span>
      <button
        type="button"
        className={`btn_play ${isPlaying ? 'active' : ''}`}
        onClick={handlePlayClick}
      />
    </li>
  );
}
