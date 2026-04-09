package com.eseict.reapi.dstr.event.domain;

import com.eseict.reapi.common.config.DbSchema;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ioc_stat_evet_gd_info", schema = DbSchema.IOC)
@IdClass(IocStatEvetGdInfo.PK.class)
public class IocStatEvetGdInfo {

    @Id @Column(name = "zn_cd") private String znCd;
    @Id @Column(name = "unit_svc_cd") private String unitSvcCd;
    @Id @Column(name = "svc_theme_cd") private String svcThemeCd;
    @Id @Column(name = "stat_evet_gd_cd") private String statEvetGdCd;
    @Id @Column(name = "stat_evet_cd") private String statEvetCd;
    @Id @Column(name = "site_cd") private String siteCd;
    @Id @Column(name = "evet_gb_cd") private String evetGbCd;
    @Id @Column(name = "client_cd") private String clientCd;

    @Column(name = "stat_evet_gd_nm") private String statEvetGdNm;
    @Column(name = "stat_evet_gd_desc") private String statEvetGdDesc;

    public String getZnCd() { return znCd; }
    public String getUnitSvcCd() { return unitSvcCd; }
    public String getSvcThemeCd() { return svcThemeCd; }
    public String getStatEvetGdCd() { return statEvetGdCd; }
    public String getStatEvetCd() { return statEvetCd; }
    public String getStatEvetGdNm() { return statEvetGdNm; }

    public static class PK implements Serializable {
        private String znCd;
        private String unitSvcCd;
        private String svcThemeCd;
        private String statEvetGdCd;
        private String statEvetCd;
        private String siteCd;
        private String evetGbCd;
        private String clientCd;

        public PK() {}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PK pk)) return false;
            return java.util.Objects.equals(znCd, pk.znCd) &&
                   java.util.Objects.equals(unitSvcCd, pk.unitSvcCd) &&
                   java.util.Objects.equals(svcThemeCd, pk.svcThemeCd) &&
                   java.util.Objects.equals(statEvetGdCd, pk.statEvetGdCd) &&
                   java.util.Objects.equals(statEvetCd, pk.statEvetCd) &&
                   java.util.Objects.equals(siteCd, pk.siteCd) &&
                   java.util.Objects.equals(evetGbCd, pk.evetGbCd) &&
                   java.util.Objects.equals(clientCd, pk.clientCd);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(znCd, unitSvcCd, svcThemeCd, statEvetGdCd, statEvetCd, siteCd, evetGbCd, clientCd);
        }
    }
}
