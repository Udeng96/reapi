package com.eseict.reapi.dstr.event.domain;

import com.eseict.reapi.common.config.DbSchema;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.io.Serializable;

@Entity
@Table(name = "vw_event", schema = DbSchema.IOC)
public class Event implements Serializable {

    @Id
    @Column(name = "STAT_EVET_OUTB_SEQN")
    private String id;

    @Column(name = "EVET_GB_CD")
    private String evetGbCd;

    @Column(name = "OUTB_DTM")
    private String outbDtm;

    @Column(name = "CLS_DTM")
    private String clsDtm;

    @Column(name = "OUTB_MAIN_GB")
    private String outbMainGb;

    @Column(name = "OUTB_PLAC")
    private String outbPlac;

    @Column(name = "PROC_ST")
    private String procSt;

    @Column(name = "STAT_EVET_CD")
    private String statEvetCd;

    @Column(name = "STAT_EVET_CNTN")
    private String statEvetCntn;

    @Column(name = "STAT_EVET_GD_CD")
    private String statEvetGdCd;

    @Column(name = "SVC_THEME_CD")
    private String svcThemeCd;

    @Column(name = "U_SVC_OUTB_ID")
    private String uSvcOutbId;

    @Column(name = "UNIT_SVC_CD")
    private String unitSvcCd;

    @Column(name = "ZN_CD")
    private String zoneId;

    @Column(name = "X_CRDNT")
    private String x;

    @Column(name = "Y_CRDNT")
    private String y;

    @Column(name = "STAT_EVET_NM")
    private String statEvetNm;

    @Column(name = "SVC_THEME_NM")
    private String svcThemeNm;

    @Column(name = "ZN_NM")
    private String znNm;

    @Column(name = "UNIT_SVC_NM")
    private String unitSvcNm;

    @Formula("ZN_CD || SVC_THEME_CD || UNIT_SVC_CD || EVET_GB_CD || STAT_EVET_CD")
    private String statEvetId;

    @Formula("ZN_CD || SVC_THEME_CD || UNIT_SVC_CD")
    private String evetCd;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEvetGbCd() { return evetGbCd; }
    public void setEvetGbCd(String evetGbCd) { this.evetGbCd = evetGbCd; }
    public String getOutbDtm() { return outbDtm; }
    public void setOutbDtm(String outbDtm) { this.outbDtm = outbDtm; }
    public String getClsDtm() { return clsDtm; }
    public void setClsDtm(String clsDtm) { this.clsDtm = clsDtm; }
    public String getOutbMainGb() { return outbMainGb; }
    public void setOutbMainGb(String outbMainGb) { this.outbMainGb = outbMainGb; }
    public String getOutbPlac() { return outbPlac; }
    public void setOutbPlac(String outbPlac) { this.outbPlac = outbPlac; }
    public String getProcSt() { return procSt; }
    public void setProcSt(String procSt) { this.procSt = procSt; }
    public String getStatEvetCd() { return statEvetCd; }
    public void setStatEvetCd(String statEvetCd) { this.statEvetCd = statEvetCd; }
    public String getStatEvetCntn() { return statEvetCntn; }
    public void setStatEvetCntn(String statEvetCntn) { this.statEvetCntn = statEvetCntn; }
    public String getStatEvetGdCd() { return statEvetGdCd; }
    public void setStatEvetGdCd(String statEvetGdCd) { this.statEvetGdCd = statEvetGdCd; }
    public String getSvcThemeCd() { return svcThemeCd; }
    public void setSvcThemeCd(String svcThemeCd) { this.svcThemeCd = svcThemeCd; }
    public String getUSvcOutbId() { return uSvcOutbId; }
    public void setUSvcOutbId(String uSvcOutbId) { this.uSvcOutbId = uSvcOutbId; }
    public String getUnitSvcCd() { return unitSvcCd; }
    public void setUnitSvcCd(String unitSvcCd) { this.unitSvcCd = unitSvcCd; }
    public String getZoneId() { return zoneId; }
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }
    public String getX() { return x; }
    public void setX(String x) { this.x = x; }
    public String getY() { return y; }
    public void setY(String y) { this.y = y; }
    public String getStatEvetNm() { return statEvetNm; }
    public void setStatEvetNm(String statEvetNm) { this.statEvetNm = statEvetNm; }
    public String getSvcThemeNm() { return svcThemeNm; }
    public void setSvcThemeNm(String svcThemeNm) { this.svcThemeNm = svcThemeNm; }
    public String getZnNm() { return znNm; }
    public void setZnNm(String znNm) { this.znNm = znNm; }
    public String getUnitSvcNm() { return unitSvcNm; }
    public void setUnitSvcNm(String unitSvcNm) { this.unitSvcNm = unitSvcNm; }
    public String getStatEvetId() { return statEvetId; }
    public String getEvetCd() { return evetCd; }
}
