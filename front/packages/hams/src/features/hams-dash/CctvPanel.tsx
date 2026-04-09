import { useEffect } from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Navigation } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/navigation';
import { useHamsCctvList } from '../../entities/hams/queries';
import { useHamsStore } from '../../entities/hams/store';
import type { HamsCctvInfo } from '../../entities/hams/types';

function CctvPlayer({ item, isPopup }: { item: HamsCctvInfo; isPopup: boolean }) {
  // TODO: Xeus Player 연동 (cctv-monitor와 동일한 방식으로)
  return (
    <div
      className="cctv_in"
      style={{
        width: isPopup ? 1600 : 554,
        height: isPopup ? 900 : 312,
        background: '#0d0d15',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        color: '#555869',
        fontSize: 13,
      }}
    >
      {item.cctvName}
    </div>
  );
}

export function CctvPanel() {
  const { selectedHams, selectedCctv, setSelectedCctv, setCctvPopup } = useHamsStore();
  const { data: cctvList } = useHamsCctvList(selectedHams?.facNm ?? null);

  // 국사 변경 시 첫 번째 CCTV 자동 선택
  useEffect(() => {
    if (cctvList && cctvList.length > 0) {
      setSelectedCctv(cctvList[0]);
    } else {
      setSelectedCctv(null);
    }
  }, [cctvList]);

  const renderPlayer = (isPopup = false) => {
    if (!cctvList || cctvList.length === 0) return null;

    if (cctvList.length === 1) {
      return <CctvPlayer item={cctvList[0]} isPopup={isPopup} />;
    }

    return (
      <Swiper
        modules={[Navigation]}
        navigation
        slidesPerView={1}
        onSlideChange={(swiper) => setSelectedCctv(cctvList[swiper.activeIndex])}
        style={{ width: isPopup ? 1600 : 554, height: isPopup ? 900 : 312 }}
      >
        {cctvList.map((cctv) => (
          <SwiperSlide key={cctv.cctvId}>
            <CctvPlayer item={cctv} isPopup={isPopup} />
          </SwiperSlide>
        ))}
      </Swiper>
    );
  };

  return (
    <>
      <p className="info_title title_cctv">CCTV</p>
      <div className="cctv_frame">
        <div className="cctv_area">{renderPlayer(false)}</div>
        <button
          type="button"
          className="btn_cctv_full"
          disabled={!selectedHams}
          onClick={() => setCctvPopup(true)}
        >
          CCTV 크게 보기
        </button>
      </div>
    </>
  );
}

// ─── CCTV 팝업 ────────────────────────────────────────────────────────────────

export function CctvPopup() {
  const { selectedHams, selectedCctv, isCctvPopupOpen, setCctvPopup } = useHamsStore();
  const { data: cctvList } = useHamsCctvList(selectedHams?.facNm ?? null);

  if (!isCctvPopupOpen) return null;

  return (
    <div className="hams modal_popup cctv_popup" style={{ display: 'block' }}>
      <div className="modal_popup_frame">
        <div className="modal_popup_header">
          <p className="modal_title">
            {selectedHams?.facNm}{selectedCctv ? ` - ${selectedCctv.cctvName}` : ''}
          </p>
          <div className="divide_bar" />
          <p className="loc_text">{selectedHams?.addr}</p>
          <button
            type="button"
            className="btn_popup_close"
            onClick={() => setCctvPopup(false)}
          />
        </div>
        <div className="modal_popup_body">
          <div className="cctv_area">
            {cctvList && cctvList.length === 1 ? (
              <CctvPlayer item={cctvList[0]} isPopup />
            ) : cctvList && cctvList.length > 1 ? (
              <Swiper
                modules={[Navigation]}
                navigation
                slidesPerView={1}
                style={{ width: 1600, height: 900 }}
              >
                {cctvList.map((cctv) => (
                  <SwiperSlide key={cctv.cctvId}>
                    <CctvPlayer item={cctv} isPopup />
                  </SwiperSlide>
                ))}
              </Swiper>
            ) : null}
          </div>
        </div>
      </div>
    </div>
  );
}
