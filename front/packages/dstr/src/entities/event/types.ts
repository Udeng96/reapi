// ─── 이벤트 ───────────────────────────────────────────────────────────────────

export interface VwEvent {
  addr: string | null;
  clsDtm: string | null;
  cntn: string | null;
  eventId: string | null;
  eventNm: string | null;
  lat: string | null;
  lng: string | null;
  locationCd: string | null;
  locationNm: string | null;
  outbDtm: string | null;
  statEvetOutbSeqn: string;
  usvcOutbId: string;
}

export interface EventResult {
  data: VwEvent[] | null;
  total: number | null;
}

export interface EventsPayload {
  eventIdList: string[] | null;
  locationCdList: string[] | null;
  startDtm: string | null;
  endDtm: string | null;
  page: number | null;
  rows: number | null;
}

// ─── 검색 공통 ────────────────────────────────────────────────────────────────

export interface Search {
  id: string;
  nm: string;
}

// ─── 히트맵 ───────────────────────────────────────────────────────────────────

export interface HeatmapClfy {
  id: string;
  nm: string;
  svcThemeCd: string;
  unitCd: string;
}

export interface HeatmapCount {
  znCd: string;
  znNm: string;
  cnt: number;
}

// ─── 상황전파 ─────────────────────────────────────────────────────────────────

export interface DstrInfo {
  uSvcOutbId: string;
  statEvetNm: string | null;
  outbDtm: string | null;
  clsDtm: string | null;
  znNm: string | null;
}

// ─── 소방차 출동 ──────────────────────────────────────────────────────────────

export interface FireCarInfo {
  orcIdx: string;
  orcNo: string;
  sndYmdHms: string;
  userId: string;
  truckNo: string;
  lon: string;
  lat: string;
  truckLcYmdHms: string;
  truckPsitn: string;
  truckTy: string;
  callNm_telNo: string;
  routeId: string;
  videoUrl: string;
}
