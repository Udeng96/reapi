import React, {useEffect, useState} from "react";

const Pagination = (props: {
    dataList: any[], rows: number, pageCnt: number, setCurrentPage: Function, maxDataCnt : number,
    currentPage : number
}) => {



    const [totalPage , setTotalPage] = useState(Math.ceil(props.maxDataCnt / props.rows))

    useEffect(()=>{
        setTotalPage(Math.ceil(props.maxDataCnt / props.rows))
    },[props.dataList])


    const setNumbers = () => {

        let pageGroup = Math.ceil(props.currentPage / props.pageCnt);    // 페이지 그룹
        let last = pageGroup * props.pageCnt;
        if (last > totalPage) {
            last = totalPage;
        }
        let first = 1 + ((pageGroup - 1) * props.pageCnt)

        const rendering = [];
        for (let i = first; i <= last; i++) {
            rendering.push(
                <li  key={i}>
                    <a href="javascript:void(0);" className={`${props.currentPage == i ? 'active' : ''}`}
                       onClick={(e) => {
                        props.setCurrentPage(i)
                    }}>{i}</a>
                </li>)
        }


        return <>{rendering}</>

    }

    function nextPage() {
        let nextPage = props.currentPage + 1;
        if (totalPage >= nextPage) {
            props.setCurrentPage(nextPage)
        }
    }

    function previousPage() {
        let previousPage = props.currentPage - 1;

        if (1 <= previousPage) {
            props.setCurrentPage(previousPage)
        }
    }


    return (
        <>
            <div className="paging_wrap">
                <ul>
                    <li><a href="javascript:void(0);" className="btn_foremost" onClick={()=> {
                        props.setCurrentPage(1)
                    }}/></li>
                    <li><a href="javascript:void(0);" className="btn_prev" onClick={previousPage}/></li>
                    {
                        setNumbers()
                    }
                    <li><a href="javascript:void(0);" className="btn_next" onClick={nextPage}/></li>
                    <li><a href="javascript:void(0);" className="btn_last" onClick={()=> {
                        props.setCurrentPage(totalPage)
                    }}/></li>
                </ul>
            </div>
        </>
    )
}

Pagination.defaultProps = {
    dataList:[],
    rows: 10,
    maxDataCnt : 10,
    pageCnt: 5,
    setCurrentPage : ()=>{},
    currentPage : 1
}


export default Pagination