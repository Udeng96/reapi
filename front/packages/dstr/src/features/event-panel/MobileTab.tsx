import { useState } from 'react';
import { useMobileStore } from '../../entities/mobile/store';
import type { MobileSearchPayload } from '../../entities/mobile/types';
import { NowMobileListArea } from './mobile/NowMobileListArea';
import { LastMobileSearchArea } from './mobile/LastMobileSearchArea';
import { LastMobileListArea } from './mobile/LastMobileListArea';

export function MobileTab() {
  const startDtm = useMobileStore((s) => s.startDtm);
  const endDtm = useMobileStore((s) => s.endDtm);

  // 검색 버튼을 눌렀을 때만 실제 fetch가 실행되도록 payload를 별도 관리
  const [searchPayload, setSearchPayload] = useState<MobileSearchPayload>({
    startDtm,
    endDtm,
    rows: 10,
    page: 1,
  });

  const handleSearch = () => {
    setSearchPayload({ startDtm, endDtm, rows: 10, page: 1 });
  };

  return (
    <div className="dash_wrap mobile_video_wrap">
      <NowMobileListArea />
      <div className="divide_area" />
      <LastMobileSearchArea onSearch={handleSearch} />
      <LastMobileListArea searchPayload={searchPayload} />
    </div>
  );
}
