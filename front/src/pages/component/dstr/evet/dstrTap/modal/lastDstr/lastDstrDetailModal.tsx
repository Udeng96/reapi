import React from "react";
import DstrDetailItem from "../../content/dstrDetailArea/dstrDetailItem";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../../../store/rootStore";
import {DetailInfoProps} from "../../../../../../../data/interface/event/eventInterface";

const LastDstrDetailModal = (props:{isOpen:boolean}) => {

    const dispatch = useDispatch()

    const detailInfo = useSelector((state:RootState)=>state.server.dstrMain.event.evetDetailModal)


    // useEffect(()=>{
    //     dispatch(getModalSelectList(null))
    // },[props.isOpen])


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
    return(

            <div className="evt_detail_info">
                <p className="evt_detail_info_title">이벤트 상세정보</p>
                <div className="evt_detail_info_frame">
                    <DstrDetailItem isModal={true} detailInfoList={detailInfoList} detailStore={detailInfo} eqKey={`modalDstrDetailItem`}/>
                </div>
            </div>

    )
}

export default LastDstrDetailModal