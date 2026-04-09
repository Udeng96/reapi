package com.eseict.reapi.dstr.facility.service;

import com.eseict.reapi.common.domain.fms.ErfFacClfy;
import com.eseict.reapi.common.domain.fms.ErfFacInfo;
import com.eseict.reapi.common.domain.fms.ErfPosCrdnt;
import com.eseict.reapi.dstr.event.dto.Paging;
import com.eseict.reapi.dstr.facility.dto.CctvClfyDto;
import com.eseict.reapi.dstr.facility.dto.GisFacInfoDto;
import com.eseict.reapi.dstr.facility.repository.FacClfyRepository;
import com.eseict.reapi.dstr.facility.repository.FacilityCriteriaRepository;
import com.eseict.reapi.dstr.facility.repository.FacilityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final FacilityCriteriaRepository facilityCriteriaRepository;
    private final FacClfyRepository facClfyRepository;

    public FacilityService(FacilityRepository facilityRepository,
                           FacilityCriteriaRepository facilityCriteriaRepository,
                           FacClfyRepository facClfyRepository) {
        this.facilityRepository = facilityRepository;
        this.facilityCriteriaRepository = facilityCriteriaRepository;
        this.facClfyRepository = facClfyRepository;
    }

    public Paging<GisFacInfoDto> getFacList(String type, String facNm, int page, int rows) {
        String facClfyNm = "cctv".equalsIgnoreCase(type) ? "cctv" : "fac";
        long total = facilityCriteriaRepository.countFacList(type, facNm, facClfyNm);
        List<ErfFacInfo> list = facilityCriteriaRepository.getFacList(type, facNm, page, rows, facClfyNm);
        List<GisFacInfoDto> dtoList = list.stream().map(this::toDto).collect(Collectors.toList());
        return new Paging<>(total, dtoList);
    }

    public GisFacInfoDto getFac(String facId) {
        ErfFacInfo fac = facilityRepository.getFacById(facId);
        return fac != null ? toDto(fac) : null;
    }

    public List<GisFacInfoDto> getFacListByClfyId(String clfyId) {
        return facilityRepository.getFacListByClfyId(clfyId).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public List<CctvClfyDto> getCctvClfy() {
        return facClfyRepository.getCctvClfy();
    }

    /** 시설물 분류 전체 목록 (CCTV 제외) */
    public List<ErfFacClfy> getFacClfy() {
        return facClfyRepository.getFacClfy();
    }

    /** 특정 분류 코드 조회 */
    public ErfFacClfy getFacClfyByCd(String clfyCd) {
        return facClfyRepository.findById(clfyCd).orElse(null);
    }

    /** 전체 시설물 통합 목록 (CCTV 포함) */
    public Paging<GisFacInfoDto> getFacAllList(String type, String facNm, int page, int rows) {
        long total = facilityCriteriaRepository.countFacList(type, facNm, null);
        List<ErfFacInfo> list = facilityCriteriaRepository.getFacList(type, facNm, page, rows, null);
        List<GisFacInfoDto> dtoList = list.stream().map(this::toDto).collect(Collectors.toList());
        return new Paging<>(total, dtoList);
    }

    private GisFacInfoDto toDto(ErfFacInfo fac) {
        String x = null, y = null;
        if (fac.getRefErfPosCrdnts() != null && !fac.getRefErfPosCrdnts().isEmpty()) {
            ErfPosCrdnt pos = fac.getRefErfPosCrdnts().get(0);
            x = pos.getXCrdnt();
            y = pos.getYCrdnt();
        }
        String facClfyNm = fac.getRefErfFacClfy() != null ? fac.getRefErfFacClfy().getFacClfyNm() : null;
        boolean ptzYn = fac.getMgtNo() != null && !fac.getMgtNo().isBlank();
        return GisFacInfoDto.of(fac.getFacId(), fac.getFacNm(), fac.getFacClfyId(), facClfyNm,
                                fac.getMgtNo(), fac.getAddrShort(), y, x, ptzYn);
    }
}
