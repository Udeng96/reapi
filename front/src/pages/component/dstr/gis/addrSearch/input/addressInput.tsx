import styled from "styled-components";
import React, {ChangeEvent, KeyboardEventHandler, useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {actions as etcActions} from "../../../../../saga/action/etc/addrActions"
import {RootState} from "../../../../../store/rootStore";
import {updateShowSearchList, updateWord} from "../../../../../store/view/etc/etcViewStore";
import {updateAddrList} from "../../../../../store/server/etc/serverEtcStore";
import {updateSelectAddrInfo} from "../../../../../store/view/dstr/mapStore";

interface Props{
    addressCnt : number
}

const  StyledAddressInput = styled.input<Props>`
    width: 270px;
    height: 40px;
    position: absolute;
    left: 85px;
    top: 50%;
    border-top-left-radius: 8px;
    border-top-right-radius: 8px;  
    border-bottom-left-radius:  ${({addressCnt}) => addressCnt > 0 ? '0' : '8px'};
    border-bottom-right-radius:  ${({addressCnt}) => addressCnt > 0 ? '0' : '8px'};
    border: solid 1px ${({addressCnt}) => addressCnt > 0 ? '#ff6f26' : '#000'};
    background-color: ${({addressCnt}) => addressCnt > 0 ? '#13151f' : '#1b1d2b'};
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    padding: 0 69px 0 24px;
    font-family: "NotoSansCJKL";
    font-size: 12px;
    letter-spacing: 0.3px;
    color: #FFFFFF;
    -webkit-transition: all .25s;
    transition: all .25s;


    &:hover {
        background-color: #292b33;
    }

    &:focus {
        border-color: #ff6f26;
        background-color: #13151f;
        -webkit-box-shadow: 0px 4px 8px 0 rgba(0, 0, 0, 0.24);
        box-shadow: 0px 4px 8px 0 rgba(0, 0, 0, 0.24);
        outline: none;
    }

    &::-webkit-input-placeholder {
        /* Edge */
        font-family: "NotoSansCJKL";
        color: #9698a3;
        font-size: 12px;
    }

    &::-ms-input-placeholder {
        /* Internet Explorer 10-11 */
        font-family: "NotoSansCJKL";
        color: #9698a3;
        font-size: 12px;
    }

    &::-ms-input-placeholder {
        font-family: "NotoSansCJKL";
        color: #9698a3;
        font-size: 12px;
    }

    &::placeholder {
        font-family: "NotoSansCJKL";
        color: #9698a3;
        font-size: 12px;
    }
`

const AddressInput = (props :{value:string, onKeyDown:any, changeValue:Function, changeType:Function})=>{

    const dispatch = useDispatch()
    //TODO 나중에 검색값으로 useSelector로 변경하여 리스트가 size가 1개 이상일 경우 active 처리
    const addrList = useSelector((state:RootState)=>state.server.etc.addrList)

    const focusEvent = ()=>{
        setIsTypeOpen(false);
        if (addrList.length>0){dispatch(updateShowSearchList(true))}
    }

    const [isTypeOpen, setIsTypeOpen] = useState<boolean>(false);
    const [selectType, setSelectType] = useState<string>("all");

    const setSelectTypeForm = () => {
        if(selectType === 'road'){
            return "도로명";
        }else if(selectType === 'address'){
            return "지번"
        }else if(selectType === 'place'){
            return "장소"
        }else if(selectType === 'all'){
            return "통합"
        }else{
            return "기타"
        }
    }
    const handleBtn = (type:string) => {
        props.changeType(type)
        setSelectType(type);
        dispatch(updateShowSearchList(false))
        dispatch(updateAddrList([]))
    }

    return(
        <>
            <button className={`gis_search_select ${isTypeOpen ? 'active' : ''}`} onClick={()=>setIsTypeOpen(!isTypeOpen)}>{setSelectTypeForm()}
                <div className={`gis_search_select_wrapper`}>
                    <ul className={`gis_search_select_list`}>
                        <li className={`gis_search_select_item ${selectType === 'all'? 'active' : ''}`} onClick={() => handleBtn('all')}>통합</li>
                        <li className={`gis_search_select_item ${selectType === 'address'? 'active' : ''}`} onClick={() => handleBtn('address')}>지번</li>
                        <li className={`gis_search_select_item ${selectType === 'road'? 'active' : ''}`} onClick={() => handleBtn('road')}>도로명</li>
                        <li className={`gis_search_select_item ${selectType === 'place'? 'active' : ''}`} onClick={() => handleBtn('place')}>장소</li>
                    </ul>
                </div>
            </button>
            <StyledAddressInput value={props.value}
                                onKeyDown={props.onKeyDown}
                                onChange={e=>props.changeValue(e)}
                                addressCnt={addrList.length} placeholder={'주소를 검색해 보세요'}
                                onFocus={focusEvent}/>
        </>
    )

}



export default AddressInput