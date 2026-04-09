package com.eseict.apiServer.domain.rino.fms

import javax.persistence.*

@Entity
@Table(name = "erf_fac_grup_mapp", schema = "rino_fms", catalog = "uctc2")
class ErfFacGrupMapp {

    @Id
    @Basic
    @Column(name = "fac_grup_mapp_id", nullable = false)
    var facGrupMappId: String? = null

    @Basic
    @Column(name = "hi_fac_id", nullable = false)
    var hiFacId: String? = null

    @Basic
    @Column(name = "lo_fac_id", nullable = false)
    var loFacId: String? = null

    @ManyToOne
    @JoinColumn(name = "lo_fac_id", referencedColumnName = "fac_id", insertable = false, updatable = false)
    private var facInfo: ErfFacInfo? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "facGrupMappId = $facGrupMappId " +
                "hiFacId = $hiFacId " +
                "loFacId = $loFacId " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ErfFacGrupMapp

        if (facGrupMappId != other.facGrupMappId) return false
        if (hiFacId != other.hiFacId) return false
        if (loFacId != other.loFacId) return false

        return true
    }

}

