import {xhr} from "../xhr"
import conf from "../../../../data/config/config"
import axios from "axios";

export const requestUser = async () => {
    const {data} = await xhr.get(conf.API.URL.USER.URL_USER.INFO)
    return data
}

export const doLogOut = async () => {

    const token = localStorage.getItem("token")
    console.log('token', token)


    if (token === '' || typeof token === "undefined" || token == null) {
        window.location.href = conf.PLATFORM.OMS.OMS_URL
    }

    await axios.post(
        conf.PLATFORM.OMS.logOut, null,
        {
            params: {token},
            headers: {
                systemKey: conf.API.SYSTEM_KEY,
                secret: conf.API.SYSTEM_SECRET
            }
        }
    ).then(function (response) {
        // 성공한 경우 실행
        console.log(response);
        const {data} = response
        const result = data.result
        const message = data.message

        if (result !== 'FAIL'){
            window.location.href = conf.PLATFORM.OMS.OMS_URL
        }else {
            alert(`로그아웃에 실패하였습니다.`)
        }

    }).catch(function (error) {
        // 에러인 경우 실행
        console.log(error);
    })


}