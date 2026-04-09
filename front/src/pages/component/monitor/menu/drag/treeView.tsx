import {NodeModel, Tree} from "@minoru/react-dnd-treeview";
import CustomNode from "./customNode";
import CustomDragPreview from "./customDragPreview";
import React, {useEffect, useState} from "react";
import {useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import {LOAD_MORE_WORD} from "../../../../../data/const/monitorMenuConst";

const TreeView = (props: { tree: NodeModel[], onDrop: any, rootId: number | string }) => {

    const searchResultMenuList = useSelector((state: RootState) => state.view.monitor.searchResultMenuList);
    const [treeOpen, setTreeOpen] = useState<NodeModel["id"][]>([])
    const [notDragNode, setNotDragNode] = useState<NodeModel["id"][]>([]);
    // 검색했을 때, 결과 값이 포함된 메뉴의 tree open

    useEffect(()=>{
        let newNotDragNode : NodeModel["id"][] = []
        props.tree.forEach((node)=>{
            if(node.droppable === false){
                newNotDragNode.push(node.id);
            }
        })

        setNotDragNode(newNotDragNode);
    },[props.tree])



    useEffect(() => {
        setTreeOpen(searchResultMenuList)
    }, [searchResultMenuList])

    const handleDrag  = (node : NodeModel) => {
        if(notDragNode){
            if(notDragNode.includes(node.id)){return false};

            return true;
        }
    }

    const getRootNode = (node:NodeModel) => {
        let result :NodeModel = node;
        if(node.parent!==0){
            props.tree.forEach((item)=>{
                if(item.id === node.parent){result = item}
            })
        }

        return result;
    }


    return (
        <>
            <Tree
                tree={props.tree}
                sort={(a,b)=> {
                    let compare = a.text.localeCompare(b.text)

                    if(a.text === LOAD_MORE_WORD) {
                        return b.text === LOAD_MORE_WORD ? 0 : 1;
                    } else {
                        return b.text === LOAD_MORE_WORD ? -1 : compare;
                    }
                } }
                onDrop={props.onDrop}
                initialOpen={treeOpen}
                canDrag={(node) => { return (node?.droppable !== false)}}
                rootId={0}
                render={(node, options) => {

                    return(<CustomNode node={node} rootNode={getRootNode}
                                       allMenuNodes={props.tree} canDrag = {handleDrag} {...options}/>)
                }}

                dragPreviewRender={(monitorProps) => (
                    <CustomDragPreview monitorProps={monitorProps}/>
                )}
            />
        </>




)

}

export default TreeView