package com.eseict.reapi.dstr.event.controller;

import com.eseict.reapi.common.dto.ClfyInfo;
import com.eseict.reapi.dstr.event.dto.*;
import com.eseict.reapi.dstr.event.service.EventItemService;
import com.eseict.reapi.dstr.event.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;
    private final EventItemService eventItemService;

    public EventController(EventService eventService, EventItemService eventItemService) {
        this.eventService = eventService;
        this.eventItemService = eventItemService;
    }

    @GetMapping
    public ResponseEntity<Paging<EventsDto>> getEvents(
        @RequestParam(required = false) List<String> eventIdList,
        @RequestParam(required = false) List<String> locationCdList,
        @RequestParam(required = false) String startDtm,
        @RequestParam(required = false) String endDtm,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int rows
    ) {
        List<String> filteredIds = eventService.buildEventIdFilter(locationCdList, eventIdList);
        Paging<EventsDto> result = eventService.getEvents(filteredIds, startDtm, endDtm, page, rows);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventsDto> getEvent(@PathVariable String id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }

    @GetMapping("/uSvcOutbId/{id}")
    public ResponseEntity<EventsDto> getEventByUSvcId(@PathVariable String id) {
        return ResponseEntity.ok(eventService.getEventByUSvcId(id));
    }

    @GetMapping("/type")
    public ResponseEntity<List<ClfyInfo>> getType() {
        return ResponseEntity.ok(eventItemService.getTypeList());
    }

    @GetMapping("/location")
    public ResponseEntity<List<ClfyInfo>> getLocation() {
        return ResponseEntity.ok(eventItemService.getZnCdList());
    }

    @GetMapping("/heatmap/clfy")
    public ResponseEntity<List<HeatmapClfyInfo>> getHeatmapClfy() {
        return ResponseEntity.ok(eventItemService.getHeatmapClfy());
    }

    @GetMapping("/heatmap/count")
    public ResponseEntity<List<HeatmapCount>> getHeatmapCount(
        @RequestParam String svcThemeCd,
        @RequestParam String unitCd,
        @RequestParam(required = false) String startDtm,
        @RequestParam(required = false) String endDtm
    ) {
        return ResponseEntity.ok(eventService.getHeatmapCount(svcThemeCd, unitCd, startDtm, endDtm));
    }

    @GetMapping("/dstr")
    public ResponseEntity<List<DstrInfo>> getDstrEvents(
        @RequestParam(defaultValue = "30") int rows,
        @RequestParam(required = false) String startDtm,
        @RequestParam(required = false) String endDtm
    ) {
        return ResponseEntity.ok(eventService.getDstrEvents(rows, startDtm, endDtm));
    }

    @GetMapping("/dstr/{uSvcOutbId}")
    public ResponseEntity<DstrInfo> getDstrEvent(@PathVariable String uSvcOutbId) {
        DstrInfo info = eventService.getDstrEvent(uSvcOutbId);
        return info != null ? ResponseEntity.ok(info) : ResponseEntity.notFound().build();
    }

    @PostMapping("/close")
    public ResponseEntity<Map<String, Object>> closeEvent(@RequestParam String statEvetOutbSeqn) {
        boolean result = eventService.closeEvent(statEvetOutbSeqn);
        if (result) {
            return ResponseEntity.ok(Map.of("result", "SUCCESS"));
        }
        return ResponseEntity.internalServerError().body(Map.of("result", "FAIL"));
    }
}
