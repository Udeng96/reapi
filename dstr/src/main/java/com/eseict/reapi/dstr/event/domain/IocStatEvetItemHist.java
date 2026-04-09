package com.eseict.reapi.dstr.event.domain;

import com.eseict.reapi.common.config.DbSchema;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "ioc_stat_evet_item_hist", schema = DbSchema.IOC)
@IdClass(IocStatEvetItemHist.PK.class)
public class IocStatEvetItemHist {

    @Column(name = "stat_evet_outb_seqn")
    private String statEvetOutbSeqn;

    @Column(name = "seq")
    private Integer seq;

    @Id
    @Column(name = "item_nm")
    private String itemNm;

    @Column(name = "item_val")
    private String itemVal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stat_evet_outb_seqn", insertable = false, updatable = false)
    private IocStatEvetOutbHist iocStatEvetOutbHist;

    public String getStatEvetOutbSeqn() { return statEvetOutbSeqn; }
    public Integer getSeq() { return seq; }
    public String getItemNm() { return itemNm; }
    public String getItemVal() { return itemVal; }
    public IocStatEvetOutbHist getIocStatEvetOutbHist() { return iocStatEvetOutbHist; }

    public static class PK implements Serializable {
        private String itemNm;
        public PK() {}
        public PK(String itemNm) { this.itemNm = itemNm; }
        public String getItemNm() { return itemNm; }
        public void setItemNm(String s) { this.itemNm = s; }
    }
}
