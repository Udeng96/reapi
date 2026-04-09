import React from "react";
import {useSelector} from "react-redux";
import {RootState} from "../../../../../store/rootStore";
import NowMobileListItem from "./nowMobileListItem";

const NowMobileListArea = () => {

    const liveLists = useSelector((state: RootState) => state.server.dstrMain.mobile.nowList)
    const liveListTotal = useSelector((state: RootState) => state.server.dstrMain.mobile.nowListTotal)

    return (
        <>
            <div className="search_setting_area">
                <h2 className="dash_sub_title live_video_title">실시간 모바일 출동 영상 리스트</h2>
                <p className="recent_count_value">현재 [{liveListTotal}]명 촬영중</p>
                <ul className="live_list_head">
                    <li>No</li>
                    <li>영상 촬영 시작 일시</li>
                    <li>담당자 소속-이름</li>
                    <li/>
                    <li/>
                </ul>
                <div className="live_list_frame">
                    {
                        liveLists!==null&&liveLists.length!==0?
                            <ul className="live_list_body">
                                {liveLists.map((item, index) => (
                                    <NowMobileListItem
                                        key = {`NowMobileListItem_${index}`}
                                        item={item} index={index+1}
                                    />
                                ))}
                            </ul>:
                            <div className="no_video_area">
                                <p className="no_video_message">
                                    <span>현재 촬영중인 영상이 없습니다.</span>
                                </p>
                            </div>
                    }
                </div>
            </div>
        </>
    )
}

export default NowMobileListArea
