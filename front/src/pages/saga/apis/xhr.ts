import axios from "axios";
import {USE_AUTH} from "../../../data/config/config";

const fetchClient = () => {

    let instance = axios.create();

    instance.interceptors.request.use(function (config) {
        let token:string = ``
        if (typeof window !== 'undefined') {

            token = localStorage.getItem('token') as string;
        }
        config.headers!.token =  token ? token : '';
        return config;
    });

    return instance;
};
export const xhr = fetchClient()