import { useEventStore } from '../../../entities/event/store';
import { useEvents, useLocations, useEventTypes } from '../../../entities/event/queries';
import { DstrListItem } from './DstrListItem';

interface Props {
  isModal?: boolean;
}

export function DstrListArea({ isModal = false }: Props) {
  const eventPayload = useEventStore((s) => s.eventPayload);
  const modalPayload = useEventStore((s) => s.modalPayload);
  const setEventPayload = useEventStore((s) => s.setEventPayload);

  const payload = isModal ? modalPayload : eventPayload;

  const { data, isError, refetch } = useEvents(payload);
  const { data: locations } = useLocations();
  const { data: eventTypes } = useEventTypes();

  const eventList = data?.data ?? null;
  const total = data?.total ?? 0;

  const getLocaNm = (id: string) =>
    locations?.find((item) => item.id === id)?.nm ?? '';

  const getTypeNm = (id: string) =>
    eventTypes?.find((item) => item.id === id)?.nm ?? '';

  const reload = () => {
    if (!isModal) {
      setEventPayload({ ...eventPayload });
    }
    refetch();
  };

  return (
    <>
      <div className="search_result_area">
        <p className="search_result_text">
          {payload.locationCdList && payload.locationCdList.length > 0 && (
            <>
              <span className="result_value"> #{getLocaNm(payload.locationCdList[0])} </span>
              {payload.locationCdList.length > 1 ? (
                <span>외{payload.locationCdList.length - 1}개 /</span>
              ) : (
                <span>&nbsp;/</span>
              )}
            </>
          )}
          {payload.eventIdList && payload.eventIdList.length > 0 && (
            <>
              <span className="result_value"> #{getTypeNm(payload.eventIdList[0])} </span>
              {payload.eventIdList.length > 1 && (
                <span>외{payload.eventIdList.length - 1}개</span>
              )}
            </>
          )}
          검색결과 [{eventList && eventList.length > 0 ? total : '0'}]
        </p>
      </div>
      <div className="search_list_frame">
        <ul className="search_list_head">
          <li>지역 명</li>
          <li>이벤트 타입</li>
          <li>주소</li>
          <li>발생 일시</li>
        </ul>
        <ul className="search_list_body">
          {isError ? (
            <div className="event_load_error_area">
              <button type="button" className="btn_reload" onClick={reload} />
              <p className="event_load_error_message">
                이벤트 로드에 문제가 발생하였습니다.
                <br />
                <span>Reload 또는 재검색을 시도해주세요.</span>
              </p>
            </div>
          ) : eventList === null || eventList.length === 0 ? (
            <div className="no_result_area">
              <p className="no_result_message">
                검색 조건의 오늘 발생한 재난 이벤트가 없습니다.
                <br />
                <span>지난 이벤트 내역은 &apos;지난 이벤트 내역&apos; 팝업을 통해 확인해주세요.</span>
              </p>
            </div>
          ) : (
            eventList.map((item) => (
              <DstrListItem key={item.statEvetOutbSeqn} item={item} isModal={isModal} />
            ))
          )}
        </ul>
      </div>
    </>
  );
}
