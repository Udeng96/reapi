# Hotspot 분석

- 분석 일시: 2026-03-30
- 분석 방식: git 기반 (변경 빈도) + 정적 분석 (라인 수)
- 변경 빈도 기준: 상위 30개 중 상위 1/3 = 높음 (25+ commits), 중간 1/3 = 중간 (16-24), 하위 1/3 = 낮음 (15)
- 라인 수 기준: 상위 20% = 많음 (200+ lines)

---

## Hotspot 목록

### 백엔드

| 파일 | 변경 빈도 | 라인 수 | 판정 | 관련 도메인 |
|------|-----------|---------|------|------------|
| `service/hams/HamsService.kt` | 39 (높음) | 855 (많음) | **Hotspot** | hams |
| `service/rino/event/EventService.kt` | 37 (높음) | 359 (많음) | **Hotspot** | event |
| `controller/event/EventController.kt` | 34 (높음) | 325 (많음) | **Hotspot** | event |
| `conf/ApiConstants.kt` | 31 (높음) | - | 자주 수정되는 설정 | conf(전체) |
| `service/rino/fms/FacService.kt` | 30 (높음) | - | 자주 수정되는 서비스 | fms |
| `controller/hams/HamsController.kt` | 26 (높음) | 254 (많음) | **Hotspot** | hams |
| `conf/db/MainDataConfig.kt` | 26 (높음) | - | 자주 수정되는 설정 | conf |
| `repository/rino/event/EventRepository.kt` | 25 (높음) | 217 (많음) | **Hotspot** | event |
| `controller/cctv/CctvController.kt` | 23 (중간) | - | 중간 우선순위 | cctv |
| `repository/rino/fms/FacEm.kt` | - | 289 (많음) | 잠재적 위험 | fms |
| `service/mobile/XeusLiveViewEventService.kt` | - | 339 (많음) | 잠재적 위험 | mobile |
| `components/VWorldParser.kt` | 17 (중간) | 294 (많음) | 잠재적 위험 | 외부API |
| `proc/hams/client/HamsSendClient.kt` | - | 292 (많음) | 복잡하지만 안정적 | hams |

### 프론트엔드

| 파일 | 변경 빈도 | 라인 수 | 판정 | 관련 도메인 |
|------|-----------|---------|------|------------|
| `data/config/config.ts` | 36 (높음) | - | 자주 수정되는 설정 | conf(전체) |
| `index.tsx` | 19 (중간) | - | 진입점 | app |
| `dstr/gis/gisArea.tsx` | 19 (중간) | 288 (많음) | 잠재적 위험 | dstr/gis |
| `hams/dash/realTime/realtimeDataBox.tsx` | - | 530 (많음) | 복잡하지만 안정적 | hams |
| `data/const/hamsConst.ts` | - | 508 (많음) | 복잡하지만 안정적 | hams |
| `dstr/player/PlayerBox.tsx` | - | 340 (많음) | 복잡하지만 안정적 | dstr |
| `monitor/menu/rightMenu.tsx` | - | 309 (많음) | 복잡하지만 안정적 | monitor |
| `dstr/gis/marker/CctvClusterMarkers.tsx` | - | 305 (많음) | 복잡하지만 안정적 | dstr/gis |

---

## 도메인별 우선순위

| 도메인 | 우선순위 | 근거 |
|--------|---------|------|
| **conf/infra** | 높음 | ApiConstants(73 Fan-in), MainDataConfig(26 변경) — 모든 도메인 선행 조건 |
| **event** | 높음 | Hotspot 3개 (Service/Controller/Repository), Event.kt(36 Fan-in) |
| **hams** | 높음 | Hotspot 2개 (Service 855줄, Controller), 가장 큰 단일 파일 |
| **fms** | 중간 | Stat.kt(61 Fan-in), FacService(30 변경), FacEm(289줄) |
| **cctv/xeus** | 중간 | Controller(23 변경), XeusLiveViewEventService(339줄) |
| **mobile** | 중간 | XeusLiveViewEventService(339줄, 16 Fan-out) |
| **proc(socket)** | 중간 | WebSocket/Netty 서버 — SSE 전환 대상 |
| **dstr(FE)** | 중간 | gisArea(288줄, 34 import), 프론트 핵심 화면 |
| **hams(FE)** | 낮음 | 큰 파일 있지만 변경 빈도 낮아 안정적 |
| **monitor(FE)** | 낮음 | 안정적, 독립 진행 가능 |
