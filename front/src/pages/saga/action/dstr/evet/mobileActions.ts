import {createAction} from "@reduxjs/toolkit";
import {SearchDefaultOption} from "../../../../../data/interface/common/CommonInterface";

const types={
    REQUEST_MOBILE_PAST_LIST : `REQUEST_MOBILE_PAST_LIST`,
    REQUEST_MOBILE_NOW_LIST : `REQUEST_MOBILE_NOW_LIST`
}

const actions={

    requestMobilePastList : createAction<SearchDefaultOption>(types.REQUEST_MOBILE_PAST_LIST),
    requestMobileNowList : createAction(types.REQUEST_MOBILE_NOW_LIST)

}

export {types,actions}