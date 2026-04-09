import {useMap} from "react-leaflet";


export const ResizeMap = ()=>{

    const map = useMap()
    map.invalidateSize();
    return null;
}