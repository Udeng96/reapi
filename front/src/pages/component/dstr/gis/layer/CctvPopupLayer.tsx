import {useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import CctvVideoPopup from "../popup/cctvVideoPopup";


const CctvPopupLayer = ()=>{


    const videoPopupList = useSelector((state:RootState) => state.view.dstrMain.cctv.playGisCctvList);

    return (

        <>
            {
                videoPopupList &&
                videoPopupList.length > 0 &&
                videoPopupList.map((item, index , list)=>{
                    return <CctvVideoPopup item={item} key={`CCTV_VIDEO_${item.facId}`}/>
                })
            }
        </>

    )
}


export default CctvPopupLayer