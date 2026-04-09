package com.eseict.reapi.dstr.mobile.controller;

import com.eseict.reapi.dstr.event.dto.Paging;
import com.eseict.reapi.dstr.mobile.dto.MobileInfoDto;
import com.eseict.reapi.dstr.mobile.service.MobileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mobile")
public class MobileController {

    private final MobileService mobileService;

    public MobileController(MobileService mobileService) {
        this.mobileService = mobileService;
    }

    @GetMapping("/live")
    public ResponseEntity<List<MobileInfoDto>> getLiveList() {
        return ResponseEntity.ok(mobileService.getLiveList());
    }

    @GetMapping("/live/{userId}")
    public ResponseEntity<MobileInfoDto> getLiveInfo(@PathVariable String userId) {
        MobileInfoDto info = mobileService.getLiveInfo(userId);
        return info != null ? ResponseEntity.ok(info) : ResponseEntity.notFound().build();
    }

    @GetMapping("/past")
    public ResponseEntity<Paging<MobileInfoDto>> getPastList(
        @RequestParam String startDtm,
        @RequestParam String endDtm,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int rows
    ) {
        return ResponseEntity.ok(mobileService.getPastList(startDtm, endDtm, page, rows));
    }

    @GetMapping("/past/{seq}")
    public ResponseEntity<MobileInfoDto> getPastInfo(@PathVariable int seq) {
        MobileInfoDto info = mobileService.getPastInfo(seq);
        return info != null ? ResponseEntity.ok(info) : ResponseEntity.notFound().build();
    }
}
