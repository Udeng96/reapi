 import React from 'react';
// import ReactDOM from 'react-dom';
import ReactDOM from "react-dom/client";
import reportWebVitals from './reportWebVitals';
import Root from "./pages/root";


//common
import './public/static/css/reset.css'
import './public/static/css/fontStyle.css'
import './public/static/css/topBar.css'
import './public/static/css/common.css'
import './public/static/css/modalPopup.css'
import './public/static/css/reactDatePicker.css'

import './public/static/css/gis/cctvContainer.css'
import './public/static/css/gis/cctvFullPopup.css'
import './public/static/css/gis/cctvSearchWrap.css'
import './public/static/css/gis/checkBox.css'

//gis
import './public/static/css/gis/dashContainer.css'
import './public/static/css/gis/disasterEventWrap.css'

import './public/static/css/gis/gis.css'
import './public/static/css/gis/gisIcon.css'
import './public/static/css/gis/gisPopup.css'

import './public/static/css/gis/mobileFullPopup.css'
import './public/static/css/gis/mobileVideoWrap.css'

import './public/static/css/gis/pagination.css'
import './public/static/css/gis/pastEvtHistoryPopup.css'
import './public/static/css/gis/radioButton.css'
import './public/static/css/gis/ripple.css'
import './public/static/css/gis/scrollStyle.css'
import './public/static/css/gis/tooltip.css'


//hams
import './public/static/css/hams/modalPopup.css'
import './public/static/css/hams/cctvPopup.css'
import './public/static/css/hams/dashContainer.css'
import './public/static/css/hams/doorlockPopup.css'
import './public/static/css/hams/gis.css'
import './public/static/css/hams/gisIcon.css'
import './public/static/css/hams/historyPopup.css'
import './public/static/css/hams/menuContainer.css'
import './public/static/css/hams/scrollStyle.css'
import './public/static/css/hams/tooltip.css'
 import 'swiper/css';
 import 'swiper/css/navigation';
 import 'swiper/css/pagination';
 import 'swiper/css/scrollbar';

import './public/static/css/monitor/cctvContainer.css'
import './public/static/css/monitor/changeControlArea.css'
import './public/static/css/monitor/dashContainer.css'
import './public/static/css/monitor/scrollStyle.css'
import './public/static/css/monitor/treeCustom.css'


import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-datepicker/dist/react-datepicker.css';
import {Reset} from "styled-reset";


import './public/static/component/jquery/jquery-ui.css'
import createSagaMiddleware from "redux-saga";
import {configureStore} from "@reduxjs/toolkit";
import rootSaga from "./pages/saga/rootSaga";
import {Provider} from "react-redux";
import rootStore from "./pages/store/rootStore";


const sagaMiddleware = createSagaMiddleware();

const store = configureStore({
    reducer: rootStore,
    middleware: [sagaMiddleware]
})

sagaMiddleware.run(rootSaga);

const rootNode = document.getElementById('root') as HTMLElement

ReactDOM.createRoot(rootNode).render(
    // <React.StrictMode>
        <Provider store={store}>
            <Reset />
            <Root />
        </Provider>
    // </React.StrictMode>,
);


// const root = ReactDOM.createRoot(document.getElementById("root") as HTMLElement);
// root.render(
//     <React.StrictMode>
//         <App />
//     </React.StrictMode>
// );

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
