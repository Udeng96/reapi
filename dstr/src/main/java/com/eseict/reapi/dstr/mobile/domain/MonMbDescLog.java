package com.eseict.reapi.dstr.mobile.domain;

import com.eseict.reapi.common.config.DbSchema;
import jakarta.persistence.*;

@Entity
@Table(name = "mon_mb_desc_log", schema = DbSchema.EAS)
public class MonMbDescLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mb_seq", insertable = false)
    private Integer mbSeq;

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

    @Column(name = "mb_chk")
    private Integer mbChk;

    @Column(name = "mb_url")
    private String mbUrl;

    @Column(name = "live_chk")
    private String liveChk;

    @Column(name = "title")
    private String title;

    @Column(name = "dpt_nm")
    private String dptNm;

    @Column(name = "mgr_no")
    private String mgrNo;

    public Integer getMbSeq() { return mbSeq; }
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
    public Integer getMbChk() { return mbChk; }
    public void setMbChk(Integer mbChk) { this.mbChk = mbChk; }
    public String getMbUrl() { return mbUrl; }
    public void setMbUrl(String mbUrl) { this.mbUrl = mbUrl; }
    public String getLiveChk() { return liveChk; }
    public void setLiveChk(String liveChk) { this.liveChk = liveChk; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDptNm() { return dptNm; }
    public void setDptNm(String dptNm) { this.dptNm = dptNm; }
    public String getMgrNo() { return mgrNo; }
    public void setMgrNo(String mgrNo) { this.mgrNo = mgrNo; }
}
