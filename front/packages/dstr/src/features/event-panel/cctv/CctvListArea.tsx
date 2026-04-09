import { useEffect, useState } from 'react';
import { useCctvStore } from '../../../entities/cctv/store';
import { useCctvList } from '../../../entities/cctv/queries';
import { CctvListItem } from './CctvListItem';

const ROWS = 11;
const PAGE_CNT = 8;

interface Props {
  currentPage: number;
  setCurrentPage: (page: number) => void;
}

export function CctvListArea({ currentPage, setCurrentPage }: Props) {
  const selectClfy = useCctvStore((s) => s.selectClfy);
  const word = useCctvStore((s) => s.word);

  const { data: cctvList, isError, refetch } = useCctvList({
    page: currentPage,
    rows: ROWS,
    type: selectClfy?.id ?? '',
    cctvNm: word,
  });

  const list = cctvList?.data ?? null;
  const total = cctvList?.total ?? 0;

  // 페이지 범위 계산
  const totalPages = Math.ceil(total / ROWS);
  const pageGroupStart = Math.floor((currentPage - 1) / PAGE_CNT) * PAGE_CNT + 1;
  const pageGroupEnd = Math.min(pageGroupStart + PAGE_CNT - 1, totalPages);
  const pages = Array.from(
    { length: pageGroupEnd - pageGroupStart + 1 },
    (_, i) => pageGroupStart + i,
  );

  return (
    <>
      <div className="search_result_area">
        <p className="search_result_text">
          {selectClfy?.id && (
            <span className="result_value">#{selectClfy.nm}</span>
          )}
          {word && (
            <span className="result_value"> &gt; #&lsquo;{word}&rsquo;</span>
          )}
          검색결과 [{total}]
        </p>
      </div>

      <div className="search_list_frame">
        <ul className="search_list_head">
          <li>No</li>
          <li>설치 목적</li>
          <li>명칭</li>
          <li />
        </ul>
        <ul className="search_list_body">
          {isError ? (
            <div className="event_load_error_area">
              <button
                type="button"
                className="btn_reload"
                onClick={() => refetch()}
              />
              <p className="event_load_error_message">
                CCTV 로드에 문제가 발생하였습니다.
                <br />
                Reload 또는 재검색을 시도해주세요.
              </p>
            </div>
          ) : list === null || list.length === 0 ? (
            <div className="no_result_area">
              <p className="no_result_message">검색 조건에 해당하는 CCTV가 없습니다.</p>
            </div>
          ) : (
            list.map((item, index) => (
              <CctvListItem
                key={item.facId}
                item={item}
                no={(index + 1) + (currentPage - 1) * ROWS}
              />
            ))
          )}
        </ul>

        {/* 페이지네이션 */}
        <div className="paging_wrap">
          <ul>
            <li>
              <a
                className={`btn_foremost${pageGroupStart === 1 ? ' dimmed' : ''}`}
                href="#"
                onClick={(e) => { e.preventDefault(); if (pageGroupStart > 1) setCurrentPage(1); }}
              />
            </li>
            <li>
              <a
                className={`btn_prev${currentPage === 1 ? ' dimmed' : ''}`}
                href="#"
                onClick={(e) => { e.preventDefault(); if (currentPage > 1) setCurrentPage(currentPage - 1); }}
              />
            </li>
            {pages.map((p) => (
              <li key={p}>
                <a
                  className={p === currentPage ? 'active' : ''}
                  href="#"
                  onClick={(e) => { e.preventDefault(); setCurrentPage(p); }}
                >
                  {p}
                </a>
              </li>
            ))}
            <li>
              <a
                className={`btn_next${currentPage === totalPages ? ' dimmed' : ''}`}
                href="#"
                onClick={(e) => { e.preventDefault(); if (currentPage < totalPages) setCurrentPage(currentPage + 1); }}
              />
            </li>
            <li>
              <a
                className={`btn_last${pageGroupEnd === totalPages ? ' dimmed' : ''}`}
                href="#"
                onClick={(e) => { e.preventDefault(); if (pageGroupEnd < totalPages) setCurrentPage(totalPages); }}
              />
            </li>
          </ul>
        </div>
      </div>
    </>
  );
}
