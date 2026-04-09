import { useRef, useState } from 'react';
import { useHamsStore } from '../../entities/hams/store';

export function DashInfo() {
  const { selectedHams } = useHamsStore();

  const addrRef = useRef<HTMLParagraphElement>(null);
  const [showAddrTooltip, setShowAddrTooltip] = useState(false);

  const addr = selectedHams?.addr ?? '';
  const groupNm = selectedHams?.groupNm ?? '';
  const lat = selectedHams?.lat ?? null;
  const lng = selectedHams?.lng ?? null;

  return (
    <>
      {/* 주소 + 그룹 */}
      <div className="dash_info_block">
        <div className="dash_info_box">
          <p className="info_title title_address">주소</p>
          <p
            className="info_value loc_value"
            ref={addrRef}
            onMouseEnter={() => {
              if (addrRef.current) {
                setShowAddrTooltip(addrRef.current.scrollWidth > addrRef.current.clientWidth);
              }
            }}
            onMouseLeave={() => setShowAddrTooltip(false)}
          >
            {addr}
          </p>
          {showAddrTooltip && <div className="tooltip_text">{addr}</div>}
        </div>
        <div className="dash_info_box">
          <p className="info_title title_group">그룹</p>
          <p className="info_value">{groupNm || '-'}</p>
        </div>
      </div>

      {/* 위경도 */}
      <div className="dash_info_block">
        <div className="dash_info_box">
          <p className="info_title title_coordinate">위경도</p>
          <p className="info_value">
            <span>위도:</span>
            {lat ?? '-'}
          </p>
          <div className="divide_bar" />
          <p className="info_value">
            <span>경도:</span>
            {lng ?? '-'}
          </p>
        </div>
      </div>
    </>
  );
}
