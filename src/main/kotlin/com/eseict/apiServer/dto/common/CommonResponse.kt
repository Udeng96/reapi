package com.eseict.apiServer.dto.common

class CommonResponse {

    var resultCode: String? = null
    var resultMessage: String? = null

    constructor(resultCode: String) {
        this.resultCode = resultCode
        this.resultMessage = getResultMessage(resultCode)
    }

    constructor(resultCode: String?, resultMessage: String?) {
        this.resultCode = resultCode
        this.resultMessage = resultMessage
    }


    private fun getResultMessage(resultCode: String): String? {
        var message = ""
        message = if ("SUCCESS".equals(resultCode, ignoreCase = true) || "true" == resultCode) {
            "성공"
        } else if ("FAIL".equals(resultCode, ignoreCase = true)) {
            "실패"
        } else {
            "실패"
        }
        return message
    }

}