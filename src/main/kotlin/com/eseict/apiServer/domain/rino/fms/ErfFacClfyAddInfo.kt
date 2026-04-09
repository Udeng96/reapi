package com.eseict.apiServer.domain.rino.fms

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "erf_fac_clfy_add_info", schema = ApiConstants.DB_SCHEMA.FMS)
@IdClass(ErfFacClfyAddInfoPk::class)
class ErfFacClfyAddInfo(
    @Id
    @Basic
    @Column(name = "add_info_id", nullable = false)
    var addInfoId: String? = null,

    @Basic
    @Column(name = "add_info_item_desc", nullable = false)
    var addInfoItemDesc: String? = null,

    @Basic
    @Column(name = "add_info_item_nm", nullable = false)
    var addInfoItemNm: String? = null,

    @Basic
    @Column(name = "add_info_item_ord", nullable = false)
    var addInfoItemOrd: Long? = null,

    @Id
    @Basic
    @Column(name = "fac_clfy_id", nullable = false)
    var facClfyId: String? = null,

    @Basic
    @Column(name = "use_yn", nullable = false)
    var useYn: String? = null
) {


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "addInfoId = $addInfoId " +
                "addInfoItemDesc = $addInfoItemDesc " +
                "addInfoItemNm = $addInfoItemNm " +
                "addInfoItemOrd = $addInfoItemOrd " +
                "facClfyId = $facClfyId " +
                "useYn = $useYn " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ErfFacClfyAddInfo

        if (addInfoId != other.addInfoId) return false
        if (addInfoItemDesc != other.addInfoItemDesc) return false
        if (addInfoItemNm != other.addInfoItemNm) return false
        if (addInfoItemOrd != other.addInfoItemOrd) return false
        if (facClfyId != other.facClfyId) return false
        if (useYn != other.useYn) return false

        return true
    }

}


class ErfFacClfyAddInfoPk(
    @Id
    @Column(name = "add_info_id", nullable = false, insertable = false, updatable = false)
    var addInfoId: String? = null,

    @Id
    @Column(name = "fac_clfy_id", nullable = false, insertable = false, updatable = false)
    var facClfyId: String? = null
) : java.io.Serializable {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ErfFacClfyAddInfoPk

        if (addInfoId != other.addInfoId) return false
        if (facClfyId != other.facClfyId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = addInfoId?.hashCode() ?: 0
        result = 31 * result + (facClfyId?.hashCode() ?: 0)
        return result
    }


}
