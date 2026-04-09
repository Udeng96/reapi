package com.eseict.apiServer.util

import org.locationtech.proj4j.CRSFactory
import org.locationtech.proj4j.CoordinateTransformFactory
import org.locationtech.proj4j.ProjCoordinate

class CoordinateTransformer {
    private val crsFactory = CRSFactory()
    private val wgs84 = crsFactory.createFromName("epsg:4326")
    private val epsg5186 = crsFactory.createFromName("epsg:5186")
    private val ctFactory = CoordinateTransformFactory()
    private val epsg5186ToWgs84 = ctFactory.createTransform(epsg5186, wgs84)

    companion object {
        @Volatile
        private var instance: CoordinateTransformer? = null

        @JvmStatic
        fun getInstance(): CoordinateTransformer {
            return instance ?: synchronized(this) {
                instance ?: CoordinateTransformer().also { transformer ->
                    instance = transformer
                }
            }
        }
    }

    fun transformFrom5186ToWgs84(x: Double, y: Double): List<Double> {
        val result = ProjCoordinate()
        epsg5186ToWgs84.transform(ProjCoordinate(x, y), result)
        return listOf(result.x, result.y)
    }
}