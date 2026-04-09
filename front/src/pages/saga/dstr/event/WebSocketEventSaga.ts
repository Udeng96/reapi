import {all, call, put, select, take} from "redux-saga/effects";
import {END, eventChannel} from "@redux-saga/core"

import _ from "underscore"

import conf, {MANAGER_TYPE_CD, USE_AUTH} from "../../../../data/config/config"
import {EventChannel} from "redux-saga";
import {RootState} from "../../../store/rootStore";
import {UserData} from "../../../../data/interface/user/userInterface";
import {unshiftEvetList, updateEvetDetail} from "../../../store/server/dstr/dstrServerEventStore";
import {VwEvent} from "../../../../data/interface/event/eventInterface";
import {requestDetailByUSvcId} from "../../apis/dstr/event/eventApis";
import {getActiveItem} from "../../../store/view/dstr/dstrViewEventStore";
import {changeMapMove} from "../../../store/view/dstr/mapStore";
import {MapMoveEnum} from "../../../../data/const/mapConst";
import * as mobileActions from "../../action/dstr/evet/mobileActions";
import {plusDoFetchPastCount} from "../../../store/view/dstr/dstrViewMobileStore";
import {updateWsDoeInfo} from "../../../store/server/hams/hamsServerStore";

let ws: WebSocket

function createEventChannel(): EventChannel<any> {
    return eventChannel(emit => {
        function createWs() {
            console.log("CREATE ... ", conf.API.API_WS_URL)
            //Subscribe to websocket
            ws = new WebSocket(conf.API.API_WS_URL)
            // ws = new WebSocket(Api.websocket.url)
            ws.onopen = () => {
                console.log("Opening Websocket")
            }
            ws.onerror = error => {
                console.log("ERROR: ", error)
            }
            ws.onmessage = e => {
                console.log('message', e)
                return emit({data: JSON.parse(e.data)})
            }
            ws.onclose = e => {
                if (e.code === 1005) {
                    console.log("WebSocket: closed")
                    // you probably want to end the channel in this case
                    emit(END)
                } else {
                    console.log('Socket is closed Unexpectedly. Reconnect will be attempted in a second.', e.reason)
                    setTimeout(() => {
                        createWs()
                    }, 1000)
                }

                console.log('close')
            }
        }

        typeof window !== 'undefined' && createWs()
        return () => {
            console.log("Closing Websocket")
            ws.close()
        }
    })
}


function* initializeWebSocketsChannel() {
    const channel: EventChannel<any> = yield call(createEventChannel);
    while (true) {
        const {data} = yield take(channel);

        let isReceive = (yield select((state: RootState) => state.view.dstrMain.event.wsEventReceive)) as boolean
        let userInfo = (yield select((state: RootState) => state.server.user.data)) as UserData | null
        if (data.data === 'mobile') { //모바일 업데이트인경우
            if (userInfo?.userType === MANAGER_TYPE_CD){//관리자인 경우에만 websocket으로 라이브 정보를 받아서 다시 조회한다.
                yield put(mobileActions.actions.requestMobileNowList())
                yield put(plusDoFetchPastCount())
            }
        }else if(data.data === 'hams') { //무인국사의 경우
            console.log('send hams message is ', data.info)

            yield put(updateWsDoeInfo(data.info))

        }else {
            //이벤트를 수신허용중인지 파악하고
            if (isReceive) {
                console.log("WEBSOCKET MESSAGE : ", data)
                console.log("userInfo", userInfo)

                let finalIsReceive = false
                //사용자 권한 처리 여부에 따라서 권한에 해당하는지 확인하고 yield put을 실시한다.
                if (USE_AUTH) { // 권한 처리를 하는경우
                    if (userInfo) {
                        const findIndex = _.findIndex(userInfo.eventAuthList!, {
                            evetId: data.statEvetId
                        })

                        findIndex > -1 && (finalIsReceive = true)
                    }
                } else { //권한처리를 하지않는경우
                    finalIsReceive = true
                }

                if (finalIsReceive) { //수신해도 좋을때만 넣어준다.

                    //이벤트 조회 //이것으로 디테일을 엎어치면 된다.
                    //디테일 조회하게 요청을 하고
                    // 디테일이 변경되면 알아서 이벤트를 투망할것
                    const detail: VwEvent = yield call(requestDetailByUSvcId, data.uSvcOutbId)
                    // const detail:VwEvent = yield call(requestDetailByUSvcId, '15202026118946-112CAR')

                    if (detail) {
                        yield put(updateEvetDetail(detail))
                        yield put(unshiftEvetList(detail))//투망을 실시해야함
                        yield put(getActiveItem(detail.statEvetOutbSeqn))


                        //좌표 이동 실시
                        yield put(changeMapMove({
                            latlng: [parseFloat(detail.lat!), parseFloat(detail.lng!)],
                            mapMoveType: MapMoveEnum.event
                        }))
                    }
                }

            } else {
                console.log('NO RECEIVE WS', data)
            }
        }



    }
}


const WebSocketEventSaga = function* () {
    yield all([initializeWebSocketsChannel()])
}


export default WebSocketEventSaga