package com.eseict.reapi.dstr.event.domain;

import com.eseict.reapi.common.config.DbSchema;
import jakarta.persistence.*;

@Entity
@Table(name = "vw_ioc_evet_info", schema = DbSchema.IOC)
public class VwIocEvetInfo {

    @Column(name = "MSG_TYP_CD")
    private String msgTypCd;

    @Column(name = "ZN_CD")
    private String znCd;

    @Column(name = "SVC_THEME_CD")
    private String svcThemeCd;

    @Column(name = "UNIT_SVC_CD")
    private String unitSvcCd;

    @Column(name = "EVET_CD")
    private String evetCd;

    @Id
    @Column(name = "EVET_ID")
    private String evetId;

    @Column(name = "EVET_NM")
    private String evetNm;

    @Column(name = "ZN_NM")
    private String znNm;

    @Column(name = "SVC_THEME_NM")
    private String svcThemeNm;

    @Column(name = "UNIT_SVC_NM")
    private String unitSvcNm;

    public VwIocEvetInfo() {}

    public VwIocEvetInfo(String svcThemeCd, String svcThemeNm, String unitSvcCd, String unitSvcNm) {
        this.svcThemeCd = svcThemeCd;
        this.svcThemeNm = svcThemeNm;
        this.unitSvcCd = unitSvcCd;
        this.unitSvcNm = unitSvcNm;
    }

    public String getMsgTypCd() { return msgTypCd; }
    public String getZnCd() { return znCd; }
    public String getSvcThemeCd() { return svcThemeCd; }
    public String getUnitSvcCd() { return unitSvcCd; }
    public String getEvetCd() { return evetCd; }
    public String getEvetId() { return evetId; }
    public String getEvetNm() { return evetNm; }
    public String getZnNm() { return znNm; }
    public String getSvcThemeNm() { return svcThemeNm; }
    public String getUnitSvcNm() { return unitSvcNm; }
}
