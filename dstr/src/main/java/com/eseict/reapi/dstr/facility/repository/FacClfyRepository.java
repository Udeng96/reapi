package com.eseict.reapi.dstr.facility.repository;

import com.eseict.reapi.common.domain.fms.ErfFacClfy;
import com.eseict.reapi.dstr.facility.dto.CctvClfyDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FacClfyRepository extends JpaRepository<ErfFacClfy, String> {

    @Query("SELECT new com.eseict.reapi.dstr.facility.dto.CctvClfyDto(c.facClfyId, c.facClfyNm) " +
           "FROM ErfFacClfy c WHERE LOWER(c.facClfyNm) LIKE '%cctv%' AND c.useYn = 1")
    List<CctvClfyDto> getCctvClfy();

    @Query("SELECT c FROM ErfFacClfy c " +
           "WHERE LOWER(c.facClfyNm) NOT LIKE '%cctv%' AND c.useYn = 1 " +
           "ORDER BY c.facClfyNm")
    List<ErfFacClfy> getFacClfy();
}
