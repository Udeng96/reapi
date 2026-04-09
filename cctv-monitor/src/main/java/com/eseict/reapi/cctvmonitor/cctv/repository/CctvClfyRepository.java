package com.eseict.reapi.cctvmonitor.cctv.repository;

import com.eseict.reapi.common.domain.fms.ErfFacClfy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CctvClfyRepository extends JpaRepository<ErfFacClfy, String> {

    @Query("SELECT c FROM ErfFacClfy c WHERE LOWER(c.facClfyNm) LIKE '%cctv%' AND c.useYn = 1")
    List<ErfFacClfy> findCctvClfy();
}
