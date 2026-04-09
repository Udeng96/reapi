import React from "react";
import {DetailInfoProps, VwEvent} from "../../../../../../../data/interface/event/eventInterface";

const DstrDetailItem = (props:{isModal:boolean,detailInfoList:Array<DetailInfoProps>,detailStore:VwEvent|null , eqKey:string}) => {

    const setDtm = (dtm : any) => {
        let year = dtm.substring(0,4);
        let month = dtm.substring(4,6);
        let date = dtm.substring(6,8);
        let hour = dtm.substring(8,10);
        let min = dtm.substring(10,12);
        let sec = dtm.substring(12,14);

        return year+'년 '+month+'월 '+date+'일 '+hour+'시 '+min+'분 '+sec+'초 '
    }
    return (
        <>
            {
                props?.detailStore ?
                    props.detailInfoList.map((item,index) => (
                        <div className="detail_info_block" key={`${props.eqKey}_${item.value}_${index}`}>
                            <p className="detail_info_title">{item.title}</p>
                            <div className={`detail_info_box ${item.title==="내용"? 'wide_size':''}`}>
                                <div className={`detail_info_value`}>{item.title==="발생일시"? setDtm(item.value) : item.value}</div>
                            </div>
                        </div>
                    )) :
                    props.detailInfoList.map((item,index) => (
                        <React.Fragment key={`${props.eqKey}_${item.value}_${index}`}>
                            <div className="detail_info_block">
                                <p className="detail_info_title">{item.title}</p>
                                <div className={`detail_info_box ${item.title==="내용"? 'wide_size':''}`}>
                                    <div className={`detail_info_value`}>{item.title==="발생일시"? setDtm(item.value) : item.value}</div>
                                </div>
                            </div>
                            <div className="no_select_area" >
                                <p className="no_select_message">선택된 이벤트가 없습니다.</p>
                            </div>

                        </React.Fragment>

                ))
            }


        </>
    )
}

DstrDetailItem.defaultProps = {
    eqKey : ''
}


export default DstrDetailItem