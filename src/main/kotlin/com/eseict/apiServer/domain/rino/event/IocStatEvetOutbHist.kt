package com.eseict.apiServer.domain.rino.event

import com.eseict.apiServer.conf.ApiConstants
import org.hibernate.annotations.Formula
import javax.persistence.*

@Entity
@Table(name = "ioc_stat_evet_outb_hist", schema = ApiConstants.DB_SCHEMA.IOC)
@IdClass(IocStatEvetOutbHistEntityPK::class)
class IocStatEvetOutbHist {
    @Id
    @Column(name = "stat_evet_outb_seqn", nullable = false, insertable = false, updatable = false)
    var statEvetOutbSeqn: String? = null

    @Basic
    @Column(name = "clr_dtm", nullable = true)
    var clrDtm: String? = null

    @Basic
    @Column(name = "cls_dtm", nullable = true)
    var clsDtm: String? = null

    @Basic
    @Column(name = "cls_typ", nullable = true)
    var clsTyp: String? = null

    @Basic
    @Column(name = "evet_gb_cd", nullable = false, insertable = false, updatable = false)
    var evetGbCd: String? = null

    @Basic
    @Column(name = "outb_dtm", nullable = true)
    var outbDtm: String? = null

    @Basic
    @Column(name = "outb_main_gb", nullable = true)
    var outbMainGb: String? = null

    @Basic
    @Column(name = "outb_plac", nullable = true)
    var outbPlac: String? = null

    @Basic
    @Column(name = "outb_scop_rads", nullable = true)
    var outbScopRads: String? = null

    @Basic
    @Column(name = "proc_st", nullable = true)
    var procSt: String? = null

    @Basic
    @Column(name = "stat_evet_cd", nullable = false, insertable = false, updatable = false)
    var statEvetCd: String? = null

    @Basic
    @Column(name = "stat_evet_cntn", nullable = true)
    var statEvetCntn: String? = null

    @Basic
    @Column(name = "stat_evet_gd_cd", nullable = false, insertable = false, updatable = false)
    var statEvetGdCd: String? = null

    @Basic
    @Column(name = "svc_theme_cd", nullable = false, insertable = false, updatable = false)
    var svcThemeCd: String? = null

    @Basic
    @Column(name = "u_svc_outb_id", nullable = true)
    var uSvcOutbId: String? = null

    @Basic
    @Column(name = "unit_svc_cd", nullable = false, insertable = false, updatable = false)
    var unitSvcCd: String? = null

    @Basic
    @Column(name = "zn_cd", nullable = false, insertable = false, updatable = false)
    var znCd: String? = null


    @OneToMany(mappedBy = "iocStatEvetOutbHist")
    var iocStatEvetItemHists: List<IocStatEvetItemHist>? = null


    @Formula("( SELECT P.X_CRDNT FROM ioc.IOC_STAT_EVET_POS_HIST P WHERE P.STAT_EVET_OUTB_SEQN = STAT_EVET_OUTB_SEQN )")
    var x: String? = null

    @Formula("( SELECT P.Y_CRDNT FROM ioc.IOC_STAT_EVET_POS_HIST P WHERE P.STAT_EVET_OUTB_SEQN = STAT_EVET_OUTB_SEQN )")
    var y: String? = null

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumns(
//
//        JoinColumn(name = "zn_cd", referencedColumnName = "zn_cd"),
//        JoinColumn(name = "unit_svc_cd", referencedColumnName = "unit_svc_cd"),
//        JoinColumn(name = "svc_theme_cd", referencedColumnName = "svc_theme_cd"),
//        JoinColumn(name = "stat_evet_gd_cd", referencedColumnName = "stat_evet_gd_cd"),
//        JoinColumn(name = "stat_evet_cd", referencedColumnName = "stat_evet_cd"),
//        JoinColumn(name = "site_cd", referencedColumnName = "site_cd", updatable = false, insertable = false),
//        JoinColumn(name = "evet_gb_cd", referencedColumnName = "evet_gb_cd"),
//        JoinColumn(name = "client_cd", referencedColumnName = "client_cd", updatable = false, insertable = false)
//    )
//    var iocStatEvetGdInfo: IocStatEvetGdInfo? = null


    @OneToMany(mappedBy = "iocStatEvetOutbHist")
    var iocStatEvetPosHists: List<IocStatEvetPosHist>? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "statEvetOutbSeqn = $statEvetOutbSeqn " +
                "clrDtm = $clrDtm " +
                "clsDtm = $clsDtm " +
                "clsTyp = $clsTyp " +
                "evetGbCd = $evetGbCd " +
                "outbDtm = $outbDtm " +
                "outbMainGb = $outbMainGb " +
                "outbPlac = $outbPlac " +
                "outbScopRads = $outbScopRads " +
                "procSt = $procSt " +
                "statEvetCd = $statEvetCd " +
                "statEvetCntn = $statEvetCntn " +
                "statEvetGdCd = $statEvetGdCd " +
                "svcThemeCd = $svcThemeCd " +
                "USvcOutbId = $uSvcOutbId " +
                "unitSvcCd = $unitSvcCd " +
                "znCd = $znCd " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as IocStatEvetOutbHist

        if (statEvetOutbSeqn != other.statEvetOutbSeqn) return false
        if (clrDtm != other.clrDtm) return false
        if (clsDtm != other.clsDtm) return false
        if (clsTyp != other.clsTyp) return false
        if (evetGbCd != other.evetGbCd) return false
        if (outbDtm != other.outbDtm) return false
        if (outbMainGb != other.outbMainGb) return false
        if (outbPlac != other.outbPlac) return false
        if (outbScopRads != other.outbScopRads) return false
        if (procSt != other.procSt) return false
        if (statEvetCd != other.statEvetCd) return false
        if (statEvetCntn != other.statEvetCntn) return false
        if (statEvetGdCd != other.statEvetGdCd) return false
        if (svcThemeCd != other.svcThemeCd) return false
        if (uSvcOutbId != other.uSvcOutbId) return false
        if (unitSvcCd != other.unitSvcCd) return false
        if (znCd != other.znCd) return false

        return true
    }

}

class IocStatEvetOutbHistEntityPK : java.io.Serializable {
    @Id
    @Column(name = "stat_evet_outb_seqn", nullable = false, insertable = false, updatable = false)
    var statEvetOutbSeqn: String? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as IocStatEvetOutbHistEntityPK

        if (statEvetOutbSeqn != other.statEvetOutbSeqn) return false

        return true
    }

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

}
