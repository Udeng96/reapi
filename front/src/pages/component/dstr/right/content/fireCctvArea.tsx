import React from "react";
import PlayerBox from "../../player/PlayerBox";
import {useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import {EMPTY_PLAYER_PREFIX, VIDEO_TYPE} from "../../../../../data/const/const";

const FireCctvArea : React.FC = () => {

    const playCctvList = useSelector((state: RootState) => state.view.dstrMain.cctv.playCctvList)

    const cctvType = (index:number) => {
        if(playCctvList.length>0){
            if(playCctvList[index].clfyNm.includes("방범")){
                return VIDEO_TYPE.CRIME
            }else{
                return VIDEO_TYPE.TRAFFIC
            }
        }
    }
    return(
        <>
            <div className="small_cctv_wrap">
                <PlayerBox
                    cctvName={playCctvList.length >0 ?playCctvList[0].facNm : '선택없음'}
                    cctvId={playCctvList.length >0 ?playCctvList[0].mgrNo : `${EMPTY_PLAYER_PREFIX}_FIRE_1`}
                    type={cctvType(0)}
                    no={1}
                           isSmall={true}/>
                <PlayerBox
                    cctvName={playCctvList.length >0 ?playCctvList[1].facNm : '선택없음'}
                    cctvId={playCctvList.length >0 ?playCctvList[1].mgrNo : `${EMPTY_PLAYER_PREFIX}_FIRE_2`}
                    type={cctvType(1)}
                    no={2}
                    isSmall={true}/>
                <PlayerBox
                    cctvName={playCctvList.length >0 ?playCctvList[2].facNm : '선택없음'}
                    cctvId={playCctvList.length >0 ?playCctvList[2].mgrNo : `${EMPTY_PLAYER_PREFIX}_FIRE_3`}
                    type={cctvType(2)}
                    no={3}
                    isSmall={true}/>
                <PlayerBox
                    cctvName={playCctvList.length >0 ?playCctvList[3].facNm : '선택없음'}
                    cctvId={playCctvList.length >0 ?playCctvList[3].mgrNo : `${EMPTY_PLAYER_PREFIX}_FIRE_4`}
                    type={cctvType(3)}
                    no={4}
                    isSmall={true}/>
                <PlayerBox
                    cctvName={playCctvList.length >0 ?playCctvList[4].facNm : '선택없음'}
                    cctvId={playCctvList.length >0 ?playCctvList[4].mgrNo : `${EMPTY_PLAYER_PREFIX}_FIRE_5`}
                    type={cctvType(4)}
                    no={5}
                    isSmall={true}/>
                <PlayerBox
                    cctvName={playCctvList.length >0 ?playCctvList[5].facNm : '선택없음'}
                    cctvId={playCctvList.length >0 ?playCctvList[5].mgrNo : `${EMPTY_PLAYER_PREFIX}_FIRE_6`}
                    type={cctvType(5)}
                    no={6}
                    isSmall={true}/>
            </div>
        </>
    )
}

export default FireCctvArea