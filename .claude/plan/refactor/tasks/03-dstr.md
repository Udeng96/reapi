# 03. dstr — 재난상황 모듈 (BE + FE)

- 우선순위: 높음
- 선행 조건: 02-common 완료
- 테스트 현황: 없음

---

## 진행 현황

- [ ] 03-0. 특성화 테스트 작성 (EventService, FacService, EventController)
- [x] 03-1. BE: event 도메인 Layered + DDD 전환
  - EventController, EventService, EventItemService, Repository, Domain, DTO 구현 완료
  - EventService 단일 클래스 유지 (QueryService/CommandService 분리는 복잡도 대비 효과 낮아 보류)
  - closeEvent 소켓 전송 TODO 유지 (Phase 5 SSE 전환 시 구현 예정)
- [x] 03-2. BE: fms+cctv 도메인 → facility 도메인 Layered + DDD 전환
  - CctvController, FacilityController, FacilityService, CctvControlService, Repository, DTO 구현 완료
  - GET /cctv/monitor (getCctvListGrpByClfy) 추가 완료
- [x] 03-3. BE: OMS 권한 체크 로직 분리 (컨트롤러 인라인 → Port)
  - EventController 인라인 OMS 권한 체크 제거 완료 (뉴 모듈에서는 컨트롤러에 인라인 없음)
- [x] 03-4. BE: 외부 시스템 연동 Adapter 분리 (VWorld, OCS, 소방대응)
  - VWorldService → VWorldApiClient (external/client/) 전환, VWorldController 업데이트
  - StandardSystemController 인라인 RestTemplate → StandardSystemClient 분리
  - FireResponder: 레거시부터 미구현 + 실제 미사용 확인 → FireResponderClient/Controller 제거
- [ ] 03-5. BE: dstr 모듈 독립 실행 검증
- [x] 03-6. FE: dstr 화면 → dstr 패키지로 이동
  - entities/ 레이어 (event/cctv/mobile/map) 신규 생성: types, api, queries, store
  - features/event-panel/ 구현 완료: EventPanel (3탭) + DstrEvetTab/CctvTab/MobileTab
    - dstr/: DstrSearchBox, DstrSearchArea, DstrListItem, DstrListArea, DstrDetailArea, DstrModal
    - cctv/: CctvSearchArea, CctvListItem, CctvListArea, CctvDetailArea
    - mobile/: NowMobileListItem/Area, LastMobileSearchArea, LastMobileListItem/Area
  - Redux/styled-components → Zustand + TanStack Query + className 전환
- [x] 03-7. FE: Redux store/saga → Zustand + TanStack Query 전환 (event, cctv, mobile)
  - server store → TanStack Query (queries.ts), view store → Zustand (store.ts) 전환 완료
  - WebSocket 실시간 이벤트: useEventWebSocket 훅 구현 (mobile invalidate + 재난이벤트 prepend)
  - hams 메시지는 hams 패키지에서 처리 (무시)
- [ ] 03-8. FE: gisArea.tsx 288줄 FSD 분해 (widgets/features 분리)
- [ ] 03-9. FE: dstr 패키지 독립 실행 검증

---

## 상세

### 03-0. 특성화 테스트 작성

대상:
- EventService: getEvents, closeEvent, getHeatmapCount
- FacService: getFacList, getCctvList
- EventController: 권한 체크 + 서비스 위임 패턴

### 03-1. BE: event 도메인 Layered + DDD 전환

현재 문제: Hotspot 3개 (Service 359줄, Controller 325줄, Repository 217줄), OMS 권한 인라인
변경 방향:
```
dstr/event/
  controller/EventController.java
  service/EventQueryService.java, EventCommandService.java
  repository/EventRepository.java, EventCriteriaRepository.java (SET LOCAL jit=off 유지)
  domain/Event.java (비즈니스 규칙 포함)
  dto/EventRequest.java, EventResponse.java
  client/OmsApiClient.java (외부 OMS API 호출)
  client/MrsSocketClient.java (MRS 소켓 송신)
```

### 03-2. BE: fms+cctv → facility 도메인 Layered + DDD 전환

현재 문제: cctv가 fms의 FacService를 직접 사용, 별도 엔티티 없음
변경 방향:
- cctv를 fms에 통합하여 `facility` 도메인으로 구성
- FacEm 289줄 (수동 EM + Criteria API) → Repository로 통합
- FacRepository JPQL과 FacEm Criteria 중복 정리
```
dstr/facility/
  controller/FacilityController.java, CctvController.java
  service/FacilityQueryService.java, CctvControlService.java
  repository/FacilityRepository.java, FacilityCriteriaRepository.java
  domain/Facility.java, FacilityCategory.java
  dto/FacilityResponse.java, CctvResponse.java
  client/XeusCctvApiClient.java (외부 xeus HTTP 호출)
```

### 03-3. BE: OMS 권한 체크 분리

현재 문제: EventController의 모든 GET에 `useEventAuth`+`omsService.fetchUserInfo()` 인라인
변경 방향: 별도 서비스(`OmsAuthService`)로 분리, AOP 또는 ArgumentResolver로 횡단 관심사 처리

### 03-4. BE: 외부 시스템 연동 Adapter 분리

현재 문제: VWorldParser(294줄), StandardSystemService(HTTP), FireResponderController
변경 방향: 각각 client 패키지로 분리
- `client/VWorldApiClient.java`
- `client/StandardSystemClient.java`
- `client/FireResponderClient.java`

### 03-5. BE: dstr 모듈 독립 실행 검증

- `./gradlew :dstr:bootRun` 으로 단독 실행
- 기존 API 엔드포인트 정상 응답 확인
- DataSource 연결 확인

### 03-6. FE: dstr 화면 → dstr 패키지로 이동

현재 문제: pages/component/dstr/ 전체가 모노리스에 있음
변경 방향:
- `packages/dstr/src/` 로 이동
- app/(라우터), pages/(조합), widgets/, features/, entities/ FSD 구조

### 03-7. FE: Redux → Zustand + TanStack Query

현재 문제: server/view 2-tier Redux + Redux-Saga
변경 방향:
- server store (DstrServerEventStore, DstrServerCctvStore, dstrServerMobileStore) → TanStack Query
- view store (DstrViewEventStore, DstrViewCctvStore, DstrViewMobileStore, mapStore) → Zustand
- saga (eventTapSaga, cctvTapSaga, mobileTapSaga, mapServerSaga, heatMapSaga) → TanStack Query queryFn/mutationFn
- action/ 디렉토리 전체 제거

### 03-8. FE: gisArea.tsx 288줄 FSD 분해

현재 문제: 34 import, 지도+마커+레이어+팝업+핸들러 전부 한 파일
변경 방향:
- `widgets/gis-map/` — MapContainer (지도 초기화만)
- `features/event-marker/` — EventMarker, EventLine
- `features/cctv-marker/` — CctvClusterMarkers, CctvPopupLayer
- `features/mobile-marker/` — MobileLiveMarkers, MobilePastMarkers, MobilePopupLayer
- `features/addr-search/` — addrSearchBox, addrSearchList
- `features/gis-settings/` — gisSettingArea, gisHeatMap
- `features/map-draw/` — MapDrawLayer, MapDraw.js

### 03-9. FE: dstr 패키지 독립 실행 검증

- `pnpm --filter dstr dev` 로 단독 실행
- 기존 재난상황 화면 정상 렌더링 확인
- API 호출, 지도, CCTV 플레이어 동작 확인
