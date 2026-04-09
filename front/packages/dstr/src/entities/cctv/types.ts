export interface CctvInfo {
  facId: string;
  facNm: string;
  addrShort: string;
  lat: number | null;
  lng: number | null;
  facClfyId: string;
  facClfyNm: string;
  mgtNo: string;
  ptzYn: boolean;
}

export interface CctvClfy {
  id: string;
  nm: string;
}

export interface CctvListResult {
  total: number;
  data: CctvInfo[] | null;
}

export interface CctvSearchOption {
  type?: string | null;
  cctvNm?: string | null;
  page: number;
  rows: number;
}
