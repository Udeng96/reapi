package com.eseict.reapi.cctvmonitor.cctv.service;

import com.eseict.reapi.common.domain.fms.ErfFacClfy;
import com.eseict.reapi.common.domain.fms.ErfFacInfo;
import com.eseict.reapi.common.domain.fms.ErfPosCrdnt;
import com.eseict.reapi.cctvmonitor.cctv.dto.CctvDetailResponse;
import com.eseict.reapi.cctvmonitor.cctv.dto.CctvMonitorResponse;
import com.eseict.reapi.cctvmonitor.cctv.dto.CctvMonitorResponse.CctvItemDto;
import com.eseict.reapi.cctvmonitor.cctv.repository.CctvClfyRepository;
import com.eseict.reapi.cctvmonitor.cctv.repository.CctvFacRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CctvQueryService {

    private final CctvClfyRepository cctvClfyRepository;
    private final CctvFacRepository cctvFacRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${cctv.ptz.ids:}")
    private String usePtzCctvIds;

    private List<CctvMonitorResponse> disasterWideList = new ArrayList<>();

    public CctvQueryService(CctvClfyRepository cctvClfyRepository, CctvFacRepository cctvFacRepository) {
        this.cctvClfyRepository = cctvClfyRepository;
        this.cctvFacRepository = cctvFacRepository;
    }

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void loadDisasterWideList() {
        try {
            ClassPathResource resource = new ClassPathResource("cctvData/disasterWideList.json");
            try (InputStream is = resource.getInputStream()) {
                List<Map<String, Object>> raw = objectMapper.readValue(is, new TypeReference<>() {});
                disasterWideList = raw.stream().map(group -> {
                    String clfyNm = (String) group.get("clfyNm");
                    String clfyId = (String) group.get("clfyId");
                    List<Map<String, Object>> cctvs = (List<Map<String, Object>>) group.get("cctvList");
                    List<CctvItemDto> items = cctvs == null ? new ArrayList<>() : cctvs.stream().map(c -> new CctvItemDto(
                        (String) c.get("facId"),
                        (String) c.get("facNm"),
                        c.get("mgtNo") != null ? (String) c.get("mgtNo") : (String) c.get("mgrNo"),
                        c.get("lat") instanceof Number ? ((Number) c.get("lat")).doubleValue() : null,
                        c.get("lng") instanceof Number ? ((Number) c.get("lng")).doubleValue() : null
                    )).collect(Collectors.toList());
                    return new CctvMonitorResponse(clfyNm, clfyId, items.size(), items);
                }).collect(Collectors.toList());
            }
        } catch (Exception e) {
            disasterWideList = new ArrayList<>();
        }
    }

    /** 분류별 CCTV 그룹 목록 (모니터 화면용) */
    public List<CctvMonitorResponse> getMonitorList(String clfyId) {
        List<ErfFacClfy> clfyList;

        if (clfyId != null && !clfyId.isBlank()) {
            clfyList = cctvClfyRepository.findById(clfyId)
                .map(List::of)
                .orElse(List.of());
        } else {
            clfyList = cctvClfyRepository.findCctvClfy();
        }

        List<CctvMonitorResponse> result = clfyList.stream()
            .map(clfy -> {
                List<ErfFacInfo> facList = cctvFacRepository.findByClfyId(clfy.getFacClfyId());
                List<CctvItemDto> cctvItems = facList.stream()
                    .filter(this::hasValidCoordinates)
                    .sorted(naturalNameComparator())
                    .map(this::toItemDto)
                    .collect(Collectors.toList());
                return new CctvMonitorResponse(
                    clfy.getFacClfyNm(),
                    clfy.getFacClfyId(),
                    cctvItems.size(),
                    cctvItems
                );
            })
            .collect(Collectors.toList());

        // 재난대응광역카메라 등 JSON 하드코딩 분류 머지
        if (clfyId == null || clfyId.isBlank()) {
            result.addAll(disasterWideList);
        } else {
            disasterWideList.stream()
                .filter(d -> d.clfyId().equals(clfyId))
                .findFirst()
                .ifPresent(result::add);
        }

        return result;
    }

    /** CCTV 목록 페이징 조회 */
    public Map<String, Object> getList(String cctvNm, int page, int rows) {
        Page<ErfFacInfo> result;
        PageRequest pageRequest = PageRequest.of(page - 1, rows);

        if (cctvNm != null && !cctvNm.isBlank()) {
            result = cctvFacRepository.findByName(cctvNm, pageRequest);
        } else {
            result = cctvFacRepository.findAllCctv(pageRequest);
        }

        List<CctvItemDto> items = result.getContent().stream()
            .map(this::toItemDto)
            .collect(Collectors.toList());

        return Map.of(
            "records", result.getTotalElements(),
            "total", result.getTotalPages(),
            "page", page,
            "rows", items
        );
    }

    /** CCTV 단건 상세 */
    public CctvDetailResponse getDetail(String cctvId) {
        return cctvFacRepository.findById(cctvId)
            .map(fac -> {
                Double lat = null, lng = null;
                if (fac.getRefErfPosCrdnts() != null && !fac.getRefErfPosCrdnts().isEmpty()) {
                    ErfPosCrdnt pos = fac.getRefErfPosCrdnts().get(0);
                    lat = parseDouble(pos.getYCrdnt());
                    lng = parseDouble(pos.getXCrdnt());
                }
                String clfyNm = fac.getRefErfFacClfy() != null ? fac.getRefErfFacClfy().getFacClfyNm() : null;
                boolean ptzYn = isPtzEnabled(fac.getMgtNo());
                return new CctvDetailResponse(
                    fac.getFacId(), fac.getFacNm(), fac.getMgtNo(),
                    clfyNm, fac.getAddrShort(), lat, lng, ptzYn
                );
            })
            .orElse(null);
    }

    /** CCTV 분류 목록 */
    public List<ErfFacClfy> getClfyList() {
        return cctvClfyRepository.findCctvClfy();
    }

    private CctvItemDto toItemDto(ErfFacInfo fac) {
        Double lat = null, lng = null;
        if (fac.getRefErfPosCrdnts() != null && !fac.getRefErfPosCrdnts().isEmpty()) {
            ErfPosCrdnt pos = fac.getRefErfPosCrdnts().get(0);
            lat = parseDouble(pos.getYCrdnt());
            lng = parseDouble(pos.getXCrdnt());
        }
        return new CctvItemDto(fac.getFacId(), fac.getFacNm(), fac.getMgtNo(), lat, lng);
    }

    /** 좌표 유효성 검증 (위경도 모두 있고 '.'이 포함돼야 함) */
    private boolean hasValidCoordinates(ErfFacInfo fac) {
        if (fac.getRefErfPosCrdnts() == null || fac.getRefErfPosCrdnts().isEmpty()) return false;
        ErfPosCrdnt pos = fac.getRefErfPosCrdnts().get(0);
        return isValidCoord(pos.getXCrdnt()) && isValidCoord(pos.getYCrdnt());
    }

    private boolean isValidCoord(String coord) {
        return coord != null && !coord.isBlank() && coord.contains(".");
    }

    /** 이름 기준 자연수 정렬 — 숫자로 시작하면 숫자 오름차순, 아니면 가나다 오름차순 */
    private static final Pattern LEADING_NUMBER = Pattern.compile("^(\\d+)");

    private int naturalSortKey(String name) {
        if (name == null) return Integer.MAX_VALUE;
        Matcher m = LEADING_NUMBER.matcher(name);
        return m.find() ? Integer.parseInt(m.group(1)) : Integer.MAX_VALUE;
    }

    private Comparator<ErfFacInfo> naturalNameComparator() {
        return Comparator
            .comparingInt((ErfFacInfo f) -> naturalSortKey(f.getFacNm()))
            .thenComparing(f -> f.getFacNm() == null ? "" : f.getFacNm());
    }

    private boolean isPtzEnabled(String mgtNo) {
        if (usePtzCctvIds == null || usePtzCctvIds.isBlank() || mgtNo == null) return false;
        return Arrays.asList(usePtzCctvIds.split(",")).contains(mgtNo);
    }

    private Double parseDouble(String value) {
        if (value == null || value.isBlank()) return null;
        try { return Double.parseDouble(value); }
        catch (NumberFormatException e) { return null; }
    }
}
