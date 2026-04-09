package com.eseict.apiServer.util

import com.eseict.common.base.SystemProperties

class ProxyUtil(val iProxy: String) {

    private val isDev = SystemProperties.getProperty("IS_DEV").toBoolean()

    fun getUrlWithProxy(url: String): String {

        if (!isDev) {
            return iProxy.plus(Regex("""https?://""").replace(url, "/"))
        } else {
            return url
        }
    }
}