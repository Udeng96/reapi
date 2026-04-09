import styled from "styled-components";
import {MouseEvent, MutableRefObject, useEffect, useRef, useState} from "react";
import {EMPTY_PLAYER_PREFIX, PLAYER_ID_PREFIX, VIDEO_TYPE} from "../../../../data/const/const";
import conf from "../../../../data/config/config";
// import XeusGate from "../../../../external/xeus.player.2.1.0_backup";
import XeusGate from "../../../../external/xeus.player.2.1.0";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../store/rootStore";
import {updateMobilePopupSize} from "../../../store/view/dstr/dstrViewMobileStore";


const StyledPlayer = styled.div`
  width: 100%;
  height: 100%;
`;


const Player = (props: { cctvId: string, facId: string, type: VIDEO_TYPE, rtsp: string }) => {

    let playerId = PLAYER_ID_PREFIX + props.cctvId;
    if (props.facId !== '') {
        playerId = PLAYER_ID_PREFIX + props.facId;
    }


    const reloadCnt = useSelector((state: RootState) => state.view.etc.videoReloadCnt) as number

    // const fullPopupRef = useRef<any>()

    const dispatch = useDispatch()


    const [player, setPlayer] = useState(null);

    //@ts-ignore

    useEffect(() => {
        if (props.facId === '') {

            const fullPopupRef = document!!.querySelector('#' + playerId!! + ' > video')

            if (fullPopupRef !== null) {

                // @ts-ignore
                dispatch(updateMobilePopupSize({height: fullPopupRef.videoHeight!!, width: fullPopupRef.videoWidth!!}))
                // @ts-ignore
                // console.log("videoHeight",fullPopupRef.videoHeight!!)
            } else {
                dispatch(updateMobilePopupSize({height: 0, width: 0}))
            }
            // const fullPopup = fullPopupRef.current
        }
    }, [])
    useEffect(() => {
        //@ts-ignore
        if (player && player.destroy) {
            //@ts-ignore
            player.destroy();
        }

        return () => {
            //@ts-ignore
            if (player && player.destroy) {
                //@ts-ignore
                player?.destroy();
            }
        }
    }, []);



    useEffect(() => {
        if (props.cctvId !== 'FULL_'){
            makePlayerAndStart()
        }
    }, [props.cctvId,props.rtsp]);

    const setStreamUrl = () => {
        let streamUrl = conf.XEUS.STREAM_URL_CRIME
        if (props.type == VIDEO_TYPE.CRIME) {
            streamUrl = conf.XEUS.STREAM_URL_CRIME
        } else if (props.type === VIDEO_TYPE.TRAFFIC) {
            streamUrl = conf.XEUS.STREAM_URL_TRAFFIC
        } else if (props.type == VIDEO_TYPE.FIRE) {
            streamUrl = conf.XEUS.STREAM_URL_FIRETRUCK
        } else if (props.type == VIDEO_TYPE.MOBILE) {
            streamUrl = conf.XEUS.STREAM_URL_MOBILE
        } else if (props.type == VIDEO_TYPE.HAMS) {
            streamUrl = conf.XEUS.STREAM_URL_HAMS
        } else if (props.type == VIDEO_TYPE.WIDE) {
            streamUrl = conf.XEUS.STREAM_URL_WIDE
        }

        return streamUrl
    }


    useEffect(() => {
        if (reloadCnt > 0) {

            if (props.type ===  VIDEO_TYPE.CRIME || props.type === VIDEO_TYPE.TRAFFIC || props.type === VIDEO_TYPE.FIRE || props.type ===VIDEO_TYPE.WIDE) {
                makePlayerAndStart()
            }
        }

    }, [reloadCnt])


    const makePlayerAndStart = () => {
        //@ts-ignore
        if (player && player.destroy) {
            //@ts-ignore
            player?.destroy();
        }

        if (!props.cctvId.startsWith(EMPTY_PLAYER_PREFIX)) {
            let streamUrl = setStreamUrl()

            // @ts-ignore
            let xeusPlayer = new XeusGate.Player({
                playerId: playerId,
                cctvMgrNo: props.type == VIDEO_TYPE.MOBILE ? '' : props.cctvId,
                timestamp: '',
                debug: false,
                // url: changeDomainUrl(streamUrl),
                url: streamUrl,
                // url: streamUrl,
                evtType: '112',
                userId: 'tester1',
                speed: '',
                size:'',
                codec: 'h264',
                // rtsp: props.rtsp,
                rtspUrl: props.rtsp,
            });


            setPlayer(xeusPlayer);

        }
    }

    const doubleClickEvent = ((event: any) => {
        var targetElem = event.target;
        if (targetElem.requestFullscreen) {
            targetElem.requestFullscreen();
        } else if (targetElem.mozRequestFullScreen) {
            targetElem.mozRequestFullScreen();
        } else if (targetElem.webkitRequestFullscreen) {
            targetElem.webkitRequestFullscreen();
        } else if (targetElem.msRequestFullscreen) {
            targetElem.msRequestFullscreen();
        }
    });

    const playerRef: MutableRefObject<any> = useRef()


    return (
        <StyledPlayer id={playerId} ref={playerRef} onDoubleClick={(event: MouseEvent) => {
            doubleClickEvent(event)
        }}></StyledPlayer>
    )
}


Player.defaultProps = {
    type: VIDEO_TYPE.CRIME,
    rtsp: '',
    facId: '',
}

export default Player