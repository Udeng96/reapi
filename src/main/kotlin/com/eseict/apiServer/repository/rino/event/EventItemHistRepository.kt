package com.eseict.apiServer.repository.rino.event

import com.eseict.apiServer.domain.rino.event.IocStatEvetItemHist
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface EventItemHistRepository : JpaRepository<IocStatEvetItemHist, String> {
    @Query(
        """
        SELECT I
        FROM IocStatEvetItemHist I
        WHERE I.statEvetOutbSeqn = :id
    """
    )
    fun findItemHistWithId(id: String): IocStatEvetItemHist


    @Query(
        """
        SELECT I
        FROM IocStatEvetItemHist I
        WHERE I.statEvetOutbSeqn = :seqn
    """
    )
    fun findItemListBySeqn(seqn: String): List<IocStatEvetItemHist>
}