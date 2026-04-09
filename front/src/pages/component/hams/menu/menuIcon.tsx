import {HamsGisFacInfo} from "../../../../data/interface/hams/hamsInterface";
import {updateActiveHamsInfo, updateHoverMenuHamsId} from "../../../store/view/hams/hamsViewStore";
import {useDispatch, useSelector} from "react-redux";
import {MutableRefObject, useEffect, useRef, useState} from "react";
import {RootState} from "../../../store/rootStore";
import * as actions from "../../../saga/action/hams/hams"
import * as actionHams from "../../../saga/action/hams/hams";


const MenuIcon = (props: { item: HamsGisFacInfo, iconNm: string }) => {

    const dispatch = useDispatch()
    const [isMenuActive, setIsMenuActive] = useState<boolean>(false)
    // const activeMenuInfo = useSelector((state: RootState) => state.view.hams.activeHamsInfo)
    const activeMenuInfo = useSelector((state: RootState) => state.server.hams.activeHamsInfo)

    const menuRef: MutableRefObject<any> = useRef()
    const [showTooltip, setShowTooltip] = useState(false)

    useEffect(() => {
        setShowTooltip(false)
    }, [])

    const onClickMenu = (item: HamsGisFacInfo) => {
        // dispatch(updateActiveHamsInfo(item))
        dispatch(actions.actions.requestActiveHamsInfo(item.hamsId!!))
        setIsMenuActive(!isMenuActive)
    }

    return (
        <li className={(activeMenuInfo && activeMenuInfo.facId === props.item.facId) ? `active state_${(props.item.status?.isConnect) ? 'connected' : 'disconnected'}` : ''}
            data-icon={props.iconNm}
            onClick={event => onClickMenu(props.item)}
            onMouseEnter={()=>{
                dispatch(updateHoverMenuHamsId(props.item.hamsId))
            }}
            onMouseLeave={()=>{
                dispatch(updateHoverMenuHamsId(null))
            }}
            >
            <a
                ref={menuRef}
                onMouseEnter={() => {
                    setShowTooltip(true)
                }}
                onMouseLeave={() => {
                    setShowTooltip(false)
                }}
            >{props.item.facNm?.includes('BBS')? "동탄"+props.item.facNm : props.item.facNm}</a>
            <div className="hams tooltip_text"
                 style={{"display": `${showTooltip ? "block" : "none"}`}}>{props.item.facNm}</div>
        </li>
    )
}

export default MenuIcon

MenuIcon.defaultProps = {
    item: null,
    iconNm: "icon1",
}