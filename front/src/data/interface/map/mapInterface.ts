

export interface MapTypeProps {
    type : string;
    mapNm : string;
    mapTypeNm : string;
    tileNm? : string;
    map? : string;
}


export interface DaumAddrInfo {
    category : string;
    highlighted : Array<Array<number>>;
    item : string;
    key : string;
    score : number;
}