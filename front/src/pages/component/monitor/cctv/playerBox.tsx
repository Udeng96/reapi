import PtzControlBox from "./ptzControlBox";
import {useState} from "react";
import {CctvInfo} from "../../../../data/interface/monitor/monitorInterface";
import Player from "../../dstr/player/Player";
import {VIDEO_TYPE} from "../../../../data/const/const";
import {useSelector} from "react-redux";
import {RootState} from "../../../store/rootStore";

const PlayerBox = (props: { cctvInfo: CctvInfo | null }) => {

    const [showPtz, setShowPtz] = useState(false)
    const userType = useSelector((state:RootState)=>state.server.user.data!.userType);


    return (

        <div className={`cctv_body ${props.cctvInfo?.ptzYn ? "ptz_in" : ""}`}
            onMouseOver={(e) => setShowPtz(true)}
            onMouseLeave={(e) => setShowPtz(false)}
        >
            {/*videoInsert*/}

            {
                props.cctvInfo === null ?
                    <div className="cctv_default_area">
                        <p className="cctv_default_message">왼쪽 CCTV 목록에서 특정 CCTV 리스트를 드래그&드롭 하여<br />영상을 재생시켜주세요.</p>
                    </div>
                    :
                        <>
                            <div className="cctv_in">
                                <Player cctvId={props.cctvInfo.mgrNo} facId={props.cctvInfo.facId} type={props.cctvInfo.clfyNm.includes("방범")?0:props.cctvInfo.clfyNm.includes("광역")?5:1}/>
                            </div>
                            {/*TODO 추후 작업 혹은 연계에 따라서 다시 활성화를 해야한다.*/}
                            {
                                (props.cctvInfo.ptzYn && userType==="001") &&
                                <PtzControlBox isShow={showPtz} cctvId={props.cctvInfo.mgrNo} />
                            }

                        </>

            }


        </div>
    )

}

export default PlayerBox