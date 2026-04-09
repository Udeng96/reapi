import { useState } from 'react';
import dayjs from 'dayjs';
import { useEventStore } from '../../../entities/event/store';
import { useEventTypes, useLocations } from '../../../entities/event/queries';
import type { EventsPayload } from '../../../entities/event/types';
import { DstrSearchBox } from './DstrSearchBox';

const BOX_LIST = [
  { type: 'location', title: '지역' },
  { type: 'eventType', title: '이벤트 유형' },
] as const;

export function DstrSearchArea() {
  const [updateTime, setUpdateTime] = useState(dayjs().format('YYYY-MM-DD HH:mm:ss'));

  const eventPayload = useEventStore((s) => s.eventPayload);
  const setEventPayload = useEventStore((s) => s.setEventPayload);
  const setActiveItem = useEventStore((s) => s.setActiveItem);
  const setActiveModal = useEventStore((s) => s.setActiveModal);

  const checkedLocaList = useEventStore((s) => s.checkedLocaList);
  const checkedTypeList = useEventStore((s) => s.checkedTypeList);

  const { data: locations } = useLocations();
  const { data: eventTypes } = useEventTypes();

  const getSearchOptions = (boxType: string) => {
    return boxType === 'location' ? (locations ?? null) : (eventTypes ?? null);
  };

  const getChkOptionsList = (boxType: string) => {
    return boxType === 'location' ? checkedLocaList : checkedTypeList;
  };

  const clickSearchBtn = () => {
    const eventIdList = checkedTypeList
      .filter((item) => item.id !== 'all')
      .map((item) => item.id);
    const locationCdList = checkedLocaList
      .filter((item) => item.id !== 'all')
      .map((item) => item.id);

    const payload: EventsPayload = {
      ...eventPayload,
      eventIdList: eventIdList.length > 0 ? eventIdList : null,
      locationCdList: locationCdList.length > 0 ? locationCdList : null,
      rows: 3000,
    };

    setEventPayload(payload);
    setActiveItem(null);
    setUpdateTime(dayjs().format('YYYY-MM-DD HH:mm:ss'));
  };

  return (
    <div className="search_setting_area">
      <h2 className="dash_sub_title event_search_title">이벤트 검색 조건 설정</h2>
      <button
        type="button"
        className="btn_event_history"
        onClick={() => setActiveModal(true)}
      >
        지난 이벤트 내역
      </button>

      {BOX_LIST.map((item) => (
        <div className="select_block" key={item.type}>
          <p className="select_title">
            <span>&middot;</span>
            {item.title}
          </p>
          <DstrSearchBox
            searchBoxType={item.type}
            searchOptions={getSearchOptions(item.type)}
            chkOptionsList={getChkOptionsList(item.type)}
          />
        </div>
      ))}

      <button type="button" className="btn_search" onClick={clickSearchBtn}>
        검색<span>{`(업데이트 일시 : ${updateTime})`}</span>
      </button>
    </div>
  );
}
