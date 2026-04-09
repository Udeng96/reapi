import CctvHamsPlayerBox from "./cctvHamsPlayerBox";
import {HamsGisFacInfo} from "../../../../../data/interface/hams/hamsInterface";
import {useDispatch, useSelector} from "react-redux";
import {updateCctvPopupOpen} from "../../../../store/view/hams/hamsViewStore";
import {RootState} from "../../../../store/rootStore";

const CctvPopup = (props:{item:HamsGisFacInfo|null}) => {

    const isPopupOpen = useSelector((state:RootState)=>state.view.hams.cctvPopupOpen)
    const activeHamsCctvInfo = useSelector((state:RootState)=>state.view.hams.activePlayHamsCctv);

    const dispatch = useDispatch()
    const onClickCloseBtn = () =>{
        dispatch(updateCctvPopupOpen(false))
    }

    return (

        <section className="hams modal_popup cctv_popup" id="cctvPopup" style={{"display":`${isPopupOpen? "block":"none"}`}}>
            <div className="modal_popup_frame">
                <header className="modal_popup_header">
                    <p className="modal_title">{activeHamsCctvInfo? activeHamsCctvInfo.hamsName +  ' - ' + activeHamsCctvInfo.cctvName : 'No Camera'}</p>
                    <div className="divide_bar"/>
                    <p className="loc_text">{(props.item&&props.item.addr)&&props.item.addr}</p>
                    <button type="button" className="btn_popup_close" onClick={event=>onClickCloseBtn()}/>
                </header>
                <div className="modal_popup_body">
                    <CctvHamsPlayerBox isPopup={true}/>
            </div>
        </div>
</section>

)

}

CctvPopup.defaultProps={
    item: null,
}

export default CctvPopup