import Select from "react-select";
import React, {ChangeEvent, MutableRefObject, useEffect, useRef, useState} from "react";

import 'react-checkbox-tree/lib/react-checkbox-tree.css';
import "../../../../public/static/css/tree.css"
import {

    LOAD_MORE_WORD,
    MenuSelectStyle,
    TransCycleOptions
} from "../../../../data/const/monitorMenuConst";
import {useDispatch, useSelector} from "react-redux";

import {RootState} from "../../../store/rootStore";
import {
    updateMenuNodesFn,
    updateMenuState,
    updatePageTransCycle,
    updateSearchMenuNodesFn,
    updateSearchResultMenuList,
    updateSearchWord
} from "../../../store/view/monitor/monitorViewStore";
import {
    CctvInfo,
    MenuListResultType,
    MenuNodeChild,
    MenuNodeParent
} from "../../../../data/interface/monitor/monitorInterface";
import {DropOptions, NodeModel} from "@minoru/react-dnd-treeview";
import TreeView from "./drag/treeView";
import SearchInput from "./input/searchInput";

const RightMenu = () => {

    const dispatch = useDispatch()

    // menu component
    const isMenuOpen = useSelector((state: RootState) => state.view.monitor.isMenuOpen)
    const onClickHideBtn = () => {
        dispatch(updateMenuState(false))
    }
    const onClickOpenBtn = () => {
        dispatch(updateMenuState(true))
    }


    // cctv 전환 주기 설정
    const pageTransCycle = useSelector((state: RootState) => state.view.monitor.pageTransCycle);
    const [newTransCycle, setNewTransCycle] = useState(pageTransCycle)
    const onChangeCycle = (e: any) => {
        setNewTransCycle({value: e.value, label: e.label})
    }
    const onClickCycleApply = () => {
        dispatch(updatePageTransCycle(newTransCycle))
        // dispatch(updatePageTransCycle(newTransCycle))
    }

    // cctv 목록 & tree
    // api로 받아온 메뉴 리스트
    const menuList = useSelector((state: RootState) => state.server.monitor.menuList)
    // tree node men
    const [menuNodes, setMenuNodes] = useState<NodeModel[]>([])
    const [searchNodes, setSearchNodes] = useState<NodeModel[]>([])
    const [newMenuList, setNewMenuList] = useState<MenuListResultType[]>([]);
    const containerRef: MutableRefObject<any> = useRef();

    const setLoadFirstMenu = () => {

        let newMenuNodes: Array<MenuNodeChild | MenuNodeParent> = []

        newMenuList &&
        newMenuList.map((item) => {
            let childNodeList: CctvInfo[] = []
            let nowChildNodeList: CctvInfo[] = []
            item.cctvList &&
            item.cctvList.map((cctv, index) => {
                let childNode = {id: cctv.facId, parent: item.clfyId, text: cctv.facNm, data: cctv}
                if (index < 10) {
                    newMenuNodes.push(childNode)
                    nowChildNodeList.push(childNode.data)
                }
                childNodeList.push(childNode.data)

            })

            //추가
            let parentData = {allData: childNodeList, nowData: nowChildNodeList};

            let parentNode = {
                id: item.clfyId,
                parent: 0,
                droppable: true,
                text: `${item.clfyNm} (${item.cctvCnt})`,
                // data: childNodeList
                data: parentData
            }

            if (item.cctvCnt!== 0){

                newMenuNodes.push(parentNode)

                if (childNodeList.length !== nowChildNodeList.length) {
                    let loadMoreNode = {
                        id: `load_btn ${item.clfyId}`,
                        parent: item.clfyId,
                        droppable: false,
                        text: LOAD_MORE_WORD,
                        data: {allData: [], nowData: []}
                    }

                    newMenuNodes.push(loadMoreNode);

                }
            }
        })

        return newMenuNodes

    }


    useEffect(() => {

        let menu = [...menuList];


        setNewMenuList(menu);



    }, [menuList])

    useEffect(()=>{
        dispatch(updateMenuNodesFn(setMenuNodes))
        dispatch(updateSearchMenuNodesFn(setSearchNodes))

        const newMenuNodes = setLoadFirstMenu();

        setMenuNodes(newMenuNodes)

    },[newMenuList])


    const handleDrop = ({dragSourceId, dropTargetId}: DropOptions) => {

        setMenuNodes(
            menuNodes.map((node) => {
                if (node.id === dragSourceId) {
                    return {
                        ...node,
                        parent: dropTargetId
                    };
                }
                return node;
            })
        )
    }


    // cctv 검색

    const searchWord = useSelector((state: RootState) => state.view.monitor.searchWord)
    const [inputWord, setInputWord] = useState(searchWord)


    const doSearch = () => {

        if (inputWord !== '') {
            dispatch(updateSearchWord(inputWord))
        } else {
            dispatch(updateSearchWord(''))
        }

        dispatch(updateSearchResultMenuList([]))
        setMenuNodes(setLoadFirstMenu())


        let searchMenuNodeList: Array<MenuNodeChild | MenuNodeParent> = [];

        if (inputWord !== '') {
            menuNodes.map((node, index) => {
                if (node.parent === 0) {

                    //parent는 allData를 가지고 있다.
                    // @ts-ignore
                    let allData: CctvInfo[] = node.data.allData;
                    let searchNowChildNodeList: CctvInfo[] = [];

                    let clfyNm: string = "";

                    let searchChildCctvList: CctvInfo[] = [];

                    allData.forEach((cctv) => {
                        clfyNm = cctv.clfyNm;

                        if (cctv.facNm.includes(inputWord)) {
                            searchChildCctvList.push(cctv);
                        }
                    })
                    searchChildCctvList.forEach((cctv, index) => {
                        if (index < 10) {
                            let searchChildNode = {id: cctv.facId, parent: cctv.clfyCd, text: cctv.facNm, data: cctv}

                            searchNowChildNodeList.push(cctv);
                            searchMenuNodeList.push(searchChildNode);

                        }
                    })

                    let parentData = {allData: searchChildCctvList, nowData: searchNowChildNodeList};

                    let parentNode = {
                        id: node.id.toString(),
                        parent: 0,
                        droppable: true,
                        text: `${clfyNm} (${searchChildCctvList.length})`,
                        data: parentData,
                    };

                    searchMenuNodeList.push(parentNode);

                    if (searchChildCctvList.length !== searchNowChildNodeList.length) {
                        let loadMoreNode = {
                            id: `load_btn`,
                            parent: node.id,
                            droppable: false,
                            text: LOAD_MORE_WORD,
                            data: {allData: [], nowData: []}
                        }

                        searchMenuNodeList.push(loadMoreNode)
                    }
                }
            })

            searchMenuNodeList = searchMenuNodeList.reverse();
            setSearchNodes(searchMenuNodeList);
        } else {

            let newMenuNodes = setLoadFirstMenu();
            newMenuNodes = newMenuNodes.reverse();
            setSearchNodes(newMenuNodes);

        }

        dispatch(updateSearchWord(inputWord));
    }


    return (

        <section className="monitor dash_container"
                 style={{"left": `${isMenuOpen ? "0px" : "-246px"}`, "opacity": `${isMenuOpen ? "1" : "0.4"}`}}
                 ref={containerRef}
        >
            <button type="button" className="btn_hide" onClick={onClickHideBtn}/>
            <button type="button" className="btn_open"
                    style={{"display": `${isMenuOpen ? "none" : "block"}`}}
                    onClick={onClickOpenBtn}
                    onMouseOver={event => {
                        containerRef.current.style.opacity = "1";
                    }}
                    onMouseLeave={event => {
                        if (!isMenuOpen) {
                            containerRef.current.style.opacity = "0.4"
                        }
                    }}
            />
            <div className="dash_frame">
                <p className="dash_title">영상 전환 주기 시간설정</p>
                <div className="select_area">
                    <Select defaultValue={TransCycleOptions[2]} options={TransCycleOptions} styles={MenuSelectStyle}
                            isSearchable={ false }
                            onChange={e => onChangeCycle(e)}/>
                </div>
                <button type="button"
                        className={`btn_apply ${newTransCycle.value === pageTransCycle.value ? "dimmed" : ""}`}
                        onClick={e => onClickCycleApply()}>적용
                </button>
            </div>
            <div className="dash_frame">
                <p className="dash_title">CCTV 목록</p>
                <div className="cctv_search_area">
                    <p className="search_hint">*CCTV리스트 버튼을 드래그하여 우측 영상 &nbsp;영역에 놓아 주세요</p>
                    <div className="search_box">
                        <SearchInput value={inputWord} changeValue={(e: ChangeEvent<HTMLInputElement>) => {
                            setInputWord(e.target.value)
                        }} enterEvent={(e: KeyboardEvent) => {
                            doSearch()
                        }}/>
                        <button type="button" className="btn_search" onClick={event => doSearch()}/>
                    </div>
                </div>
                <div className="tree_area">
                    <div className="tree_in">
                        <TreeView
                            tree={searchWord === '' ? menuNodes : searchNodes}
                            onDrop={handleDrop}
                            rootId={0}
                        />
                    </div>
                </div>
            </div>
        </section>

    )


}
export default RightMenu;