import {NodeModel} from "@minoru/react-dnd-treeview";
import {useDrag} from "react-dnd";
import {useDispatch, useSelector} from "react-redux";
import {
    updateAllPagePlayList,
    updateCycleState,
    updateDragTarget,
    updateNowPagePlayList
} from "../../../../store/view/monitor/monitorViewStore";
import {CctvInfo, MenuNodeChild} from "../../../../../data/interface/monitor/monitorInterface";
import {RootState} from "../../../../store/rootStore";
import {GetAllCctvList, GetAllPagePlayList} from "../../cctv/getAllCctvList";
import React, {MutableRefObject, useEffect, useRef, useState} from "react";
import {PageTransType} from "../../../../../data/const/monitorMenuConst";


const CustomNode = (props: { node: NodeModel, rootNode: Function, allMenuNodes: NodeModel[], canDrag: any, depth: number, isOpen: boolean, onToggle: (id: NodeModel["id"]) => void; }) => {
    const {id, droppable} = props.node;
    const dispatch = useDispatch();
    const menuList = useSelector((state: RootState) => state.server.monitor.menuList)
    const setMenuNodes = useSelector((state: RootState) => state.view.monitor.menuUpdateFn);
    const setSearchMenuNodes = useSelector((state: RootState) => state.view.monitor.searchMenuUpdateFn)
    const searchWord = useSelector((state: RootState) => state.view.monitor.searchWord)


    const handleToggle = (e: React.MouseEvent | React.KeyboardEvent) => {
        e.stopPropagation();
        props.onToggle(props.node.id);
    };

    const itemRef: MutableRefObject<any> = useRef();
    const [showTooltip, setShowTooltip] = useState(false);

    const onClickLoadMore = (e: any) => {

        let rootNode = props.rootNode(props.node); // 현재 누른 버튼의 최상위 노드 가져오기
        let cctvAllCnt = 0; // 최상위 노드가 원래 가진 cctv 갯수
        let cctvCnt = rootNode.data.nowData.length; // 실제 보여지고 있는 cctv 갯수
        let afterCctvCnt = 0;
        let newMenuNodeChild: Array<MenuNodeChild> = [...rootNode.data.nowData]// 새로 반영할 cctv

        let allMenu = [...props.allMenuNodes];
        let allData: CctvInfo[] = rootNode.data.allData;

        if (e.currentTarget.className.includes("load_btn")) {

            menuList.forEach((item) => {

                if (item.clfyId === props.node.parent.toString()) {

                    if (searchWord == '') {
                        cctvAllCnt = item.cctvCnt;
                    } else {
                        cctvAllCnt = rootNode.data.allData?.length;
                    }

                    if (cctvAllCnt - cctvCnt >= 10) {
                        for (let i = cctvCnt; i < cctvCnt + 10; i++) {
                            let childNode = {
                                id: allData[i].facId,
                                parent: allData[i].clfyCd,
                                text: allData[i].facNm,
                                data: allData[i]
                            }
                            allMenu.push(childNode)
                            newMenuNodeChild.push(childNode)

                        }
                        afterCctvCnt = cctvCnt + 10;
                        allMenu.forEach((item, index) => {

                            if (item.id === rootNode.id) {
                                let newParentData = {allData: allData, nowData: newMenuNodeChild}
                                // @ts-ignore
                                let newItem = {
                                    id: item.id,
                                    parent: 0,
                                    droppable: true,
                                    text: item.text,
                                    data: newParentData
                                }
                                allMenu.splice(index, 1);
                                allMenu.push(newItem);
                            }
                        })

                    } else {
                        for (let i = cctvCnt; i < cctvAllCnt; i++) {
                            let childNode = {
                                id: allData[i].facId,
                                parent: allData[i].clfyCd,
                                text: allData[i].facNm,
                                data: allData[i]
                            }
                            allMenu.push(childNode);
                            newMenuNodeChild.push(childNode);
                        }

                        allMenu.forEach((item, index) => {

                            if (item.id === rootNode.id) {
                                let newParentData = {allData: rootNode.data.allNode, nowData: newMenuNodeChild}
                                // @ts-ignore
                                let newItem = {
                                    id: item.id,
                                    parent: 0,
                                    droppable: true,
                                    text: item.text,
                                    data: newParentData
                                }
                                allMenu.splice(index, 1);
                                allMenu.push(newItem);
                            }

                        })

                        allMenu.forEach((item, index) => {
                            if (item.id === props.node.id) {
                                if (item.parent === props.node.parent) {
                                    allMenu.splice(index, 1);
                                }
                            }
                        })

                    }
                }
            })
            if (searchWord !== '') {
                setSearchMenuNodes(allMenu);
            } else {
                setMenuNodes(allMenu);
            }
        }
    }

    // load btn 을 맨 마지막에 놓는 코드
    useEffect(()=>{
        let item = itemRef.current.parentNode.parentNode;

        if(itemRef.current.className.includes('load_btn')){
            item.parentNode.parentNode.childNodes[1].appendChild(item);
        }

        if(itemRef.current.parentNode.className.includes('방범')){
            let ul = item.parentNode;
            let firstChild = ul.firstChild
            ul.replaceChild(item,firstChild);
            ul.appendChild(firstChild);

        }

    },[])



    // drag
    const [{isDragging}, dragRef] = useDrag(() => ({
        type: 'tree',
        item: props.node,
        canDrag: props.canDrag(props.node),
        collect: (monitor => ({
            isDragging: monitor.isDragging(),
            canDrag: props.canDrag
        })),
        end: (item, monitor) => {
            //getItem 은 선택한 tree의 정보를 가져오고, ( tree id / parent / children ... )
            //getDropResult 는 useDrop에서 지정해준 drop 을 가져온다. ( cctvInfo / index / cctvGridAmount ... )


            const dropItem = monitor.getItem();
            const dropResultItem = monitor.getDropResult<any>();
            const cctvGridAmount = parseInt(dropResultItem.cctvGridAmount);

            // 상위 메뉴를 drag 할 경우 (다중 drag)
            if (dropItem.parent === 0) {

                // 하위 cctv list
                let childCctvList: CctvInfo[] = dropResultItem.item.allData;

                // 하위 cctv의 총 개수
                let childCctvAmount = childCctvList.length

                // 하위 cctv의 개수가 grid 수 보다 클 경우
                if (childCctvAmount > cctvGridAmount) {

                    let allPageCctvList = GetAllCctvList(childCctvAmount, cctvGridAmount, childCctvList)
                    let allPagePlayingCctvList: Array<Array<CctvInfo | null>> = GetAllPagePlayList(allPageCctvList, cctvGridAmount)

                    dispatch(updateAllPagePlayList(allPagePlayingCctvList));
                    dispatch(updateCycleState(PageTransType.PLAY));  //사이클 시작

                } else {
                    // grid 수보다 작은 경우
                    let nowPagePlayingCctvList = Array(cctvGridAmount).fill(null);
                    childCctvList.forEach((item, index) => {
                        nowPagePlayingCctvList.splice(index, 1, item);
                    })

                    dispatch(updateNowPagePlayList(nowPagePlayingCctvList));
                    dispatch(updateCycleState(PageTransType.NONE)); //사이클 삭제

                }

                // 하위 cctv를 drag 할 경우 (개별 cctv drag)
            } else {
                let gridIndex = parseInt(dropResultItem.index)
                let newNowPagePlayingCctvList = JSON.parse(dropResultItem.list)

                const findIndex = newNowPagePlayingCctvList.findIndex((v: CctvInfo | null) => v?.facId === dropItem.id)

                if (findIndex < 0) {
                    //@ts-ignore
                    newNowPagePlayingCctvList.splice(gridIndex, 1, item.data);
                } else {
                    // @ts-ignore
                    let newFacId = item.data.facId + "_" + Date.now();
                    // @ts-ignore
                    let realNewData = {...item.data, facId: newFacId};
                    newNowPagePlayingCctvList.splice(gridIndex, 1, realNewData);
                }

                dispatch(updateNowPagePlayList(newNowPagePlayingCctvList))
                dispatch(updateCycleState(PageTransType.EACH)) //사이클 중지로 변경
            }
        }
    }))

    //cctv 검색
    const word: string = useSelector((state: RootState) => state.view.monitor.searchWord)

    const setSearchKey = () => {

        let cctvName: string = props.node.text

        if (props.node.parent !== 0) {
            cctvName = cctvName.replaceAll(word, `<span style="color:#ff6f26">${word}</span>`)
        }

        return cctvName
    }


    return (
        <div ref={dragRef}
             className={`${props.node.text.split(' ')[0]}`}
             style={{marginTop: props.depth * 5, marginLeft: props.depth * 10, color: "white"}}
             onMouseDown={e => {
                 dispatch(updateDragTarget(props.node))
             }}
             onMouseUp={e => {
                 dispatch(updateDragTarget(null))
             }}
        >
            {droppable && (
                // dropdown 했을 때 메뉴 이름 앞에 icon
                <span
                    className={"toggle-btn"}
                    onClick={handleToggle}
                    style={{fontSize: "10px", color: "#71727a", marginRight: 4.7}}>{props.isOpen ? "▼" : "▶"}</span>
            )}
            {/*부모 노드 이름*/}
            <p role={"list"} ref={itemRef}
               onClick={(e) => onClickLoadMore(e)}
               className={props.node.id.toString()}
               style={{textOverflow: "ellipsis", overflow: "hidden", whiteSpace: "nowrap"}}
               onMouseEnter={() => {
                   const isOver = itemRef.current.scrollWidth > itemRef.current.clientWidth;
                   setShowTooltip(isOver);
               }}
               onMouseLeave={() => {
                   setShowTooltip(false);
               }}
               dangerouslySetInnerHTML={{__html: setSearchKey()}}/>
            {showTooltip && <div className="monitor tooltip_text" style={{"left": "41px"}}>{props.node.text}</div>}
        </div>

    )


}

export default CustomNode