package com.eseict.reapi.dstr.event.domain;

import com.eseict.reapi.common.config.DbSchema;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "ioc_stat_evet_outb_hist", schema = DbSchema.IOC)
@IdClass(IocStatEvetOutbHist.PK.class)
public class IocStatEvetOutbHist {

    @Id
    @Column(name = "stat_evet_outb_seqn")
    private String statEvetOutbSeqn;

    @Column(name = "clr_dtm")
    private String clrDtm;

    @Column(name = "cls_dtm")
    private String clsDtm;

    @Column(name = "cls_typ")
    private String clsTyp;

    @Column(name = "evet_gb_cd", nullable = false)
    private String evetGbCd;

    @Column(name = "outb_dtm")
    private String outbDtm;

    @Column(name = "outb_main_gb")
    private String outbMainGb;

    @Column(name = "outb_plac")
    private String outbPlac;

    @Column(name = "outb_scop_rads")
    private String outbScopRads;

    @Column(name = "proc_st")
    private String procSt;

    @Column(name = "stat_evet_cd", nullable = false)
    private String statEvetCd;

    @Column(name = "stat_evet_cntn")
    private String statEvetCntn;

    @Column(name = "stat_evet_gd_cd", nullable = false)
    private String statEvetGdCd;

    @Column(name = "svc_theme_cd", nullable = false)
    private String svcThemeCd;

    @Column(name = "u_svc_outb_id")
    private String uSvcOutbId;

    @Column(name = "unit_svc_cd", nullable = false)
    private String unitSvcCd;

    @Column(name = "zn_cd", nullable = false)
    private String znCd;

    @Formula("(SELECT p.X_CRDNT FROM rino_ioc.IOC_STAT_EVET_POS_HIST p WHERE p.STAT_EVET_OUTB_SEQN = stat_evet_outb_seqn LIMIT 1)")
    private String x;

    @Formula("(SELECT p.Y_CRDNT FROM rino_ioc.IOC_STAT_EVET_POS_HIST p WHERE p.STAT_EVET_OUTB_SEQN = stat_evet_outb_seqn LIMIT 1)")
    private String y;

    @OneToMany(mappedBy = "iocStatEvetOutbHist", fetch = FetchType.LAZY)
    private List<IocStatEvetItemHist> iocStatEvetItemHists;

    @OneToMany(mappedBy = "iocStatEvetOutbHist", fetch = FetchType.LAZY)
    private List<IocStatEvetPosHist> iocStatEvetPosHists;

    // Getters
    public String getStatEvetOutbSeqn() { return statEvetOutbSeqn; }
    public String getClrDtm() { return clrDtm; }
    public String getClsDtm() { return clsDtm; }
    public String getEvetGbCd() { return evetGbCd; }
    public String getOutbDtm() { return outbDtm; }
    public String getOutbMainGb() { return outbMainGb; }
    public String getOutbPlac() { return outbPlac; }
    public String getOutbScopRads() { return outbScopRads; }
    public String getProcSt() { return procSt; }
    public String getStatEvetCd() { return statEvetCd; }
    public String getStatEvetCntn() { return statEvetCntn; }
    public String getStatEvetGdCd() { return statEvetGdCd; }
    public String getSvcThemeCd() { return svcThemeCd; }
    public String getUSvcOutbId() { return uSvcOutbId; }
    public String getUnitSvcCd() { return unitSvcCd; }
    public String getZnCd() { return znCd; }
    public String getX() { return x; }
    public String getY() { return y; }
    public List<IocStatEvetItemHist> getIocStatEvetItemHists() { return iocStatEvetItemHists; }
    public List<IocStatEvetPosHist> getIocStatEvetPosHists() { return iocStatEvetPosHists; }

    public static class PK implements Serializable {
        private String statEvetOutbSeqn;
        public PK() {}
        public PK(String statEvetOutbSeqn) { this.statEvetOutbSeqn = statEvetOutbSeqn; }
        public String getStatEvetOutbSeqn() { return statEvetOutbSeqn; }
        public void setStatEvetOutbSeqn(String s) { this.statEvetOutbSeqn = s; }
    }
}
