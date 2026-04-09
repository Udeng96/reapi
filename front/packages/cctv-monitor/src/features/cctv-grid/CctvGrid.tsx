import { useDrop } from 'react-dnd';
import { useMonitorStore } from '../../entities/monitor/store';
import { getClfyPagePlayList } from '../../entities/monitor/getAllCctvList';
import { DND_ITEM_TYPE } from '../../entities/monitor/dndTypes';
import type { DndClfyItem } from '../../entities/monitor/dndTypes';
import { PageTransType } from '../../entities/monitor/types';
import { CctvFrame } from './CctvFrame';

export function CctvGrid() {
  const {
    gridRatio,
    currentPagePlayList,
    isMenuOpen,
    setCurrentPagePlayList,
    setAllPagePlayList,
    setCurrentPageIndex,
    setPageTrans,
  } = useMonitorStore();

  const [cols, rows] = gridRatio;
  const totalCells = cols * rows;

  // 분류(폴더) 드롭 시 해당 분류 전체 CCTV를 그리드에 배치
  const [{ isOver }, drop] = useDrop(() => ({
    accept: DND_ITEM_TYPE.CLFY,
    drop: (item: DndClfyItem) => {
      const pages = getClfyPagePlayList(item.cctvList, totalCells);
      if (pages.length > 0) {
        setAllPagePlayList(pages);
        setCurrentPagePlayList(pages[0]);
        setCurrentPageIndex(0);
        if (pages.length > 1) {
          setPageTrans(PageTransType.PLAY);
        } else {
          setPageTrans(PageTransType.NONE);
        }
      }
    },
    collect: (monitor) => ({ isOver: monitor.isOver() }),
  }), [totalCells]);

  const cells = Array.from({ length: totalCells }, (_, i) =>
    currentPagePlayList[i] ?? null,
  );

  return (
    <div
      ref={drop}
      className={`monitor cctv_container ${isMenuOpen ? '' : 'full_size'}`}
      style={{
        outline: isOver ? '3px solid #5d73f0' : 'none',
      }}
    >
      {cells.map((cctv, i) => (
        <CctvFrame key={i} cctv={cctv} index={i} isFull={!isMenuOpen} gridRatio={gridRatio} />
      ))}
    </div>
  );
}
