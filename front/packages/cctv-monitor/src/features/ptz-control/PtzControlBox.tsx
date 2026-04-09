import { useRef } from 'react';
import { PtzControlCode, PtzAction } from '../../entities/monitor/types';
import { useCctvControl } from '../../entities/monitor/queries';

interface Props {
  isShow: boolean;
  cctvId: string;
}

export function PtzControlBox({ isShow, cctvId }: Props) {
  const { mutate: controlPtz } = useCctvControl();
  const ptzRef = useRef<HTMLDivElement>(null);

  const doControl = (code: string, action: string) => {
    controlPtz({ cctvId, code, action });
  };

  const doubleClickEvent = () => {
    const targetPtz = ptzRef.current?.previousElementSibling?.firstElementChild as HTMLElement;
    targetPtz?.requestFullscreen?.();
  };

  return (
    <div
      className="ptz_control_area"
      style={{ display: isShow ? 'block' : 'none' }}
      ref={ptzRef}
      onDoubleClick={doubleClickEvent}
    >
      <div className="btn_ptz_direct_group">
        <button type="button" className="btn_ptz_direct ptz_up"
          onDoubleClick={(e) => e.stopPropagation()}
          onMouseUp={() => doControl(PtzControlCode.Up, PtzAction.STOP)}
          onMouseDown={() => doControl(PtzControlCode.Up, PtzAction.START)}
        />
        <button type="button" className="btn_ptz_direct ptz_down"
          onDoubleClick={(e) => e.stopPropagation()}
          onMouseUp={() => doControl(PtzControlCode.Down, PtzAction.STOP)}
          onMouseDown={() => doControl(PtzControlCode.Down, PtzAction.START)}
        />
        <button type="button" className="btn_ptz_direct ptz_left"
          onDoubleClick={(e) => e.stopPropagation()}
          onMouseUp={() => doControl(PtzControlCode.Left, PtzAction.STOP)}
          onMouseDown={() => doControl(PtzControlCode.Left, PtzAction.START)}
        />
        <button type="button" className="btn_ptz_direct ptz_right"
          onDoubleClick={(e) => e.stopPropagation()}
          onMouseUp={() => doControl(PtzControlCode.Right, PtzAction.STOP)}
          onMouseDown={() => doControl(PtzControlCode.Right, PtzAction.START)}
        />
      </div>
      <div className="btn_ptz_zoom_group">
        <button type="button" className="btn_ptz_zoom ptz_plus"
          onDoubleClick={(e) => e.stopPropagation()}
          onMouseUp={() => doControl(PtzControlCode.ZoomIn, PtzAction.STOP)}
          onMouseDown={() => doControl(PtzControlCode.ZoomIn, PtzAction.START)}
        />
        <button type="button" className="btn_ptz_zoom ptz_minus"
          onDoubleClick={(e) => e.stopPropagation()}
          onMouseUp={() => doControl(PtzControlCode.ZoomOut, PtzAction.STOP)}
          onMouseDown={() => doControl(PtzControlCode.ZoomOut, PtzAction.START)}
        />
      </div>
    </div>
  );
}
