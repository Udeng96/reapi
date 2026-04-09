package com.eseict.apiServer.util

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser

object JsonParserUtil {

    /**
     * JSON 문자열에서 path를 따라 내려가 JsonArray를 찾아서
     * 해당 배열을 T 객체 리스트로 변환한다.
     *
     * 예: ["response", "result", "items"]
     *
     * @param jsonString 전체 JSON 문자열
     * @param path JsonObject 내부 경로
     * @param clazz 변환할 클래스 타입
     */
    fun <T> getJsonArray(
        jsonString: String,
        path: Array<String>,
        clazz: Class<T>
    ): List<T> {
        if (jsonString.isBlank()) return emptyList()

        val root: JsonElement = JsonParser.parseString(jsonString)
        var current: JsonElement = root

        // path 따라 내려가기
        for (p in path) {
            if (current.isJsonObject && current.asJsonObject.has(p)) {
                current = current.asJsonObject.get(p)
            } else {
                return emptyList()
            }
        }

        // JsonArray인지 검사
        if (!current.isJsonArray) return emptyList()

        val gson = Gson()
        return current.asJsonArray.map { element ->
            gson.fromJson(element, clazz)
        }
    }
}
