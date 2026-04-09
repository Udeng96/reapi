# 01. infra — 빌드/프레임워크 전환

- 우선순위: 높음
- 선행 조건: Phase 0 (테스트 안전망)
- 테스트 현황: 없음

---

## 진행 현황

- [ ] 01-0. 특성화 테스트 작성 (BE 핵심 서비스 + FE 핵심 store/saga)
- [ ] 01-1. BE: Maven → Gradle Groovy DSL 전환
- [ ] 01-2. BE: Gradle multi-module 구조 생성 (common/dstr/hams/cctv-monitor)
- [ ] 01-3. BE: Spring Boot 2.6.4 → 3.x 업그레이드 + Java 8 → 17
- [ ] 01-4. BE: javax.* → jakarta.* 전환 (48개 파일, 74회)
- [ ] 01-5. BE: 사내 라이브러리 `com.eseict:common` SB3 호환성 확인 및 대응
- [ ] 01-6. FE: CRA+Webpack → pnpm workspace + Vite 전환
- [ ] 01-7. FE: pnpm workspace 구조 생성 (shared/dstr/hams/cctv-monitor)

---

## 상세

### 01-0. 특성화 테스트 작성

대상 파일:
- BE: EventService, HamsService, FacService, CctvController (Hotspot 상위)
- FE: rootStore, WebSocketEventSaga, hamsSaga, eventTapSaga

작성 방식: input/output 고정 방식. 현재 동작을 그대로 캡처하여 리팩토링 후 동일 동작 보장.

### 01-1. BE: Maven → Gradle Groovy DSL 전환

현재 문제: `pom.xml` 단일 프로젝트, multi-module 구조 불가
변경 방향:
- `pom.xml` → `build.gradle` + `settings.gradle`
- 의존성 그대로 이전 (hibernate-entitymanager, ojdbc8 등)
- `mvnw` → `gradlew` wrapper 생성
- WAR 패키징은 일단 유지 (SB3 전환 후 JAR로 변경)

### 01-2. BE: Gradle multi-module 구조 생성

현재 문제: 모노리식 단일 프로젝트
변경 방향:
```
settings.gradle: include 'common', 'disaster', 'unmanned', 'monitor'
```
- 이 단계에서는 구조만 생성하고 코드 이동은 하지 않음
- common 모듈에 루트 패키지 설정
- 각 모듈에 `@SpringBootApplication` 진입점 생성

### 01-3. BE: Spring Boot 2.6.4 → 3.x + Java 17

현재 문제: Java 8 / SB 2.6.4 / javax.*
변경 방향:
- `sourceCompatibility = '17'`
- `spring-boot-starter-parent` → SB 3.x
- Kotlin 1.6.10 → 1.9.x (Java 17 호환)
- `hibernate-entitymanager` 제거 (SB3의 hibernate-core에 통합)
- `hibernate-jpa-2.1-api` 제거 (Jakarta 전환)
- `@ConstructorBinding` 제거 (ApiProperties, ApSystemProperties)

### 01-4. BE: javax → jakarta 전환

현재 문제: javax.servlet, javax.persistence 48개 파일
변경 방향: 전체 치환
- `javax.persistence` → `jakarta.persistence`
- `javax.servlet` → `jakarta.servlet`
- `javax.validation` → `jakarta.validation`
- `javax.annotation` → `jakarta.annotation`

### 01-5. BE: 사내 라이브러리 호환성 확인

현재 문제: `com.eseict:common:2.3.10-SNAPSHOT` — SB2 기반 가능성
변경 방향:
- SB3 호환 버전이 있으면 업그레이드
- 없으면 사용하는 클래스를 직접 구현으로 대체
- **보류 가능**: 확인 결과에 따라 대응 방향 결정

### 01-6. FE: CRA+Webpack → Vite 전환

현재 문제: CRA eject + Webpack 5, config/webpack.config.js 커스텀
변경 방향:
- `config/`, `scripts/` 디렉토리 제거
- `vite.config.ts` 생성 (proxy, alias 설정 이전)
- `react-scripts` 의존성 제거
- CSS import 방식 유지 (Tailwind 도입은 별도 결정)
- `index.html` 루트로 이동 (CRA → Vite 구조)

### 01-7. FE: pnpm workspace 구조 생성

현재 문제: 단일 package.json
변경 방향:
- `pnpm-workspace.yaml` 생성
- `packages/shared`, `packages/disaster`, `packages/unmanned`, `packages/monitor` 디렉토리 생성
- 각 패키지에 `package.json`, `vite.config.ts`, `tsconfig.json`
- 이 단계에서는 구조만 생성하고 코드 이동은 Phase 2~4에서 진행
