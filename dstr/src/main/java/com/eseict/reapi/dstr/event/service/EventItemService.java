package com.eseict.reapi.dstr.event.service;

import com.eseict.reapi.common.dto.ClfyInfo;
import com.eseict.reapi.dstr.event.domain.VwIocEvetInfo;
import com.eseict.reapi.dstr.event.dto.HeatmapClfyInfo;
import com.eseict.reapi.dstr.event.repository.VwIocEvetInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EventItemService {

    private final VwIocEvetInfoRepository vwIocEvetInfoRepository;

    public EventItemService(VwIocEvetInfoRepository vwIocEvetInfoRepository) {
        this.vwIocEvetInfoRepository = vwIocEvetInfoRepository;
    }

    /** 이벤트 타입 목록 */
    public List<ClfyInfo> getTypeList() {
        List<VwIocEvetInfo> allEvents = vwIocEvetInfoRepository.findAll();
        Map<String, List<String>> grouped = new LinkedHashMap<>();

        for (VwIocEvetInfo item : allEvents) {
            String id = item.getEvetId() != null && item.getEvetId().length() > 3
                ? item.getEvetId().substring(3) : "";
            String category;
            if (id.startsWith("ANM")) category = "법무부";
            else if (id.startsWith("NDM")) category = "재난상황";
            else category = item.getEvetNm() != null ? item.getEvetNm() : "";
            grouped.computeIfAbsent(id, k -> new ArrayList<>()).add(category);
        }

        return grouped.entrySet().stream()
            .map(entry -> {
                String mostFrequent = entry.getValue().stream()
                    .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey).orElse("");
                return new ClfyInfo(entry.getKey(), mostFrequent);
            })
            .collect(Collectors.toList());
    }

    /** 지자체(znCd) 위치 목록 */
    public List<ClfyInfo> getZnCdList() {
        List<VwIocEvetInfo> allEvents = vwIocEvetInfoRepository.findAll();
        return allEvents.stream()
            .map(item -> new ClfyInfo(
                item.getZnCd() != null ? item.getZnCd() : "",
                item.getZnNm() != null ? item.getZnNm() : ""
            ))
            .collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ClfyInfo::id))),
                ArrayList::new
            ));
    }

    /** 히트맵 분류 정보 */
    public List<HeatmapClfyInfo> getHeatmapClfy() {
        List<VwIocEvetInfo> allEvents = vwIocEvetInfoRepository.findAll();

        Map<String, Map<String, List<VwIocEvetInfo>>> grouped = allEvents.stream()
            .filter(e -> e.getSvcThemeCd() != null && e.getSvcThemeNm() != null)
            .collect(Collectors.groupingBy(
                e -> e.getSvcThemeCd() + "|" + e.getSvcThemeNm(),
                Collectors.groupingBy(e -> e.getUnitSvcCd() != null ? e.getUnitSvcCd() : "")
            ));

        return grouped.entrySet().stream()
            .map(entry -> {
                String[] parts = entry.getKey().split("\\|", 2);
                List<HeatmapClfyInfo.UnitItem> items = entry.getValue().entrySet().stream()
                    .map(unitEntry -> {
                        VwIocEvetInfo first = unitEntry.getValue().get(0);
                        return new HeatmapClfyInfo.UnitItem(
                            unitEntry.getKey(),
                            first.getUnitSvcNm() != null ? first.getUnitSvcNm() : ""
                        );
                    })
                    .sorted(Comparator.comparing(HeatmapClfyInfo.UnitItem::unitCd))
                    .collect(Collectors.toList());
                return new HeatmapClfyInfo(parts[0], parts.length > 1 ? parts[1] : "", items);
            })
            .collect(Collectors.toList());
    }
}
