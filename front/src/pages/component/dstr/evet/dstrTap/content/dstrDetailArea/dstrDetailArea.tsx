import React from "react";
import DstrDetailItem from "./dstrDetailItem";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../../../store/rootStore";
import {DetailInfoProps} from "../../../../../../../data/interface/event/eventInterface";

const DstrDetailArea = () => {

    const listStore = useSelector((state:RootState)=>state.server.dstrMain.event.evetList)
    const detailInfo = useSelector((state:RootState)=>state.server.dstrMain.event.evetDetail)

    // useEffect(() => {
    //     dispatch(getSelectList(null))
    // },[listStore])


    const detailInfoList:Array<DetailInfoProps> =
        detailInfo ?
            [
                {
                    title: "타입",
                    value: detailInfo.eventNm
                },
                {
                    title: "주소",
                    value: detailInfo.addr
                },
                {
                    title: "발생일시",
                    value: detailInfo.outbDtm
                },
                {
                    title: "내용",
                    value: detailInfo.cntn
                },
            ]
            :
            [
                {
                    title: "타입",
                    value: ''
                },
                {
                    title: "주소",
                    value: ''
                },
                {
                    title: "발생일시",
                    value: ''
                },
                {
                    title: "내용",
                    value: ''
                },
            ]
    return (

        <div className="detail_info_area">
            <h2 className="dash_sub_title event_detail_title">선택 이벤트 상세정보</h2>
            <div className="detail_info_frame">
                <DstrDetailItem isModal={false} detailInfoList={detailInfoList} detailStore={detailInfo} eqKey={`tapDstrDetailItem`}/>
            </div>
        </div>

    )
}

export default DstrDetailArea