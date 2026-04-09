import dayjs from 'dayjs';
import { useMobileStore } from '../../../entities/mobile/store';
import { DTM_OPTION } from '../../../entities/event/consts';

interface Props {
  onSearch: () => void;
}

export function LastMobileSearchArea({ onSearch }: Props) {
  const startDtm = useMobileStore((s) => s.startDtm);
  const endDtm = useMobileStore((s) => s.endDtm);
  const setStartDtm = useMobileStore((s) => s.setStartDtm);
  const setEndDtm = useMobileStore((s) => s.setEndDtm);

  const handleDtmOption = (opt: { startDtm: string; endDtm: string }) => {
    setStartDtm(opt.startDtm);
    setEndDtm(opt.endDtm);
  };

  return (
    <div className="search_setting_area">
      <h2 className="dash_sub_title past_video_title">과거 모바일 출동 영상 조회</h2>

      {/* 기간 버튼 */}
      <div className="select_block">
        <p className="select_title">
          <span>&middot;</span>기간 선택
        </p>
        <div className="dtm_btn_group">
          {DTM_OPTION.map((opt) => (
            <button
              key={opt.id}
              type="button"
              className={`btn_dtm ${startDtm === opt.startDtm ? 'active' : ''}`}
              onClick={() => handleDtmOption(opt)}
            >
              {opt.id}
            </button>
          ))}
        </div>
      </div>

      {/* 직접 날짜 입력 */}
      <div className="select_block date_range_block">
        <input
          type="date"
          className="date_input"
          value={startDtm}
          max={endDtm}
          onChange={(e) => setStartDtm(e.target.value)}
        />
        <span className="date_range_sep">~</span>
        <input
          type="date"
          className="date_input"
          value={endDtm}
          min={startDtm}
          max={dayjs().format('YYYY-MM-DD')}
          onChange={(e) => setEndDtm(e.target.value)}
        />
      </div>

      <button type="button" className="btn_search" onClick={onSearch}>
        검색
      </button>
    </div>
  );
}
