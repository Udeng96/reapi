package com.eseict.reapi.dstr.mobile.dto;

/**
 * Xeus addCctv.json 수신 DTO
 */
public class XeusAssetCctvDto {

    private String userId;
    private String mgrNo;
    private String orgMgrNo;
    private String mdMgrNo;
    private String vmsMgrNo;
    private String siteMgrNo;
    private String cctvNm;
    private String deviceId;  // RTSP URL
    private String chnlNo;
    private String gbnCd;
    private String useYn;
    private String locDesc;
    private String lng;
    private String lat;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMgrNo() { return mgrNo; }
    public void setMgrNo(String mgrNo) { this.mgrNo = mgrNo != null ? mgrNo.replace("-", "") : null; }
    public String getOrgMgrNo() { return orgMgrNo; }
    public void setOrgMgrNo(String orgMgrNo) { this.orgMgrNo = orgMgrNo; }
    public String getMdMgrNo() { return mdMgrNo; }
    public void setMdMgrNo(String mdMgrNo) { this.mdMgrNo = mdMgrNo; }
    public String getVmsMgrNo() { return vmsMgrNo; }
    public void setVmsMgrNo(String vmsMgrNo) { this.vmsMgrNo = vmsMgrNo; }
    public String getSiteMgrNo() { return siteMgrNo; }
    public void setSiteMgrNo(String siteMgrNo) { this.siteMgrNo = siteMgrNo; }
    public String getCctvNm() { return cctvNm; }
    public void setCctvNm(String cctvNm) { this.cctvNm = cctvNm; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getChnlNo() { return chnlNo; }
    public void setChnlNo(String chnlNo) { this.chnlNo = chnlNo; }
    public String getGbnCd() { return gbnCd; }
    public void setGbnCd(String gbnCd) { this.gbnCd = gbnCd; }
    public String getUseYn() { return useYn; }
    public void setUseYn(String useYn) { this.useYn = useYn; }
    public String getLocDesc() { return locDesc; }
    public void setLocDesc(String locDesc) { this.locDesc = locDesc; }
    public String getLng() { return lng; }
    public void setLng(String lng) { this.lng = lng; }
    public String getLat() { return lat; }
    public void setLat(String lat) { this.lat = lat; }
}
