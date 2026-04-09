package com.eseict.apiServer.util.geoJson

import com.google.gson.JsonArray
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon

fun searchZoneName(x: Double, y: Double): GeoResponse? {
    val result = GeoResponse()
    try {
        val geo = GeoJsonCache.getInstance()

        val regex = "\"".toRegex()

        var coordArray = JsonArray()
        val keyset: Set<*>? = geo.getKeySet()
        val iterator = keyset?.iterator()
        while (iterator!!.hasNext()) {
            // key -> 지역명
            val key = iterator.next().toString()
            coordArray = geo.getCoordinates(key)!! //지역 multipolygon
            if (coordArray == null) {
                return null
            }

            val gf = GeometryFactory()
            for (i in 0 until coordArray.size()) {
                val subPolyArray = coordArray[i].asJsonArray
                val polygonArray = arrayOfNulls<Polygon>(subPolyArray.size())
                for (j in 0 until subPolyArray.size()) { // sub polygon
                    val polyArray = subPolyArray[j].asJsonArray
                    val size = polyArray.size()
                    val points = arrayOfNulls<Coordinate>(size)
                    for (k in 0 until size) {
                        points[k] = Coordinate(
                            polyArray[k].asJsonArray[0].asDouble,
                            polyArray[k].asJsonArray[1].asDouble
                        )
                    }
                    val polygon = gf.createPolygon(points)
                    polygonArray[j] = polygon
                }
                val mp = gf.createMultiPolygon(polygonArray)
                val coord = Coordinate(x, y)
                val pt = gf.createPoint(coord)
                val check = mp.contains(pt)



                if (check) {
                    result.znCd = (
                            GeoJsonCache.getInstance().getProperties(key)?.get("admCd").toString().replace(regex, "")
                            )
                    result.znNm = (key)
                    return result
                } else {
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return result
}