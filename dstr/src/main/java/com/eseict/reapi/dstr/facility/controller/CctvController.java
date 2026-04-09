package com.eseict.reapi.dstr.facility.controller;

import com.eseict.reapi.dstr.event.dto.Paging;
import com.eseict.reapi.dstr.facility.dto.CctvClfyDto;
import com.eseict.reapi.dstr.facility.dto.GisFacInfoDto;
import com.eseict.reapi.dstr.facility.dto.PtzInfo;
import com.eseict.reapi.dstr.facility.service.CctvControlService;
import com.eseict.reapi.dstr.facility.service.FacilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cctv")
public class CctvController {

    private final FacilityService facilityService;
    private final CctvControlService cctvControlService;

    public CctvController(FacilityService facilityService, CctvControlService cctvControlService) {
        this.facilityService = facilityService;
        this.cctvControlService = cctvControlService;
    }

    @GetMapping
    public ResponseEntity<Paging<GisFacInfoDto>> getCctvList(
        @RequestParam(required = false) String facNm,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int rows
    ) {
        return ResponseEntity.ok(facilityService.getFacList("cctv", facNm, page, rows));
    }

    @GetMapping("/{cctvId}")
    public ResponseEntity<GisFacInfoDto> getCctv(@PathVariable String cctvId) {
        GisFacInfoDto cctv = facilityService.getFac(cctvId);
        return cctv != null ? ResponseEntity.ok(cctv) : ResponseEntity.notFound().build();
    }

    @GetMapping("/clfy")
    public ResponseEntity<List<CctvClfyDto>> getCctvClfy() {
        return ResponseEntity.ok(facilityService.getCctvClfy());
    }

    @PostMapping("/control/{cctvId}")
    public ResponseEntity<Map<String, Object>> controlCctv(
        @PathVariable String cctvId,
        @RequestBody PtzInfo controlInfo
    ) {
        controlInfo.setCctvId(cctvId);
        return ResponseEntity.ok(cctvControlService.doControl(controlInfo));
    }
}
