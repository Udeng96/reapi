package com.eseict.apiServer.domain.rino.oms

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "oms_orgn_info", schema = ApiConstants.DB_SCHEMA.OMS)
class OmsOrgnInfoEntity {
    @Id
    @Column(name = "orgn_id", nullable = false)
    var orgnId: String? = null

    @Basic
    @Column(name = "grup_id", nullable = true)
    var grupId: String? = null

    @Basic
    @Column(name = "orgn_cd", nullable = true)
    var orgnCd: String? = null

    @Basic
    @Column(name = "orgn_desc", nullable = true)
    var orgnDesc: String? = null

    @Basic
    @Column(name = "orgn_nm", nullable = true)
    var orgnNm: String? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "orgnId = $orgnId " +
                "grupId = $grupId " +
                "orgnCd = $orgnCd " +
                "orgnDesc = $orgnDesc " +
                "orgnNm = $orgnNm " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as OmsOrgnInfoEntity

        if (orgnId != other.orgnId) return false
        if (grupId != other.grupId) return false
        if (orgnCd != other.orgnCd) return false
        if (orgnDesc != other.orgnDesc) return false
        if (orgnNm != other.orgnNm) return false

        return true
    }

}

