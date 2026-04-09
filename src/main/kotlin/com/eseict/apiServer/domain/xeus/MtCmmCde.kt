package com.eseict.apiServer.domain.xeus

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "mt_cmm_cde", schema = ApiConstants.DB_SCHEMA.XEUS)
@IdClass(MtCmmCdeEntityPK::class)
class MtCmmCde(
    @Id
    @Column(name = "grp_cde", nullable = false)
    var grpCde: String? = null, //코드그룹코드

    @Basic
    @Column(name = "crp_nm", nullable = true)
    var crpNm: String? = null,  //코드그룹명

    @Id
    @Column(name = "cde_cde", nullable = false)
    var cdeKde: String? = null, //코드

    @Basic
    @Column(name = "cde_nm", nullable = true)
    var cdeNm: String? = null   //코드명
) {

    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "grpCde = $grpCde " +
                "crpNm = $crpNm " +
                "cdeKde = $cdeKde " +
                "cdeNm = $cdeNm " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MtCmmCde

        if (grpCde != other.grpCde) return false
        if (crpNm != other.crpNm) return false
        if (cdeKde != other.cdeKde) return false
        if (cdeNm != other.cdeNm) return false

        return true
    }

}

class MtCmmCdeEntityPK : java.io.Serializable {
    @Id
    @Column(name = "grp_cde", nullable = false)
    var grpCde: String? = null

    @Id
    @Column(name = "cde_cde", nullable = false)
    var cdeKde: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MtCmmCdeEntityPK

        if (grpCde != other.grpCde) return false
        if (cdeKde != other.cdeKde) return false

        return true
    }

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

}
