package com.eseict.apiServer.domain.rino.event

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "ioc_stat_evet_item_info", schema = ApiConstants.DB_SCHEMA.IOC)
@IdClass(IocStatEvetItemInfoEntityPK::class)
class IocStatEvetItemInfo {
    @Id
    @Column(name = "zn_cd", nullable = false, insertable = false, updatable = false)
    var znCd: String? = null

    @Id
    @Column(name = "unit_svc_cd", nullable = false, insertable = false, updatable = false)
    var unitSvcCd: String? = null

    @Id
    @Column(name = "svc_theme_cd", nullable = false, insertable = false, updatable = false)
    var svcThemeCd: String? = null

    @Id
    @Column(name = "stat_evet_item_id", nullable = false)
    var statEvetItemId: String? = null

    @Id
    @Column(name = "stat_evet_cd", nullable = false, insertable = false, updatable = false)
    var statEvetCd: String? = null

    @Id
    @Column(name = "site_cd", nullable = false, insertable = false, updatable = false)
    var siteCd: String? = null

    @Id
    @Column(name = "evet_gb_cd", nullable = false, insertable = false, updatable = false)
    var evetGbCd: String? = null

    @Id
    @Column(name = "client_cd", nullable = false, insertable = false, updatable = false)
    var clientCd: String? = null

    @Basic
    @Column(name = "cre_dtm", nullable = true)
    var creDtm: String? = null

    @Basic
    @Column(name = "cre_mn", nullable = true)
    var creMn: String? = null

    @Basic
    @Column(name = "item_desc", nullable = true)
    var itemDesc: String? = null

    @Basic
    @Column(name = "item_key", nullable = true)
    var itemKey: String? = null

    @Basic
    @Column(name = "item_nm", nullable = true)
    var itemNm: String? = null

    @Basic
    @Column(name = "upd_dtm", nullable = true)
    var updDtm: String? = null

    @Basic
    @Column(name = "upd_mn", nullable = true)
    var updMn: String? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "znCd = $znCd " +
                "unitSvcCd = $unitSvcCd " +
                "svcThemeCd = $svcThemeCd " +
                "statEvetItemId = $statEvetItemId " +
                "statEvetCd = $statEvetCd " +
                "siteCd = $siteCd " +
                "evetGbCd = $evetGbCd " +
                "clientCd = $clientCd " +
                "creDtm = $creDtm " +
                "creMn = $creMn " +
                "itemDesc = $itemDesc " +
                "itemKey = $itemKey " +
                "itemNm = $itemNm " +
                "updDtm = $updDtm " +
                "updMn = $updMn " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as IocStatEvetItemInfo

        if (znCd != other.znCd) return false
        if (unitSvcCd != other.unitSvcCd) return false
        if (svcThemeCd != other.svcThemeCd) return false
        if (statEvetItemId != other.statEvetItemId) return false
        if (statEvetCd != other.statEvetCd) return false
        if (siteCd != other.siteCd) return false
        if (evetGbCd != other.evetGbCd) return false
        if (clientCd != other.clientCd) return false
        if (creDtm != other.creDtm) return false
        if (creMn != other.creMn) return false
        if (itemDesc != other.itemDesc) return false
        if (itemKey != other.itemKey) return false
        if (itemNm != other.itemNm) return false
        if (updDtm != other.updDtm) return false
        if (updMn != other.updMn) return false

        return true
    }

}

class IocStatEvetItemInfoEntityPK : java.io.Serializable {
    @Id
    @Column(name = "zn_cd", nullable = false, insertable = false, updatable = false)
    var znCd: String? = null

    @Id
    @Column(name = "unit_svc_cd", nullable = false, insertable = false, updatable = false)
    var unitSvcCd: String? = null

    @Id
    @Column(name = "svc_theme_cd", nullable = false, insertable = false, updatable = false)
    var svcThemeCd: String? = null

    @Id
    @Column(name = "stat_evet_item_id", nullable = false)
    var statEvetItemId: String? = null

    @Id
    @Column(name = "stat_evet_cd", nullable = false, insertable = false, updatable = false)
    var statEvetCd: String? = null

    @Id
    @Column(name = "site_cd", nullable = false, insertable = false, updatable = false)
    var siteCd: String? = null

    @Id
    @Column(name = "evet_gb_cd", nullable = false, insertable = false, updatable = false)
    var evetGbCd: String? = null

    @Id
    @Column(name = "client_cd", nullable = false, insertable = false, updatable = false)
    var clientCd: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as IocStatEvetItemInfoEntityPK

        if (znCd != other.znCd) return false
        if (unitSvcCd != other.unitSvcCd) return false
        if (svcThemeCd != other.svcThemeCd) return false
        if (statEvetItemId != other.statEvetItemId) return false
        if (statEvetCd != other.statEvetCd) return false
        if (siteCd != other.siteCd) return false
        if (evetGbCd != other.evetGbCd) return false
        if (clientCd != other.clientCd) return false

        return true
    }

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

}
