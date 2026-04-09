package com.eseict.reapi.dstr.event.repository;

import com.eseict.reapi.dstr.event.domain.IocStatEvetItemHist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventItemHistRepository extends JpaRepository<IocStatEvetItemHist, IocStatEvetItemHist.PK> {

    @Query("SELECT h FROM IocStatEvetItemHist h WHERE h.statEvetOutbSeqn = :seqn")
    List<IocStatEvetItemHist> findItemListBySeqn(@Param("seqn") String seqn);
}
