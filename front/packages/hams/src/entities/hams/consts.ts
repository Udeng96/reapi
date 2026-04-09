export const DOOR_LOCK_TYPE = {
  NONE: '1',
  UNLOCK: '2',
  LOCK: '3',
  NORECOG: '4',
  ERROR: '5',
} as const;

export const DOOR_STATE_TYPE = {
  NONE: '1',
  OPEN: '2',
  CLOSE: '3',
} as const;

export const STATUS_COLOR = {
  tRed: '#f06969',
  tSky: '#3acef0',
  tMint: '#42e3c5',
  tPurple: '#ba3ce8',
  tGray: '#777987',
} as const;

export const REALTIME_BOX_TYPES = [
  {
    typeId: 'doorLock',
    typeNm: '앞문 도어락 상태',
  },
  {
    typeId: 'doorState',
    typeNm: '앞문 개폐 상태',
  },
  {
    typeId: 'connection',
    typeNm: '연결 상태',
  },
] as const;
