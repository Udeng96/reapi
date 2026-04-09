package com.eseict.apiServer.domain.rino.event

import com.eseict.apiServer.conf.ApiConstants
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "VW_EVENT_ITEM", schema = ApiConstants.DB_SCHEMA.IOC)
@IdClass(EventItem::class)
class EventItem(
    @Id
    @Column(name = "SVC_THEME_CD")
    var svcThemeCd: String,
    @Column(name = "SVC_THEME_NM")
    var svcThemeNm: String,
    @Id
    @Column(name = "UNIT_SVC_CD")
    var unitSvcCd: String,
    @Column(name = "UNIT_SVC_NM")
    var unitSvcNm: String,
    @Column(name = "STAT_EVET_CD")
    var statEvetCd: String,
    @Column(name = "STAT_EVET_NM")
    var statEvetNm: String,
    @Column(name = "EVENT_ID")
    var eventId: String,
//    @Id
//    @Column(name = "CLIENT_CD")
//    var clientCd: String,
//    @Id
//    @Column(name = "SITE_CD")
//    var siteCd: String,


) : Serializable