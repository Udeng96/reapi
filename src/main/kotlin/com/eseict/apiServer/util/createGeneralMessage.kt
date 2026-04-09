package com.eseict.apiServer.util

import com.eseict.apiServer.dto.event.EventBody
import com.eseict.apiServer.dto.event.GeneralMessage
import com.eseict.common.core.unique.IdGenerator
import com.google.gson.Gson


fun createGeneralMessage(
    uSvcOutbId: String,
    statEvetId: String,
    statEvetNm: String,
    statEvetGdCd: String,
    procSt: String,
    x: String,
    y: String,
    outbPosNm: String,
    statEvetCntn: String,
    statEvetOutbDtm: String,
    statEvetItemMap: Array<EventBody.StatEvetItem>,
    statEvetClrDtm: String,

    ): GeneralMessage {
    val eventBody = EventBody(
        uSvcOutbId, statEvetId, statEvetNm,
        statEvetGdCd, procSt, x, y, outbPosNm, statEvetCntn, statEvetOutbDtm,
        statEvetItemMap, statEvetClrDtm
    )
    val gson = Gson()
    val body = gson.toJson(eventBody).encodeToByteArray()
    val header = HeaderUtil.makeHeader(
        "A1",
        "API", "001", "1", IdGenerator.getUUID64(), body.size
    )
    return GeneralMessage(
        "A1".encodeToByteArray(), header!!, body, body.size
    )
}