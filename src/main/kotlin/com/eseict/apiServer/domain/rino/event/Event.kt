package com.eseict.apiServer.domain.rino.event

import com.eseict.apiServer.conf.ApiConstants
import org.hibernate.annotations.Formula
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "vw_event", schema = ApiConstants.DB_SCHEMA.IOC)
data class Event(
    @Id
    @Column(name = "STAT_EVET_OUTB_SEQN")
    var id: String? = null,
    @Column(name = "EVET_GB_CD")
    var evetGbCd: String? = null,
    @Column(name = "OUTB_DTM")
    var outbDtm: String? = null,
    @Column(name = "CLS_DTM")
    var clsDtm: String? = null,
    @Column(name = "OUTB_MAIN_GB")
    var outbMainGb: String? = null,
    @Column(name = "OUTB_PLAC")
    var outbPlac: String? = null,
    @Column(name = "PROC_ST")
    var procSt: String? = null,
    @Column(name = "STAT_EVET_CD")
    var statEvetCd: String? = null,
    @Column(name = "STAT_EVET_CNTN")
    var statEvetCntn: String? = null,
    @Column(name = "STAT_EVET_GD_CD")
    var statEvetGdCd: String? = null,
    @Column(name = "SVC_THEME_CD")
    var svcThemeCd: String? = null,
    @Column(name = "U_SVC_OUTB_ID")
    var uSvcOutbId: String? = null,
    @Column(name = "UNIT_SVC_CD")
    var unitSvcCd: String? = null,
    @Column(name = "ZN_CD")
    var zoneId: String? = null,
    @Column(name = "X_CRDNT")
    var x: String? = null,
    @Column(name = "Y_CRDNT")
    var y: String? = null,
    @Column(name = "STAT_EVET_NM")
    var statEvetNm: String? = null,
    @Column(name = "SVC_THEME_NM")
    var svcThemeNm: String? = null,
    @Column(name = "ZN_NM")
    var znNm: String? = null,
    @Column(name = "UNIT_SVC_NM")
    var unitSvcNm: String? = null,

    @Formula("CONCAT(ZN_CD, CONCAT(SVC_THEME_CD, CONCAT(UNIT_SVC_CD, CONCAT(EVET_GB_CD, STAT_EVET_CD))))")
    var statEvetId: String? = null,

    @Formula("CONCAT(ZN_CD, CONCAT(SVC_THEME_CD,UNIT_SVC_CD))")
    var evetCd: String? = null,


    ) : Serializable