# reapi

재난상황관제 시스템 리팩토링 프로젝트

레거시 Maven + Kotlin + Spring Boot 2 단일 프로젝트를 Gradle 멀티모듈 + Java 17 + Spring Boot 3 구조로 전환하고, 프론트엔드를 CRA에서 pnpm workspace + Vite 멀티패키지로 재구축한 프로젝트입니다.

---

## 모듈 구성

| 모듈 | 포트 | 설명 |
|------|------|------|
| `dstr` | 30001 | 재난상황관제 — 이벤트 조회, 시설물/CCTV 관리, 외부 플랫폼 연동 |
| `hams` | 30002 | 무인국사 — TCP 장비 통신, 도어 제어, 실시간 이벤트 수신(WebSocket) |
| `cctv-monitor` | 30003 | CCTV 모니터링 — 그리드 배치, 드래그앤드롭, PTZ 제어 |
| `common` | — | 공통 상수, DTO, FMS Entity |

## 프론트엔드 패키지

| 패키지 | 포트 | 설명 |
|--------|------|------|
| `front/packages/dstr` | 3001 | 재난상황관제 대시보드 |
| `front/packages/hams` | 3002 | 무인국사 대시보드 |
| `front/packages/cctv-monitor` | 3003 | CCTV 모니터링 |

---

## 기술 스택

**Backend**
- Java 17 / Spring Boot 3.2
- Spring Data JPA (PostgreSQL, MariaDB)
- Netty (TCP 서버)
- WebSocket / STOMP
- Gradle 멀티모듈

**Frontend**
- React 18 / Vite / TypeScript
- Zustand (클라이언트 상태)
- TanStack Query (서버 상태)
- react-leaflet, recharts, react-select, react-dnd

---

## 환경 설정

각 모듈의 `src/main/resources/application.yml`에서 환경변수를 참고하여 `application-local.yml`을 생성하세요.

```yaml
# application-local.yml 예시 (dstr)
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/your_db
    username: your_username
    password: your_password
```
