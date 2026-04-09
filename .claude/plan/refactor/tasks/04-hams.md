# 04. hams — 무인국사 모듈 (BE + FE)

- 우선순위: 높음
- 선행 조건: 02-common 완료
- 테스트 현황: 없음

---

## 진행 현황

- [x] 04-0. 특성화 테스트 작성 (CrcUtil, Aes128, HamsCmd 빌드/파싱 4개 파일, 전체 통과)
- [x] 04-1. BE: HamsService 855줄 → 6개 서비스로 분해
  - HamsStatusService (DOS/THS 조회), HamsDoorControlService (DOC 제어),
    HamsDoeEventService (이력 저장), HamsDoeService (DOE 수신+WS 브로드캐스트),
    HamsStatService, HamsCctvService
- [x] 04-2. BE: hams 도메인 Layered + DDD 전환
  - client/ (HamsSendClient, config/, protocol/, util/, netty/)
  - hams/ (controller/, service/, repository/, domain/, dto/)
  - config/ (WebSocketConfig, MariaDataSourceConfig, PgDataSourceConfig)
- [ ] 04-3. BE: HamsSendClient 동기 블로킹 I/O 개선
- [x] 04-4. BE: Netty DOE 핸들러 → Spring 관리 빈 전환
  - HamsDoeServer (@Component @PostConstruct), HamsDoeServerInitializer,
    HamsDoeServerHandler (@Sharable @Component) — WebApplicationContextUtil 제거
- [ ] 04-5. BE: hams 모듈 독립 실행 검증
- [x] 04-6. FE: hams 화면 → hams 패키지로 이동 (FSD) — entities/hams/ 완성
- [x] 04-7. FE: Redux store/saga → Zustand + TanStack Query 전환
  - Zustand store (wsConnected 포함), TanStack Query (queries.ts), ws.ts (STOMP DOE 구독)
- [ ] 04-8. FE: hams 패키지 독립 실행 검증

---

## 상세

### 04-0. 특성화 테스트 작성

대상: HamsService의 12가지 책임별 테스트
- connectHams, checkDoor, checkTempHumi, sendDoc, receiveDoe
- saveDoorHist, getCctvList, updateFmsData, getList, doLock, getStat

### 04-1. BE: HamsService 855줄 → 6개 서비스로 분해

현재 문제: 소켓 연결/도어 제어/DOE 수신/FMS 동기화/CCTV/통계 12가지 책임 혼재
변경 방향:
- `HamsStatusService` ← checkDoor + checkTempHumi + getList + get (상태 조회)
- `HamsDoorControlService` ← doLock + sendDoc (도어락 제어)
- `HamsDoeEventService` ← receiveDoe + saveDoorHist + saveDoorEventHist (이벤트 수신)
- `HamsFmsSyncService` ← updateFmsData (FMS 동기화)
- `HamsStatService` ← getStat (통계)
- `HamsCctvService` ← getCctvList + getSampleData (CCTV)

### 04-2. BE: hams 도메인 Layered + DDD 전환

변경 방향:
```
hams/
  controller/HamsController.java
  service/HamsStatusService.java, HamsDoorControlService.java
  service/HamsDoeEventService.java, HamsFmsSyncService.java
  service/HamsStatService.java, HamsCctvService.java
  repository/HamsDoorHistRepository.java, HamsEquipmentRepository.java
  domain/HamsEquipment.java, DoorStatus.java, DoorHist.java
  dto/HamsRequest.java, HamsResponse.java, DoorStatResponse.java
  client/HamsSocketClient.java (장비 소켓 통신)
  client/CctvInfoFileReader.java (cctvInfo.json 파싱)
  netty/HamsDoeHandler.java (DOE 수신 핸들러)
```

### 04-3. BE: HamsSendClient 블로킹 I/O 개선

현재 문제: `Thread.sleep(2000)` 재귀 폴링 (최대 5회 = 10초 블로킹)
변경 방향:
- `HamsSocketClient`로 래핑하여 서비스에서 직접 호출
- 응답 대기를 `CompletableFuture` + timeout으로 대체
- 실패 시 재시도 전략 정비

### 04-4. BE: Netty DOE 핸들러 → Spring 관리 빈 전환

현재 문제: `WebApplicationContextUtil.getBean("hamsService")` Anti-pattern
변경 방향:
- `HamsDoeHandler`가 `HamsDoeEventService`를 생성자 주입
- Netty ChannelHandler에서 `WebApplicationContextUtil.getBean()` 직접 조회 제거

### 04-5. BE: hams 모듈 독립 실행 검증

- `./gradlew :hams:bootRun` 으로 단독 실행
- HAMS 조회/도어 제어 API 정상 응답 확인
- DOE 수신 Netty 서버 기동 확인

### 04-6. FE: hams 화면 → hams 패키지 FSD

변경 방향:
- `widgets/hams-dashboard/` ← hams/dash/ (dashRoot, dashInfo, realTime, cctv)
- `widgets/hams-gis-map/` ← hams/gis/ (gisArea, gisHamsMarker)
- `features/door-lock-control/` ← dash/realTime/modal/doorLockModal
- `features/hams-cctv-popup/` ← dash/cctv/cctvPopup
- `features/hams-chart/` ← dash/realTime/modal/chart/
- `entities/hams/` ← data/interface/hams/ + hamsServerStore + hamsViewStore

### 04-7. FE: Redux → Zustand + TanStack Query

- serverHamsStore → TanStack Query (hamsList, doorStat, hamsCctvList)
- hamsViewStore → Zustand (cctvPopupOpen, lockPopupOpen, isLoading)
- hamsSaga → TanStack Query queryFn/mutationFn
- WebSocket hams 수신 → Zustand 직접 업데이트 (Phase 5에서 SSE로 전환)

### 04-8. FE: hams 패키지 독립 실행 검증

- `pnpm --filter hams dev` 단독 실행
- 무인국사 목록/도어 상태/CCTV 정상 렌더링 확인
