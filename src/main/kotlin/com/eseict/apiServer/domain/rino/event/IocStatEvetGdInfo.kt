package com.eseict.apiServer.domain.rino.event

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "ioc_stat_evet_gd_info", schema = ApiConstants.DB_SCHEMA.IOC)
@IdClass(IocStatEvetGdInfoEntityPK::class)
class IocStatEvetGdInfo {
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
    @Column(name = "stat_evet_gd_cd", nullable = false, insertable = false, updatable = false)
    var statEvetGdCd: String? = null

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
    @Column(name = "stat_evet_gd_desc", nullable = true)
    var statEvetGdDesc: String? = null

    @Basic
    @Column(name = "stat_evet_gd_nm", nullable = true)
    var statEvetGdNm: String? = null

    @Basic
    @Column(name = "upd_dtm", nullable = true)
    var updDtm: String? = null

    @Basic
    @Column(name = "upd_mn", nullable = true)
    var updMn: String? = null

    @Basic
    @Column(name = "notify_platform", nullable = false)
    var notifyPlatform: String? = null

    @Basic
    @Column(name = "notify_client", nullable = false)
    var notifyClient: String? = null

    @Basic
    @Column(name = "notify_site", nullable = false)
    var notifySite: String? = null

    @Basic
    @Column(name = "notify_user", nullable = false)
    var notifyUser: String? = null


//    @OneToMany(mappedBy = "iocStatEvetGdInfo")
//    var iocStatEvetOutbHists: List<IocStatEvetOutbHist>? = null

    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "znCd = $znCd " +
                "unitSvcCd = $unitSvcCd " +
                "svcThemeCd = $svcThemeCd " +
                "statEvetGdCd = $statEvetGdCd " +
                "statEvetCd = $statEvetCd " +
                "siteCd = $siteCd " +
                "evetGbCd = $evetGbCd " +
                "clientCd = $clientCd " +
                "creDtm = $creDtm " +
                "creMn = $creMn " +
                "statEvetGdDesc = $statEvetGdDesc " +
                "statEvetGdNm = $statEvetGdNm " +
                "updDtm = $updDtm " +
                "updMn = $updMn " +
                "notifyPlatform = $notifyPlatform " +
                "notifyClient = $notifyClient " +
                "notifySite = $notifySite " +
                "notifyUser = $notifyUser " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as IocStatEvetGdInfo

        if (znCd != other.znCd) return false
        if (unitSvcCd != other.unitSvcCd) return false
        if (svcThemeCd != other.svcThemeCd) return false
        if (statEvetGdCd != other.statEvetGdCd) return false
        if (statEvetCd != other.statEvetCd) return false
        if (siteCd != other.siteCd) return false
        if (evetGbCd != other.evetGbCd) return false
        if (clientCd != other.clientCd) return false
        if (creDtm != other.creDtm) return false
        if (creMn != other.creMn) return false
        if (statEvetGdDesc != other.statEvetGdDesc) return false
        if (statEvetGdNm != other.statEvetGdNm) return false
        if (updDtm != other.updDtm) return false
        if (updMn != other.updMn) return false
        if (notifyPlatform != other.notifyPlatform) return false
        if (notifyClient != other.notifyClient) return false
        if (notifySite != other.notifySite) return false
        if (notifyUser != other.notifyUser) return false

        return true
    }

}

class IocStatEvetGdInfoEntityPK : java.io.Serializable {
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
    @Column(name = "stat_evet_gd_cd", nullable = false, insertable = false, updatable = false)
    var statEvetGdCd: String? = null

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
        other as IocStatEvetGdInfoEntityPK

        if (znCd != other.znCd) return false
        if (unitSvcCd != other.unitSvcCd) return false
        if (svcThemeCd != other.svcThemeCd) return false
        if (statEvetGdCd != other.statEvetGdCd) return false
        if (statEvetCd != other.statEvetCd) return false
        if (siteCd != other.siteCd) return false
        if (evetGbCd != other.evetGbCd) return false
        if (clientCd != other.clientCd) return false

        return true
    }

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

}
