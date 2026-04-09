import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import type { QueryClient } from '@tanstack/react-query';

const WS_URL = '/api/ws';
const DOE_TOPIC = '/topic/hams.doe';

let stompClient: Client | null = null;

interface DoePayload {
  eqId: string;
  doorType: string;
  doorState: string;
  doorLockState: string;
  eventType: string;
}

/**
 * STOMP WebSocket 연결 및 DOE 이벤트 구독.
 * DOE 수신 시 해당 hams detail 쿼리를 invalidate → RealtimePanel 자동 갱신.
 */
export function connectHamsWs(
  queryClient: QueryClient,
  onConnect?: () => void,
  onDisconnect?: () => void,
) {
  disconnectHamsWs();

  stompClient = new Client({
    webSocketFactory: () => new SockJS(WS_URL),
    reconnectDelay: 5000,
    onConnect: () => {
      onConnect?.();

      stompClient!.subscribe(DOE_TOPIC, (frame) => {
        try {
          const payload: DoePayload = JSON.parse(frame.body);
          // BE eqId는 "000000001" 형태(9자리 0-패딩), FE hamsId는 "1" 형태
          const numericId = payload.eqId ? String(parseInt(payload.eqId, 10)) : null;
          if (numericId && numericId !== 'NaN') {
            queryClient.invalidateQueries({ queryKey: ['hams', 'detail', numericId] });
            queryClient.invalidateQueries({ queryKey: ['hams', 'list'] });
          }
        } catch (e) {
          console.warn('[hams-ws] DOE 메시지 파싱 실패', e);
        }
      });
    },
    onDisconnect: () => {
      onDisconnect?.();
    },
    onStompError: (frame) => {
      console.error('[hams-ws] STOMP 오류:', frame.headers['message']);
    },
  });

  stompClient.activate();
}

export function disconnectHamsWs() {
  if (stompClient?.active) {
    stompClient.deactivate();
  }
  stompClient = null;
}
