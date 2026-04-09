import PlayerBox from "./playerBox";
import { CctvInfo } from "../../../../data/interface/monitor/monitorInterface";
import { useDrop } from "react-dnd";
import { useSelector } from "react-redux";
import { RootState } from "../../../store/rootStore";

const CctvFrame = (props: { info: CctvInfo | null; index: number; isFull: boolean }) => {
  const gridRatio = useSelector((state: RootState) => state.view.monitor.gridRatio);
  const currentPagePlayList = useSelector((state: RootState) => state.view.monitor.currentPagePlayList);
  const dragTargetInfo = useSelector((state: RootState) => state.view.monitor.dragTargetInfo);

  // drop 에서 list 를 집어넣은 이유 => useDrag에서는 전역 변수의 값을 가져올 수 없다. (꼭 useDrop에서 전달해 줘야한다.)
  // drag가 끝났을 때, drop한 위치인 index와, 현재 실행중인 cctv 배열의 list를 전달해준다.
  // drop은 string type 으로 전달이 된다.

  const [{ canDrop, isOver }, dropRef] = useDrop({
    accept: "tree",
    drop: () => ({
      index: dragTargetInfo?.parent !== 0 && `${props.index}`,
      list: dragTargetInfo?.parent !== 0 && `${JSON.stringify(currentPagePlayList)}`,
    }),
    collect: (monitor) => ({
      isOver: monitor.isOver(),
      canDrop: dragTargetInfo?.parent !== 0 && monitor.canDrop(),
    }),
  });

  const isDragState = canDrop && isOver && dragTargetInfo?.parent !== 0;

  return (
    <div
      className={`cctv_frame ${isDragState ? "drag_state" : ""}`}
      ref={dropRef}
      data-testid={`${props.index}`}
      style={{
        width: `${props.isFull ? parseInt(String(1922 / gridRatio[0] - 5)) : parseInt(String(1657 / gridRatio[0] - 5))}px`,
        height: `${props.isFull ? 1080 / gridRatio[1] : 936 / gridRatio[1]}px`,
      }}
    >
      <header className="cctv_header">{props.info ? props.info.facNm : ""}</header>
      <PlayerBox cctvInfo={props.info} />
    </div>
  );
};
export default CctvFrame;
