import React, {useEffect, useState} from "react";
import {Search} from "../../../../../../../data/interface/event/eventInterface";
import {
    getCheckedLoca,
    getCheckedModalLoca,
    getCheckedModalType,
    getCheckedType
} from "../../../../../../store/view/dstr/dstrViewEventStore";
import {useDispatch} from "react-redux";
import {SEARCH_BOX_TYPE} from "../../../../../../../data/const/eventTapConsts";

const DstrSearchChkBox = (props: { name: string, id: string, boxType: string, checkedOptions: Array<Search> }) => {

    const dispatch = useDispatch()

    let [isChk, setIsChk] = useState(false)
    let checkOptionsId:Array<string> = []

    // 처음 렌더링 시, store에 저장되어 있는 항목들과 비교해서 chkBox 화면에 그려주기

    useEffect(() => {

        let isInclude = false;
        //체크된 옵션들 중
        props.checkedOptions.map((item) => {
            //방금 체크한 옵션이 안에 있는 경우
            checkOptionsId.push(item.id)
        })
        if(checkOptionsId.includes(props.id)) {
            //있다고 판단. 따라서 isChk true 로 변경
            isInclude = true
        }else{

        }
        setIsChk(isInclude)


    }, [props.checkedOptions])

    const chkBox = (e:any) => {


        setIsChk(!isChk)

        //체크가 된 경우 isChecked = true / isChk = false

        let Search : Search = {nm:props.name, id:props.id}
        let isChecked = e.currentTarget.checked
        let newChkList:Array<Search> = []

        let checkedOptionsId:Array<string> = []

        props.checkedOptions.map((item)=>{
            newChkList.push(item)
            checkedOptionsId.push(item.id)
        })

        if(isChecked){
            //빈체크 -> 체크
            if(props.id==='all'){
                // 선택한 것이 all 이면
                newChkList = []
                newChkList.push(Search)
            }else{
                // 선택한 것이 all이 아닌데
                if(checkedOptionsId.includes("all")){
                    // all이 이미 선택되어 있을 경우
                    newChkList= []
                    newChkList.push(Search)
                }else{
                    newChkList.push(Search)
                }
            }
        }else{
            //체크 -> 체크아웃
            let index = checkedOptionsId.indexOf(props.id)
            newChkList.splice(index, 1)
        }


        switch (props.boxType) {
            case SEARCH_BOX_TYPE.LOCATION :
                dispatch(getCheckedLoca(newChkList));
                break;
            case SEARCH_BOX_TYPE.EVENT_TYPE :
                dispatch(getCheckedType(newChkList));
                break;
            case SEARCH_BOX_TYPE.MODAL_LOCATION :
                dispatch(getCheckedModalLoca(newChkList));
                break;
            default :
                dispatch(getCheckedModalType(newChkList));
        }
    }

    return (
        <div className="event_check_box">
            <label className="container">
                <input type="checkbox" id={props.id} checked={isChk}
                       onChange={e => chkBox(e)}
                />
                <span className="checkmark"/>
                <div className="check_name">#{props.name}</div>
            </label>
        </div>
    )
}

export default DstrSearchChkBox