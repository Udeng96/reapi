import React from "react";
import {BrowserRouter, Route, Routes} from 'react-router-dom'
import DstrRoot from "../component/dstr/dstrRoot";
import HamsRoot from "../component/hams/hamsRoot";
import MonitorRoot from "../component/monitor/monitorRoot";


const Router : React.FC = () => {

    return(
        <BrowserRouter basename={"/api"} >
            <Routes>
                <Route path = "/gis" element = {<DstrRoot/>}/>
                <Route path = "/hams" element={<HamsRoot/>}/>
                <Route path = "/cctvMonitor" element={<MonitorRoot/>}/>
            </Routes>
        </BrowserRouter>
    )

}

export default Router