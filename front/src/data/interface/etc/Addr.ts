export interface AddrInfo{
    id : string,
    title:string
    address : Address,
    point : Point
}


interface Address {
    zipcode:string,
    road : string,
    parcel : string,
    category:string,
    bldnm:string,
}

interface Point {
    x:string,
    y:string
}