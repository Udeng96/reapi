import React, {useEffect} from "react";
import TopBar from "./component/topBar/topBar";
import Router from "./router/router";
import {useDispatch} from "react-redux";
import * as userActions from "./saga/action/user/userActions"
import conf, {isDev} from "../data/config/config";


//NOTE TOKEN 셋팅은 여기서 한다.
const setToken = (() => {
    let token = "aP6R0XNpAgb8wqRMCja1BX"
    if (!isDev) {

        const getCookie = (cName: string) => {
            cName = cName + '=';
            let cookieData = document.cookie;
            let start = cookieData.indexOf(cName);
            let cValue = '';
            if (start != -1) {
                start += cName.length;
                let end = cookieData.indexOf(';', start);
                if (end == -1) end = cookieData.length;
                cValue = cookieData.substring(start, end);
            }
            return unescape(cValue);
        }

        const sessionTk = sessionStorage.getItem('token') as string
        const cookieTk = getCookie('token')
        const localTk = localStorage.getItem('token')
        token = sessionTk ?
            sessionTk : cookieTk ?
                cookieTk : localTk ?
                    localTk : ''
    }

    console.log("setToken token is ", token)
    if (token === '' || typeof token === "undefined" || token == null){
        window.location.href = conf.PLATFORM.OMS.OMS_URL
    }
    localStorage.setItem('token', token)

})()


const Root: React.FC = () => {

    const dispatch = useDispatch()

    useEffect(() => {
        dispatch(userActions.actions.requestUser())


    }, [])





    return (
        <section className="hwaseong">
            <section className="hams dashWrap">
                <TopBar/>
                <Router/>
            </section>
        </section>
    )

}
export default Root