import styled from "styled-components"
import React, {ChangeEvent, useState} from "react";
import AddressInput from "./input/addressInput";

import searchBtnImg from '../../../../../public/static/images/dark/page/btn/btn_gis_searchBar_magnifier_normal.png';
import searchBtnHoverImg from '../../../../../public/static/images/dark/page/btn/btn_gis_searchBar_magnifier_hover.png';
import closeBtnImg from '../../../../../public/static/images/dark/page/btn/btn_gis_searchBar_delete_normal.png';
import closeBtnHoverImg from '../../../../../public/static/images/dark/page/btn/btn_gis_searchBar_delete_hover.png';
import {useDispatch, useSelector} from "react-redux";
import {actions as etcActions} from "../../../../saga/action/etc/addrActions";
import {updateAddrList} from "../../../../store/server/etc/serverEtcStore";
import {RootState} from "../../../../store/rootStore";
import {updateWord} from "../../../../store/view/etc/etcViewStore";
import {updateSelectAddrInfo} from "../../../../store/view/dstr/mapStore";


const StyledAddrSearchBox = styled.div`
  width: 270px;
  height: auto;
  position: absolute;
  top: 0;
  left: 85px;
`
const StyledSearchBtn = styled.button`
  width: 28px;
  height: 28px;
  background: url(${searchBtnImg}) no-repeat;
  position: absolute;
  top: 6px;
  right: -70.5px;
  
  &:hover{
    background: url(${searchBtnHoverImg}) no-repeat;
  }
`

const StyledCloseBtn = styled.button`
  width: 22px;
  height: 22px;
  background: url(${closeBtnImg}) no-repeat;
  position: absolute;
  top: 9px;
  right: -38px;
  
  &:hover{
    background: url(${closeBtnHoverImg}) no-repeat;
  }
`


const AddrSearchBox : React.FC = () => {

    const dispatch = useDispatch()
    const storeWord = useSelector((state:RootState)=>state.view.etc.word)
    const [viewWord,setViewWord] = useState(storeWord)
    const [viewType, setViewType] = useState('all');
    const onKeyDown = (e: React.KeyboardEvent<HTMLElement>)=>{
        if(e.key === 'Enter') {
            doSearch()
        }
    }

    const doSearch = ()=>{
        dispatch(updateSelectAddrInfo(null))
        //만약 word 가 빈문자열이라면 검색을 하지않는다
        if(viewWord !== ''){
            dispatch(updateWord(viewWord))
            dispatch(etcActions.fetchAddr({word:viewWord, type:viewType}))
        }else {
            //문자가 없는경우에는 기존의 list를 비워준다.
            dispatch(updateWord(''))
            dispatch(updateAddrList([]))
        }

    }

    const clearAddrSearch =()=>{
        setViewWord('')
        dispatch(updateSelectAddrInfo(null))
        dispatch(updateWord(''))
        dispatch(updateAddrList([]))
    }


    return(
        <>
            <StyledAddrSearchBox>
                <AddressInput value={viewWord} onKeyDown={onKeyDown}
                              changeValue={(e:ChangeEvent<HTMLInputElement>)=> {
                                  setViewWord(e.target.value)
                              }}
                              changeType={(type:string)=>{
                                  setViewType(type)
                              }}
                />
                <StyledSearchBtn onClick={doSearch}/>
                <StyledCloseBtn onClick={clearAddrSearch}/>
            </StyledAddrSearchBox>
        </>
    )
}

export default AddrSearchBox