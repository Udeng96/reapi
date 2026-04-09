package com.eseict.reapi.common.domain.fms;

import com.eseict.reapi.common.config.DbSchema;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "erf_fac_info", schema = DbSchema.FMS)
public class ErfFacInfo {

    @Id
    @Column(name = "fac_id", insertable = false, updatable = false)
    private String facId;

    @Column(name = "fac_nm")
    private String facNm;

    @Column(name = "fac_clfy_id")
    private String facClfyId;

    @Column(name = "mgt_no")
    private String mgtNo;

    @Column(name = "addr_short")
    private String addrShort;

    @Column(name = "pos_nm")
    private String posNm;

    @Column(name = "use_yn")
    private Integer useYn;

    @Column(name = "area")
    private String area;

    @Column(name = "file_grup_id")
    private String fileGrupId;

    @Column(name = "upd_dtm")
    private String updDtm;

    @Column(name = "sync_yn")
    private String syncYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fac_clfy_id", insertable = false, updatable = false)
    private ErfFacClfy refErfFacClfy;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "refErfFacInfo")
    private List<ErfPosCrdnt> refErfPosCrdnts;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "fac_id")
    private List<ErfFacClfyAddInfoData> refErfFacClfyAddInfoDatas;

    // Getters
    public String getFacId() { return facId; }
    public String getFacNm() { return facNm; }
    public String getFacClfyId() { return facClfyId; }
    public String getMgtNo() { return mgtNo; }
    public String getAddrShort() { return addrShort; }
    public String getPosNm() { return posNm; }
    public Integer getUseYn() { return useYn; }
    public String getArea() { return area; }
    public String getFileGrupId() { return fileGrupId; }
    public String getUpdDtm() { return updDtm; }
    public String getSyncYn() { return syncYn; }
    public ErfFacClfy getRefErfFacClfy() { return refErfFacClfy; }
    public List<ErfPosCrdnt> getRefErfPosCrdnts() { return refErfPosCrdnts; }
    public List<ErfFacClfyAddInfoData> getRefErfFacClfyAddInfoDatas() { return refErfFacClfyAddInfoDatas; }

    // Setters
    public void setFacId(String facId) { this.facId = facId; }
    public void setFacNm(String facNm) { this.facNm = facNm; }
    public void setFacClfyId(String facClfyId) { this.facClfyId = facClfyId; }
    public void setMgtNo(String mgtNo) { this.mgtNo = mgtNo; }
    public void setUseYn(Integer useYn) { this.useYn = useYn; }
    public void setSyncYn(String syncYn) { this.syncYn = syncYn; }
}
