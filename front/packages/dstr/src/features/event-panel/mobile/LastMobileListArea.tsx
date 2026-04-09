import { useState } from 'react';
import { useMobileStore } from '../../../entities/mobile/store';
import { useMobilePast } from '../../../entities/mobile/queries';
import { LastMobileListItem } from './LastMobileListItem';
import type { MobileSearchPayload } from '../../../entities/mobile/types';

const ROWS = 10;
const PAGE_CNT = 8;

interface Props {
  /** 검색 트리거용 payload (LastMobileSearchArea에서 전달) */
  searchPayload: MobileSearchPayload;
}

export function LastMobileListArea({ searchPayload }: Props) {
  const [currentPage, setCurrentPage] = useState(1);

  const payload: MobileSearchPayload = { ...searchPayload, page: currentPage, rows: ROWS };
  const { data, isError, refetch } = useMobilePast(payload);

  const list = data?.data ?? [];
  const total = data?.total ?? 0;

  const totalPages = Math.ceil(total / ROWS);
  const pageGroupStart = Math.floor((currentPage - 1) / PAGE_CNT) * PAGE_CNT + 1;
  const pageGroupEnd = Math.min(pageGroupStart + PAGE_CNT - 1, totalPages);
  const pages = Array.from(
    { length: Math.max(0, pageGroupEnd - pageGroupStart + 1) },
    (_, i) => pageGroupStart + i,
  );

  return (
    <div className="past_list_area">
      <div className="search_result_area">
        <p className="search_result_text">검색결과 [{total}]</p>
      </div>
      <div className="search_list_frame">
        <ul className="search_list_head">
          <li>No</li>
          <li>영상 촬영 시작 일시</li>
          <li>담당자 소속-이름</li>
          <li />
        </ul>
        <ul className="search_list_body">
          {isError ? (
            <div className="event_load_error_area">
              <button type="button" className="btn_reload" onClick={() => refetch()} />
              <p className="event_load_error_message">
                데이터 로드에 문제가 발생하였습니다.
                <br />
                <span>Reload 또는 재검색을 시도해주세요.</span>
              </p>
            </div>
          ) : list.length === 0 ? (
            <div className="no_result_area">
              <p className="no_result_message">검색 결과가 없습니다.</p>
            </div>
          ) : (
            list.map((item, index) => (
              <LastMobileListItem
                key={item.id ?? `past_${index}`}
                item={item}
                index={(index + 1) + (currentPage - 1) * ROWS}
              />
            ))
          )}
        </ul>

        {/* 페이지네이션 */}
        {totalPages > 1 && (
          <div className="pagination">
            {pageGroupStart > 1 && (
              <button
                type="button"
                className="btn_page btn_prev"
                onClick={() => setCurrentPage(pageGroupStart - 1)}
              >
                &lt;
              </button>
            )}
            {pages.map((p) => (
              <button
                key={p}
                type="button"
                className={`btn_page ${p === currentPage ? 'active' : ''}`}
                onClick={() => setCurrentPage(p)}
              >
                {p}
              </button>
            ))}
            {pageGroupEnd < totalPages && (
              <button
                type="button"
                className="btn_page btn_next"
                onClick={() => setCurrentPage(pageGroupEnd + 1)}
              >
                &gt;
              </button>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
