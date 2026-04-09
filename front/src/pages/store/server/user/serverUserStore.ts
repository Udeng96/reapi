import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {UserInfo} from "../../../../data/interface/user/userInterface";


const initialState : UserInfo ={
    result :'',
    data : {
        "token": "",
        "userId": "",
        "userName": "",
        "loginId": "",
        "mobile": "",
        "phone": null,
        "email": null,
        "grupId": "",
        "orgnId": "",
        "dptId": "",
        "userType": "",
        "userZnCd": null,
        "eventAuthList": [

        ]
    }
}



const serverUserStore = createSlice({
    name : 'serverUserStore',
    initialState,
    reducers : {
        updateUserInfo : (state:UserInfo =initialState, action : PayloadAction<UserInfo>) =>{
            return action.payload
        }
    }

})

export const {updateUserInfo} = serverUserStore.actions
export default serverUserStore.reducer