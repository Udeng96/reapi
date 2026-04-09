export interface CctvInfo {
  facId: string;
  facNm: string;
  addr: string;
  lat: number;
  lng: number;
  clfyCd: string;
  clfyNm: string;
  useYn: number;
  mgrNo: string;
  ptzYn?: boolean;
}

export interface MenuListResult {
  clfyNm: string;
  clfyId: string;
  cctvCnt: number;
  cctvList: CctvInfo[];
}

export interface CctvControlParam {
  cctvId: string;
  action: string;
  code: string;
}

export interface TransCycleOption {
  label: string;
  value: string;
}

export const PageTransType = {
  NONE: 0,
  PLAY: 1,
  STOP: 2,
  EACH: 3,
} as const;
export type PageTransType = (typeof PageTransType)[keyof typeof PageTransType];

export const PtzControlCode = {
  Up: 'Up',
  Down: 'Down',
  Left: 'Left',
  Right: 'Right',
  ZoomIn: 'ZoomIn',
  ZoomOut: 'ZoomOut',
} as const;
export type PtzControlCode = (typeof PtzControlCode)[keyof typeof PtzControlCode];

export const PtzAction = {
  START: 'START',
  STOP: 'STOP',
} as const;
export type PtzAction = (typeof PtzAction)[keyof typeof PtzAction];

export const TransCycleOptions: TransCycleOption[] = [
  { value: '10000', label: '10초마다' },
  { value: '60000', label: '1분마다' },
  { value: '300000', label: '5분마다' },
  { value: '600000', label: '10분마다' },
  { value: '1800000', label: '30분마다' },
];
