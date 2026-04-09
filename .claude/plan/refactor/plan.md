# 리팩토링 계획 — 화면별 모듈 분리 및 스택 전환

## 현황 요약

레거시 모놀리식 프로젝트(Java 8 / Spring Boot 2.6 / Maven / CRA+Webpack / Redux-Saga)를
화면 단위 독립 모듈로 분리하고 목표 스택으로 전환한다.

- BE: Kotlin+Java 8 / Maven / SB 2.6.4 / Layered → **Java 17 / Gradle multi-module / SB 3.x / Layered + DDD**
- FE: CRA+Webpack / Redux+Saga / React Router → **pnpm workspace + Vite / Zustand+TanStack Query / TanStack Router / FSD**
- 실시간: Netty WebSocket → **SSE**
- 지도: Leaflet 유지
- 각 모듈 독립 실행/배포 가능

## 분석 요약

- Hotspot: HamsService(855줄/39변경), EventService(359줄/37변경), EventController(325줄/34변경)
- Fan-in 최고: ApiConstants(73), Stat(61), Event(36), Message(35)
- 테스트: BE/FE 모두 **0개** (전체 삭제됨)
- javax→jakarta 전환 필요: 48개 파일, 74회
- 사내 라이브러리 `com.eseict:common:2.3.10-SNAPSHOT` SB3 호환성 미확인

상세: [analysis/dependency.md](./analysis/dependency.md), [analysis/hotspots.md](./analysis/hotspots.md)

---

## 도메인별 작업 현황

| # | 모듈 | 파일 | 테스트 현황 | 상태 |
|---|------|------|------------|------|
| 01 | infra | [tasks/01-infra.md](./tasks/01-infra.md) | 없음 | 미완료 (0/8) |
| 02 | common | [tasks/02-common.md](./tasks/02-common.md) | 없음 | 미완료 (0/6) |
| 03 | dstr | [tasks/03-dstr.md](./tasks/03-dstr.md) | 없음 | 부분 완료 (5/10) |
| 04 | hams | [tasks/04-hams.md](./tasks/04-hams.md) | 단위 테스트 4파일 통과 | 부분 완료 (6/9) |
| 05 | cctv-monitor | [tasks/05-cctv-monitor.md](./tasks/05-cctv-monitor.md) | 없음 | 미완료 (0/7) |
| 06 | realtime | [tasks/06-realtime.md](./tasks/06-realtime.md) | 없음 | 미완료 (0/6) |

---

## 실행 원칙

- 기능 파괴 없이 점진적으로 진행한다
- 안정성을 최우선으로 하고 유효성 검사를 확실하게 한다
- 각 Phase 완료 시 빌드 + 기능 동작 검증 후 다음 Phase로 넘어간다
- 사내 라이브러리(`com.eseict:common`) SB3 호환성을 Phase 1에서 반드시 확인한다

---

## 실행 순서

### Phase 0. 테스트 안전망 구축
- 대상: 전체 모듈 (BE + FE)
- 전략: 특성화 테스트(기본) + BDD(user-facing 보완) + 단위 테스트(Phase 1 이후)
- BE: JUnit5 + Mockito, 핵심 서비스 input/output 고정
- FE: vitest + testing-library, 핵심 saga/store 로직 테스트

### Phase 1. infra (01)
- BE: Maven → Gradle multi-module 전환, SB 2.6 → 3.x, Java 8 → 17, javax → jakarta
- FE: CRA+Webpack → Vite, Redux → Zustand + TanStack Query, React Router → TanStack Router

### Phase 2. common (02)
- BE: ApiConstants 도메인별 분리, DataSource 모듈별 분리, 공통 DTO/유틸 추출
- FE: shared 패키지 구성 (API, 타입, UI, Leaflet 헬퍼, 설정)

### Phase 3. dstr (03)
- BE: event + fms + cctv → dstr 모듈 Layered + DDD 전환
- FE: dstr 화면 → dstr 패키지 FSD 전환
- Hotspot 최다 영역, 가장 높은 우선순위

### Phase 4. hams (04) + cctv-monitor (05) — 병렬 가능
- hams: BE hams Layered + DDD + FE hams FSD (HamsService 855줄 분해)
- cctv-monitor: BE cctv 제어 Layered + DDD + FE cctvMonitor FSD

### Phase 5. realtime (06)
- Netty WebSocket → Spring SSE 전환
- BE: SseEmitter 기반 브로드캐스트
- FE: WebSocketEventSaga → EventSource 기반 Zustand 커스텀 훅
- 모든 도메인 전환 완료 후 수행

---

## 의존 관계

```
Phase 0 (테스트)
    |
Phase 1 (infra)
    |
Phase 2 (common)
    |
    +--------+--------+
    |                 |
Phase 3           Phase 4
 (dstr)     (hams + cctv-monitor)
    |                 |
    +--------+--------+
             |
        Phase 5
       (realtime)
```

---

## 목표 프로젝트 구조

### BE (Gradle multi-module)

```
reapi/
├── build.gradle                 (루트: 공통 플러그인/의존성)
├── settings.gradle              (include 'common', 'dstr', 'hams', 'cctv-monitor')
├── common/
│   ├── build.gradle             (java-library)
│   └── src/main/java/
│       └── com.eseict.reapi.common/
│           ├── config/          (공통 설정, Properties)
│           ├── domain/          (공유 엔티티, 값 객체)
│           ├── dto/             (공유 DTO, Message 등)
│           ├── util/            (공통 유틸리티)
│           └── infra/           (공통 인프라: 필터, AOP)
├── dstr/
│   ├── build.gradle             (spring-boot, bootJar 가능)
│   └── src/main/java/
│       └── com.eseict.reapi.dstr/
│           ├── DstrApplication.java
│           ├── event/           (DDD: controller/service/repository/domain/dto)
│           ├── facility/        (fms+cctv 통합)
│           └── config/          (모듈 전용 설정)
├── hams/
│   ├── build.gradle             (spring-boot, bootJar 가능)
│   └── src/main/java/
│       └── com.eseict.reapi.hams/
│           ├── HamsApplication.java
│           ├── hams/            (DDD: controller/service/repository/domain/dto)
│           └── config/
└── cctv-monitor/
    ├── build.gradle             (spring-boot, bootJar 가능)
    └── src/main/java/
        └── com.eseict.reapi.cctvmonitor/
            ├── CctvMonitorApplication.java
            ├── cctv/            (DDD: controller/service/repository/domain/dto)
            └── config/
```

### FE (pnpm workspace)

```
front/
├── pnpm-workspace.yaml
├── packages/
│   ├── shared/                  (공통 패키지)
│   │   ├── package.json
│   │   └── src/
│   │       ├── api/             (axios 래퍼, TanStack Query 설정)
│   │       ├── config/          (API URL, 환경변수)
│   │       ├── types/           (공통 타입)
│   │       ├── ui/              (공통 UI 컴포넌트)
│   │       ├── lib/             (cn, 유틸)
│   │       └── map/             (Leaflet 헬퍼, 타일맵 설정)
│   ├── dstr/                    (재난 화면 — 독립 Vite App)
│   │   ├── package.json
│   │   ├── vite.config.ts
│   │   └── src/
│   │       ├── app/             (FSD)
│   │       ├── pages/
│   │       ├── widgets/         (gis-map, event-panel, cctv-view)
│   │       ├── features/        (event-search, cctv-modal 등)
│   │       └── entities/        (event, cctv, mobile)
│   ├── hams/                    (무인국사 화면 — 독립 Vite App)
│   │   └── ...
│   └── cctv-monitor/            (모니터 화면 — 독립 Vite App)
│       └── ...
```

---

## 검증 방법

| 단계 | 방법 |
|------|------|
| 빌드/컴파일 | `./gradlew build` (BE), `pnpm -r build` (FE) |
| 테스트 통과 | `./gradlew test` (BE), `pnpm -r test` (FE) |
| 기능 동작 확인 | 각 모듈 독립 실행 후 기존 API/화면 정상 동작 수동 확인 |

---

## 핵심 파일 목록

### BE 고위험 (전환 시 주의)

| 파일 | 이유 |
|------|------|
| `conf/ApiConstants.kt` | Fan-in 73, 도메인별 분리 필요 |
| `service/hams/HamsService.kt` | 855줄, 12가지 책임, 분해 필수 |
| `service/rino/event/EventService.kt` | Hotspot, OMS 권한 인라인 |
| `repository/rino/event/EventEm.kt` | `SET LOCAL jit = off` 유지 필수 |
| `proc/hams/client/HamsSendClient.kt` | 동기 블로킹 I/O (요청당 최대 10초) |
| `conf/db/MainDataConfig.kt` | 다중 DataSource, 모듈 분리 시 재구성 |

### FE 고위험

| 파일 | 이유 |
|------|------|
| `data/config/config.ts` | 변경 36회, API URL 36개 |
| `pages/component/dstr/gis/gisArea.tsx` | 288줄, 34 import, FSD 분해 대상 |
| `pages/saga/dstr/event/WebSocketEventSaga.ts` | SSE 전환 대상 |
| `pages/store/rootStore.ts` | Redux → Zustand 전환 기준 |
| `index.tsx` | CSS 30개 import, 진입점 재구성 |

---

## 보류 요약

| 항목 | 보류 이유 |
|------|----------|
| `com.eseict:common:2.3.10-SNAPSHOT` SB3 호환성 | Phase 1에서 확인 후 결정 |
| Oracle DataSource (`standardsystem`) | 실제 사용 여부 확인 필요 (HTTP 방식으로 대체된 것으로 추정) |
| `DataSourceRouting` / `DataContextHolder` | 현재 비활성화 상태, 모듈 분리 후 필요 여부 재판단 |
| Tailwind 도입 여부 | FE 스타일링 방식 확정 후 결정 (현재 순수 CSS) |
