import CctvArea from "./cctv/cctvArea";
import MenuArea from "./menu/menuArea";
import { DndProvider } from "react-dnd";
import { HTML5Backend } from "react-dnd-html5-backend";
import { useSearchParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  updateAllPagePlayList,
  updateCycleState,
  updateGridAmount,
  updateNowPagePlayList,
} from "../../store/view/monitor/monitorViewStore";
import * as monitorActions from "../../saga/action/monitor/monitor";
import { RootState } from "../../store/rootStore";
import { GetAllCctvList, GetAllPagePlayList } from "./cctv/getAllCctvList";
import { CctvInfo } from "../../../data/interface/monitor/monitorInterface";
import { PageTransType } from "../../../data/const/monitorMenuConst";

const MonitorRoot = () => {
  const [cctvParams, setCctvParams] = useSearchParams();
  const [gridAmount, setGridAmount] = useState<number>(9);
  const dispatch = useDispatch();
  const firstPlayPage = useSelector((state: RootState) => state.server.monitor.firstPlayPage);

  // 초기 메뉴 불러오기
  useEffect(() => {
    dispatch(monitorActions.actions.requestCctvMonitorList());
  }, []);

  //초기 화면 분할 수 및 초기 표출 cctv
  useEffect(() => {
    let x: string | number | null = cctvParams.get("x");
    let y: string | number | null = cctvParams.get("y");
    const clfyId = cctvParams.get("clfyId");

    const newGridRatio: Array<number> = [];

    // x와 y가 둘다 null 이 아닐 경우
    if (x !== null && y !== null) {
      x = parseInt(x) > 5 ? 5 : parseInt(x);
      y = parseInt(y) > 5 ? 5 : parseInt(y);
    } else if (x === null && y === null) {
      x = 3;
      y = 3;
    } else {
      x = x !== null ? (parseInt(x) > 5 ? 5 : parseInt(x)) : 1;
      y = y !== null ? (parseInt(y) > 5 ? 5 : parseInt(y)) : 1;
    }

    newGridRatio.push(x);
    newGridRatio.push(y);

    setGridAmount(x * y);

    dispatch(updateGridAmount(newGridRatio));
    if (clfyId !== null) {
      dispatch(monitorActions.actions.requestFirstPlayPage(clfyId));
    }
  }, [cctvParams]);

  // 초기 cctv 재생 설정
  useEffect(() => {
    let childNodeList: CctvInfo[] = firstPlayPage.length > 0 ? firstPlayPage[0].cctvList : [];

    if (childNodeList.length > gridAmount) {
      let allPageCctvList = GetAllCctvList(childNodeList.length, gridAmount, childNodeList);
      let allPagePlayingCctvList: Array<Array<CctvInfo | null>> = GetAllPagePlayList(allPageCctvList, gridAmount);

      dispatch(updateAllPagePlayList(allPagePlayingCctvList));
      dispatch(updateCycleState(PageTransType.PLAY)); //사이클 시작
    } else {
      let nowPagePlayList = Array(gridAmount).fill(null);
      childNodeList?.map((item, index) => {
        nowPagePlayList.splice(index, 1, item);
      });
      dispatch(updateNowPagePlayList(nowPagePlayList));

      dispatch(updateCycleState(PageTransType.NONE)); //초기 사이클 도는것 방지
    }
  }, [firstPlayPage]);

  return (
    <div className={"monitor dashWrap"}>
      <DndProvider backend={HTML5Backend}>
        <CctvArea />
        <MenuArea />
      </DndProvider>
    </div>
  );
};
export default MonitorRoot;
