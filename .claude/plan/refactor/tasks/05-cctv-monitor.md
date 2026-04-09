# 05. cctv-monitor — CCTV 모니터링 모듈 (BE + FE)

- 우선순위: 중간
- 선행 조건: 02-common 완료
- 테스트 현황: 없음

---

## 진행 현황

- [ ] 05-0. 특성화 테스트 작성 (CctvControlService, MonitorController)
- [ ] 05-1. BE: cctv 모니터 도메인 Layered + DDD 전환
- [ ] 05-2. BE: xeus 외부 HTTP 연동 Adapter 분리
- [ ] 05-3. BE: cctv-monitor 모듈 독립 실행 검증
- [ ] 05-4. FE: monitor 화면 → cctv-monitor 패키지로 이동 (FSD)
- [ ] 05-5. FE: Redux store/saga → Zustand + TanStack Query 전환
- [ ] 05-6. FE: cctv-monitor 패키지 독립 실행 검증

---

## 상세

### 05-0. 특성화 테스트 작성

대상: CctvControlService(PTZ 제어), CctvController(FacService 위임 패턴)

### 05-1. BE: cctv 모니터 도메인 Layered + DDD 전환

현재 문제: CctvController가 fms의 FacService 직접 주입, 독립 서비스 없음
변경 방향:
```
cctv-monitor/cctv/
  controller/MonitorController.java (CCTV 그리드 목록/재생)
  controller/PtzController.java (PTZ 제어)
  service/CctvQueryService.java, PtzControlService.java
  repository/CctvRepository.java
  domain/Cctv.java, PtzCommand.java
  dto/CctvResponse.java, PtzRequest.java
  client/XeusPtzApiClient.java (platform.xeus.url HTTP)
```
- dstr 모듈의 facility 도메인과 공유하는 데이터(ErfFacInfo)는 common 모듈의 엔티티로 참조
- 모니터 전용 쿼리는 cctv-monitor 모듈 내부에서 처리

### 05-2. BE: xeus 외부 HTTP 연동 Adapter 분리

현재 문제: CctvControlService가 `platform.xeus.url`로 직접 HTTP 호출
변경 방향: `XeusPtzApiClient`로 분리, 서비스에서 직접 호출

### 05-3. BE: cctv-monitor 모듈 독립 실행 검증

- `./gradlew :cctv-monitor:bootRun` 단독 실행
- CCTV 목록/PTZ 제어 API 정상 응답 확인

### 05-4. FE: monitor 화면 → cctv-monitor 패키지 FSD

변경 방향:
- `widgets/cctv-grid/` ← cctv-monitor/cctv/ (cctvArea, cctvFrame, playerBox)
- `widgets/monitor-menu/` ← monitor/menu/ (menuArea, topMenu, rightMenu)
- `features/ptz-control/` ← cctv-monitor/cctv/ptzControlBox
- `features/cctv-drag/` ← monitor/menu/drag/ (react-dnd treeview)
- `features/cycle-play/` ← monitor/menu/rightMenu (사이클 재생 로직)
- `entities/monitor/` ← data/interface/monitor/ + monitorServerStore + monitorViewStore

### 05-5. FE: Redux → Zustand + TanStack Query

- monitorServerStore → TanStack Query (menuList, firstPlayPage)
- monitorViewStore → Zustand (gridRatio, isPageTrans, currentPagePlayList, dragTargetInfo)
- monitorSaga → TanStack Query queryFn/mutationFn

### 05-6. FE: cctv-monitor 패키지 독립 실행 검증

- `pnpm --filter cctv-monitor dev` 단독 실행
- CCTV 그리드/PTZ 제어/사이클 재생 정상 동작 확인
