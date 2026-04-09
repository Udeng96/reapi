package com.eseict.reapi.dstr.mobile.controller;

import com.eseict.reapi.dstr.mobile.dto.AddMobileDto;
import com.eseict.reapi.dstr.mobile.dto.XeusAssetCctvDto;
import com.eseict.reapi.dstr.mobile.service.XeusLiveViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/xeus/cctv")
public class XeusCctvRcvController {

    private final XeusLiveViewService xeusLiveViewService;

    public XeusCctvRcvController(XeusLiveViewService xeusLiveViewService) {
        this.xeusLiveViewService = xeusLiveViewService;
    }

    @RequestMapping(value = "/addCctv.json", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> addCctv(
        @RequestParam(value = "userId", defaultValue = "") String userId,
        @RequestParam(value = "mgrNo", defaultValue = "") String mgrNo,
        @RequestParam(value = "orgMgrNo", defaultValue = "000001") String orgMgrNo,
        @RequestParam(value = "mdMgrNo", defaultValue = "MDL0000014") String mdMgrNo,
        @RequestParam(value = "vmsMgrNo", defaultValue = "VMS0000002") String vmsMgrNo,
        @RequestParam(value = "siteMgrNo", defaultValue = "") String siteMgrNo,
        @RequestParam(value = "cctvNm", defaultValue = "") String cctvNm,
        @RequestParam(value = "deviceId", defaultValue = "") String deviceId,
        @RequestParam(value = "chnlNo", defaultValue = "0") String chnlNo,
        @RequestParam(value = "gbnCd", defaultValue = "99") String gbnCd,
        @RequestParam(value = "useYn", defaultValue = "Y") String useYn,
        @RequestParam(value = "locDesc", defaultValue = "") String locDesc,
        @RequestParam(value = "lng", defaultValue = "") String lng,
        @RequestParam(value = "lat", defaultValue = "") String lat
    ) {
        XeusAssetCctvDto dto = new XeusAssetCctvDto();
        dto.setUserId(userId);
        dto.setMgrNo(mgrNo);
        dto.setOrgMgrNo(orgMgrNo);
        dto.setMdMgrNo(mdMgrNo);
        dto.setVmsMgrNo(vmsMgrNo);
        dto.setSiteMgrNo(siteMgrNo);
        dto.setCctvNm(cctvNm);
        dto.setDeviceId(deviceId);
        dto.setChnlNo(chnlNo);
        dto.setGbnCd(gbnCd);
        dto.setUseYn(useYn);
        dto.setLocDesc(locDesc);
        dto.setLng(lng);
        dto.setLat(lat);

        return ResponseEntity.ok(xeusLiveViewService.handleAddCctv(dto));
    }

    @RequestMapping(value = "/addMobile.json", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> addMobile(
        @RequestParam(value = "userId", defaultValue = "") String userId,
        @RequestParam(value = "userDtm", defaultValue = "") String userDtm,
        @RequestParam(value = "mgrNo", defaultValue = "") String mgrNo,
        @RequestParam(value = "lon", defaultValue = "0.0") String lon,
        @RequestParam(value = "lat", defaultValue = "0.0") String lat,
        @RequestParam(value = "mobileChk", defaultValue = "") String mobileChk,
        @RequestParam(value = "mbUrl", defaultValue = "") String mbUrl,
        @RequestParam(value = "title", defaultValue = "") String title
    ) {
        AddMobileDto dto = new AddMobileDto();
        dto.setUserId(userId);
        dto.setUserDtm(userDtm);
        dto.setMgrNo(mgrNo);
        dto.setLon(lon);
        dto.setLat(lat);
        dto.setMobileChk(mobileChk);
        dto.setMbUrl(mbUrl);
        dto.setTitle(title);

        return ResponseEntity.ok(xeusLiveViewService.handleAddMobile(dto));
    }

    @RequestMapping(value = "/getMobileList.json", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> getMobileList() {
        return ResponseEntity.ok(xeusLiveViewService.getMobileList());
    }
}
