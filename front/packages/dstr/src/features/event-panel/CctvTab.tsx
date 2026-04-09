import { useState } from 'react';
import { CctvSearchArea } from './cctv/CctvSearchArea';
import { CctvListArea } from './cctv/CctvListArea';
import { CctvDetailArea } from './cctv/CctvDetailArea';

export function CctvTab() {
  const [currentPage, setCurrentPage] = useState(1);

  return (
    <div className="dash_wrap cctv_search_wrap">
      <CctvSearchArea setCurrentPage={setCurrentPage} />
      <CctvListArea currentPage={currentPage} setCurrentPage={setCurrentPage} />
      <CctvDetailArea />
    </div>
  );
}
