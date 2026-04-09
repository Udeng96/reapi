import React from "react";

const MobileModal:React.FC = () => {
    return(
        <>
            {/*mobileFullPopup 가로모드*/}

            {/*mobileFullPopup 세로모드*/}
            <section className="mobile_full_popup portrait_mobile_mode">
                <div className="mobile_full_popup_frame">
                    <button type="button" className="btn_mobile_full_popup_close"/>
                    <div className="video_in"/>
                    <div className="video_info_area">
                        <button type="button" className="btn_info_open"/>
                        <div className="video_info_frame">
                            <div className="video_info_block">
                                <p className="video_info_title">영상 촬영 시작 일시</p>
                                <p className="video_info_value">2022-08-01 10:42:28</p>
                            </div>
                            <div className="video_info_block">
                                <p className="video_info_title">담당자 소속-이름</p>
                                <p className="video_info_value">재난안전과-홍길동</p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </>
    )
}

export default MobileModal