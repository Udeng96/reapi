package com.eseict.apiServer.dto.event

interface EventHist : EventInfo {

    var accidentId: String

    //추가되어야하는 값을
    var eventGdCd: String
    var uid: String
}