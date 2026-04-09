import {RootState} from "../../../../store/rootStore";
import {useSelector} from "react-redux";
import {MobileInfo} from "../../../../../data/interface/event/mobileInterface";
import MobileVideoPopup from "../popup/mobileVideoPopup";
import {useEffect, useState} from "react";

const MobilePopupLayer = () => {

    const mobileLivePopupList = useSelector((state:RootState)=>state.view.dstrMain.mobile.activeLiveBtnList)
    const mobilePastPopupList = useSelector((state:RootState)=>state.view.dstrMain.mobile.activePastBtnList)

    const [mobilePopupList , setMobilePopupList] = useState<Array<MobileInfo>>([])

    useEffect(()=>{
        let list  = mobileLivePopupList?[...mobileLivePopupList]:[]
        if (mobilePastPopupList) {list.push(mobilePastPopupList[0])}

        setMobilePopupList(list)

    },[mobileLivePopupList,mobilePastPopupList])


    return(

        <>
            {

                mobileLivePopupList &&
                    mobileLivePopupList.length>0 &&
                    mobileLivePopupList.map((item)=>{
                        return <MobileVideoPopup item={item} isLive={true}/>
                    })

            }

            {
                mobilePastPopupList &&
                    mobilePastPopupList.length>0 &&
                    mobilePastPopupList.map((item)=>{
                        return <MobileVideoPopup item={item} isLive={false}/>
                    })
            }


        </>

    )
}

export default MobilePopupLayer