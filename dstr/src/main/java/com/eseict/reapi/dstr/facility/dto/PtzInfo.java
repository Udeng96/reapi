package com.eseict.reapi.dstr.facility.dto;

public class PtzInfo {

    private String cctvId;
    private String action;
    private String code;

    public PtzInfo() {}

    public PtzInfo(String action, String code) {
        this.action = action;
        this.code = code;
    }

    public String getCctvId() { return cctvId; }
    public void setCctvId(String cctvId) { this.cctvId = cctvId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
