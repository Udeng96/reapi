import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../store/rootStore";
import {updateCycleState} from "../../../store/view/monitor/monitorViewStore";
import {PageTransType} from "../../../../data/const/monitorMenuConst";

const TopMenu = () => {

    const dispatch = useDispatch()

    const isMenuOpen = useSelector((state:RootState)=> state.view.monitor.isMenuOpen)
    const pageTransCycle = useSelector((state:RootState)=>state.view.monitor.pageTransCycle)//사이클
    const isPageTrans = useSelector((state:RootState)=>state.view.monitor.isPageTrans) as PageTransType

    const onClickStopCycleBtn = () => {
        if (isPageTrans === PageTransType.STOP){ //다시시작
            dispatch(updateCycleState(PageTransType.PLAY))
        }else if (isPageTrans === PageTransType.PLAY){//중지
            dispatch(updateCycleState(PageTransType.STOP))
        }

    }

    return(
        <div className="change_control_area" style={{"display":`${isMenuOpen?"flex":"none"}`}}>
            <p className={`change_text ${isPageTrans === PageTransType.PLAY? "" : "dimmed"}`}>
                {isPageTrans === PageTransType.PLAY ?"전환중" :
                    isPageTrans === PageTransType.STOP ? "전환 STOP":
                    "미전환"}
                <span>{isPageTrans?`[${pageTransCycle.label}]`:""}</span>
            </p>
            <button type="button" className={`btn_change_stop ${isPageTrans?"" : "dimmed"}`} onClick={onClickStopCycleBtn}>
                전환
                {isPageTrans == PageTransType.PLAY ? "STOP": "START"}
            </button>
        </div>
    )

}

export default TopMenu