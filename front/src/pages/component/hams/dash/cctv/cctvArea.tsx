import CctvHamsPlayerBox from "./cctvHamsPlayerBox";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../../../store/rootStore";
import { updateActiveHamsCctvInfo, updateCctvPopupOpen } from "../../../../store/view/hams/hamsViewStore";
import * as actionHams from "../../../../saga/action/hams/hams";

import { useEffect } from "react";
import { updateHamsCctvList } from "../../../../store/server/hams/hamsServerStore";

const CctvArea = () => {
  const activeHamsInfo = useSelector((state: RootState) => state.server.hams.activeHamsInfo);
  const activeCctvInfo = useSelector((state: RootState) => state.view.hams.activePlayHamsCctv);

  const dispatch = useDispatch();
  const onClickOpenPopupBtn = () => {
    dispatch(updateCctvPopupOpen(true));
  };

  useEffect(() => {
    if (activeHamsInfo) {
      dispatch(actionHams.actions.requestActiveHamsCctvList(activeHamsInfo.facNm!!));
    } else {
      dispatch(updateActiveHamsCctvInfo(null));
      dispatch(updateHamsCctvList([]));
    }
  }, [activeHamsInfo]);

  return (
    <>
      {/*cctvFrame*/}
      <div className="cctv_frame">
        <p className="info_title title_cctv">실시간 CCTV 현황 - {activeCctvInfo ? activeCctvInfo.cctvName : "-"}</p>
        <button
          type="button"
          disabled={activeCctvInfo === null}
          className="btn_cctv_full"
          onClick={(event) => onClickOpenPopupBtn()}
        >
          CCTV 크게 보기
        </button>
        {<CctvHamsPlayerBox />}
      </div>
    </>
  );
};

export default CctvArea;
