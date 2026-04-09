package com.eseict.reapi.dstr.mobile.dto;

/**
 * Xeus addMobile.json 수신 DTO
 */
public class AddMobileDto {

    private String userId;
    private String userDtm;
    private String mgrNo;
    private String lon;  // 경도
    private String lat;  // 위도
    private String mobileChk;  // 1:미실행 2:촬영시작 3:촬영중 4:촬영종료
    private String mbUrl;  // 영상주소
    private String title;  // 영상 제목

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserDtm() { return userDtm; }
    public void setUserDtm(String userDtm) { this.userDtm = userDtm; }
    public String getMgrNo() { return mgrNo; }
    public void setMgrNo(String mgrNo) { this.mgrNo = mgrNo != null ? mgrNo.replace("-", "") : null; }
    public String getLon() { return lon; }
    public void setLon(String lon) { this.lon = lon; }
    public String getLat() { return lat; }
    public void setLat(String lat) { this.lat = lat; }
    public String getMobileChk() { return mobileChk; }
    public void setMobileChk(String mobileChk) { this.mobileChk = mobileChk; }
    public String getMbUrl() { return mbUrl; }
    public void setMbUrl(String mbUrl) { this.mbUrl = mbUrl; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
