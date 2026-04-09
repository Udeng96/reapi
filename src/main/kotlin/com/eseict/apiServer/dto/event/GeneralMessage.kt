package com.eseict.apiServer.dto.event

import java.io.Serializable

class GeneralMessage(
    var headerTyp: ByteArray = byteArrayOf(),
    var header: ByteArray = byteArrayOf(),
    var body: ByteArray? = byteArrayOf(),
    var bodyLen: Int = 0,
) : Serializable {
    companion object {
        private const val serialVersionUID = 2657355430074131014L
    }

}