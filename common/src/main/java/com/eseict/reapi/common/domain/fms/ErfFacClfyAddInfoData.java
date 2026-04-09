package com.eseict.reapi.common.domain.fms;

import com.eseict.reapi.common.config.DbSchema;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "erf_fac_clfy_add_info_data", schema = DbSchema.FMS)
@IdClass(ErfFacClfyAddInfoData.PK.class)
public class ErfFacClfyAddInfoData {

    @Id
    @Column(name = "fac_id")
    private String facId;

    @Id
    @Column(name = "add_info_id")
    private String addInfoId;

    @Column(name = "add_info_val")
    private String addInfoVal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fac_id", insertable = false, updatable = false)
    private ErfFacInfo refErfFacInfo;

    public String getFacId() { return facId; }
    public String getAddInfoId() { return addInfoId; }
    public String getAddInfoVal() { return addInfoVal; }

    public static class PK implements Serializable {
        private String facId;
        private String addInfoId;
        public PK() {}
        public String getFacId() { return facId; }
        public void setFacId(String s) { this.facId = s; }
        public String getAddInfoId() { return addInfoId; }
        public void setAddInfoId(String s) { this.addInfoId = s; }
    }
}
