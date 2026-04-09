import { useState } from "react";
import { EventPanel } from "./features/event-panel/EventPanel";
import { GisMap } from "./features/gis-map/GisMap";
import { CctvViewPanel } from "./features/cctv-view/CctvViewPanel";
import { useEventWebSocket } from "./features/realtime/useEventWebSocket";

export default function DstrApp() {
  const [isLeftOpen, setLeftOpen] = useState(true);
  const [isRightOpen, setRightOpen] = useState(true);

  useEventWebSocket();

  return (
    <section className="dashWrap">
      <EventPanel isOpen={isLeftOpen} onToggle={() => setLeftOpen((v) => !v)} />
      <GisMap isLeftOpen={isLeftOpen} isRightOpen={isRightOpen} />
      <CctvViewPanel isOpen={isRightOpen} onToggle={() => setRightOpen((v) => !v)} />
    </section>
  );
}
