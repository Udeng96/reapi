package com.eseict.apiServer.repository.rino.event

import com.eseict.apiServer.domain.rino.event.EventItem
import com.eseict.apiServer.domain.rino.event.VwIocEvetInfo
import com.eseict.apiServer.dto.common.ClfyInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


/**
 * api 스키마의 event item 뷰테이블 reposiotry
 */
@Repository
interface EventItemRepository : JpaRepository<VwIocEvetInfo, String> {

    @Query(
        """
        SELECT new com.eseict.apiServer.dto.common.ClfyInfo(SUBSTRING(E.eventId,4) , E.statEvetNm)
        FROM EventItem E
        WHERE 1=1
        AND ((:eventIdList) IS NOT NULL AND (SUBSTRING(E.eventId,4) IN (:eventIdList))) OR :eventIdList is null
    """
    )
    fun getTypeList(eventIdList: List<String>?): List<ClfyInfo>

    @Query(
        """
        SELECT DISTINCT new com.eseict.apiServer.dto.common.ClfyInfo( E.eventId, E.statEvetNm)
        FROM EventItem E
        WHERE 
        1=1
        AND ((:isFire = true AND E.svcThemeCd = 'FFT') OR (:isFire = false AND E.svcThemeCd <> 'FFT'))
    """
    )
    fun findTypes(isFire: Boolean): List<ClfyInfo>

    @Query(
        """
        SELECT  E
        FROM EventItem E
    """
    )
    fun findHeatmapClfy(sites: List<String>?): List<EventItem>

    @Query(
        """
        SELECT 
            new com.eseict.apiServer.domain.rino.event.VwIocEvetInfo(V.svcThemeCd,V.svcThemeNm,V.unitSvcCd,V.unitSvcNm)
        FROM VwIocEvetInfo V
        group by V.svcThemeCd , V.svcThemeNm,V.unitSvcCd, V.unitSvcNm
    """
    )
    fun findHeatmapClfy(): List<VwIocEvetInfo>
}