import { useEffect, useState } from 'react';
import dayjs from 'dayjs';
import DatePicker, { registerLocale } from 'react-datepicker';
import { ko } from 'date-fns/locale';
import { useEventStore } from '../../../entities/event/store';

registerLocale('ko', ko);
import { useEvents, useLocations, useEventTypes, useEventDetail } from '../../../entities/event/queries';
import { DTM_OPTION } from '../../../entities/event/consts';
import { DstrSearchBox } from './DstrSearchBox';
import { DstrListItem } from './DstrListItem';
import type { EventsPayload } from '../../../entities/event/types';

/** yyyyMMdd → "yyyy-MM-dd" */
function toDashDate(dtm: string): string {
  return `${dtm.substring(0, 4)}-${dtm.substring(4, 6)}-${dtm.substring(6, 8)}`;
}

/** "yyyy-MM-dd" → "yyyyMMdd" */
function fromDashDate(val: string): string {
  return val.replace(/-/g, '');
}

/** yyyyMMddHHmmss → "yyyy년 MM월 dd일 HH시 mm분 ss초" */
function formatDtm(dtm: string | null): string {
  if (!dtm) return '';
  return `${dtm.substring(0, 4)}년 ${dtm.substring(4, 6)}월 ${dtm.substring(6, 8)}일 ${dtm.substring(8, 10)}시 ${dtm.substring(10, 12)}분 ${dtm.substring(12, 14)}초`;
}

// ─── 검색 영역 ─────────────────────────────────────────────────────────────────

function LastDstrSearchModal({ onSearch }: { onSearch: () => void }) {
  const modalPayload = useEventStore((s) => s.modalPayload);
  const setModalPayload = useEventStore((s) => s.setModalPayload);
  const checkedModalLocaList = useEventStore((s) => s.checkedModalLocaList);
  const setCheckedModalLocaList = useEventStore((s) => s.setCheckedModalLocaList);
  const checkedModalTypeList = useEventStore((s) => s.checkedModalTypeList);
  const setCheckedModalTypeList = useEventStore((s) => s.setCheckedModalTypeList);

  const { data: locations } = useLocations();
  const { data: eventTypes } = useEventTypes();

  const handlePeriod = (opt: (typeof DTM_OPTION)[number]) => {
    setModalPayload({
      ...modalPayload,
      startDtm: opt.startDtm,
      endDtm: opt.endDtm,
      page: 1,
    });
  };

  return (
    <>
      {/* 기간 선택 */}
      <div className="evt_search_row">
        <div className="evt_search_name"><span>&middot;</span>기간 선택</div>
        <div className="btn_period_group">
          {DTM_OPTION.map((opt) => (
            <button
              key={opt.id}
              type="button"
              className={`btn_period${modalPayload.startDtm === opt.startDtm ? ' active' : ''}`}
              onClick={() => handlePeriod(opt)}
            >
              {opt.id}
            </button>
          ))}
        </div>
        <div className="datepicker_area">
          <div className="calendar_input_box">
            <DatePicker
              wrapperClassName="datePicker"
              locale="ko"
              selectsStart
              dateFormat="yyyy-MM-dd"
              selected={modalPayload.startDtm ? new Date(toDashDate(modalPayload.startDtm)) : null}
              maxDate={modalPayload.endDtm ? new Date(toDashDate(modalPayload.endDtm)) : new Date()}
              onChange={(date: Date | null) => {
                if (date) setModalPayload({ ...modalPayload, startDtm: fromDashDate(dayjs(date).format('YYYY-MM-DD')), page: 1 });
              }}
              customInput={<input type="text" className="calendar_input" />}
            />
          </div>
          <p className="term_text">~</p>
          <div className="calendar_input_box">
            <DatePicker
              wrapperClassName="datePicker"
              locale="ko"
              selectsEnd
              dateFormat="yyyy-MM-dd"
              selected={modalPayload.endDtm ? new Date(toDashDate(modalPayload.endDtm)) : null}
              minDate={modalPayload.startDtm ? new Date(toDashDate(modalPayload.startDtm)) : undefined}
              maxDate={new Date()}
              onChange={(date: Date | null) => {
                if (date) setModalPayload({ ...modalPayload, endDtm: fromDashDate(dayjs(date).format('YYYY-MM-DD')), page: 1 });
              }}
              customInput={<input type="text" className="calendar_input" />}
            />
          </div>
        </div>
      </div>

      {/* 발생 지역 */}
      <div className="evt_search_row">
        <div className="evt_search_name"><span>&middot;</span>발생 지역</div>
        <DstrSearchBox
          searchBoxType="modal_location"
          searchOptions={locations ?? null}
          chkOptionsList={checkedModalLocaList}
          onSetChecked={setCheckedModalLocaList}
          maxVisible={6}
        />
      </div>

      {/* 이벤트 유형 */}
      <div className="evt_search_row">
        <div className="evt_search_name"><span>&middot;</span>이벤트 유형</div>
        <DstrSearchBox
          searchBoxType="modal_eventType"
          searchOptions={eventTypes ?? null}
          chkOptionsList={checkedModalTypeList}
          onSetChecked={setCheckedModalTypeList}
          maxVisible={6}
        />
      </div>

      {/* 검색 버튼 */}
      <button type="button" className="btn_evt_search" onClick={onSearch}>
        검색<span>{dayjs().format('YYYY.MM.DD HH:mm:ss')} 기준</span>
      </button>
    </>
  );
}

// ─── 목록 영역 ─────────────────────────────────────────────────────────────────

const ROWS = 10;
const PAGE_CNT = 8;

function LastDstrListModal() {
  const modalPayload = useEventStore((s) => s.modalPayload);
  const setModalPayload = useEventStore((s) => s.setModalPayload);
  const { data } = useEvents(modalPayload);

  const list = data?.data ?? [];
  const total = data?.total ?? 0;
  const currentPage = modalPayload.page ?? 1;

  const totalPages = Math.ceil(total / ROWS);
  const pageGroupStart = Math.floor((currentPage - 1) / PAGE_CNT) * PAGE_CNT + 1;
  const pageGroupEnd = Math.min(pageGroupStart + PAGE_CNT - 1, totalPages);
  const pages = Array.from({ length: pageGroupEnd - pageGroupStart + 1 }, (_, i) => pageGroupStart + i);

  const goTo = (page: number) => setModalPayload({ ...modalPayload, page });

  return (
    <div className="past_evt_info">
      <ul className="search_list_head">
        <li>지역 명</li>
        <li>이벤트 타입</li>
        <li>주소</li>
        <li>발생 일시</li>
      </ul>
      <ul className="search_list_body">
        {list.length === 0 ? (
          <div className="no_result_area">
            <p className="no_result_message">검색 결과가 없습니다.</p>
          </div>
        ) : (
          list.map((item) => (
            <DstrListItem key={item.statEvetOutbSeqn} item={item} isModal />
          ))
        )}
      </ul>
      {(
        <div className="paging_wrap">
          <ul>
            <li>
              <a
                className={`btn_foremost${pageGroupStart === 1 ? ' dimmed' : ''}`}
                href="#"
                onClick={(e) => { e.preventDefault(); if (pageGroupStart > 1) goTo(1); }}
              />
            </li>
            <li>
              <a
                className={`btn_prev${currentPage === 1 ? ' dimmed' : ''}`}
                href="#"
                onClick={(e) => { e.preventDefault(); if (currentPage > 1) goTo(currentPage - 1); }}
              />
            </li>
            {pages.map((p) => (
              <li key={p}>
                <a
                  className={p === currentPage ? 'active' : ''}
                  href="#"
                  onClick={(e) => { e.preventDefault(); goTo(p); }}
                >
                  {p}
                </a>
              </li>
            ))}
            <li>
              <a
                className={`btn_next${currentPage === totalPages ? ' dimmed' : ''}`}
                href="#"
                onClick={(e) => { e.preventDefault(); if (currentPage < totalPages) goTo(currentPage + 1); }}
              />
            </li>
            <li>
              <a
                className={`btn_last${pageGroupEnd === totalPages ? ' dimmed' : ''}`}
                href="#"
                onClick={(e) => { e.preventDefault(); if (pageGroupEnd < totalPages) goTo(totalPages); }}
              />
            </li>
          </ul>
        </div>
      )}
    </div>
  );
}

// ─── 상세 영역 ─────────────────────────────────────────────────────────────────

function LastDstrDetailModal() {
  const activeModalItem = useEventStore((s) => s.activeModalItem);
  const { data: detail } = useEventDetail(activeModalItem);

  const rows = [
    { title: '타입', value: detail?.eventNm ?? '' },
    { title: '주소', value: detail?.addr ?? '' },
    { title: '발생일시', value: formatDtm(detail?.outbDtm ?? null) },
    { title: '내용', value: detail?.cntn ?? '' },
  ];

  return (
    <div className="evt_detail_info">
      <p className="evt_detail_info_title">이벤트 상세정보</p>
      <div className="evt_detail_info_frame">
        {!detail && (
          <div className="no_select_area">
            <p className="no_select_message">선택된 이벤트가 없습니다.</p>
          </div>
        )}
        {rows.map((row) => (
          <div className="detail_info_block" key={row.title}>
            <p className="detail_info_title">{row.title}</p>
            <div className={`detail_info_box${row.title === '내용' ? ' wide_size' : ''}`}>
              <div className="detail_info_value">{row.value}</div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

// ─── 메인 모달 ─────────────────────────────────────────────────────────────────

export function DstrModal() {
  const isOpen = useEventStore((s) => s.activeModal);
  const setActiveModal = useEventStore((s) => s.setActiveModal);
  const setCheckedModalLocaList = useEventStore((s) => s.setCheckedModalLocaList);
  const setCheckedModalTypeList = useEventStore((s) => s.setCheckedModalTypeList);
  const setActiveModalItem = useEventStore((s) => s.setActiveModalItem);
  const checkedModalLocaList = useEventStore((s) => s.checkedModalLocaList);
  const checkedModalTypeList = useEventStore((s) => s.checkedModalTypeList);
  const modalPayload = useEventStore((s) => s.modalPayload);
  const setModalPayload = useEventStore((s) => s.setModalPayload);
  const { data } = useEvents(modalPayload, isOpen);

  const { data: locations } = useLocations();
  const { data: eventTypes } = useEventTypes();

  const [lastSearchDtm, setLastSearchDtm] = useState('');

  // 모달이 열릴 때 최초 검색 (3개월 기간)
  useEffect(() => {
    if (isOpen) {
      const opt = DTM_OPTION[0];
      setModalPayload({
        eventIdList: null,
        locationCdList: null,
        startDtm: opt.startDtm,
        endDtm: opt.endDtm,
        page: 1,
        rows: 10,
      } satisfies EventsPayload);
    }
  }, [isOpen]);

  const closeModal = () => {
    setActiveModal(false);
    setCheckedModalLocaList([]);
    setCheckedModalTypeList([]);
    setActiveModalItem(null);
  };

  const handleSearch = () => {
    const eventIdList = checkedModalTypeList
      .filter((i) => i.id !== 'all')
      .map((i) => i.id);
    const locationCdList = checkedModalLocaList
      .filter((i) => i.id !== 'all')
      .map((i) => i.id);
    setModalPayload({
      ...modalPayload,
      eventIdList: eventIdList.length > 0 ? eventIdList : null,
      locationCdList: locationCdList.length > 0 ? locationCdList : null,
      page: 1,
    });
    setLastSearchDtm(dayjs().format('YYYY.MM.DD HH:mm:ss'));
  };

  const getLocaNm = (id: string) =>
    locations?.find((item) => item.id === id)?.nm ?? '';
  const getTypeNm = (id: string) =>
    eventTypes?.find((item) => item.id === id)?.nm ?? '';

  return (
    <section
      className="gis modal_popup past_evt_history_popup"
      style={{ display: isOpen ? 'block' : 'none' }}
    >
      <div className="modal_popup_frame">
        <header className="modal_popup_header">
          <div className="popup_title">지난 이벤트 내역</div>
          <button type="button" className="btn_popup_close" onClick={closeModal} />
        </header>
        <div className="modal_popup_body">
          <div className="evt_search_area">
            <LastDstrSearchModal onSearch={handleSearch} />
          </div>
          <div className="past_evt_history_area">
            <div className="evt_search_result_area">
              <p className="evt_search_result_text">
                <span>
                  {modalPayload.startDtm && modalPayload.endDtm
                    ? `${toDashDate(modalPayload.startDtm)} ~ ${toDashDate(modalPayload.endDtm)} 까지`
                    : ''}
                  {modalPayload.locationCdList &&
                    modalPayload.locationCdList.length > 0 &&
                    ` #${getLocaNm(modalPayload.locationCdList[0])}${
                      modalPayload.locationCdList.length > 1
                        ? ` 외${modalPayload.locationCdList.length - 1}개`
                        : ''
                    }`}
                  {modalPayload.eventIdList &&
                    modalPayload.eventIdList.length > 0 &&
                    ` / #${getTypeNm(modalPayload.eventIdList[0])}${
                      modalPayload.eventIdList.length > 1
                        ? ` 외${modalPayload.eventIdList.length - 1}개`
                        : ''
                    }`}
                </span>
                검색결과 [{data?.total ?? 0}]
                {lastSearchDtm && ` (${lastSearchDtm} 기준)`}
              </p>
            </div>
            <div className="past_evt_info_wrap">
              <LastDstrListModal />
              <LastDstrDetailModal />
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
