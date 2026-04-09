import { useMonitorStore } from '../../entities/monitor/store';
import { PageTransType } from '../../entities/monitor/types';

export function TopMenu() {
  const { isMenuOpen, isPageTrans, pageTransCycle, setPageTrans } = useMonitorStore();

  const onClickStopCycleBtn = () => {
    if (isPageTrans === PageTransType.STOP) {
      setPageTrans(PageTransType.PLAY);
    } else if (isPageTrans === PageTransType.PLAY) {
      setPageTrans(PageTransType.STOP);
    }
  };

  return (
    <div className="change_control_area" style={{ display: isMenuOpen ? 'flex' : 'none' }}>
      <p className={`change_text ${isPageTrans === PageTransType.PLAY ? '' : 'dimmed'}`}>
        {isPageTrans === PageTransType.PLAY
          ? '전환중'
          : isPageTrans === PageTransType.STOP
            ? '전환 STOP'
            : '미전환'}
        <span>{isPageTrans ? `[${pageTransCycle.label}]` : ''}</span>
      </p>
      <button
        type="button"
        className={`btn_change_stop ${isPageTrans ? '' : 'dimmed'}`}
        onClick={onClickStopCycleBtn}
      >
        전환{isPageTrans === PageTransType.PLAY ? 'STOP' : 'START'}
      </button>
    </div>
  );
}
