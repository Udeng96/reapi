package com.eseict.reapi.common.domain.fms;

import com.eseict.reapi.common.config.DbSchema;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "erf_fac_clfy", schema = DbSchema.FMS)
public class ErfFacClfy {

    @Id
    @Column(name = "fac_clfy_id")
    private String facClfyId;

    @Column(name = "fac_clfy_nm")
    private String facClfyNm;

    @Column(name = "top_fac_clfy_id")
    private String topFacClfyId;

    @Column(name = "lvl_no")
    private Integer lvlNo;

    @Column(name = "use_yn")
    private Integer useYn;

    @Lob
    @Column(name = "fac_clfy_ico")
    private byte[] facClfyIco;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "fac_clfy_id")
    private List<ErfFacInfo> erfFacInfos;

    public String getFacClfyId() { return facClfyId; }
    public String getFacClfyNm() { return facClfyNm; }
    public String getTopFacClfyId() { return topFacClfyId; }
    public Integer getLvlNo() { return lvlNo; }
    public Integer getUseYn() { return useYn; }
    public byte[] getFacClfyIco() { return facClfyIco; }
    public List<ErfFacInfo> getErfFacInfos() { return erfFacInfos; }
}
