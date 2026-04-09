import CctvFrame from "./cctvFrame";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../store/rootStore";
import {useEffect, useState} from "react";
import {updateCycleState, updateNowPagePlayList} from "../../../store/view/monitor/monitorViewStore";
import {useDrop} from "react-dnd";
import {PageTransType} from "../../../../data/const/monitorMenuConst";
import {usePrevious} from "@restart/hooks";

const CctvArea = () => {

    const dispatch = useDispatch();


    // cctv frame
    const gridRatio = useSelector((state: RootState) => state.view.monitor.gridRatio)
    const currentPagePlayList = useSelector((state: RootState) => state.view.monitor.currentPagePlayList)

    // menu state
    const isMenuOpen = useSelector((state: RootState) => state.view.monitor.isMenuOpen)

    // drag & drop
    const dragTargetInfo = useSelector((state: RootState) => state.view.monitor.dragTargetInfo)

    // cctv cycle
    const isPageTrans = useSelector((state: RootState) => state.view.monitor.isPageTrans) as PageTransType
    const previousPageTrans = usePrevious(isPageTrans)
    const pageTransCycle = useSelector((state: RootState) => state.view.monitor.pageTransCycle)
    const allPagePlayList = useSelector((state: RootState) => state.view.monitor.allPagePlayList)

    const [intervalId, setIntervalId] = useState<any>(null);
    const [timeOutId, setTimeOutId] = useState<any>(null);
    const [currentPage, setCurrentPage] = useState<number>(0);
    const [gridAmount, setGridAmount] = useState<number>(9);


    // 초기 grid 형성
    useEffect(() => {

        setGridAmount(gridRatio[0] * gridRatio[1])

        // grid 비율이 변화하거나 처음 rendering 시
        if (currentPagePlayList.length !== gridAmount) {
            let newPlayCctvList = []
            for (let i = 0; i < gridAmount; i++) {
                newPlayCctvList.push(null)
            }
            dispatch(updateNowPagePlayList(newPlayCctvList))
        }

    }, [gridRatio, gridAmount])


    // 화면 전환 interval
    const cycle = (index: number) => {
        return setInterval(() => {

            let pageCnt = allPagePlayList.length;

            if (index < pageCnt - 1) {
                index = index + 1;
                setCurrentPage(index);
            } else {
                setCurrentPage(0);
                index = 0;
            }

            console.log("allPagePlayList[index] ", allPagePlayList[index]);
            console.log('현재 cycle index ,', index)

            dispatch(updateNowPagePlayList(allPagePlayList[index]))

        }, parseInt(pageTransCycle.value))


    }

    // cctv list 변경 시
    useEffect(() => {

        if (intervalId !== null) {
            // 일단 돌고 있는 interval 초기화
            clearInterval(intervalId)
            console.log("cycle paused :: new list inserted");
            // 다시 첫 페이지부터 시작
            dispatch(updateNowPagePlayList(allPagePlayList[0]))
            let nowTimeOutId = setTimeout(()=>{
                let newInterId = cycle(0);
                setIntervalId(newInterId);
            },parseInt(pageTransCycle.value))
            setTimeOutId(nowTimeOutId);
            console.log("new Cycle Start :: cctv list changed")
            console.log("New Time : ", pageTransCycle.label)
            console.log("Start Page : ", currentPage + 1);
        }
    }, [allPagePlayList])


    // 전환 시간 변경시
    useEffect(() => {

        //현재 cycle이 돌고있는 경우
        if (intervalId !== null) { //전환중
            // // 일단 돌고 있는 interval 을 초기화 하고
            clearInterval(intervalId)
            console.log("cycle paused :: cycle time changed")
            // 현재 페이지를 유지하며 새로운 cycle로 돌린다.
            let nowTimeOutId = setTimeout(()=>{
                let newInterId = cycle(0);
                setIntervalId(newInterId);
            },parseInt(pageTransCycle.value))
            setTimeOutId(nowTimeOutId);
            console.log("new Cycle Start :: cycle time changed")
            console.log("New Time : ", pageTransCycle.label)
            console.log("Start Page : ", currentPage + 1)

        }else { //전환중이 아닌경우
            if (isPageTrans === PageTransType.STOP){
                dispatch(updateCycleState(PageTransType.PLAY))
            }

        }

    }, [pageTransCycle])


    useEffect(() => {

        //cctvCycle 여부가 변화 -> 즉, 처음 시작했을 경우
        if (isPageTrans === PageTransType.PLAY) { //플레이를 하는경우 //다시시작하는경우
            if (allPagePlayList.length !== 0) {
                //인터벌이 시작되는경우
                console.log("First Cycle Start")
                console.log("set time : ", pageTransCycle.label)
                console.log("Start Page : ", currentPage + 1);

                if (intervalId == null){
                    dispatch(updateNowPagePlayList(allPagePlayList[currentPage]))
                    let interId = cycle(currentPage);
                    setIntervalId(interId);
                }else {
                    let nowTimeOutId = setTimeout(()=>{
                        dispatch(updateNowPagePlayList(allPagePlayList[currentPage]))
                        let interId = cycle(currentPage);
                        setIntervalId(interId);
                    },parseInt(pageTransCycle.value))
                    setTimeOutId(nowTimeOutId);
                }


            }
        } else if (isPageTrans === PageTransType.NONE) { //clear되는경우
            // 무조건 멈춰야 하는 경우
            // false 일 경우에는 무조건 멈추도록 되어있으므로
            setCurrentPage(0);
            console.log("End Cycle")
            clearInterval(intervalId);
            setIntervalId(null);
            clearTimeout(timeOutId);
        } else if (isPageTrans === PageTransType.EACH){ //개별 요청

            if (intervalId != null){
                dispatch(updateCycleState(PageTransType.STOP))
                clearInterval(intervalId);
                setIntervalId(null);
                clearTimeout(timeOutId);
            }else {//이렇게 되면 두번째 개별 입력때 미전환으로 변경된다.
                if (previousPageTrans === PageTransType.NONE){//아무것도 아닌경우
                    dispatch(updateCycleState(PageTransType.NONE))
                }else if (previousPageTrans === PageTransType.STOP){
                    dispatch(updateCycleState(PageTransType.STOP))
                }else if (previousPageTrans === PageTransType.PLAY){
                    dispatch(updateCycleState(PageTransType.STOP))
                }
            }

        } else{//중지시키는경우
            console.log("CCTV MONITOR STOP!!")
            clearInterval(intervalId);
            setIntervalId(null);
            clearTimeout(timeOutId);
        }
    }, [isPageTrans])


    // 확인용
    // todo : 해당 코드 삭제하기
    useEffect(() => {
        console.log("whole Page : ", allPagePlayList.length);
        console.log("current page : ", currentPage + 1);
        console.log("set Time : ", pageTransCycle.label);


    }, [currentPage])


    // drop event
    const [{canDrop, isOver}, dropRef] = useDrop({
        accept: 'tree',
        drop: () => ({item: dragTargetInfo?.data, cctvGridAmount: gridAmount}),
        collect: (monitor) => ({
            isOver: monitor.isOver(),
            canDrop: dragTargetInfo?.parent === 0 ? monitor.canDrop() : false,
        }),
    })

    const isDragState = canDrop && isOver && dragTargetInfo?.parent === 0

    return (
        <section
            className={`monitor cctv_container ${isDragState ? "drag_state" : ""} ${isMenuOpen ? "" : "full_size"}`}
            ref={dragTargetInfo?.parent === 0 ? dropRef : null}>
            {
                currentPagePlayList && currentPagePlayList.map((item, index) =>
                    <CctvFrame key={`CCTV_FRAME_${index}`} info={item} index={index} isFull={!isMenuOpen}/>
                )
            }
        </section>
    )
}

export default CctvArea