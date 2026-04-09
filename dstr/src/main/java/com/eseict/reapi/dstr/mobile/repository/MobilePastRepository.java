package com.eseict.reapi.dstr.mobile.repository;

import com.eseict.reapi.dstr.mobile.domain.MonMbDescLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MobilePastRepository extends JpaRepository<MonMbDescLog, Integer> {

    @Query("SELECT m FROM MonMbDescLog m " +
           "WHERE m.liveChk = 'vod' " +
           "AND SUBSTRING(m.userDtm, 1, 8) >= :startDtm " +
           "AND SUBSTRING(m.userDtm, 1, 8) <= :endDtm " +
           "ORDER BY m.userDtm DESC")
    Page<MonMbDescLog> getPastList(
        @Param("startDtm") String startDtm,
        @Param("endDtm") String endDtm,
        Pageable pageable
    );

    @Query("SELECT m FROM MonMbDescLog m WHERE m.mbSeq = :seq AND m.liveChk = 'vod'")
    Optional<MonMbDescLog> getPastBySeq(@Param("seq") Integer seq);
}
