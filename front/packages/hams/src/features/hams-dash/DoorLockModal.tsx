import { useHamsStore } from '../../entities/hams/store';
import { useLockDoor, useUnlockDoor } from '../../entities/hams/queries';

export function DoorLockModal() {
  const { selectedHams, isDoorLockModalOpen, isDoorLockAction, setDoorLockModal } =
    useHamsStore();

  const { mutate: doLock, isPending: isLocking } = useLockDoor();
  const { mutate: doUnlock, isPending: isUnlocking } = useUnlockDoor();

  const isPending = isLocking || isUnlocking;
  const isLockAction = isDoorLockAction === 'lock';

  const handleConfirm = () => {
    if (!selectedHams?.hamsId) return;
    const params = { hamsId: selectedHams.hamsId, body: selectedHams };

    if (isLockAction) {
      doLock(params, { onSuccess: () => setDoorLockModal(false) });
    } else {
      doUnlock(params, { onSuccess: () => setDoorLockModal(false) });
    }
  };

  if (!isDoorLockModalOpen) return null;

  return (
    <div className="hams modal_popup doorlock_popup" style={{ display: 'block' }}>
      <div className="modal_popup_frame">
        <div className="modal_popup_header">
          <button
            type="button"
            className="btn_popup_close"
            onClick={() => setDoorLockModal(false)}
            disabled={isPending}
          />
        </div>
        <div className="modal_popup_body">
          <div className={`lock_img ${isLockAction ? 'state_lock' : 'state_unlock'}`} />
          <p className="lock_message">
            <span className={isLockAction ? 'tBlue' : 'tRed'}>
              {selectedHams?.facNm}
            </span>
            앞문 도어락을
            <span className={isLockAction ? 'tBlue' : 'tRed'}>
              {isLockAction ? ' 잠금' : ' 잠금해제'}
            </span>
            하시겠습니까?
          </p>
          <div className="btn_lock_group">
            <button
              type="button"
              className="btn_lock"
              onClick={() => setDoorLockModal(false)}
              disabled={isPending}
            >
              취소
            </button>
            <button
              type="button"
              className="btn_lock"
              onClick={handleConfirm}
              disabled={isPending}
            >
              {isLockAction ? '잠금' : '잠금해제'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
