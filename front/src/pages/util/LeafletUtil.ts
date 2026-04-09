import * as L from "leaflet";
import {LatLng} from "leaflet";

export const getClosestClusterMarker = (marker:any, zoomLevel: number): LatLng => {
    let parent = marker.__parent
    while (parent._zoom > zoomLevel) {
        parent = parent.__parent
    }
    return zoomLevel === parent._zoom ?
        new L.LatLng(parent._latlng.lat, parent._latlng.lng) :
        new L.LatLng(marker._latlng.lat, marker._latlng.lng)
}