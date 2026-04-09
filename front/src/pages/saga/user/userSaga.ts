import {call, put, takeLatest} from "redux-saga/effects"
import * as userActions from "../action/user/userActions"
import {requestUser} from "../apis/user/userApis";
import {updateUserInfo} from "../../store/server/user/serverUserStore";
import {UserInfo} from "../../../data/interface/user/userInterface";
import conf, {isDev} from "../../../data/config/config";


const requestUserInfo = function* () {

    try {
        const user:UserInfo  = isDev? {
            result : '',
            data :{
                token : '',
                userId: '',
                userName: '',
                loginId: '',
                mobile: '',
                phone: '',
                email: '',
                grupId: '',
                orgnId: '',
                dptId: '',
                userType: '002',
                userZnCd: '',
                eventAuthList:null,
            }
        } : yield call(requestUser)
        if (user){
            yield put(updateUserInfo(user))
        }else {
            if (!isDev){ //만약 dev mode 일경우 팅기지않는다.
                window.location.href = conf.PLATFORM.OMS.OMS_URL
            }
        }
    } catch (error) {
        if (isDev){ //만약 dev mode 일경우 팅기지않는다.

        }else {
            window.location.href = conf.PLATFORM.OMS.OMS_URL
        }
    }
}


export default function* userSaga() {
    yield takeLatest(userActions.types.REQUEST_USER, requestUserInfo)
}
