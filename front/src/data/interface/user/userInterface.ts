import {VwIocEvet} from "../event/eventInterface";


export interface UserInfo {
    result : string,
    data : UserData | null
}
export interface UserData {
    token : string,
    userId: string,
    userName: string,
    loginId: string,
    mobile: string,
    phone: string |null,
    email: string | null,
    grupId: string,
    orgnId: string,
    dptId: string,
    userType: string,
    userZnCd: string | null,
    eventAuthList? : VwIocEvet[] | null,
}