package com.eseict.apiServer.dto.event

import java.io.Serializable

class EventBody {
    var StatEvet: StatusEvent

    constructor(
        uSvcOutbId: String, statEvetId: String, statEvetNm: String,
        statEvetGdCd: String, procSt: String, x: String, y: String,
        outbPosNm: String, statEvetCntn: String, statEvetOutbDtm: String,
        statEvetItem: Array<StatEvetItem>?, statEvetClrDtm: String? = null
    ) {
        val outbPos = OutbPos(x, y, "0")
        val statEvetItemCnt = statEvetItem?.size ?: 0
        StatEvet = StatusEvent(
            uSvcOutbId, statEvetId, statEvetNm, statEvetGdCd, procSt,
            1, outbPosNm, arrayOf(outbPos), statEvetCntn, statEvetOutbDtm, 0, statEvetItemCnt,
            statEvetItem, statEvetClrDtm
        )
    }

    class StatusEvent(
        var uSvcOutbId: String,
        var statEvetId: String,
        var statEvetNm: String,
        var statEvetGdCd: String,
        var procSt: String,
        var outbPosCnt: Int = 0,
        var outbPosNm: String,
        var outbPos: Array<OutbPos>,
        var statEvetCntn: String,
        var statEvetOutbDtm: String,
        var cpxRelEvetOutbSeqnCnt: Int,
        var statEvetItemCnt: Int = 0,
        var statEvetItem: Array<StatEvetItem>? = arrayOf(),
        var statEvetClrDtm: String? = null,
        var outbScopRads: String? = null,
        var outbMainGb: String? = null,
        var statEvetActnMn: String? = null,
        var statEvetActnDtm: String? = null,
        var statEvetActnCntn: String? = null,
        var statEvetActnRslt: String? = null,
        var cpxRelEvetOutbSeqn: Array<Int>? = arrayOf(),
    ) : Serializable

    class OutbPos(var x: String, var y: String, var z: String) : Serializable

    class StatEvetItem(val key: String, val value: String) : Serializable
}