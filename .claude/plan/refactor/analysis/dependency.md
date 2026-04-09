# 의존 관계 분석

- 분석 일시: 2026-03-30
- 기준: 코드베이스 내 분포 기반, 상위 20% = "높음"
- 백엔드: 213개 파일 / 16,092 라인 (Kotlin)
- 프론트엔드: 198개 파일 / 15,415 라인 (TypeScript)

---

## Fan-out 높은 파일 (내부 import 수 상위 20%)

| 파일 | import 수 | 영향 도메인 |
|------|-----------|------------|
| `service/hams/HamsService.kt` | 21 | hams, rino, dto |
| `service/mobile/XeusLiveViewEventService.kt` | 16 | mobile, xeus, event |
| `service/rino/event/EventService.kt` | 11 | event, dto, domain |
| `controller/cctv/CctvController.kt` | 11 | cctv, service, dto |
| `ServletInitializer.kt` | 10 | conf, proc, scheduler (앱 전체) |
| `controller/mobile/XeusEventRcvController.kt` | 9 | mobile, xeus |
| `service/rino/fms/FacClfyService.kt` | 8 | fms, dto |
| `controller/event/EventController.kt` | 8 | event, service, dto |

### 프론트엔드 Fan-out 상위

| 파일 | import 수 | 영향 도메인 |
|------|-----------|------------|
| `index.tsx` | 52 | 전역 (CSS import 포함) |
| `dstr/gis/gisArea.tsx` | 34 | dstr, gis, store |
| `dstr/player/PlayerBox.tsx` | 18 | dstr, player |
| `saga/dstr/event/WebSocketEventSaga.ts` | 16 | dstr, websocket, store |

---

## Fan-in 높은 파일 (다른 파일에서 참조 횟수 상위 20%)

| 파일 | 참조 수 | 영향 도메인 |
|------|---------|------------|
| `conf/ApiConstants.kt` | 73 | **전체** (상수 허브) |
| `dto/fac/Stat.kt` | 61 | fms, controller, service |
| `domain/rino/event/Event.kt` | 36 | event 전체 |
| `dto/common/Message.kt` | 35 | controller 전체 (응답 래퍼) |
| `dto/openapi/Item.kt` | 22 | 외부 API 연동 |
| `dto/fac/FacClfy.kt` | 20 | fms |
| `dto/oms/UserInfo.kt` | 18 | 인증, controller |
| `dto/common/ClfyInfo.kt` | 12 | fms, event |
| `dto/hams/HamsCmd.kt` | 10 | hams |
| `dto/GisFacInfo.kt` | 10 | fms, gis |
| `domain/rino/fms/ErfFacClfy.kt` | 10 | fms |

---

## 도메인 간 선행/후행 관계

```
                    conf (ApiConstants, DataSource)
                    |
            +-------+-------+
            |               |
            v               v
         common           domain
      (Message,Stat)    (Event,ErfFac*)
            |               |
            v               v
    +-------+-------+-------+-------+
    |       |       |       |       |
    v       v       v       v       v
  event    fms    hams    cctv   mobile
    |       |       |       |       |
    |       |       |    xeus/eas   |
    |       |       |       |       |
    +---+---+---+---+---+---+---+---+
        |               |
        v               v
    controller       proc(socket)
        |               |
        +-------+-------+
                |
                v
          ServletInitializer
```

### 의존 방향 요약

- `conf` → 모든 도메인이 의존 (DataSource 라우팅, 상수)
- `common/dto` → controller/service 전체가 의존 (Message, Stat 등)
- `domain` → service/repository가 의존
- `proc(socket)` → event, hams 도메인에 의존
- `ServletInitializer` → proc, scheduler를 직접 기동 (Fan-out 높음, Phase 후반)
