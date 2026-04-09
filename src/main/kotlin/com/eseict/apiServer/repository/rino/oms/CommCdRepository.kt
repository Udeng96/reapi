package com.eseict.apiServer.repository.rino.oms

import com.eseict.apiServer.domain.rino.OmsCommCdInfo
import com.eseict.apiServer.dto.common.ClfyInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CommCdRepository : JpaRepository<OmsCommCdInfo, String> {

    @Query(
        """
        SELECT new com.eseict.apiServer.dto.common.ClfyInfo(COMM.cd, COMM.cdNm)
        FROM OmsCommCdInfo COMM 
        WHERE 
                COMM.grupCd = :grupCd
                AND COMM.grupYn = 'N'
        ORDER BY COMM.sort ASC
    """
    )
    fun findCommCdByGroupCd(grupCd: String): List<ClfyInfo>
}