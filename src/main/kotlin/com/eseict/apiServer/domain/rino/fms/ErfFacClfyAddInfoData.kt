package com.eseict.apiServer.domain.rino.fms

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "erf_fac_clfy_add_info_data", schema = ApiConstants.DB_SCHEMA.FMS)
@IdClass(ErfFacClfyAddInfoDataPk::class)
class ErfFacClfyAddInfoData : java.io.Serializable {

    @Id
    @Column(name = "fac_id", nullable = false)
    var facId: String? = null

    @Id
    @Column(name = "add_info_id", nullable = false)
    var addInfoId: String? = null


    @Column(name = "add_info_data", nullable = true)
    var addInfoData: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fac_id", referencedColumnName = "fac_id", insertable = false, updatable = false)
    var refErfFacInfo: ErfFacInfo? = null

    constructor(facId: String?, addInfoId: String?, addInfoData: String?) {
        this.facId = facId
        this.addInfoId = addInfoId
        this.addInfoData = addInfoData
    }

    constructor(facId: String?, addInfoId: String?, addInfoData: String?, refErfFacInfo: ErfFacInfo?) {
        this.facId = facId
        this.addInfoId = addInfoId
        this.addInfoData = addInfoData
        this.refErfFacInfo = refErfFacInfo
    }


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "facId = $facId " +
                "addInfoId = $addInfoId " +
                "addInfoData = $addInfoData " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ErfFacClfyAddInfoData

        if (facId != other.facId) return false
        if (addInfoId != other.addInfoId) return false
        if (addInfoData != other.addInfoData) return false

        return true
    }

}


class ErfFacClfyAddInfoDataPk : java.io.Serializable {
    @Id
    @Column(name = "fac_id", nullable = false, insertable = false, updatable = false)
    var facId: String? = null

    @Id
    @Column(name = "add_info_id", nullable = false, insertable = false, updatable = false)
    var addInfoId: String? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ErfFacClfyAddInfoDataPk

        if (facId != other.facId) return false
        if (addInfoId != other.addInfoId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = facId?.hashCode() ?: 0
        result = 31 * result + (addInfoId?.hashCode() ?: 0)
        return result
    }


}
