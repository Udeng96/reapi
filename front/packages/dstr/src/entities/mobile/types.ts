export interface MobileInfo {
  id: string | null;
  seq: number;
  userId: string | null;
  userNm: string | null;
  userDept: string | null;
  userDtm: string | null;
  lat: number | null;
  lng: number | null;
  mbChk: number;
  mbUrl: string | null;
}

export interface MobilePaging<T> {
  total: number;
  data: T[];
}

export interface FullPopupInfo {
  isOpen: boolean;
  dtm: string | null;
  userDept: string | null;
  userNm: string | null;
  id: string;
  rtsp: string | null;
}

export interface MobilePopupSize {
  height: number;
  width: number;
}

export interface MobileSearchPayload {
  startDtm: string;
  endDtm: string;
  rows: number;
  page: number;
}
