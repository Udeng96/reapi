import CctvSearchArea from "./content/cctvSearchArea";
import CctvListArea from "./content/cctvListArea/cctvListArea";
import CctvDetailArea from "./content/cctvDetailArea";
import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import * as actions from "../../../../saga/action/dstr/cctv/cctvActions"
import {RootState} from "../../../../store/rootStore";

const CctvTabArea: React.FC = () => {

    const dispatch = useDispatch()

    useEffect(() => {
        //초기값을 셋팅한다.
        dispatch(actions.actions.fetchList({
            page: 1,
            rows: 11
        }))

        dispatch(actions.actions.fetchCLFY())
    }, [])

    const [currentPage, setCurrentPage] = useState(1);
    // const [activeCctv , setActiveCctv] = useState<CctvInfo|null>(null)

    const activeCctv = useSelector((state: RootState) => state.view.dstrMain.cctv.activeCctv)


    return (
        <div className="dash_wrap cctv_search_wrap">
            <CctvSearchArea setCurrentPage={setCurrentPage}/>
            <CctvListArea currentPage={currentPage} setCurrentPage={setCurrentPage}/>
            <CctvDetailArea info={activeCctv}/>
        </div>
    )
}

export default CctvTabArea