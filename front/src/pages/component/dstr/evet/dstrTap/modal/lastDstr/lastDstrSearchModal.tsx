import React, {useEffect, useState} from "react";
import {EventsPayload} from "../../../../../../../data/interface/event/eventInterface";
import moment from "moment";
import DstrSearchBox from "../../content/dstrSearchArea/dstrSearchBox";
import DatePicker, {registerLocale} from "react-datepicker";
import ko from 'date-fns/locale/ko';
import {DTM_OPTION, MODAL_EVENT_TAP_BOX_TYPE, SEARCH_BOX_TYPE} from "../../../../../../../data/const/eventTapConsts";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../../../store/rootStore";
import * as eventActions from "../../../../../../saga/action/dstr/evet/evet";
import {getActiveModalItem, getModalPayload} from "../../../../../../store/view/dstr/dstrViewEventStore";


registerLocale('ko', ko);

const LastDstrSearchModal = (props: { isOpen: boolean }) => {


    const [startDtm, setStartDtm] = useState<string>(DTM_OPTION[3].startDtm)
    const [endDtm, setEndDtm] = useState<string>(DTM_OPTION[3].endDtm)
    const [selectDtmOption, setSelectDtmOption] = useState<string>(DTM_OPTION[3].id)
    const [nowDateTime, setNowDateTime] = useState(moment().format("YYYY-MM-DD HH:mm:ss"))

    let searchLocaOptions = useSelector((state: RootState) => state.server.dstrMain.event.evetLoca);
    let searchTypeOptions = useSelector((state: RootState) => state.server.dstrMain.event.evetType)
    let chkLocaOptionsList = useSelector((state: RootState) => state.view.dstrMain.event.checkedModalLocaList);
    let chkTypeOptionsList = useSelector((state: RootState) => state.view.dstrMain.event.checkedModalTypeList);
    let payload = useSelector((state: RootState) => state.view.dstrMain.event.modalPayload);

    const dispatch = useDispatch()

    useEffect(() => {
        setStartDtm(DTM_OPTION[3].startDtm);
        setEndDtm(DTM_OPTION[3].endDtm);
        setSelectDtmOption(DTM_OPTION[3].id)
        strDateToDate(startDtm)
    }, [props.isOpen])

    useEffect(() => {

        setDateOption("none", startDtm, endDtm, null)

        DTM_OPTION.map((item) => {
            if (startDtm === item.startDtm) setDateOption(item.id, startDtm, endDtm, null)
        })
    },[startDtm])

    const setDateOption = (id: string, startDtm: string, endDtm: string, e: any) => {
        setStartDtm(startDtm); // 시작 날짜
        setEndDtm(endDtm); // 끝 날짜 설정
        setSelectDtmOption(id) // 클릭한 버튼 설정 ( 1주, 1개월, 3개월, 6개월, 1년 )
    }

    // 검색 버튼 눌렀을 때
    const clickSearchBtn = () => {

        let eventIdList: Array<string> = []
        let locationCdList: Array<string> = []

        for (let item of chkLocaOptionsList) {
            if (item.id !== "all") {
                locationCdList.push(item.id)
            }
        }
        for (let item of chkTypeOptionsList) {
            if (item.id !== "all") {
                eventIdList.push(item.id)
            }
        }

        let payload: EventsPayload = {
            eventIdList: eventIdList ? eventIdList : null,
            locationCdList: locationCdList ? locationCdList : null,
            startDtm: startDtm.replaceAll("-", ""),
            endDtm: endDtm.replaceAll("-", ""),
            page: 1,
            rows: 10
        }

        // payload 설정
        dispatch(getModalPayload(payload))
        setNowDateTime(moment().format("YYYY-MM-DD HH:mm:ss"))

        // 검색 리스트 조회
        dispatch(eventActions.actions.requestModalEvents(payload))
        dispatch(getActiveModalItem(null))

        //WHAT IS IT?
        dispatch(eventActions.actions.requestModalDetail(null))

    }


    const strDateToDate = (date: string) => {

        let dateSplit = date.split("-")
        return new Date(parseInt(dateSplit[0]), parseInt(dateSplit[1]), parseInt(dateSplit[2]))

    }

    const dateTostr = (date: Date) => {
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

    return (
        <>
            <div className="evt_search_row">
                <div className="evt_search_name"><span>&middot;</span>검색 기간 설정</div>
                <div className="btn_period_group">
                    {
                        DTM_OPTION.map((item) => (
                            <button type="button" key={item.id}
                                id={item.id}
                                className={`btn_period ${(selectDtmOption === item.id) ? "active" : ""}`}
                                onClick={event => setDateOption(item.id, item.startDtm, item.endDtm, event)}>{item.name}
                            </button>
                        ))
                    }
                </div>
                <div className="datepicker_area">
                    <div className="calendar_input_box">
                        {/*<input type="text" className="calendar_input" style={{"display": "block"}} value={startDtm}/>*/}
                        <DatePicker wrapperClassName={'datePicker'}
                            onChange={(date: any) => {
                                setStartDtm(dateTostr(date!!))
                            }}
                            locale="ko"
                            selectsStart
                            dateFormat={"yyyy-MM-dd"}
                            selected={new Date(startDtm)}
                            minDate={new Date(startDtm)}
                            maxDate={new Date(endDtm)}
                            customInput={<input type="text" className="calendar_input" value={startDtm}/>}
                        />
                    </div>
                    <p className="term_text">~</p>
                    <div className="calendar_input_box">
                        {/*<input type="text" className="calendar_input" value={endDtm}/>*/}
                        <DatePicker wrapperClassName={'datePicker'}
                            className={'calendar_input'}
                            locale="ko"
                            onChange={(date: any) => setEndDtm(dateTostr(date!!))}
                            selectsEnd
                            dateFormat={"yyyy-MM-dd"}
                            selected={new Date(endDtm)}
                            minDate={new Date(startDtm)}
                            maxDate={new Date()}
                            customInput={<input type="text" className="calendar_input" />}
                        />
                    </div>
                </div>
            </div>

            {
                MODAL_EVENT_TAP_BOX_TYPE.map((item) => (

                    <div className="evt_search_row" key={item.type}>
                        <div className="evt_search_name"><span>&middot;</span>{item.title}</div>
                        <DstrSearchBox
                            searchBoxType={item.type}
                            searchOptions={item.type === SEARCH_BOX_TYPE.MODAL_LOCATION ? searchLocaOptions : searchTypeOptions}
                            chkOptionsList={item.type === SEARCH_BOX_TYPE.MODAL_LOCATION ? chkLocaOptionsList : chkTypeOptionsList}
                        />
                    </div>

                ))
            }
            <button type="button" className="btn_evt_search"
                onClick={event => clickSearchBtn()}>검색
                <span>{`(업데이트 일시 : ${nowDateTime})`}</span>
            </button>
        </>
    )
}

export default LastDstrSearchModal