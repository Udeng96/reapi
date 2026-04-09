package com.eseict.reapi.dstr.facility.controller;

import com.eseict.reapi.common.domain.fms.ErfFacClfy;
import com.eseict.reapi.dstr.event.dto.Paging;
import com.eseict.reapi.dstr.facility.dto.CctvClfyDto;
import com.eseict.reapi.dstr.facility.dto.GisFacInfoDto;
import com.eseict.reapi.dstr.facility.service.FacilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FacilityController {

    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @GetMapping("/fac")
    public ResponseEntity<Paging<GisFacInfoDto>> getList(
        @RequestParam(defaultValue = "fac") String type,
        @RequestParam(required = false) String facNm,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int rows
    ) {
        return ResponseEntity.ok(facilityService.getFacList(type, facNm, page, rows));
    }

    @GetMapping("/fac/{facId}")
    public ResponseEntity<GisFacInfoDto> getFac(@PathVariable String facId) {
        GisFacInfoDto fac = facilityService.getFac(facId);
        return fac != null ? ResponseEntity.ok(fac) : ResponseEntity.notFound().build();
    }

    @GetMapping("/fac/clfy/{clfyId}/facInfo")
    public ResponseEntity<List<GisFacInfoDto>> getListByClfyId(@PathVariable String clfyId) {
        return ResponseEntity.ok(facilityService.getFacListByClfyId(clfyId));
    }

    /** 시설물 분류 전체 목록 */
    @GetMapping("/fac/clfy")
    public ResponseEntity<List<ErfFacClfy>> getFacClfy() {
        return ResponseEntity.ok(facilityService.getFacClfy());
    }

    /** 특정 분류 코드 조회 */
    @GetMapping("/fac/clfy/{clfyCd}")
    public ResponseEntity<ErfFacClfy> getFacClfyByCd(@PathVariable String clfyCd) {
        ErfFacClfy clfy = facilityService.getFacClfyByCd(clfyCd);
        return clfy != null ? ResponseEntity.ok(clfy) : ResponseEntity.notFound().build();
    }

    /** 전체 시설물 통합 목록 */
    @GetMapping("/facAll")
    public ResponseEntity<Paging<GisFacInfoDto>> getFacAll(
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String facNm,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int rows
    ) {
        return ResponseEntity.ok(facilityService.getFacAllList(type, facNm, page, rows));
    }
}
