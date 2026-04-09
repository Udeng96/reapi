# 06. realtime — 실시간 통신 전환 (WebSocket → SSE)

- 우선순위: 중간
- 선행 조건: 03-disaster, 04-unmanned, 05-monitor 완료
- 테스트 현황: 없음

---

## 진행 현황

- [ ] 06-0. 특성화 테스트 작성 (WebSocket 메시지 수신/변환 로직)
- [ ] 06-1. BE: SseEmitter 기반 브로드캐스트 매니저 구현
- [ ] 06-2. BE: Netty TCP 소켓 수신 → SSE 발행 연결
- [ ] 06-3. BE: Netty WebSocket 서버 제거 (포트 26000)
- [ ] 06-4. FE: WebSocketEventSaga → EventSource 기반 Zustand 훅 전환
- [ ] 06-5. FE: 모듈별 SSE 구독 통합 검증

---

## 상세

### 06-0. 특성화 테스트 작성

대상:
- BE: SocketMsgQueuePolling(메시지 변환), ClientNotiMsgConverter(StatEvet 변환)
- FE: WebSocketEventSaga(3종 이벤트 dispatch 로직)

### 06-1. BE: SseEmitter 브로드캐스트 매니저

현재 문제: Netty WebSocket으로 브로드캐스트, Spring 생명주기 밖
변경 방향:
- `common/infra/sse/SseEmitterManager.java` — 연결 관리, 구독/해제, ping/timeout
- 각 모듈에서 SseEmitterManager를 주입받아 이벤트 발행
- disaster: 재난이벤트/모바일 위치 SSE
- unmanned: DOE 도어 상태 SSE
- 키 설계: `token + connectionId` (동일 사용자 다중 탭 지원)

### 06-2. BE: Netty TCP 수신 → SSE 발행 연결

현재 데이터 흐름:
```
MRS → Netty TCP → SocketMsgQueue → SocketMsgQueuePolling → WebSocket 브로드캐스트
```

변경 방향:
```
MRS → Netty TCP → SocketMsgQueue → SocketMsgQueuePolling → SseEmitterManager.send()
```
- SocketMsgQueuePolling의 WebSocket 참조만 SseEmitter로 교체
- SocketMsgQueue(LinkedBlockingQueue) 패턴은 유지 (안정적)
- ClientNotiMsgConverter 변환 로직 유지

### 06-3. BE: Netty WebSocket 서버 제거

현재 문제: WebSocketServer(포트 26000), WebSocketServerHandler, BinderWebSocketServer
변경 방향:
- SSE 전환 완료 후 proc/rino/webSocket/ 디렉토리 제거
- ServletInitializer에서 WebSocket 서버 기동 코드 제거
- 단, Netty TCP 소켓 서버(SocketServerMain, 포트 8087)는 유지 (MRS 수신용)

### 06-4. FE: WebSocket → EventSource 전환

현재 문제: WebSocketEventSaga에서 3종 이벤트(재난/모바일/hams) 단일 WebSocket으로 수신
변경 방향:
- `shared/api/sse.ts` — EventSource 래퍼 (재연결, 에러 핸들링)
- `disaster` 패키지:
  - `features/realtime-event/useEventSse.ts` — 재난이벤트 SSE 구독 → Zustand dispatch
  - `features/realtime-mobile/useMobileSse.ts` — 모바일 위치 SSE 구독
- `unmanned` 패키지:
  - `features/realtime-doe/useDoeEventSse.ts` — DOE 도어 상태 SSE 구독
- Redux-Saga `eventChannel` 패턴 → React 커스텀 훅 + Zustand 직접 업데이트

### 06-5. FE: 모듈별 SSE 통합 검증

- disaster: 재난이벤트 수신 → 목록 자동 갱신 + 지도 이동
- unmanned: DOE 수신 → 도어 상태 실시간 반영
- 재연결 시나리오 테스트 (서버 재시작 시)
- 다중 탭 시나리오 테스트
