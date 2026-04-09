package com.eseict.apiServer.domain.eas

import com.eseict.apiServer.conf.ApiConstants
import com.google.gson.Gson
import javax.persistence.*

@Entity
@Table(name = "tbl_xeus_live_view_event", schema = ApiConstants.DB_SCHEMA.EAS)
class XeusEventObj {
    @Column(name = "stat_evet_type_cd")
    var statEvetTypCd //이벤트 타입코드
            : String? = null

    @Column(name = "stat_msg_type_cd")
    var statMsgTypCd: String? = null

    @Column(name = "outb_pos_name")
    var outbPosNm: String? = null

    @Column(name = "stat_evet_name")
    var statEvetNm: String? = null

    @Column(name = "stat_evet_clr_dtm")
    var statEvetClrDtm: String? = null

    @Column(name = "stat_evet_cntn")
    var statEvetCntn: String? = null

    @Column(name = "stat_evet_type")
    var statEvetType: String? = null

    @Transient
    var outbPos: List<Map<String, String>>? = null

    @Column(name = "loc_x")
    var locX: Float? = null

    @Column(name = "loc_y")
    var locY: Float? = null

    @Column(name = "stat_evet_outb_dtm")
    var statEvetOutbDtm: String? = null

    @Column(name = "stat_evet_actn_cntn")
    var statEvetActnCntn: String? = null

    @Column(name = "proc_st")
    var procSt //  (10 발생 | 40 정보변경 | 50 해제 | 90 취소 | 91 종료)
            : String? = null

    @Column(name = "is_test")
    var isTest: String? = null

    @Id
    @Column(name = "u_svc_outb_id")
    var uSvcOutbId: String? = null

    @Column(name = "state_evet_actn_mn")
    var statEvetActnMn: String? = null

    @Column(name = "stat_evet_actn_dtm")
    var statEvetActnDtm: String? = null

    @Column(name = "stat_evet_svc_type")
    var statEvetSvcTyp: String? = null

    @Column(name = "tm_x")
    var tmx: String? = null

    @Column(name = "tm_y")
    var tmy: String? = null

    @Column(name = "target_group")
    var targetGrp: String? = null

    @Column(name = "target_id")
    var targetId: String? = null

    @Transient
    var etcCntn: Map<String, String>? = null

    @Column(name = "cell_phone_number")
    var phoneNumber: String? = null

    @Column(name = "is_not_external")
    var isNotExternal: String? = null

    @Transient
    var rtspUrl: String? = null

    @Transient
    var userName: String? = null

    constructor(dataMap: Map<String, Any>) {
        statEvetTypCd = dataMap["statEvetTypCd"] as String?
        statMsgTypCd = dataMap["statMsgTypCd"] as String?
        outbPosNm = dataMap["outbPosNm"] as String?
        statEvetNm = dataMap["statEvetNm"] as String?
        statEvetClrDtm = dataMap["statEvetClrDtm"] as String?
        statEvetCntn = dataMap["statEvetCntn"] as String?
        statEvetType = dataMap["statEvetType"] as String?
        outbPos = dataMap["outbPos"] as List<Map<String, String>>?
        locX = outbPos!![0]["x"]!!.toFloat()
        locY = outbPos!![0]["y"]!!.toFloat()
        statEvetOutbDtm = dataMap["statEvetOutbDtm"] as String?
        statEvetActnCntn = dataMap["statEvetActnCntn"] as String?
        procSt = dataMap["procSt"] as String?
        isTest = dataMap["isTest"] as String?
        uSvcOutbId = dataMap["uSvcOutbId"] as String?
        statEvetActnMn = dataMap["statEvetActnMn"] as String?
        statEvetActnDtm = dataMap["statEvetActnDtm"] as String?
        statEvetSvcTyp = dataMap["statEvetSvcTyp"] as String?
        tmx = dataMap["tmx"] as String?
        tmy = dataMap["tmy"] as String?
        targetGrp = dataMap["targetGrp"] as String?
        targetId = dataMap["targetId"] as String?
        val gson = Gson()
        etcCntn = gson.fromJson<Map<String, String>>(dataMap["etcCntn"].toString(), MutableMap::class.java)
        phoneNumber = etcCntn?.get("cctvId")!!.replace("-".toRegex(), "")
        isNotExternal = dataMap["isNotExternal"] as String?
    }

    constructor() {}

    constructor(
        statEvetTypCd: String?,
        statMsgTypCd: String?,
        outbPosNm: String?,
        statEvetNm: String?,
        statEvetClrDtm: String?,
        statEvetCntn: String?,
        statEvetType: String?,
        outbPos: List<Map<String, String>>?,
        locX: Float?,
        locY: Float?,
        statEvetOutbDtm: String?,
        statEvetActnCntn: String?,
        procSt: String?,
        isTest: String?,
        uSvcOutbId: String?,
        statEvetActnMn: String?,
        statEvetActnDtm: String?,
        statEvetSvcTyp: String?,
        tmx: String?,
        tmy: String?,
        targetGrp: String?,
        targetId: String?,
        etcCntn: Map<String, String>?,
        phoneNumber: String?,
        isNotExternal: String?
    ) {
        this.statEvetTypCd = statEvetTypCd
        this.statMsgTypCd = statMsgTypCd
        this.outbPosNm = outbPosNm
        this.statEvetNm = statEvetNm
        this.statEvetClrDtm = statEvetClrDtm
        this.statEvetCntn = statEvetCntn
        this.statEvetType = statEvetType
        this.outbPos = outbPos
        this.locX = locX
        this.locY = locY
        this.statEvetOutbDtm = statEvetOutbDtm
        this.statEvetActnCntn = statEvetActnCntn
        this.procSt = procSt
        this.isTest = isTest
        this.uSvcOutbId = uSvcOutbId
        this.statEvetActnMn = statEvetActnMn
        this.statEvetActnDtm = statEvetActnDtm
        this.statEvetSvcTyp = statEvetSvcTyp
        this.tmx = tmx
        this.tmy = tmy
        this.targetGrp = targetGrp
        this.targetId = targetId
        this.etcCntn = etcCntn
        this.phoneNumber = phoneNumber
        this.isNotExternal = isNotExternal
    }


    override fun toString(): String {
        return "XeusEventObj{" +
                "statEvetTypCd='" + statEvetTypCd + '\'' +
                ", statMsgTypCd='" + statMsgTypCd + '\'' +
                ", outbPosNm='" + outbPosNm + '\'' +
                ", statEvetNm='" + statEvetNm + '\'' +
                ", statEvetClrDtm='" + statEvetClrDtm + '\'' +
                ", statEvetCntn='" + statEvetCntn + '\'' +
                ", statEvetType='" + statEvetType + '\'' +
                ", outbPos=" + outbPos +
                ", locX=" + locX +
                ", locY=" + locY +
                ", statEvetOutbDtm='" + statEvetOutbDtm + '\'' +
                ", statEvetActnCntn='" + statEvetActnCntn + '\'' +
                ", procSt='" + procSt + '\'' +
                ", isTest='" + isTest + '\'' +
                ", uSvcOutbId='" + uSvcOutbId + '\'' +
                ", statEvetActnMn='" + statEvetActnMn + '\'' +
                ", statEvetActnDtm='" + statEvetActnDtm + '\'' +
                ", statEvetSvcTyp='" + statEvetSvcTyp + '\'' +
                ", tmx='" + tmx + '\'' +
                ", tmy='" + tmy + '\'' +
                ", targetGrp='" + targetGrp + '\'' +
                ", targetId='" + targetId + '\'' +
                ", etcCntn=" + etcCntn +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isNotExternal='" + isNotExternal + '\'' +
                '}'
    }

}

