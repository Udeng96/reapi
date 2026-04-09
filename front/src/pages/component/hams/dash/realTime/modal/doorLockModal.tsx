import {useDispatch, useSelector} from "react-redux";
import {changeLockRequestLockUnLockBtn, updateLockPopupOpen} from "../../../../../store/view/hams/hamsViewStore";
import {RootState} from "../../../../../store/rootStore";
import * as hamsAction from "../../../../../saga/action/hams/hams";
import {HamsGisFacInfo, LockParam} from "../../../../../../data/interface/hams/hamsInterface";
import {useEffect, useState} from "react";

const DoorLockModal = (props:{isLockPopup:boolean, hamsItem:HamsGisFacInfo|null}) => {
    {/* 테스트 후 setLock props 삭제*/}

    let isPopupOpen = useSelector((state:RootState)=>state.view.hams.lockPopupOpen)

    const dispatch = useDispatch()
    const closePopup = () => {
        dispatch(updateLockPopupOpen(false))
    }

    const doLock = () => {
        dispatch(changeLockRequestLockUnLockBtn())
        const param:LockParam = {hamsId:props.hamsItem?props.hamsItem.hamsId!!:"", body:props.hamsItem}
        dispatch(hamsAction.actions.requestDoorLock(param))
        closePopup()
    }

    const doUnlock = () => {
        dispatch(changeLockRequestLockUnLockBtn())
        const param:LockParam = {hamsId:props.hamsItem?props.hamsItem.hamsId!!:"", body:props.hamsItem}
        dispatch(hamsAction.actions.requestDoorUnlock(param))
        closePopup()
    }

    return(
        <>
            {/*doorLockPopup*/}
            <section className="hams modal_popup doorlock_popup" id="doorLockPopup" style={{"display":(isPopupOpen?"block":"none")}}>
                <div className="modal_popup_frame">
                    <header className="modal_popup_header">
                        <button type="button" className="btn_popup_close" onClick={closePopup}/>
                    </header>
                    <div className="modal_popup_body">
                        <div className="lock_img state_lock"/>

                        <p className="lock_message">앞문 도어락을<span className={`${props.isLockPopup?'tRed':'tBlue'}`}>{props.isLockPopup?"‘잠금’":"‘잠금해제’"}</span>하시겠습니까?</p>
                        <div className="btn_lock_group">
                            <button type="button" className="btn_lock" onClick={closePopup}>취소</button>
                            <button type="button" className="btn_lock" onClick={props.isLockPopup? doLock:doUnlock}>{props.isLockPopup?"잠금":"잠금해제"}</button>
                        </div>
                    </div>
                </div>
            </section>
        </>
    )

}

export default DoorLockModal