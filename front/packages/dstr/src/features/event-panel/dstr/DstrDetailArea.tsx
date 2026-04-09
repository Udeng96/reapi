import { useEventStore } from '../../../entities/event/store';
import { useEventDetail } from '../../../entities/event/queries';

function formatDtm(dtm: string | null): string {
  if (!dtm) return '';
  return `${dtm.substring(0, 4)}년 ${dtm.substring(4, 6)}월 ${dtm.substring(6, 8)}일 ${dtm.substring(8, 10)}시 ${dtm.substring(10, 12)}분 ${dtm.substring(12, 14)}초`;
}

interface DetailRow {
  title: string;
  value: string | null;
}

export function DstrDetailArea() {
  const activeItem = useEventStore((s) => s.activeItem);
  const { data: detail } = useEventDetail(activeItem);

  const rows: DetailRow[] = [
    { title: '타입', value: detail?.eventNm ?? '' },
    { title: '주소', value: detail?.addr ?? '' },
    { title: '발생일시', value: formatDtm(detail?.outbDtm ?? null) },
    { title: '내용', value: detail?.cntn ?? '' },
  ];

  return (
    <div className="detail_info_area">
      <h2 className="dash_sub_title event_detail_title">선택 이벤트 상세정보</h2>
      <div className="detail_info_frame">
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
