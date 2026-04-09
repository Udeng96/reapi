package com.eseict.reapi.dstr.mobile.repository;

import com.eseict.reapi.dstr.mobile.domain.MonMbDesc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MobileLiveRepository extends JpaRepository<MonMbDesc, String> {

    @Query("SELECT m FROM MonMbDesc m WHERE m.mobileChk IN (2, 3) ORDER BY m.userDtm DESC")
    List<MonMbDesc> getLiveList();

    @Query("SELECT m FROM MonMbDesc m WHERE m.userId = :userId AND m.mobileChk IN (2, 3)")
    Optional<MonMbDesc> getLiveByUserId(@Param("userId") String userId);

    Optional<MonMbDesc> findByUserId(String userId);

    Optional<MonMbDesc> findByMgrNo(String mgrNo);
}
