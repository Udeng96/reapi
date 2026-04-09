export interface HamsGisFacInfo {
  facId: string | null;
  facNm: string | null;
  addr: string | null;
  lat: number | null;
  lng: number | null;
  clfyCd: string | null;
  clfyNm: string | null;
  useYn: number | null;
  updDtm: number | null;
  data: string | null;
  hamsId: string | null;
  groupNm: string | null;
  ip: string | null;
  port: string | null;
  lowFacInfos: LowFacInfos[] | null;
  status: HamsFacStatus | null;
}

export interface HamsCctvInfo {
  cctvId: string;
  cctvName: string;
  hamsName: string;
  mgrNo: string;
}

export interface HamsFacStatus {
  frontDoor: string;
  frontDoorLock: string;
  backDoor: string;
  backDoorLock: string;
  temp: number;
  humi: number;
  isConnect: boolean;
}

export interface LowFacInfos {
  facId: string | null;
  facNm: string | null;
  addr: string | null;
  lat: number | null;
  lng: number | null;
  clfyCd: string | null;
  clfyNm: string | null;
  useYn: number | null;
  updDtm: string | null;
  data: string | null;
  mgrNm: string | null;
}

export interface HamsDoorStat {
  date: string;
  cnt: number;
  openCnt: number;
  clsCnt: number;
  noneCnt: number;
}

export interface StatOptions {
  doorType: string;
  frontBack: string;
  dateType: string;
  startDtm: string | null;
}

export interface LockParam {
  hamsId: string;
  body: HamsGisFacInfo | null;
}

export interface LockResult {
  code: string;
  message: string;
  data: HamsGisFacInfo;
}

export type WsDoeInfo = {
  DOOR_LOCK: string;
  EQ_ID: string;
  EVENT_DOOR_TYPE: string;
  EVENT_TYPE: string;
  FRONT_DOOR_LOCK_S: string;
  FRONT_DOOR_S: string;
  ORDERER_IP: string;
  ORDERER_TYPE: string;
  RCV_DATE: string;
  USER_ID: string;
};

export type DateType = 'D' | 'M' | 'Y';
