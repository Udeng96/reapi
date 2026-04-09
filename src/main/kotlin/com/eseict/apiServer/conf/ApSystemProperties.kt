package com.eseict.apiServer.conf

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "system")
class ApSystemProperties(val values: MutableMap<String, String>) {

    fun getValue(key: String): String? {

        return try {
            values[key] as String
        } catch (e: NullPointerException) {
            null
        }
    }


}