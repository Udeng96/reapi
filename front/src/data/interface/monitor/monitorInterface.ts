export interface TreeProps {
    id: number;
    parent: number;
    droppable: boolean;
    text: string;
}

export interface TransCycleOptionType {
    label: string;
    value: string;
};

// export interface MenuNodeParent {
//     id:string,
//     parent:string|number,
//     droppable:boolean,
//     text : string
//     data:Array<CctvInfo>
// }

export interface MenuNodeParent {
    id:string,
    parent:string|number,
    droppable:boolean,
    text : string
    data: ParentDataType,
}

export interface ParentDataType {
    allData : Array<CctvInfo>,
    nowData : Array<CctvInfo>,
}

export interface MenuNodeChild {
    id:string,
    parent:string|number,
    text:string
    data : CctvInfo
}

export interface MenuListResultType {
    clfyNm : string;
    clfyId : string;
    cctvCnt : number;
    cctvList : CctvInfo[];
}

export interface CctvInfo{
    facId:string;
    facNm:string;
    addr:string;
    lat:number;
    lng:number;
    clfyCd:string;
    clfyNm: string;
    useYn : number;
    data: any|null;
    mgrNo:string;
    ptzYn? : boolean;
}

export interface MenuListParam{
    gridAmount : number,
    clfyId : string|null;
}

export interface CctvControlParam{
    cctvId : string,
    action : string,
    code :string
}