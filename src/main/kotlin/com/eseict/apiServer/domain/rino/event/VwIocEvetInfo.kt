package com.eseict.apiServer.domain.rino.event

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "vw_ioc_evet_info", schema = ApiConstants.DB_SCHEMA.IOC)
class VwIocEvetInfo {
    @Basic
    @Column(name = "msg_typ_cd", nullable = true)
    var msgTypCd: String? = null

    @Basic
    @Column(name = "zn_cd", nullable = true)
    var znCd: String? = null

    @Basic
    @Column(name = "svc_theme_cd", nullable = true)
    var svcThemeCd: String? = null

    @Basic
    @Column(name = "unit_svc_cd", nullable = true)
    var unitSvcCd: String? = null

    @Basic
    @Column(name = "evet_cd", nullable = true)
    var evetCd: String? = null

    @Id
    @Basic
    @Column(name = "evet_id", nullable = true)
    var evetId: String? = null

    @Basic
    @Column(name = "evet_nm", nullable = true)
    var evetNm: String? = null

    @Basic
    @Column(name = "zn_nm", nullable = true)
    var znNm: String? = null

    @Basic
    @Column(name = "svc_theme_nm", nullable = true)
    var svcThemeNm: String? = null

    @Basic
    @Column(name = "unit_svc_nm", nullable = true)
    var unitSvcNm: String? = null

    constructor(svcThemeCd: String?, svcThemeNm: String?, unitSvcCd: String?, unitSvcNm: String?) {
        this.svcThemeCd = svcThemeCd
        this.unitSvcCd = unitSvcCd
        this.svcThemeNm = svcThemeNm
        this.unitSvcNm = unitSvcNm
    }


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "msgTypCd = $msgTypCd " +
                "znCd = $znCd " +
                "svcThemeCd = $svcThemeCd " +
                "unitSvcCd = $unitSvcCd " +
                "evetCd = $evetCd " +
                "evetId = $evetId " +
                "evetNm = $evetNm " +
                "znNm = $znNm " +
                "svcThemeNm = $svcThemeNm " +
                "unitSvcNm = $unitSvcNm " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as VwIocEvetInfo

        if (msgTypCd != other.msgTypCd) return false
        if (znCd != other.znCd) return false
        if (svcThemeCd != other.svcThemeCd) return false
        if (unitSvcCd != other.unitSvcCd) return false
        if (evetCd != other.evetCd) return false
        if (evetId != other.evetId) return false
        if (evetNm != other.evetNm) return false
        if (znNm != other.znNm) return false
        if (svcThemeNm != other.svcThemeNm) return false
        if (unitSvcNm != other.unitSvcNm) return false

        return true
    }


}

