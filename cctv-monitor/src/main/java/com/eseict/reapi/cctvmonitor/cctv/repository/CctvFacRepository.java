package com.eseict.reapi.cctvmonitor.cctv.repository;

import com.eseict.reapi.common.domain.fms.ErfFacInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CctvFacRepository extends JpaRepository<ErfFacInfo, String> {

    @Query("SELECT f FROM ErfFacInfo f " +
           "LEFT JOIN FETCH f.refErfFacClfy c " +
           "LEFT JOIN FETCH f.refErfPosCrdnts p " +
           "WHERE f.facClfyId = :clfyId AND f.useYn = 1")
    List<ErfFacInfo> findByClfyId(@Param("clfyId") String clfyId);

    @Query(value = "SELECT f FROM ErfFacInfo f " +
           "LEFT JOIN FETCH f.refErfFacClfy c " +
           "WHERE f.useYn = 1 AND LOWER(c.facClfyNm) LIKE '%cctv%'",
           countQuery = "SELECT COUNT(f) FROM ErfFacInfo f " +
           "LEFT JOIN f.refErfFacClfy c " +
           "WHERE f.useYn = 1 AND LOWER(c.facClfyNm) LIKE '%cctv%'")
    Page<ErfFacInfo> findAllCctv(Pageable pageable);

    @Query(value = "SELECT f FROM ErfFacInfo f " +
           "LEFT JOIN FETCH f.refErfFacClfy c " +
           "WHERE f.useYn = 1 AND LOWER(c.facClfyNm) LIKE '%cctv%' " +
           "AND LOWER(f.facNm) LIKE LOWER(CONCAT('%', :name, '%'))",
           countQuery = "SELECT COUNT(f) FROM ErfFacInfo f " +
           "LEFT JOIN f.refErfFacClfy c " +
           "WHERE f.useYn = 1 AND LOWER(c.facClfyNm) LIKE '%cctv%' " +
           "AND LOWER(f.facNm) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<ErfFacInfo> findByName(@Param("name") String name, Pageable pageable);
}
