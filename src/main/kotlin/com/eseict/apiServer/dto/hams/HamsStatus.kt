package com.eseict.apiServer.dto.hams

import java.math.BigDecimal

/**
 *
 */
data class HamsStatus(

    var frontDoor: String? = null,
    var frontDoorLock: String? = null,
    var backDoor: String? = null,
    var backDoorLock: String? = null,

    var temp: BigDecimal = BigDecimal.ZERO,
    var humi: BigDecimal = BigDecimal.ZERO,//습도

    var isConnect: Boolean = false
) {

}