package com.eseict.reapi.dstr.mobile.service;

import com.eseict.reapi.dstr.mobile.domain.MonMbDesc;
import com.eseict.reapi.dstr.mobile.domain.MonMbDescLog;
import com.eseict.reapi.dstr.mobile.dto.AddMobileDto;
import com.eseict.reapi.dstr.mobile.dto.XeusAssetCctvDto;
import com.eseict.reapi.dstr.mobile.dto.XeusEventDto;
import com.eseict.reapi.dstr.mobile.repository.MobileLiveRepository;
import com.eseict.reapi.dstr.mobile.repository.MobilePastRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class XeusLiveViewService {

    private static final Logger log = LoggerFactory.getLogger(XeusLiveViewService.class);
    private static final DateTimeFormatter DTM_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final MobileLiveRepository mobileLiveRepository;
    private final MobilePastRepository mobilePastRepository;

    // 메모리 기반 이벤트 목록 (레거시 DAO 대체)
    private final List<XeusEventDto> liveEventList = new CopyOnWriteArrayList<>();

    public XeusLiveViewService(MobileLiveRepository mobileLiveRepository,
                                MobilePastRepository mobilePastRepository) {
        this.mobileLiveRepository = mobileLiveRepository;
        this.mobilePastRepository = mobilePastRepository;
    }

    /**
     * addCctv.json - 모바일 CCTV 등록
     */
    @Transactional
    public Map<String, Object> handleAddCctv(XeusAssetCctvDto dto) {
        log.info("addCctv: userId={}, mgrNo={}", dto.getUserId(), dto.getMgrNo());

        Optional<MonMbDesc> existing = mobileLiveRepository.findByUserId(dto.getUserId());
        String now = LocalDateTime.now().format(DTM_FORMAT);

        if (existing.isEmpty()) {
            MonMbDesc live = new MonMbDesc();
            live.setUserId(dto.getUserId());
            live.setUserNm(dto.getSiteMgrNo());
            live.setUserDtm(now);
            live.setLat(parseDouble(dto.getLat()));
            live.setLon(parseDouble(dto.getLng()));
            live.setMobileChk(1);
            live.setMbUrl(dto.getDeviceId());
            live.setMgrNo(dto.getMgrNo());
            mobileLiveRepository.save(live);
        } else {
            MonMbDesc find = existing.get();
            find.setMobileChk(1);
            find.setUserDtm(now);
            find.setMbUrl(dto.getDeviceId());
            find.setLat(parseDouble(dto.getLat()));
            find.setLon(parseDouble(dto.getLng()));
            mobileLiveRepository.save(find);
        }

        return Map.of("result", "SUCCESS");
    }

    /**
     * addMobile.json - 촬영 상태 수신
     */
    @Transactional
    public Map<String, Object> handleAddMobile(AddMobileDto dto) {
        log.info("addMobile: userId={}, mgrNo={}, mobileChk={}",
            dto.getUserId(), dto.getMgrNo(), dto.getMobileChk());

        String now = LocalDateTime.now().format(DTM_FORMAT);
        Optional<MonMbDesc> existing = mobileLiveRepository.findByUserId(dto.getUserId());

        MonMbDesc live;
        if (existing.isEmpty()) {
            live = new MonMbDesc();
            live.setUserId(dto.getUserId());
            live.setUserDtm(now);
            live.setLat(parseDouble(dto.getLat()));
            live.setLon(parseDouble(dto.getLon()));
            live.setMobileChk(parseInt(dto.getMobileChk()));
            live.setMbUrl(dto.getMbUrl());
            live.setMgrNo(dto.getMgrNo());
            live.setTitle(dto.getTitle());
        } else {
            live = existing.get();
            live.setMobileChk(parseInt(dto.getMobileChk()));
            live.setMbUrl(dto.getMbUrl());
            live.setLat(parseDouble(dto.getLat()));
            live.setLon(parseDouble(dto.getLon()));
            live.setTitle(dto.getTitle());
            live.setUserDtm(now);
        }

        mobileLiveRepository.save(live);

        // 촬영종료(4) 시 로그 테이블에 이력 저장
        if ("4".equals(dto.getMobileChk())) {
            MonMbDescLog logEntry = new MonMbDescLog();
            logEntry.setUserId(live.getUserId());
            logEntry.setUserNm(live.getUserNm());
            logEntry.setUserDtm(now);
            logEntry.setLat(live.getLat());
            logEntry.setLon(live.getLon());
            logEntry.setMbChk(5);
            logEntry.setMbUrl(live.getMbUrl());
            logEntry.setLiveChk("vod");
            logEntry.setTitle(live.getTitle());
            logEntry.setDptNm(live.getDptNm());
            logEntry.setMgrNo(live.getMgrNo());
            mobilePastRepository.save(logEntry);
            log.info("촬영종료 이력 저장 완료: userId={}", dto.getUserId());
        }

        return Map.of("result", "SUCCESS");
    }

    /**
     * getMobileList.json - 모바일 현황 목록
     */
    public Map<String, Object> getMobileList() {
        List<MonMbDesc> list = mobileLiveRepository.getLiveList();
        return Map.of("result", "true", "error", "false", "data", list);
    }

    /**
     * addEvent - 영상 이벤트 수신
     */
    public Map<String, Object> handleAddEvent(String userId, String eventDataStr) {
        log.info("addEvent: userId={}, data={}", userId, eventDataStr);

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> eventMap = mapper.readValue(eventDataStr, Map.class);
            XeusEventDto eventDto = new XeusEventDto(eventMap);

            if ("10".equals(eventDto.getProcSt())) {
                eventDto.setUSvcOutbId(UUID.randomUUID().toString().replace("-", ""));
                liveEventList.add(eventDto);
                log.info("새 이벤트 등록: uSvcOutbId={}", eventDto.getUSvcOutbId());
            } else {
                // 기존 이벤트 업데이트
                liveEventList.stream()
                    .filter(e -> e.getPhoneNumber() != null && e.getPhoneNumber().equals(eventDto.getPhoneNumber()))
                    .findFirst()
                    .ifPresent(e -> {
                        e.setProcSt(eventDto.getProcSt());
                        if (eventDto.getStatEvetOutbDtm() != null) {
                            e.setStatEvetOutbDtm(eventDto.getStatEvetOutbDtm());
                        }
                    });
            }

            return Map.of("result", "SUCCESS");
        } catch (Exception e) {
            log.error("addEvent 처리 실패: {}", e.getMessage(), e);
            return Map.of("result", "FAIL", "message", e.getMessage());
        }
    }

    /**
     * get/list - 이벤트 목록 조회
     */
    public List<XeusEventDto> getLiveViewEventList() {
        return new ArrayList<>(liveEventList);
    }

    private Double parseDouble(String val) {
        if (val == null || val.isEmpty()) return null;
        try { return Double.parseDouble(val); }
        catch (NumberFormatException e) { return null; }
    }

    private Integer parseInt(String val) {
        if (val == null || val.isEmpty()) return null;
        try { return Integer.parseInt(val); }
        catch (NumberFormatException e) { return null; }
    }
}
