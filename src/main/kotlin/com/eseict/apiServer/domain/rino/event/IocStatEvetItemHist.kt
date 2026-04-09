package com.eseict.apiServer.domain.rino.event

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "ioc_stat_evet_item_hist", schema = ApiConstants.DB_SCHEMA.IOC)
@IdClass(IocStatEvetItemHistPK::class)
class IocStatEvetItemHist : java.io.Serializable {

    //    @Id
    @Basic
    @Column(name = "stat_evet_outb_seqn", nullable = false, insertable = false, updatable = false)
    var statEvetOutbSeqn: String? = null

    @Basic
    @Column(name = "seq", nullable = false)
    var seq: Int? = null

    @Id
    @Basic
    @Column(name = "item_nm", nullable = true)
    var itemNm: String? = null

    @Basic
    @Column(name = "item_val", nullable = true)
    var itemVal: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
        JoinColumn(name = "stat_evet_outb_seqn", referencedColumnName = "stat_evet_outb_seqn"),

        )
    var iocStatEvetOutbHist: IocStatEvetOutbHist? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "statEvetOutbSeqn = $statEvetOutbSeqn " +
                "seq = $seq " +
                "itemNm = $itemNm " +
                "itemVal = $itemVal " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as IocStatEvetItemHist

        if (statEvetOutbSeqn != other.statEvetOutbSeqn) return false
        if (seq != other.seq) return false
        if (itemNm != other.itemNm) return false
        if (itemVal != other.itemVal) return false

        return true
    }

}

class IocStatEvetItemHistPK : java.io.Serializable {
    @Id
    @Column(name = "stat_evet_outb_seqn", nullable = false, insertable = false, updatable = false)
    var statEvetOutbSeqn: String? = null

    @Id
    @Column(name = "item_nm", nullable = true)
    var itemNm: String? = null
}

