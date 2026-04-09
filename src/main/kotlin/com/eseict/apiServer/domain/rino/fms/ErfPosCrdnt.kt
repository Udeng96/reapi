package com.eseict.apiServer.domain.rino.fms

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "erf_pos_crdnt", schema = ApiConstants.DB_SCHEMA.FMS)
class ErfPosCrdnt {
    @Id
    @Column(name = "pos_crdnt_id", nullable = false)
    var posCrdntId: String? = null

    @Basic
    @Column(name = "fac_id", nullable = false)
    var facId: String? = null

    @Basic
    @Column(name = "pos_grup_id", nullable = false)
    var posGrupId: String? = null

    @Basic
    @Column(name = "sort_ord", nullable = false)
    var sortOrd: String? = null

    @Basic
    @Column(name = "use_yn", nullable = false)
    var useYn: Int? = null

    @Basic
    @Column(name = "x_crdnt", nullable = false)
    var XCrdnt: String? = null

    @Basic
    @Column(name = "y_crdnt", nullable = false)
    var YCrdnt: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fac_id", referencedColumnName = "fac_id", insertable = false, updatable = false)
    var refErfFacInfo: ErfFacInfo? = null

    constructor(
        posCrdntId: String?,
        facId: String?,
        posGrupId: String?,
        sortOrd: String?,
        useYn: Int?,
        XCrdnt: String?,
        YCrdnt: String?
    ) {
        this.posCrdntId = posCrdntId
        this.facId = facId
        this.posGrupId = posGrupId
        this.sortOrd = sortOrd
        this.useYn = useYn
        this.XCrdnt = XCrdnt
        this.YCrdnt = YCrdnt
    }


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "posCrdntId = $posCrdntId " +
                "facId = $facId " +
                "posGrupId = $posGrupId " +
                "sortOrd = $sortOrd " +
                "useYn = $useYn " +
                "XCrdnt = $XCrdnt " +
                "YCrdnt = $YCrdnt " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ErfPosCrdnt

        if (posCrdntId != other.posCrdntId) return false
        if (facId != other.facId) return false
        if (posGrupId != other.posGrupId) return false
        if (sortOrd != other.sortOrd) return false
        if (useYn != other.useYn) return false
        if (XCrdnt != other.XCrdnt) return false
        if (YCrdnt != other.YCrdnt) return false

        return true
    }

}

