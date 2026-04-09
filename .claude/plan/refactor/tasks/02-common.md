# 02. common — 공통 레이어 분리

- 우선순위: 높음
- 선행 조건: 01-infra 완료
- 테스트 현황: 없음

---

## 진행 현황

- [ ] 02-0. 특성화 테스트 작성 (공통 유틸, DTO 변환)
- [ ] 02-1. BE: ApiConstants 도메인별 분리
- [ ] 02-2. BE: DataSource 모듈별 분리
- [ ] 02-3. BE: 공통 DTO/유틸/인터셉터 → common 모듈로 이동
- [ ] 02-4. FE: shared 패키지 구성 (API, 타입, UI, Leaflet, 설정)
- [ ] 02-5. FE: 상태관리 전환 기반 구축 (Zustand store + TanStack Query 설정)

---

## 상세

### 02-0. 특성화 테스트 작성

대상: CommonUtil, HeaderUtil, EventUtil, SftpUtil, VWorldParser
작성 방식: input/output 고정

### 02-1. BE: ApiConstants 도메인별 분리

현재 문제: 단일 파일에 DB_SCHEMA, DATE, RESULT, EVENT 등 8개 inner object, Fan-in 73
변경 방향:
- `common/config/DbSchema.java` — DB 스키마 상수 (도메인 entity가 참조)
- `common/config/DateFormat.java` — 날짜 포맷 상수
- `common/config/ResultCode.java` — 응답 코드 상수
- 각 도메인 전용 상수는 해당 모듈로 이동 (Phase 3~4에서)

### 02-2. BE: DataSource 모듈별 분리

현재 문제: MainDataConfig가 rino/eas/mobile 전부 담당, HamsDataConfig 별도
변경 방향:
- `common`: 공통 DataSource 설정 추상화
- `dstr`: rino(ioc, fms, oms 스키마) + eas + xeus DataSource
- `hams`: hams DataSource + rino(fms 스키마 일부 참조)
- `cctv-monitor`: xeus DataSource + rino(fms 스키마 일부 참조)
- Oracle DataSource(standardsystem): 실제 사용 여부 확인 후 결정 (보류)
- DataSourceRouting/DataContextHolder: 모듈 분리 후 불필요하면 제거

### 02-3. BE: 공통 모듈 코드 이동

현재 문제: 모든 코드가 단일 패키지
변경 방향 — common 모듈로 이동할 대상:
- `dto/common/Message.kt` (Fan-in 35, 전체 controller가 사용)
- `dto/common/ClfyInfo.kt` (Fan-in 12)
- `dto/oms/UserInfo.kt` (Fan-in 18)
- `util/CommonUtil.kt`, `util/HeaderUtil.kt`, `util/SftpUtil.kt`
- `interceptor/TokenInterceptor.kt` (공통 인증)
- `conf/ApiProperties.kt`, `conf/ApSystemProperties.kt`
- `service/rino/oms/OmsService.kt` (사용자 인증 — 전 모듈 공통)

### 02-4. FE: shared 패키지 구성

현재 문제: data/, pages/saga/apis/ 등에 공통 코드 산재
변경 방향:
- `shared/api/` ← `saga/apis/xhr.ts` (axios 인스턴스)
- `shared/config/` ← `data/config/config.ts` (API URL 36개)
- `shared/types/` ← `data/interface/common/CommonInterface.ts`
- `shared/map/` ← `data/config/Map.ts` (Leaflet 타일맵 설정)
- `shared/ui/` ← `pages/component/dstr/paginagtion/`, tooltip, Player
- `shared/lib/` ← cn 유틸 (신규 생성)

### 02-5. FE: 상태관리 전환 기반 구축

현재 문제: Redux Toolkit + Redux-Saga
변경 방향:
- TanStack Query `QueryClient` 설정 (shared/api/)
- Zustand store 패턴 정의 (server/view 2-tier → TanStack Query + Zustand)
- 이 단계에서는 기반만 구축, 실제 전환은 Phase 3~4에서
