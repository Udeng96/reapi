package com.eseict.apiServer.cache

/**
 * hams 로그인 여부를 판단하는 Info sigleTon으로 전역적으로 하나의 객체에만 접근하여
 * 로그인 상태에 대해서 whrite 하게 함
 */
class HamsLoginInfo private constructor() {

    enum class VALUE {
        ON, OFF
    }

    private var isLogin: VALUE = VALUE.OFF

    companion object {

        @Volatile
        private var instance: HamsLoginInfo = HamsLoginInfo()

        @JvmStatic
        fun getInstance(): HamsLoginInfo =
            instance ?: synchronized(this) {
                instance ?: HamsLoginInfo().also {
                    instance = it
                }
            }
    }

    fun updateLoginState(state: VALUE) {
        this.isLogin = state
    }

    fun getLoginState() = this.isLogin

}