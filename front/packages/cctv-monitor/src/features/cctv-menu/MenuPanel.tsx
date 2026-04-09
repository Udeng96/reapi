import { useState, useRef } from 'react';
import { useDrag } from 'react-dnd';
import Select, { type StylesConfig } from 'react-select';
import { useMonitorStore } from '../../entities/monitor/store';
import { useMonitorList } from '../../entities/monitor/queries';
import { DND_ITEM_TYPE } from '../../entities/monitor/dndTypes';
import { TransCycleOptions } from '../../entities/monitor/types';
import type { CctvInfo, MenuListResult, TransCycleOption } from '../../entities/monitor/types';

const MenuSelectStyle: StylesConfig<TransCycleOption> = {
  option: (provided, state) => ({
    ...provided,
    color: state.isSelected ? '#ff6f26' : '#fafcfc',
    backgroundColor: state.isSelected ? '#452019' : '#11111a',
    paddingTop: 9,
    paddingLeft: 17,
    height: '31px',
    cursor: 'pointer',
    '&:hover': {
      backgroundColor: state.isSelected ? '#452019' : '#282a38',
    },
  }),
  dropdownIndicator: (provided) => ({
    ...provided,
    color: '#c7c8c9',
    padding: '6px 9px 8px 8px',
    '&:hover': { color: '#ff6f26' },
  }),
  control: (provided, state) => ({
    ...provided,
    width: '202px',
    height: '38px',
    minHeight: '38px',
    color: '#f7f7f7',
    borderRadius: '6px',
    backgroundColor: state.isFocused ? '#11111a' : '#22232e',
    borderColor: state.isFocused ? '#db5c18' : '#3d3f4d',
    boxShadow: 'none',
    cursor: 'pointer',
    '&:hover': {
      borderColor: state.isFocused ? '#db5c18' : '#4f5163',
      backgroundColor: state.isFocused ? '#11111a' : '#22232e',
    },
  }),
  menu: (provided) => ({
    ...provided,
    color: '#f7f7f7',
    backgroundColor: '#11111a',
    border: '1px solid #db5c18',
    borderRadius: '6px',
    fontSize: '12px',
    fontFamily: 'NotoSansCJKR',
    margin: '1px 0',
    zIndex: 9999,
  }),
  menuList: (base) => ({
    ...base,
    '::-webkit-scrollbar': { width: '3px', height: '0px' },
    '::-webkit-scrollbar-track': { background: 'rgba(235,242,245,0.1)', borderRadius: '1.5px' },
    '::-webkit-scrollbar-thumb': { background: '#db5c18', borderRadius: '1.5px' },
  }),
  singleValue: (provided) => ({
    ...provided,
    color: '#f7f7f7',
    fontSize: '12px',
    fontFamily: 'NotoSansCJKR',
  }),
  indicatorSeparator: () => ({ display: 'none' }),
};

function DraggableCctvNode({ cctv, searchWord }: { cctv: CctvInfo; searchWord: string }) {
  const [{ isDragging }, drag] = useDrag(() => ({
    type: DND_ITEM_TYPE.CCTV,
    item: { type: DND_ITEM_TYPE.CCTV, cctv },
    collect: (monitor) => ({ isDragging: monitor.isDragging() }),
  }), [cctv]);

  const highlightName = (name: string, word: string) => {
    if (!word) return name;
    const idx = name.toLowerCase().indexOf(word.toLowerCase());
    if (idx === -1) return name;
    return (
      <>
        {name.slice(0, idx)}
        <span style={{ color: '#ff6f26' }}>{name.slice(idx, idx + word.length)}</span>
        {name.slice(idx + word.length)}
      </>
    );
  };

  return (
    <li ref={drag} style={{ opacity: isDragging ? 0.4 : 1, cursor: 'grab' }}>
      <div className="tree_node cctv_node" title={cctv.facNm}>
        {highlightName(cctv.facNm, searchWord)}
      </div>
    </li>
  );
}

const PAGE_SIZE = 10;

function DraggableClfyNode({
  item,
  isOpen,
  onToggle,
  searchWord,
}: {
  item: MenuListResult;
  isOpen: boolean;
  onToggle: () => void;
  searchWord: string;
}) {
  const [visibleCount, setVisibleCount] = useState(PAGE_SIZE);

  const [{ isDragging }, drag] = useDrag(() => ({
    type: DND_ITEM_TYPE.CLFY,
    item: { type: DND_ITEM_TYPE.CLFY, cctvList: item.cctvList, clfyId: item.clfyId },
    collect: (monitor) => ({ isDragging: monitor.isDragging() }),
  }), [item]);

  const visibleList = item.cctvList.slice(0, visibleCount);
  const hasMore = visibleCount < item.cctvList.length;

  return (
    <li>
      <div
        ref={drag}
        className="tree_node clfy_node"
        style={{ opacity: isDragging ? 0.4 : 1, cursor: 'grab' }}
        onClick={onToggle}
      >
        <span className={`toggle_arrow ${isOpen ? 'open' : ''}`}>
          {isOpen ? '▾' : '▸'}
        </span>
        {item.clfyNm} ({item.cctvCnt})
      </div>
      {isOpen && (
        <ul className="cctv_sub_list">
          {visibleList.map((cctv) => (
            <DraggableCctvNode key={cctv.facId} cctv={cctv} searchWord={searchWord} />
          ))}
          {hasMore && (
            <li>
              <button
                type="button"
                className="load_btn"
                onClick={(e) => {
                  e.stopPropagation();
                  setVisibleCount((prev) => prev + PAGE_SIZE);
                }}
              >
                더 보기
              </button>
            </li>
          )}
        </ul>
      )}
    </li>
  );
}

export function MenuPanel() {
  const {
    isMenuOpen,
    setMenuOpen,
    searchWord,
    setSearchWord,
    pageTransCycle,
    setPageTransCycle,
  } = useMonitorStore();

  const containerRef = useRef<HTMLElement>(null);
  const { data: menuList, isLoading } = useMonitorList();
  const [openClfyIds, setOpenClfyIds] = useState<string[]>([]);
  const [pendingCycle, setPendingCycle] = useState(pageTransCycle);

  const toggleClfy = (clfyId: string) => {
    setOpenClfyIds((prev) =>
      prev.includes(clfyId) ? prev.filter((id) => id !== clfyId) : [...prev, clfyId],
    );
  };

  const filteredList = menuList?.map((item) => {
    if (!searchWord) return item;
    const filtered = item.cctvList.filter((c) =>
      c.facNm.toLowerCase().includes(searchWord.toLowerCase()),
    );
    return { ...item, cctvList: filtered, cctvCnt: filtered.length };
  }).filter((item) => item.cctvCnt > 0);

  const handleApplyCycle = () => {
    setPageTransCycle(pendingCycle);
  };

  return (
    <section
      className="monitor dash_container"
      style={{
        left: isMenuOpen ? '0px' : '-246px',
        opacity: isMenuOpen ? 1 : 0.4,
      }}
      ref={containerRef}
    >
      <button
        type="button"
        className="btn_hide"
        onClick={() => setMenuOpen(false)}
      />
      <button
        type="button"
        className="btn_open"
        style={{ display: isMenuOpen ? 'none' : 'block' }}
        onClick={() => setMenuOpen(true)}
        onMouseOver={() => {
          if (containerRef.current) containerRef.current.style.opacity = '1';
        }}
        onMouseLeave={() => {
          if (!isMenuOpen && containerRef.current) {
            containerRef.current.style.opacity = '0.4';
          }
        }}
      />

      {/* 전환 주기 설정 */}
      <div className="dash_frame">
        <p className="dash_title">영상 전환 주기 시간설정</p>
        <div className="select_area">
          <Select<TransCycleOption>
            value={pendingCycle}
            options={TransCycleOptions}
            styles={MenuSelectStyle}
            isSearchable={false}
            onChange={(opt) => { if (opt) setPendingCycle(opt); }}
          />
        </div>
        <button
          type="button"
          className={`btn_apply ${pendingCycle.value === pageTransCycle.value ? 'dimmed' : ''}`}
          onClick={handleApplyCycle}
        >
          적용
        </button>
      </div>

      {/* CCTV 목록 */}
      <div className="dash_frame">
        <p className="dash_title">CCTV 목록</p>

        <div className="cctv_search_area">
          <p className="search_hint">*CCTV리스트 버튼을 드래그하여 우측 영상&nbsp;영역에 놓아 주세요</p>
          <div className="search_box">
            <input
              type="text"
              className="search_input"
              placeholder="CCTV 검색"
              value={searchWord}
              onChange={(e) => setSearchWord(e.target.value)}
            />
            <button type="button" className="btn_search" />
          </div>
        </div>

        <div className="tree_area">
          <div className="tree_in">
            {isLoading && <p style={{ color: '#999', padding: '10px', fontSize: '12px' }}>Loading...</p>}
            <ul className="cctv_tree_list">
              {filteredList?.map((item) => (
                <DraggableClfyNode
                  key={item.clfyId}
                  item={item}
                  isOpen={openClfyIds.includes(item.clfyId)}
                  onToggle={() => toggleClfy(item.clfyId)}
                  searchWord={searchWord}
                />
              ))}
            </ul>
          </div>
        </div>
      </div>
    </section>
  );
}
