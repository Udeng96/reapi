package com.eseict.apiServer.util.geoJson

import com.google.common.collect.Maps
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.slf4j.LoggerFactory

class GeoJsonCache private constructor() {

    private final val logger = LoggerFactory.getLogger(GeoJsonCache::class.java)

    companion object {
        @Volatile
        private var instance: GeoJsonCache? = null

        @JvmStatic
        fun getInstance(): GeoJsonCache {
            return instance ?: synchronized(this) {
                instance ?: GeoJsonCache().also { _geoJsonCache ->
                    instance = _geoJsonCache
                }
            }
        }
    }

    /*api 시설물 데이터용*/
    private val coordMap = Maps.newHashMap<String, JsonArray>()
    private val propMap = Maps.newHashMap<String, JsonObject>()


    @Throws(NullPointerException::class)
    fun setUpCoordData(key: String?, jsonArray: JsonArray?) {
        coordMap.put(key, jsonArray)
    }

    @Throws(NullPointerException::class)
    fun setUpPropData(key: String?, jsonObject: JsonObject?) {
        propMap[key] = jsonObject
    }

    @Throws(NullPointerException::class)
    fun setUpEwData(key: String?, jsonObject: JsonObject?) {
        propMap[key] = jsonObject
    }


    fun getCoordinates(key: String?): JsonArray? {
        return coordMap[key]
    }

    fun getProperties(key: String?): JsonObject? {
        return propMap[key]
    }

    fun getPropMap(): Map<String?, JsonObject?>? {
        return propMap
    }

    fun getKeySet(): Set<*>? {
        return coordMap.keys
    }


}