package com.eseict.reapi.dstr.mobile.dto;

import java.util.Map;

/**
 * Xeus addEvent 수신 DTO
 */
public class XeusEventDto {

    private String uSvcOutbId;
    private String procSt;  // 10=시작, 20=진행, 99=종료
    private String phoneNumber;
    private String statEvetOutbDtm;
    private String rtspUrl;
    private String userName;
    private Double lat;
    private Double lng;

    public XeusEventDto() {}

    public XeusEventDto(Map<String, Object> map) {
        this.procSt = getString(map, "procSt");
        this.phoneNumber = getString(map, "phoneNumber");
        this.statEvetOutbDtm = getString(map, "statEvetOutbDtm");
        this.lat = getDouble(map, "lat");
        this.lng = getDouble(map, "lng");
    }

    private String getString(Map<String, Object> map, String key) {
        Object val = map.get(key);
        return val != null ? val.toString() : null;
    }

    private Double getDouble(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val instanceof Number) return ((Number) val).doubleValue();
        if (val instanceof String) {
            try { return Double.parseDouble((String) val); }
            catch (NumberFormatException e) { return null; }
        }
        return null;
    }

    public String getUSvcOutbId() { return uSvcOutbId; }
    public void setUSvcOutbId(String uSvcOutbId) { this.uSvcOutbId = uSvcOutbId; }
    public String getProcSt() { return procSt; }
    public void setProcSt(String procSt) { this.procSt = procSt; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getStatEvetOutbDtm() { return statEvetOutbDtm; }
    public void setStatEvetOutbDtm(String statEvetOutbDtm) { this.statEvetOutbDtm = statEvetOutbDtm; }
    public String getRtspUrl() { return rtspUrl; }
    public void setRtspUrl(String rtspUrl) { this.rtspUrl = rtspUrl; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }
    public Double getLng() { return lng; }
    public void setLng(Double lng) { this.lng = lng; }
}
