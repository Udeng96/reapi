package com.eseict.reapi.cctvmonitor.cctv.controller;

import com.eseict.reapi.cctvmonitor.cctv.dto.CctvDetailResponse;
import com.eseict.reapi.cctvmonitor.cctv.dto.CctvMonitorResponse;
import com.eseict.reapi.cctvmonitor.cctv.dto.PtzRequest;
import com.eseict.reapi.cctvmonitor.cctv.service.CctvQueryService;
import com.eseict.reapi.cctvmonitor.cctv.service.PtzControlService;
import com.eseict.reapi.common.domain.fms.ErfFacClfy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cctv")
public class MonitorController {

    private final PtzControlService ptzControlService;
    private final CctvQueryService cctvQueryService;

    public MonitorController(PtzControlService ptzControlService, CctvQueryService cctvQueryService) {
        this.ptzControlService = ptzControlService;
        this.cctvQueryService = cctvQueryService;
    }

    /** 분류별 CCTV 그룹 목록 (모니터 화면용) */
    @GetMapping("/monitor")
    public ResponseEntity<List<CctvMonitorResponse>> getMonitorList(
        @RequestParam(required = false) String clfyId
    ) {
        return ResponseEntity.ok(cctvQueryService.getMonitorList(clfyId));
    }

    /** CCTV 목록 페이징 조회 */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getList(
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String cctvNm,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int rows
    ) {
        return ResponseEntity.ok(cctvQueryService.getList(cctvNm, page, rows));
    }

    /** CCTV 단건 상세 */
    @GetMapping("/{cctvId}")
    public ResponseEntity<CctvDetailResponse> getDetail(@PathVariable String cctvId) {
        CctvDetailResponse detail = cctvQueryService.getDetail(cctvId);
        return detail != null ? ResponseEntity.ok(detail) : ResponseEntity.notFound().build();
    }

    /** CCTV 분류 목록 */
    @GetMapping("/clfy")
    public ResponseEntity<List<ErfFacClfy>> getClfyList() {
        return ResponseEntity.ok(cctvQueryService.getClfyList());
    }

    /** PTZ 제어 */
    @PostMapping("/control/{cctvId}")
    public ResponseEntity<Map<String, String>> controlPtz(
        @PathVariable String cctvId,
        @RequestBody PtzRequest request
    ) {
        PtzRequest fullRequest = new PtzRequest(cctvId, request.action(), request.code());
        String result = ptzControlService.controlPtz(fullRequest);
        return ResponseEntity.ok(Map.of("result", result));
    }
}
