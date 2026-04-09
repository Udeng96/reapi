/**
 * 중앙 GIS 지도 영역
 *
 * 레거시: GisArea (288줄, 34 import)
 * TODO: 03-8 FSD 분해
 *   - widgets/gis-map/MapContainer (지도 초기화)
 *   - features/event-marker/ EventMarker, EventLine
 *   - features/cctv-marker/ CctvClusterMarkers, CctvPopupLayer
 *   - features/mobile-marker/ MobileLiveMarkers, MobilePastMarkers
 *   - features/addr-search/ AddrSearchBox
 *   - features/gis-settings/ GisSettingArea, HeatMap
 *   - features/map-draw/ MapDrawLayer
 */
export function GisMap({
  isLeftOpen,
  isRightOpen,
}: {
  isLeftOpen: boolean;
  isRightOpen: boolean;
}) {
  return (
    <main
      className="gisArea"
      style={{
        marginLeft: isLeftOpen ? undefined : 0,
        marginRight: isRightOpen ? undefined : 0,
      }}
    >
      {/* TODO: MapContainer + 레이어들 */}
    </main>
  );
}
