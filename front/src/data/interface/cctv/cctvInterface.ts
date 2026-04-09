export interface SearchOption {
    type?: string | null;
    cctvNm?: string | null;
    page: number | null;
    rows: number | null;
}

export type CctvInfo = {
    facId: string;
    facNm: string;
    addr: string;
    lat: number;
    lng: number;
    clfyCd: string;
    clfyNm: string;
    useYn: number;
    updDtm: string | null;
    data: any | null;
    mgrNo : string;
}