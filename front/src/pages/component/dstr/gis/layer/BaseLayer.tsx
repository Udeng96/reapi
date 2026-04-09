import {TileLayer} from "react-leaflet";
import {useSelector} from "react-redux";

import {useEffect, useState} from "react";
import {RootState} from "../../../../store/rootStore";
import {TileMapData} from "../../../../../data/config/Map";
import {TileLayer as LeafletTileLayer} from "leaflet";

const BaseMapLayer = () => {
    const mapType = useSelector((state: RootState) => state.view.map.mapType)
    const mapTile = useSelector((state: RootState) => state.view.map.mapTile)

    const [tiles, setTiles] = useState<JSX.Element[]|null>(null)

    useEffect(() => {

        const tileLayers = TileMapData[mapType].tiles[mapTile].urls.map((url, index) => {
            return <TileLayer key={`${mapType}_${mapTile}_${index}`} {...TileMapData[mapType].options} url={url}/>
        })


        setTiles(tileLayers)
    }, [mapType, mapTile])

    return <>{tiles}</>
}

export default BaseMapLayer