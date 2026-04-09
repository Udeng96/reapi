import MenuIcon from "./menuIcon";
import {useDispatch, useSelector} from "react-redux";
import {useEffect} from "react";
import * as hamsActions from "../../../saga/action/hams/hams"
import {HamsGisFacInfo} from "../../../../data/interface/hams/hamsInterface";
import {RootState} from "../../../store/rootStore";

const MenuArea = () => {

    const dispatch = useDispatch()
    const hamsList : Array<HamsGisFacInfo>|null = useSelector((state:RootState)=> state.server.hams.hamsList)
    const activeMenu : HamsGisFacInfo|null = useSelector((state:RootState)=> state.server.hams.activeHamsInfo)

    useEffect(()=>{
        dispatch(hamsActions.actions.requestHamsList())

    },[])

    // // 처음 렌더링 되었을 때, 무조건 defualt로 하나는 active (첫번째 메뉴 active)
    useEffect(()=>{
        if(hamsList!==null&&hamsList.length!==0){
            if(activeMenu===null){
                dispatch(hamsActions.actions.requestActiveHamsInfo(hamsList[0].hamsId!!))
            }
        }
    },[hamsList])

    return (
        <>
            {/*menuContainer*/}
            <section className="hams menu_container">
                <h1 className="container_title">무인국사<br /><span>Dashboard</span></h1>
                <nav className="nav_menu">
                    <ul className="nav_menu_list">
                        {
                            hamsList && hamsList.map((item,index)=>(
                                <MenuIcon item={item} key={`MENU_ICON_${item.facId}`} iconNm={`icon${index}`}/>
                            ))
                        }
                    </ul>
                </nav>
            </section>
        </>
    )
}

export default MenuArea

