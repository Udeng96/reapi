package com.eseict.apiServer.repository.rino.event

import com.eseict.apiServer.domain.rino.event.IocStatEvetItemInfo
import com.eseict.apiServer.domain.rino.event.IocStatEvetItemInfoEntityPK
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/**
 * ioc item과 관련된 interface
 */
interface EventItemRepo : JpaRepository<IocStatEvetItemInfo, IocStatEvetItemInfoEntityPK> {

    @Query(
        """
        SELECT ITEM
        FROM IocStatEvetItemInfo ITEM
        WHERE CONCAT(ITEM.clientCd,'-',ITEM.siteCd , '-', ITEM.znCd , ITEM.svcThemeCd, ITEM.unitSvcCd, ITEM.evetGbCd, ITEM.statEvetCd) = :evetId
    """
    )
    fun findByEvetId(evetId: String): List<IocStatEvetItemInfo>?

}


