package com.eseict.apiServer.dto.event

interface DisasterHist : EventHist {
    val organName: String
    val organComment: String
    val eventName: String
    val eventComment: String
}