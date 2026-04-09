package com.eseict.reapi.dstr.mobile.controller;

import com.eseict.reapi.dstr.mobile.dto.XeusEventDto;
import com.eseict.reapi.dstr.mobile.service.XeusLiveViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/xeus/ws")
public class XeusEventRcvController {

    private final XeusLiveViewService xeusLiveViewService;

    public XeusEventRcvController(XeusLiveViewService xeusLiveViewService) {
        this.xeusLiveViewService = xeusLiveViewService;
    }

    @RequestMapping(value = "/addEvent", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> addEvent(
        @RequestParam(value = "userId", required = false, defaultValue = "") String userId,
        @RequestParam("json") String eventDataStr
    ) {
        return ResponseEntity.ok(xeusLiveViewService.handleAddEvent(userId, eventDataStr));
    }

    @RequestMapping(value = "/get/list", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<List<XeusEventDto>> getLiveViewList() {
        return ResponseEntity.ok(xeusLiveViewService.getLiveViewEventList());
    }
}
