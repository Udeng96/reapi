import React from "react";
import {useDispatch} from "react-redux";
import {Search} from "../../../../../../../data/interface/event/eventInterface";
import {
    getCheckedLoca,
    getCheckedModalLoca,
    getCheckedModalType,
    getCheckedType
} from "../../../../../../store/view/dstr/dstrViewEventStore";
import {SEARCH_BOX_TYPE} from "../../../../../../../data/const/eventTapConsts";

const DstrSearchBoxTag = (props: { name: string, id:string, boxType:string, checkedOptions:Array<Search>}) => {

    const dispatch = useDispatch()
    const delOneTag = () => {

        let newChkList : Array<Search> = []

        for(let item in props.checkedOptions){
            newChkList.push(props.checkedOptions[item])
        }

        for(let item in newChkList){
            if(props.id === newChkList[item].id){
                newChkList.splice(parseInt(item), 1)
            }
        }

        switch(props.boxType){
            case SEARCH_BOX_TYPE.LOCATION : dispatch(getCheckedLoca(newChkList));break;
            case SEARCH_BOX_TYPE.EVENT_TYPE : dispatch(getCheckedType(newChkList));break;
            case SEARCH_BOX_TYPE.MODAL_LOCATION : dispatch(getCheckedModalLoca(newChkList));break;
            default : dispatch(getCheckedModalType(newChkList))
        }
    }

    return (
        <div className="search_tag_box" onClick={e=>e.stopPropagation()}>
            <p className="search_tag_name">{`#${props.name}`}</p>
            <button type="button" id={props.id} className="btn_search_delete" onClick={delOneTag}/>
        </div>
    )

}

export default DstrSearchBoxTag