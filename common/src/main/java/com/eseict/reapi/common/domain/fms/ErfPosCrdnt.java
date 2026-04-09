package com.eseict.reapi.common.domain.fms;

import com.eseict.reapi.common.config.DbSchema;
import jakarta.persistence.*;

@Entity
@Table(name = "erf_pos_crdnt", schema = DbSchema.FMS)
public class ErfPosCrdnt {

    @Id
    @Column(name = "pos_crdnt_id")
    private String posCrdntId;

    @Column(name = "fac_id")
    private String facId;

    @Column(name = "x_crdnt")
    private String xCrdnt;

    @Column(name = "y_crdnt")
    private String yCrdnt;

    @Column(name = "use_yn")
    private Integer useYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fac_id", insertable = false, updatable = false)
    private ErfFacInfo refErfFacInfo;

    public String getPosCrdntId() { return posCrdntId; }
    public String getFacId() { return facId; }
    public String getXCrdnt() { return xCrdnt; }
    public String getYCrdnt() { return yCrdnt; }
    public Integer getUseYn() { return useYn; }
}
