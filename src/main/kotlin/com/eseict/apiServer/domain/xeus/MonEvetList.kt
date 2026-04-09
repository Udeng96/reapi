package com.eseict.apiServer.domain.xeus

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "mon_evet_list", schema = ApiConstants.DB_SCHEMA.XEUS)
class MonEvetList {
    @Id
    @Column(name = "usvc_outb_id", nullable = false)
    var usvcOutbId: String? = null

    @Basic
    @Column(name = "evt_svc_nm", nullable = true)
    var evtSvcNm: String? = null

    @Basic
    @Column(name = "evt_typ", nullable = true)
    var evtTyp: String? = null

    @Basic
    @Column(name = "evt_typ_cd", nullable = true)
    var evtTypCd: String? = null

    @Basic
    @Column(name = "msg_typ_cd", nullable = true)
    var msgTypCd: String? = null

    @Basic
    @Column(name = "evt_nm", nullable = true)
    var evtNm: String? = null

    @Basic
    @Column(name = "evt_cntn", nullable = true)
    var evtCntn: String? = null

    @Basic
    @Column(name = "outb_pos_nm", nullable = true)
    var outbPosNm: String? = null

    @Basic
    @Column(name = "outb_posx", nullable = true)
    var outbPosx: java.math.BigDecimal? = null

    @Basic
    @Column(name = "outb_posy", nullable = true)
    var outbPosy: java.math.BigDecimal? = null

    @Basic
    @Column(name = "evt_outb_dtm", nullable = true)
    var evtOutbDtm: String? = null

    @Basic
    @Column(name = "evt_clr_dtm", nullable = true)
    var evtClrDtm: String? = null

    @Basic
    @Column(name = "evt_proc_cd", nullable = true)
    var evtProcCd: String? = null

    @Basic
    @Column(name = "evt_actn_dtm", nullable = true)
    var evtActnDtm: String? = null

    @Basic
    @Column(name = "evt_actn_usrid", nullable = true)
    var evtActnUsrid: String? = null

    @Basic
    @Column(name = "evt_actn_cntn", nullable = true)
    var evtActnCntn: String? = null

    @Basic
    @Column(name = "test_yn", nullable = true)
    var testYn: String? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "usvcOutbId = $usvcOutbId " +
                "evtSvcNm = $evtSvcNm " +
                "evtTyp = $evtTyp " +
                "evtTypCd = $evtTypCd " +
                "msgTypCd = $msgTypCd " +
                "evtNm = $evtNm " +
                "evtCntn = $evtCntn " +
                "outbPosNm = $outbPosNm " +
                "outbPosx = $outbPosx " +
                "outbPosy = $outbPosy " +
                "evtOutbDtm = $evtOutbDtm " +
                "evtClrDtm = $evtClrDtm " +
                "evtProcCd = $evtProcCd " +
                "evtActnDtm = $evtActnDtm " +
                "evtActnUsrid = $evtActnUsrid " +
                "evtActnCntn = $evtActnCntn " +
                "testYn = $testYn " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MonEvetList

        if (usvcOutbId != other.usvcOutbId) return false
        if (evtSvcNm != other.evtSvcNm) return false
        if (evtTyp != other.evtTyp) return false
        if (evtTypCd != other.evtTypCd) return false
        if (msgTypCd != other.msgTypCd) return false
        if (evtNm != other.evtNm) return false
        if (evtCntn != other.evtCntn) return false
        if (outbPosNm != other.outbPosNm) return false
        if (outbPosx != other.outbPosx) return false
        if (outbPosy != other.outbPosy) return false
        if (evtOutbDtm != other.evtOutbDtm) return false
        if (evtClrDtm != other.evtClrDtm) return false
        if (evtProcCd != other.evtProcCd) return false
        if (evtActnDtm != other.evtActnDtm) return false
        if (evtActnUsrid != other.evtActnUsrid) return false
        if (evtActnCntn != other.evtActnCntn) return false
        if (testYn != other.testYn) return false

        return true
    }

}

