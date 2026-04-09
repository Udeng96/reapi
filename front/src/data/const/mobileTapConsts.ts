import {SearchDtm} from "../interface/event/eventInterface";
import moment from "moment";

export const DTM_OPTION : Array<SearchDtm> = [
    {
        id : "oneYear",
        name: "1년",
        startDtm : moment().subtract(1,'years').format("YYYY-MM-DD"),
        endDtm : moment().format("YYYY-MM-DD")
    },
    {
        id: "sixMonths",
        name: "6개월",
        startDtm : moment().subtract(6,'months').format("YYYY-MM-DD"),
        endDtm : moment().format("YYYY-MM-DD")
    },
    {
        id: "threeMonths",
        name : "3개월",
        startDtm : moment().subtract(3,'months').format("YYYY-MM-DD"),
        endDtm : moment().format("YYYY-MM-DD")
    },
    {
        id : "oneMonth",
        name : "1개월",
        startDtm : moment().subtract(1,'months').format("YYYY-MM-DD"),
        endDtm : moment().format("YYYY-MM-DD")
    },
    {
        id : "oneWeek",
        name: "1주일",
        startDtm : moment().subtract(1,'weeks').format("YYYY-MM-DD"),
        endDtm : moment().format("YYYY-MM-DD")
    },
    {
        id : "today",
        name: "오늘",
        startDtm : moment().format("YYYY-MM-DD"),
        endDtm : moment().format("YYYY-MM-DD")
    }
]