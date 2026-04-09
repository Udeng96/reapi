package com.eseict.reapi.dstr.facility.repository;

import com.eseict.reapi.common.domain.fms.ErfFacInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FacilityRepository extends JpaRepository<ErfFacInfo, String> {

    @Query("SELECT f FROM ErfFacInfo f " +
           "LEFT JOIN FETCH f.refErfFacClfy c " +
           "LEFT JOIN FETCH f.refErfPosCrdnts p " +
           "WHERE f.facId = :facId AND f.useYn = 1")
    ErfFacInfo getFacById(@Param("facId") String facId);

    @Query("SELECT f FROM ErfFacInfo f " +
           "LEFT JOIN FETCH f.refErfFacClfy c " +
           "LEFT JOIN FETCH f.refErfPosCrdnts p " +
           "WHERE f.facClfyId = :clfyId AND f.useYn = 1")
    List<ErfFacInfo> getFacListByClfyId(@Param("clfyId") String clfyId);
}
