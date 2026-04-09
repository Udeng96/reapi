package com.eseict.reapi.dstr.event.service;

import com.eseict.reapi.dstr.event.domain.VwIocEvetInfo;
import com.eseict.reapi.dstr.event.dto.DstrInfo;
import com.eseict.reapi.dstr.event.dto.EventsDto;
import com.eseict.reapi.dstr.event.dto.HeatmapCount;
import com.eseict.reapi.dstr.event.dto.Paging;
import com.eseict.reapi.dstr.event.repository.EventCriteriaRepository;
import com.eseict.reapi.dstr.event.repository.EventRepository;
import com.eseict.reapi.dstr.event.repository.VwIocEvetInfoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final EventCriteriaRepository eventCriteriaRepository;
    private final VwIocEvetInfoRepository vwIocEvetInfoRepository;

    public EventService(EventRepository eventRepository,
                        EventCriteriaRepository eventCriteriaRepository,
                        VwIocEvetInfoRepository vwIocEvetInfoRepository) {
        this.eventRepository = eventRepository;
        this.eventCriteriaRepository = eventCriteriaRepository;
        this.vwIocEvetInfoRepository = vwIocEvetInfoRepository;
    }

    public Paging<EventsDto> getEvents(List<String> eventIdList, String startDtm, String endDtm, int page, int rows) {
        long total = eventCriteriaRepository.countEvents(eventIdList, startDtm, endDtm);
        var events = eventCriteriaRepository.getEvents(eventIdList, startDtm, endDtm, page, rows);

        List<EventsDto> list = events.stream()
            .map(e -> new EventsDto(
                e.getId(), e.getStatEvetId(), e.getStatEvetNm(),
                e.getEvetCd(), e.getZnNm(),
                e.getY(), e.getX(), e.getOutbPlac(),
                e.getStatEvetCntn(), e.getOutbDtm(), e.getClsDtm(), e.getUSvcOutbId()
            ))
            .collect(Collectors.toList());

        return new Paging<>(total, list);
    }

    public EventsDto getEvent(String id) {
        return eventRepository.findEvent(id);
    }

    public EventsDto getEventByUSvcId(String id) {
        return eventRepository.findEventByUSvcId(id);
    }

    public List<String> buildEventIdFilter(List<String> locationCdList, List<String> eventIdList) {
        List<VwIocEvetInfo> allEvents = vwIocEvetInfoRepository.findAll();

        if (locationCdList != null && !locationCdList.isEmpty() && eventIdList != null && !eventIdList.isEmpty()) {
            return allEvents.stream()
                .filter(e -> locationCdList.contains(e.getZnCd()) && eventIdList.contains(e.getEvetId().substring(3)))
                .map(VwIocEvetInfo::getEvetId)
                .collect(Collectors.toList());
        } else if (locationCdList != null && !locationCdList.isEmpty()) {
            return allEvents.stream()
                .filter(e -> locationCdList.contains(e.getZnCd()))
                .map(VwIocEvetInfo::getEvetId)
                .collect(Collectors.toList());
        } else if (eventIdList != null && !eventIdList.isEmpty()) {
            return allEvents.stream()
                .filter(e -> eventIdList.contains(e.getEvetId().substring(3)))
                .map(VwIocEvetInfo::getEvetId)
                .collect(Collectors.toList());
        }

        return null;
    }

    /** 히트맵 카운트 */
    public List<HeatmapCount> getHeatmapCount(String svcThemeCd, String unitCd,
                                                String startDtm, String endDtm) {
        List<VwIocEvetInfo> allEvents = vwIocEvetInfoRepository.findAll();
        List<String> eventAuthList = allEvents.stream()
            .map(VwIocEvetInfo::getEvetId)
            .collect(Collectors.toList());

        String safeStart = startDtm != null ? startDtm + "000001" : "";
        String safeEnd = endDtm != null ? endDtm + "235959" : "";

        return eventRepository.findHeatmapCount(svcThemeCd, unitCd, safeStart, safeEnd, eventAuthList);
    }

    /** 상황전파 이벤트 목록 */
    public List<DstrInfo> getDstrEvents(int rows, String startDtm, String endDtm) {
        String safeStart = startDtm != null ? startDtm : "";
        String safeEnd = endDtm != null ? endDtm : "";
        Page<DstrInfo> page = eventRepository.findDstrEvents(
            PageRequest.of(0, rows), safeStart, safeEnd
        );
        return page.getContent();
    }

    /** 상황전파 단건 */
    public DstrInfo getDstrEvent(String uSvcOutbId) {
        return eventRepository.findDstrEventByUSvcOutbId(uSvcOutbId);
    }

    /** 이벤트 종료 — TODO: 소켓 전송 구현 (Phase 5) */
    public boolean closeEvent(String statEvetOutbSeqn) {
        // TODO: 소켓 메시지 생성 후 전송 (procSt=5)
        return eventRepository.findById(statEvetOutbSeqn).isPresent();
    }
}
