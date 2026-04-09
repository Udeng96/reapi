import {PLAYER_ID_PREFIX} from "../../../../../data/const/const";
import {useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import {useEffect, useState} from "react";
import {STREAM_URL_HAMS} from "../../../../../data/config/config";
import XeusGate from "../../../../../external/xeus.player.2.1.0";
import {HamsCctvInfo} from "../../../../../data/interface/hams/hamsInterface";

const CctvHamsPlayer = (props: { isPopup: boolean, item: HamsCctvInfo | null }) => {

    let playerId = props.isPopup ? 'full' + PLAYER_ID_PREFIX + props.item?.cctvId : PLAYER_ID_PREFIX + props.item?.cctvId;

    // cctv 재생되는 숫자
    const reloadCnt = useSelector((state: RootState) => state.view.hams.videoReloadCnt);


    // 이게 div (이걸로 인해 player가 생성된다.)
    const [player, setPlayer] = useState(null);

    // 재생되는 player가 있으면 한 번 껐다가 다시 재생해야한다.

    useEffect(() => {
        return () => {
            // @ts-ignore
            player?.destroy()
        }
    }, [player])

    // mgrNo가 바뀌면 다시 생성하고 시작한다.
    useEffect(() => {


        if (props.item && props.item.mgrNo) {
            makePlayerAndStart();
        } else {
            setPlayer(null);
        }

    }, [props.item?.mgrNo, props.item])


    useEffect(() => {
        if (reloadCnt > 0) {
            makePlayerAndStart(); // 에러가 발생할 경우 다시 reload한다.
        }
    }, [reloadCnt])

    const makePlayerAndStart = () => {

        // @ts-ignore
        if (player && player.destroy) {// 재생중이 영상이 있으면
            // @ts-ignore
            setPlayer(null);
        }

        let streamUrl = STREAM_URL_HAMS;


        // @ts-ignore
        //얘는 기본 설정.
        //playerId, cctvMgrNo, url은 무조건 있어야 한다.
        let xeusPlayer = new XeusGate.Player({

            playerId: playerId,
            cctvMgrNo: props.item?.mgrNo,
            timestamp: '',
            debug: false,
            url: streamUrl,
            userId: 'tester1',
            speed: '',
            codec: 'h264',

        });

        //재생시킬 player를 생성한다.
        setPlayer(xeusPlayer);


    }

    return (

        <>
            <div id={playerId} style={{width: "100%", height: "100%"}}/>
        </>
    )

}

export default CctvHamsPlayer