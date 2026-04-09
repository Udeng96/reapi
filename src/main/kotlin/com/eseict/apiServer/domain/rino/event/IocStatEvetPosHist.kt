package com.eseict.apiServer.domain.rino.event

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "ioc_stat_evet_pos_hist", schema = ApiConstants.DB_SCHEMA.IOC)
@IdClass(IocStatEvetPosHistPK::class)
class IocStatEvetPosHist {

    @Id
    @Basic
    @Column(name = "stat_evet_outb_seqn", nullable = false, insertable = false, updatable = false)
    var statEvetOutbSeqn: String? = null

    @Id
    @Basic
    @Column(name = "site_cd", nullable = false, insertable = false, updatable = false)
    var siteCd: String? = null

    @Basic
    @Column(name = "seq", nullable = false)
    var seq: Int? = null

    @Id
    @Basic
    @Column(name = "client_cd", nullable = false, insertable = false, updatable = false)
    var clientCd: String? = null

    @Basic
    @Column(name = "x_crdnt", nullable = true)
    var XCrdnt: String? = null

    @Basic
    @Column(name = "y_crdnt", nullable = true)
    var YCrdnt: String? = null

    @Basic
    @Column(name = "z_crdnt", nullable = true)
    var ZCrdnt: String? = null

    //YJ 주석
    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "site_cd", referencedColumnName = "site_cd")
//    @JoinColumn(name = "client_cd", referencedColumnName = "client_cd")
//    @JoinColumn(name = "stat_evet_outb_seqn", referencedColumnName = "stat_evet_outb_seqn")
    var iocStatEvetOutbHist: IocStatEvetOutbHist? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "statEvetOutbSeqn = $statEvetOutbSeqn " +
                "siteCd = $siteCd " +
                "seq = $seq " +
                "clientCd = $clientCd " +
                "XCrdnt = $XCrdnt " +
                "YCrdnt = $YCrdnt " +
                "ZCrdnt = $ZCrdnt " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as IocStatEvetPosHist

        if (statEvetOutbSeqn != other.statEvetOutbSeqn) return false
        if (siteCd != other.siteCd) return false
        if (seq != other.seq) return false
        if (clientCd != other.clientCd) return false
        if (XCrdnt != other.XCrdnt) return false
        if (YCrdnt != other.YCrdnt) return false
        if (ZCrdnt != other.ZCrdnt) return false

        return true
    }

}

class IocStatEvetPosHistPK : java.io.Serializable {

    @Id
    @Column(name = "site_cd", nullable = false, insertable = false, updatable = false)
    var siteCd: String? = null

    @Id
    @Column(name = "stat_evet_outb_seqn", nullable = false, insertable = false, updatable = false)
    var statEvetOutbSeqn: String? = null

    @Id
    @Column(name = "client_cd", nullable = false, insertable = false, updatable = false)
    var clientCd: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as IocStatEvetPosHistPK

        if (siteCd != other.siteCd) return false
        if (statEvetOutbSeqn != other.statEvetOutbSeqn) return false
        if (clientCd != other.clientCd) return false

        return true
    }

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

}
