import React from "react";
import { CctvInfo } from "../../../../../../data/interface/cctv/cctvInterface";

const CctvDetailArea = (props: { info: CctvInfo | null }) => {
    return (
        <>
            <div className="detail_info_area">
                <h2 className="dash_sub_title cctv_detail_title">선택 CCTV 상세정보</h2>

                <div className="detail_info_frame">
                    <div className="detail_info_block">
                        <p className="detail_info_title">설치목적</p>
                        <div className="detail_info_box">
                            <div className="detail_info_value">{props.info?.clfyNm}</div>
                        </div>
                    </div>
                    <div className="detail_info_block">
                        <p className="detail_info_title">명칭</p>
                        <div className="detail_info_box">

                            {
                                props.info ?
                                    <div className="detail_info_value">
                                        {props.info?.facNm}
                                    </div>
                                    :
                                    <div className="no_select_area">
                                        <p className="no_select_message">선택된 CCTV가 없습니다.</p>
                                    </div>
                            }


                        </div>
                    </div>


                </div>
            </div>
        </>
    )
}

export default CctvDetailArea