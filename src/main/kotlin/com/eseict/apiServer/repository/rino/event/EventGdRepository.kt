package com.eseict.apiServer.repository.rino.event

import com.eseict.apiServer.domain.rino.event.IocStatEvetGdInfo
import com.eseict.apiServer.domain.rino.event.IocStatEvetGdInfoEntityPK
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface EventGdRepository : JpaRepository<IocStatEvetGdInfo, IocStatEvetGdInfoEntityPK> {


    @Query(
        """
        SELECT GD
        FROM IocStatEvetGdInfo GD
        WHERE CONCAT(GD.clientCd,'-',GD.siteCd , '-', GD.znCd , GD.svcThemeCd, GD.unitSvcCd, GD.evetGbCd, GD.statEvetCd) = :evetId
    """
    )
    fun findByEvetId(evetId: String): List<IocStatEvetGdInfo>?
}