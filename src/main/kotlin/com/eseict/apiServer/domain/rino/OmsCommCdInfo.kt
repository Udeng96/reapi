package com.eseict.apiServer.domain.rino

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "oms_comm_cd_info", schema = ApiConstants.DB_SCHEMA.OMS)
class OmsCommCdInfo {
    @Id
    @Column(name = "cd_id", nullable = false)
    var cdId: String? = null

    @Basic
    @Column(name = "cd", nullable = true)
    var cd: String? = null

    @Basic
    @Column(name = "cd_desc", nullable = true)
    var cdDesc: String? = null

    @Basic
    @Column(name = "cd_nm", nullable = true)
    var cdNm: String? = null

    @Basic
    @Column(name = "cre_dtm", nullable = true)
    var creDtm: String? = null

    @Basic
    @Column(name = "cre_nm", nullable = true)
    var creNm: String? = null

    @Basic
    @Column(name = "grup_cd", nullable = true)
    var grupCd: String? = null

    @Basic
    @Column(name = "grup_yn", nullable = true)
    var grupYn: String? = null

    @Basic
    @Column(name = "sort", nullable = true)
    var sort: Int? = null

    @Basic
    @Column(name = "upd_dtm", nullable = true)
    var updDtm: String? = null

    @Basic
    @Column(name = "upd_nm", nullable = true)
    var updNm: String? = null

    @Basic
    @Column(name = "use_yn", nullable = true)
    var useYn: String? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "cdId = $cdId " +
                "cd = $cd " +
                "cdDesc = $cdDesc " +
                "cdNm = $cdNm " +
                "creDtm = $creDtm " +
                "creNm = $creNm " +
                "grupCd = $grupCd " +
                "grupYn = $grupYn " +
                "sort = $sort " +
                "updDtm = $updDtm " +
                "updNm = $updNm " +
                "useYn = $useYn " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as OmsCommCdInfo

        if (cdId != other.cdId) return false
        if (cd != other.cd) return false
        if (cdDesc != other.cdDesc) return false
        if (cdNm != other.cdNm) return false
        if (creDtm != other.creDtm) return false
        if (creNm != other.creNm) return false
        if (grupCd != other.grupCd) return false
        if (grupYn != other.grupYn) return false
        if (sort != other.sort) return false
        if (updDtm != other.updDtm) return false
        if (updNm != other.updNm) return false
        if (useYn != other.useYn) return false

        return true
    }

}

