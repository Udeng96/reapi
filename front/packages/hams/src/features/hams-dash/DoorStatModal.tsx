import { useHamsStore } from '../../entities/hams/store';
import { DoorStatChart } from './DoorStatChart';

export function DoorStatModal() {
  const { selectedHams, isDoorStatModalOpen, setDoorStatModal } = useHamsStore();

  if (!isDoorStatModalOpen) return null;

  const hamsId = selectedHams?.hamsId ?? '';

  return (
    <div className="hams modal_popup history_popup" style={{ display: 'block' }}>
      <div className="modal_popup_frame">
        <div className="modal_popup_header">
          <p className="modal_title">앞문 개폐 이력</p>
          <button
            type="button"
            className="btn_popup_close"
            onClick={() => setDoorStatModal(false)}
          />
        </div>
        <div className="modal_popup_body">
          <DoorStatChart hamsId={hamsId} dateType="D" />
          <DoorStatChart hamsId={hamsId} dateType="M" />
          <DoorStatChart hamsId={hamsId} dateType="Y" />
        </div>
      </div>
    </div>
  );
}
