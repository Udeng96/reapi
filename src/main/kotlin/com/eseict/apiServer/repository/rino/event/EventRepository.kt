package com.eseict.apiServer.repository.rino.event

import com.eseict.apiServer.domain.rino.event.Event
import com.eseict.apiServer.dto.common.ClfyInfo
import com.eseict.apiServer.dto.event.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface EventRepository : JpaRepository<Event, String> {
    //        SELECT E.id AS statEvetOutbSeqn, CONCAT(E.zoneId, E.svcThemeCd, E.unitSvcCd, E.evetGbCd, E.statEvetCd) AS eventId,
//                E.statEvetNm AS eventNm, E.siteCd AS locationCd, E.siteNm AS locationNm, E.x AS lat, E.y AS lng,
//                E.outbPlac AS addr, E.statEvetCntn AS cntn, E.outbDtm AS outbDtm, E.clsDtm AS clsDtm
    @Query(
        """
      SELECT new com.eseict.apiServer.dto.event.EventsDto(EVENT.id, CONCAT(EVENT.zoneId, EVENT.svcThemeCd, EVENT.unitSvcCd, EVENT.evetGbCd, EVENT.statEvetCd),
                EVENT.statEvetNm, EVENT.zoneId, EVENT.znNm, EVENT.x, EVENT.y, EVENT.outbPlac, EVENT.statEvetCntn, EVENT.outbDtm, EVENT.clsDtm,
                EVENT.uSvcOutbId
                )           
        FROM Event EVENT
        WHERE 
            ((:isFire = true AND EVENT.svcThemeCd = 'FFT') OR (:isFire = false AND EVENT.svcThemeCd <> 'FFT'))
        AND 
            (:isEvent = false OR CONCAT(EVENT.zoneId, EVENT.svcThemeCd, EVENT.unitSvcCd, EVENT.evetGbCd, EVENT.statEvetCd) IN :eventIdList)
        AND 
            1=1
        AND 
            (:startDtm = '' OR EVENT.outbDtm > :startDtm)
        AND 
            (:endDtm = '' OR EVENT.outbDtm < :endDtm)
    """
    )
    fun findEvents(
        isFire: Boolean, isEvent: Boolean, eventIdList: List<String>,
//                   isLocation:Boolean,
//                   locationCdList: List<String>,
        startDtm: String, endDtm: String, pageable: Pageable
    ): Page<EventsDto>?

    @Query(
        """
        SELECT new com.eseict.apiServer.dto.event.EventsDto(E.id, CONCAT(E.zoneId, E.svcThemeCd, E.unitSvcCd, E.evetGbCd, E.statEvetCd) ,
                E.statEvetNm, E.zoneId, E.znNm , E.y , E.x ,
                E.outbPlac , E.statEvetCntn , E.outbDtm , E.clsDtm ,
                E.uSvcOutbId )
        FROM Event E
        WHERE E.id = :id
    """
    )
    fun findEvent(id: String): EventInfo


    @Query(
        """
        SELECT new com.eseict.apiServer.dto.event.EventsDto(EVENT.id, CONCAT(EVENT.zoneId, EVENT.svcThemeCd, EVENT.unitSvcCd, EVENT.evetGbCd, EVENT.statEvetCd),
                EVENT.statEvetNm, EVENT.zoneId, EVENT.znNm, EVENT.y, EVENT.x, EVENT.outbPlac, EVENT.statEvetCntn, EVENT.outbDtm, EVENT.clsDtm,
                EVENT.uSvcOutbId
            )  
        FROM Event EVENT
        WHERE EVENT.uSvcOutbId = :id
    """
    )
    fun findEventByUSvcId(id: String): EventInfo

    @Query(
        """
        SELECT new com.eseict.apiServer.dto.common.ClfyInfo( CONCAT(E.svcThemeCd, '_', E.unitSvcCd), E.unitSvcNm) 
        FROM Event E
        WHERE 
            1=1 
        AND
            ((:isFire = true AND E.svcThemeCd = 'FFT') OR (:isFire = false AND E.svcThemeCd <> 'FFT'))
    """
    )
    fun findTypes(isFire: Boolean): List<ClfyInfo>

    @Query(
        """
        SELECT E.x as lng, E.y as lat, COUNT(E.id) AS count
        FROM Event E
        WHERE
         1=1
        AND E.svcThemeCd = :svcThemeCd AND E.unitSvcCd = :unitCd
        AND E.evetCd in :eventAuthList
        AND 
            (:startDtm = '' OR E.outbDtm >= :startDtm)
        AND 
            (:endDtm = '' OR E.outbDtm <= :endDtm)
        GROUP BY E.x, E.y
    """
    )
    fun findHeatmapCount(
        svcThemeCd: String,
        unitCd: String,
        startDtm: String?,
        endDtm: String?,
        eventAuthList: List<String>?
    ): List<HeatmapCount>


    @Query(
        """
        
        SELECT HIST.statEvetOutbSeqn AS statEvetOutbSeqn, CONCAT(HIST.znCd, HIST.svcThemeCd, HIST.unitSvcCd, HIST.evetGbCd, HIST.statEvetCd) AS eventId,
                '', POS.YCrdnt as lat , POS.XCrdnt as lng,
                HIST.outbPlac AS addr, HIST.statEvetCntn AS cntn, HIST.outbDtm AS outbDtm, HIST.clsDtm AS clsDtm,
                IH.itemVal AS accidentId, HIST.statEvetGdCd AS eventGdCd, HIST.uSvcOutbId AS uid
                
            FROM IocStatEvetOutbHist HIST
            LEFT OUTER JOIN IocStatEvetItemHist IH ON (IH.itemNm = 'accidentId' AND IH.statEvetOutbSeqn = HIST.statEvetOutbSeqn)
            INNER JOIN HIST.iocStatEvetPosHists POS

            WHERE 
                (:eventId = '' OR CONCAT(HIST.znCd, HIST.svcThemeCd, HIST.unitSvcCd, HIST.evetGbCd, HIST.statEvetCd) IN :eventId)
                AND 
                (IH.itemVal = :accidentId OR :accidentId = '')
                 AND 
                (:startDtm = '' OR HIST.outbDtm >= :startDtm)
                 AND 
                (:endDtm = '' OR HIST.outbDtm <= :endDtm)

    """
    )
    fun findEventWithAccidentId(
        accidentId: String?,
        eventId: String?,
        startDtm: String?,
        endDtm: String?
    ): List<EventHist>?

    @Query(
        """
        
        SELECT HIST.statEvetOutbSeqn AS statEvetOutbSeqn, CONCAT(HIST.znCd, HIST.svcThemeCd, HIST.unitSvcCd, HIST.evetGbCd, HIST.statEvetCd) AS eventId,
                '', POS.YCrdnt as lat , POS.XCrdnt as lng,
                HIST.outbPlac AS addr, HIST.statEvetCntn AS cntn, HIST.outbDtm AS outbDtm, HIST.clsDtm AS clsDtm,
                IH.itemVal AS accidentId, HIST.statEvetGdCd AS eventGdCd, HIST.uSvcOutbId AS uid
                
            FROM IocStatEvetOutbHist HIST
            LEFT OUTER JOIN IocStatEvetItemHist IH ON (IH.itemNm = 'accidentId' AND IH.statEvetOutbSeqn = HIST.statEvetOutbSeqn)
            INNER JOIN HIST.iocStatEvetPosHists POS

            WHERE 
                (CONCAT(HIST.znCd, HIST.svcThemeCd, HIST.unitSvcCd, HIST.evetGbCd, HIST.statEvetCd) IN :eventId)
                AND 
                (IH.itemVal = :accidentId OR :accidentId = '')
                 AND 
                (:startDtm = '' OR HIST.outbDtm >= :startDtm)
                 AND 
                (:endDtm = '' OR HIST.outbDtm <= :endDtm)

    """
    )
    fun findEventsWithAccidentId(
        accidentId: String?,
        eventId: List<String>,
        startDtm: String?,
        endDtm: String?
    ): List<EventHist>?


    @Query(
        """
        SELECT EV.clsDtm AS clrDtm, '' AS evetCd , EV.statEvetNm AS evetCdNm,
            EV.evetGbCd AS evetGbCd, EV.outbDtm AS outbDtm, EV.outbPlac AS outbPlac, EV.procSt AS procSt, 
            CASE WHEN EV.procSt = '1' THEN '발생'
                 WHEN EV.procSt = '2' THEN '지령'
                 WHEN EV.procSt = '3' THEN '조치중'
                 WHEN EV.procSt = '4' THEN '해제'
                 WHEN EV.procSt = '5' THEN '종료' 
                 ELSE '' END AS procStNm,
            EV.statEvetCd AS statEvetCd, EV.statEvetCntn AS statEvetCntn, EV.statEvetGdCd AS statEvetGdCd, EV.id AS statEvetOutbSeqn,
            EV.svcThemeCd AS svcThemeCd, EV.svcThemeNm AS svcThemeCdNm, EV.uSvcOutbId AS usvcOutbId, EV.unitSvcCd AS unitSvcCd, 
            EV.unitSvcNm AS unitSvcCdNm, EV.x AS xcrdnt, EV.y AS ycrdnt, EV.zoneId AS znCd, EV.znNm AS znCdNm
            
        FROM Event EV
        INNER JOIN IocStatEvetGdInfo GD ON (
            EV.zoneId = GD.znCd AND EV.unitSvcCd = GD.unitSvcCd AND EV.svcThemeCd = GD.svcThemeCd AND 
            EV.statEvetCd = GD.statEvetCd AND EV.statEvetGdCd = GD.statEvetGdCd 
        )
        WHERE 
        (:startDtm = '' OR EV.outbDtm >= :startDtm)
                 AND 
                (:endDtm = '' OR EV.outbDtm <= :endDtm)
                
        ORDER BY EV.outbDtm DESC
    """
    )
    fun findDstrEvents(pageable: Pageable, startDtm: String, endDtm: String): Page<DstrInfo>


    @Query(
        """
        SELECT EV.clsDtm AS clrDtm,'', EV.statEvetNm AS evetCdNm,
            EV.evetGbCd AS evetGbCd, EV.outbDtm AS outbDtm, EV.outbPlac AS outbPlac, EV.procSt AS procSt, 
            CASE WHEN EV.procSt = '1' THEN '발생'
                 WHEN EV.procSt = '2' THEN '지령'
                 WHEN EV.procSt = '3' THEN '조치중'
                 WHEN EV.procSt = '4' THEN '해제'
                 WHEN EV.procSt = '5' THEN '종료' 
                 ELSE '' END AS procStNm,
            EV.statEvetCd AS statEvetCd, EV.statEvetCntn AS statEvetCntn, EV.statEvetGdCd AS statEvetGdCd, EV.id AS statEvetOutbSeqn,
            EV.svcThemeCd AS svcThemeCd, EV.svcThemeNm AS svcThemeCdNm, EV.uSvcOutbId AS usvcOutbId, EV.unitSvcCd AS unitSvcCd, 
            EV.unitSvcNm AS unitSvcCdNm, EV.x AS xcrdnt, EV.y AS ycrdnt, EV.zoneId AS znCd, EV.znNm AS znCdNm
            
        FROM Event EV
        INNER JOIN IocStatEvetGdInfo GD ON (
            EV.zoneId = GD.znCd AND EV.unitSvcCd = GD.unitSvcCd AND EV.svcThemeCd = GD.svcThemeCd AND 
            EV.statEvetCd = GD.statEvetCd AND EV.statEvetGdCd = GD.statEvetGdCd
        )
        WHERE 
            EV.uSvcOutbId = :uSvcOutbId
        ORDER BY EV.outbDtm DESC
    """
    )
    fun findDstrEventByUSvcOutbId(uSvcOutbId: String): DstrInfo
}