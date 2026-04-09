package com.eseict.reapi.dstr.event.domain;

import com.eseict.reapi.common.config.DbSchema;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "ioc_stat_evet_pos_hist", schema = DbSchema.IOC)
@IdClass(IocStatEvetPosHist.PK.class)
public class IocStatEvetPosHist {

    @Id
    @Column(name = "stat_evet_outb_seqn")
    private String statEvetOutbSeqn;

    @Id
    @Column(name = "site_cd")
    private String siteCd;

    @Id
    @Column(name = "client_cd")
    private String clientCd;

    @Column(name = "seq")
    private Integer seq;

    @Column(name = "x_crdnt")
    private String xCrdnt;

    @Column(name = "y_crdnt")
    private String yCrdnt;

    @Column(name = "z_crdnt")
    private String zCrdnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stat_evet_outb_seqn", insertable = false, updatable = false)
    private IocStatEvetOutbHist iocStatEvetOutbHist;

    public String getStatEvetOutbSeqn() { return statEvetOutbSeqn; }
    public String getSiteCd() { return siteCd; }
    public String getClientCd() { return clientCd; }
    public Integer getSeq() { return seq; }
    public String getXCrdnt() { return xCrdnt; }
    public String getYCrdnt() { return yCrdnt; }
    public String getZCrdnt() { return zCrdnt; }

    public static class PK implements Serializable {
        private String statEvetOutbSeqn;
        private String siteCd;
        private String clientCd;
        public PK() {}
        public String getStatEvetOutbSeqn() { return statEvetOutbSeqn; }
        public void setStatEvetOutbSeqn(String s) { this.statEvetOutbSeqn = s; }
        public String getSiteCd() { return siteCd; }
        public void setSiteCd(String s) { this.siteCd = s; }
        public String getClientCd() { return clientCd; }
        public void setClientCd(String s) { this.clientCd = s; }
    }
}
