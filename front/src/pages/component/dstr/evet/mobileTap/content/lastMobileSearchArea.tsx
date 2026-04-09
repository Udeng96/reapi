import React, {useEffect, useState} from "react";
import moment from "moment";
import {DTM_OPTION} from "../../../../../../data/const/mobileTapConsts";
import DatePicker from "react-datepicker";
import {useDispatch} from "react-redux";
import * as mobileActions from "../../../../../saga/action/dstr/evet/mobileActions";
import {updateEndDtm, updateStartDtm} from "../../../../../store/view/dstr/dstrViewMobileStore";

import _ from "underscore"

const LastMobileSearchArea = (props: {
    startDtm: string, setStartDtm: Function,
    endDtm: string, setEndDtm: Function
}) => {

    const dispatch = useDispatch()

    const [updateDtm, setUpdateDtm] = useState(moment().format("yyyy-MM-DD HH:mm:ss"))
    const [selectDtmOption, setSelectDtmOption] = useState<string>(DTM_OPTION[3].id)


    const clickUpdateBtn = () => {
        setUpdateDtm(moment().format("yyyy-MM-DD HH:mm:ss"))
        dispatch(mobileActions.actions.requestMobilePastList({
                startDtm : props.startDtm,
                endDtm : props.endDtm,
                rows : 10,
                page : 1}))

        dispatch(updateStartDtm(props.startDtm))
        dispatch(updateEndDtm(props.endDtm))
    }

    const setDateOption = (id: string, startDtm: string, endDtm: string, e: any) => {
        props.setStartDtm(startDtm)
        props.setEndDtm(endDtm)
        setSelectDtmOption(id)
    }

    const dateToStr = (date: Date) => {
        let d = new Date(date);
        let month = '' + (d.getMonth() + 1);
        let day = '' + d.getDate();
        let year = d.getFullYear();

        if (month.length < 2)
            month = '0' + month;
        if (day.length < 2)
            day = '0' + day;

        return [year, month, day].join('-');
    }


    useEffect(() => {

        const findIndex = _.findIndex(DTM_OPTION,{
            startDtm : props.startDtm,
            endDtm : props.endDtm
        })

        if (findIndex > -1){
            setSelectDtmOption(DTM_OPTION[findIndex].id)
        }else {
            setSelectDtmOption('')
        }


    }, [props.startDtm ,props.endDtm]);


    return (
        <>
            <div className="search_setting_area">
                <h2 className="dash_sub_title video_search_title">과거 모바일 출동영상 리스트 검색</h2>
                <div className="select_block">
                    <p className="select_title"><span>&middot;</span>촬영 일자</p>
                    <div className="btn_period_group">
                        {
                            DTM_OPTION.map((item, index) => (
                                <button type="button"
                                        id={item.id}
                                        key={`DTM_OPTION_${index}`}
                                        className={`btn_period ${(selectDtmOption === item.id) ? "active" : ""}`}
                                        onClick={event => setDateOption(item.id, item.startDtm, item.endDtm, event)}>{item.name}
                                </button>
                            ))
                        }
                    </div>
                </div>
                <div className="select_block">
                    <div className="datepicker_area">
                        <div className="calendar_input_box">
                            <DatePicker wrapperClassName={"datePicker"}
                                        onChange={date =>
                                            props.setStartDtm(dateToStr(date!!))}
                                        dateFormat={"yyyy-MM-dd"}
                                        selected={new Date(props.startDtm)}
                                        maxDate={new Date(props.endDtm)}
                                        customInput={<input type="text" className={"calendar_input"}/>}
                            />
                        </div>
                        <p className="term_text">~</p>
                        <div className="calendar_input_box">
                            <DatePicker wrapperClassName={"datePicker"}
                                        onChange={date =>
                                            props.setEndDtm(dateToStr(date!!))}
                                        dateFormat={"yyyy-MM-dd"}
                                        selected={new Date(props.endDtm)}
                                        minDate={new Date(props.startDtm)}
                                        maxDate={new Date()}
                                        customInput={<input type="text" className={"calendar_input"}/>}
                            />
                        </div>
                    </div>
                </div>
                <button type="button" className="btn_search"
                        onClick={clickUpdateBtn}>검색<span>(업데이트 일시 : {updateDtm})</span>
                </button>
            </div>
        </>
    )
}

export default LastMobileSearchArea