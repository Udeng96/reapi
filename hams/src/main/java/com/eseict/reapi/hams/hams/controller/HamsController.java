package com.eseict.reapi.hams.hams.controller;

import com.eseict.reapi.hams.client.HamsSendClient;
import com.eseict.reapi.hams.hams.dto.HamsCctvInfoDto;
import com.eseict.reapi.hams.hams.dto.HamsInfoDto;
import com.eseict.reapi.hams.hams.service.HamsCctvService;
import com.eseict.reapi.hams.hams.service.HamsDoorControlService;
import com.eseict.reapi.hams.hams.service.HamsStatService;
import com.eseict.reapi.hams.hams.service.HamsStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hams/api")
public class HamsController {

    private final HamsStatusService hamsStatusService;
    private final HamsDoorControlService hamsDoorControlService;
    private final HamsStatService hamsStatService;
    private final HamsCctvService hamsCctvService;
    private final HamsSendClient hamsSendClient;

    public HamsController(HamsStatusService hamsStatusService,
                          HamsDoorControlService hamsDoorControlService,
                          HamsStatService hamsStatService,
                          HamsCctvService hamsCctvService,
                          HamsSendClient hamsSendClient) {
        this.hamsStatusService = hamsStatusService;
        this.hamsDoorControlService = hamsDoorControlService;
        this.hamsStatService = hamsStatService;
        this.hamsCctvService = hamsCctvService;
        this.hamsSendClient = hamsSendClient;
    }

    @GetMapping
    public ResponseEntity<List<HamsInfoDto>> getList() {
        return ResponseEntity.ok(hamsStatusService.getList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HamsInfoDto> get(@PathVariable String id) {
        HamsInfoDto info = hamsStatusService.get(id);
        return info != null ? ResponseEntity.ok(info) : ResponseEntity.notFound().build();
    }

    @GetMapping("/cctv")
    public ResponseEntity<List<HamsCctvInfoDto>> getCctvList(@RequestParam String hamsName) {
        return ResponseEntity.ok(hamsCctvService.getCctvList(hamsName));
    }

    @PostMapping("/{id}/lock")
    public ResponseEntity<Map<String, Object>> lock(
        @PathVariable String id,
        @RequestParam(defaultValue = "1") int target
    ) {
        return ResponseEntity.ok(hamsDoorControlService.doLock(id, target, 2));
    }

    @PostMapping("/{id}/unlock")
    public ResponseEntity<Map<String, Object>> unlock(
        @PathVariable String id,
        @RequestParam(defaultValue = "1") int target
    ) {
        return ResponseEntity.ok(hamsDoorControlService.doLock(id, target, 1));
    }

    /** 레거시 호환 엔드포인트 — GET /hams/api/client/isConnect */
    @GetMapping("/client/isConnect")
    public ResponseEntity<Boolean> isConnect() {
        return ResponseEntity.ok(hamsSendClient.isConnect());
    }

    @GetMapping("/{id}/stat")
    public ResponseEntity<?> getStat(
        @PathVariable String id,
        @RequestParam String doorType,
        @RequestParam String frontBack,
        @RequestParam String dateType,
        @RequestParam(required = false) String startDtm,
        @RequestParam(required = false) String endDtm
    ) {
        String eqId = id.startsWith("HAMS_") ? id.substring(5) : id;
        return ResponseEntity.ok(hamsStatService.getStat(eqId, frontBack, doorType, dateType, startDtm, endDtm));
    }
}
