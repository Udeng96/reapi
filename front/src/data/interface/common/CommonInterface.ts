export interface ClfyInterface {
    id: string,
    nm: string
}

export interface Paging<T> {
    total: number;
    data: Array<T> | null
}

export interface SearchDefaultOption {
    startDtm: string,
    endDtm: string,
    page: number,
    rows: number
}

export interface CommonResponse {
    resultCode : string
    resultMessage : string
}