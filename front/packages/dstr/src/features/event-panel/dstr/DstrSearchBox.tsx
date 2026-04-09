import { useEventStore } from '../../../entities/event/store';
import type { Search } from '../../../entities/event/types';

const BOX_TYPE = {
  LOCATION: 'location',
  EVENT_TYPE: 'eventType',
} as const;

interface Props {
  searchBoxType: string;
  searchOptions: Search[] | null;
  /** 현재 체크된 항목 목록 */
  chkOptionsList: Search[];
  /** 제공 시 store setter 대신 이 콜백으로 체크 상태를 업데이트 */
  onSetChecked?: (list: Search[]) => void;
  /** 헤더에 표시할 최대 토큰 수 (기본값: 2) */
  maxVisible?: number;
}

const ALL_ITEM: Search = { id: 'all', nm: '전체' };

export function DstrSearchBox({ searchBoxType, searchOptions, chkOptionsList, onSetChecked, maxVisible = 2 }: Props) {
  const activeSearchBox = useEventStore((s) => s.activeSearchBox);
  const setActiveSearchBox = useEventStore((s) => s.setActiveSearchBox);
  const setCheckedLocaList = useEventStore((s) => s.setCheckedLocaList);
  const setCheckedTypeList = useEventStore((s) => s.setCheckedTypeList);

  const isOpen = activeSearchBox === searchBoxType;

  const setChecked = (list: Search[]) => {
    if (onSetChecked) {
      onSetChecked(list);
      return;
    }
    if (searchBoxType === BOX_TYPE.LOCATION) {
      setCheckedLocaList(list);
    } else {
      setCheckedTypeList(list);
    }
  };

  const toggle = () => {
    setActiveSearchBox(isOpen ? 'none' : searchBoxType);
  };

  const isChecked = (id: string) => {
    if (id === 'all') return chkOptionsList.length === 0;
    if (chkOptionsList.length === 0) return true;
    return chkOptionsList.some((item) => item.id === id);
  };

  const handleCheck = (item: Search) => {
    if (item.id === 'all') {
      setChecked([]);
      return;
    }
    // 전체 상태에서 개별 항목 클릭 → 해당 항목만 제외하고 나머지 전체 선택
    if (chkOptionsList.length === 0) {
      setChecked(options.filter((o) => o.id !== item.id));
      return;
    }
    const exists = chkOptionsList.some((c) => c.id === item.id);
    if (exists) {
      const next = chkOptionsList.filter((c) => c.id !== item.id);
      // 모두 체크 해제되면 전체 상태로
      setChecked(next.length === options.length ? [] : next);
    } else {
      const next = [...chkOptionsList, item];
      // 모든 항목이 개별 선택되면 전체 상태로
      setChecked(next.length === options.length ? [] : next);
    }
  };

  const visibleCount = Math.min(chkOptionsList.length, maxVisible);

  const options = searchOptions ?? [];
  const allLabel = searchBoxType.includes(BOX_TYPE.LOCATION) ? '#화성시_전체' : '#전체';

  return (
    <div className={`search_select_frame${isOpen ? ' active' : ''}`}>
      <header className="search_select_header" onClick={toggle}>
        <div className="search_tag_area">
          <div className="search_tag_box_frame">
            {chkOptionsList.length === 0 ? (
              <div className="search_tag_box">
                <p className="search_tag_name">{allLabel}</p>
              </div>
            ) : (
              chkOptionsList.slice(0, visibleCount).map((item) => (
                <div key={item.id} className="search_tag_box" onClick={(e) => e.stopPropagation()}>
                  <p className="search_tag_name">{`#${item.nm}`}</p>
                  <button
                    type="button"
                    className="btn_search_delete"
                    onClick={() => handleCheck(item)}
                  />
                </div>
              ))
            )}
          </div>
          {visibleCount < chkOptionsList.length && (
            <div className="tag_func_area">
              <div className="tag_more_text">...</div>
              <div className="tag_count_num">{`+${chkOptionsList.length - visibleCount}`}</div>
            </div>
          )}
        </div>
        <button
          type="button"
          className={`btn_search_reset${chkOptionsList.length === 0 ? ' dimmed' : ''}`}
          onClick={(e) => { setChecked([]); e.stopPropagation(); }}
        />
        <button
          type="button"
          className="btn_search_arrow"
          onClick={(e) => { e.stopPropagation(); toggle(); }}
        />
      </header>
      <div className="search_select_body" style={{ display: isOpen ? 'block' : 'none' }}>
        <div className="total_check_area">
          <div className="event_check_box">
            <label className="container">
              <input
                type="checkbox"
                checked={isChecked('all')}
                onChange={() => handleCheck(ALL_ITEM)}
              />
              <span className="checkmark" />
              <div className="check_name">
                {searchBoxType === BOX_TYPE.LOCATION ? '화성시_전체' : '전체'}
              </div>
            </label>
          </div>
        </div>
        <div className="each_check_area">
          {options.map((option) => (
            <div key={option.id} className="event_check_box">
              <label className="container">
                <input
                  type="checkbox"
                  checked={isChecked(option.id)}
                  onChange={() => handleCheck(option)}
                />
                <span className="checkmark" />
                <div className="check_name">{`#${option.nm}`}</div>
              </label>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
