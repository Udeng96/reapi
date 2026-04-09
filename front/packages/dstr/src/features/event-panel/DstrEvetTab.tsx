import { DstrSearchArea } from './dstr/DstrSearchArea';
import { DstrListArea } from './dstr/DstrListArea';
import { DstrDetailArea } from './dstr/DstrDetailArea';
import { DstrModal } from './dstr/DstrModal';

export function DstrEvetTab() {
  return (
    <>
      <div className="dash_wrap disaster_event_wrap" id="tab1">
        <DstrSearchArea />
        <DstrListArea />
        <DstrDetailArea />
      </div>
      <DstrModal />
    </>
  );
}
