package com.eseict.reapi.dstr.mobile.domain;

import com.eseict.reapi.common.config.DbSchema;
import jakarta.persistence.*;

@Entity
@Table(name = "mon_mb_desc", schema = DbSchema.EAS)
public class MonMbDesc {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_nm")
    private String userNm;

    @Column(name = "user_dtm")
    private String userDtm;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lon")
    private Double lon;

    @Column(name = "_gid", insertable = false, updatable = false)
    private Integer gid;

    @Column(name = "mobile_chk")
    private Integer mobileChk;

    @Column(name = "mb_url")
    private String mbUrl;

    @Column(name = "title")
    private String title;

    @Column(name = "dpt_nm")
    private String dptNm;

    @Column(name = "mgr_no")
    private String mgrNo;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserNm() { return userNm; }
    public void setUserNm(String userNm) { this.userNm = userNm; }
    public String getUserDtm() { return userDtm; }
    public void setUserDtm(String userDtm) { this.userDtm = userDtm; }
    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }
    public Double getLon() { return lon; }
    public void setLon(Double lon) { this.lon = lon; }
    public Integer getMobileChk() { return mobileChk; }
    public void setMobileChk(Integer mobileChk) { this.mobileChk = mobileChk; }
    public String getMbUrl() { return mbUrl; }
    public void setMbUrl(String mbUrl) { this.mbUrl = mbUrl; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDptNm() { return dptNm; }
    public void setDptNm(String dptNm) { this.dptNm = dptNm; }
    public String getMgrNo() { return mgrNo; }
    public void setMgrNo(String mgrNo) { this.mgrNo = mgrNo; }
}
