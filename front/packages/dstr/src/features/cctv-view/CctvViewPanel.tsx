/**
 * 우측 CCTV / 소방차 영상 패널
 *
 * 레거시: CctvViewArea + truckPlayerArea + fireCctvArea + normalCctvArea
 * TODO: 각 컴포넌트 이전 (03-6)
 */
export function CctvViewPanel({
  isOpen,
  onToggle,
}: {
  isOpen: boolean;
  onToggle: () => void;
}) {
  return (
    <aside className={`cctvViewArea ${isOpen ? 'open' : 'closed'}`}>
      <button type="button" className="toggleBtn" onClick={onToggle} />
      {/* TODO: TruckPlayerArea / FireCctvArea / NormalCctvArea */}
    </aside>
  );
}
