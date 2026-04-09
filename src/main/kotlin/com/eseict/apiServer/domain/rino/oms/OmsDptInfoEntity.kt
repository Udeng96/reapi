package com.eseict.apiServer.domain.rino.oms

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "oms_dpt_info", schema = ApiConstants.DB_SCHEMA.OMS)
class OmsDptInfoEntity {
    @Id
    @Column(name = "dpt_id", nullable = false)
    var dptId: String? = null

    @Basic
    @Column(name = "dpt_cd", nullable = true)
    var dptCd: String? = null

    @Basic
    @Column(name = "dpt_desc", nullable = true)
    var dptDesc: String? = null

    @Basic
    @Column(name = "dpt_nm", nullable = true)
    var dptNm: String? = null

    @Basic
    @Column(name = "orgn_id", nullable = true)
    var orgnId: String? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "dptId = $dptId " +
                "dptCd = $dptCd " +
                "dptDesc = $dptDesc " +
                "dptNm = $dptNm " +
                "orgnId = $orgnId " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as OmsDptInfoEntity

        if (dptId != other.dptId) return false
        if (dptCd != other.dptCd) return false
        if (dptDesc != other.dptDesc) return false
        if (dptNm != other.dptNm) return false
        if (orgnId != other.orgnId) return false

        return true
    }

}

