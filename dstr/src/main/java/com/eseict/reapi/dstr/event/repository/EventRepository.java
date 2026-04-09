package com.eseict.reapi.dstr.event.repository;

import com.eseict.reapi.dstr.event.domain.Event;
import com.eseict.reapi.dstr.event.dto.DstrInfo;
import com.eseict.reapi.dstr.event.dto.EventsDto;
import com.eseict.reapi.dstr.event.dto.HeatmapCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, String> {

    @Query("SELECT new com.eseict.reapi.dstr.event.dto.EventsDto(" +
           "E.id, E.statEvetId, E.statEvetNm, E.evetCd, E.znNm, " +
           "E.y, E.x, E.outbPlac, E.statEvetCntn, E.outbDtm, E.clsDtm, E.uSvcOutbId) " +
           "FROM Event E " +
           "WHERE (:isEvent = false OR E.statEvetId IN :eventIdList) " +
           "AND (:startDtm IS NULL OR E.outbDtm >= :startDtm) " +
           "AND (:endDtm IS NULL OR E.outbDtm <= :endDtm) " +
           "ORDER BY E.outbDtm DESC")
    List<EventsDto> findEvents(
        @Param("isEvent") boolean isEvent,
        @Param("eventIdList") List<String> eventIdList,
        @Param("startDtm") String startDtm,
        @Param("endDtm") String endDtm,
        Pageable pageable
    );

    @Query("SELECT new com.eseict.reapi.dstr.event.dto.EventsDto(" +
           "E.id, E.statEvetId, E.statEvetNm, E.evetCd, E.znNm, " +
           "E.y, E.x, E.outbPlac, E.statEvetCntn, E.outbDtm, E.clsDtm, E.uSvcOutbId) " +
           "FROM Event E WHERE E.id = :id")
    EventsDto findEvent(@Param("id") String id);

    @Query("SELECT new com.eseict.reapi.dstr.event.dto.EventsDto(" +
           "E.id, E.statEvetId, E.statEvetNm, E.evetCd, E.znNm, " +
           "E.y, E.x, E.outbPlac, E.statEvetCntn, E.outbDtm, E.clsDtm, E.uSvcOutbId) " +
           "FROM Event E WHERE E.uSvcOutbId = :id")
    EventsDto findEventByUSvcId(@Param("id") String id);

    // Heatmap count
    @Query("SELECT E.x as lng, E.y as lat, COUNT(E.id) AS count " +
           "FROM Event E " +
           "WHERE E.svcThemeCd = :svcThemeCd AND E.unitSvcCd = :unitCd " +
           "AND E.evetCd IN :eventAuthList " +
           "AND (:startDtm = '' OR E.outbDtm >= :startDtm) " +
           "AND (:endDtm = '' OR E.outbDtm <= :endDtm) " +
           "GROUP BY E.x, E.y")
    List<HeatmapCount> findHeatmapCount(
        @Param("svcThemeCd") String svcThemeCd,
        @Param("unitCd") String unitCd,
        @Param("startDtm") String startDtm,
        @Param("endDtm") String endDtm,
        @Param("eventAuthList") List<String> eventAuthList
    );

    // Dstr events
    @Query("SELECT EV.clsDtm AS clrDtm, EV.statEvetNm AS evetCdNm, " +
           "EV.evetGbCd AS evetGbCd, EV.outbDtm AS outbDtm, EV.outbPlac AS outbPlac, EV.procSt AS procSt, " +
           "CASE WHEN EV.procSt = '1' THEN '발생' " +
           "     WHEN EV.procSt = '2' THEN '지령' " +
           "     WHEN EV.procSt = '3' THEN '조치중' " +
           "     WHEN EV.procSt = '4' THEN '해제' " +
           "     WHEN EV.procSt = '5' THEN '종료' " +
           "     ELSE '' END AS procStNm, " +
           "EV.statEvetCd AS statEvetCd, EV.statEvetCntn AS statEvetCntn, EV.statEvetGdCd AS statEvetGdCd, EV.id AS statEvetOutbSeqn, " +
           "EV.svcThemeCd AS svcThemeCd, EV.svcThemeNm AS svcThemeCdNm, EV.uSvcOutbId AS usvcOutbId, EV.unitSvcCd AS unitSvcCd, " +
           "EV.unitSvcNm AS unitSvcCdNm, EV.x AS xcrdnt, EV.y AS ycrdnt, EV.zoneId AS znCd, EV.znNm AS znCdNm " +
           "FROM Event EV " +
           "INNER JOIN IocStatEvetGdInfo GD ON (" +
           "EV.zoneId = GD.znCd AND EV.unitSvcCd = GD.unitSvcCd AND EV.svcThemeCd = GD.svcThemeCd AND " +
           "EV.statEvetCd = GD.statEvetCd AND EV.statEvetGdCd = GD.statEvetGdCd) " +
           "WHERE (:startDtm = '' OR EV.outbDtm >= :startDtm) " +
           "AND (:endDtm = '' OR EV.outbDtm <= :endDtm) " +
           "ORDER BY EV.outbDtm DESC")
    Page<DstrInfo> findDstrEvents(Pageable pageable, @Param("startDtm") String startDtm, @Param("endDtm") String endDtm);

    // Dstr single event
    @Query("SELECT EV.clsDtm AS clrDtm, EV.statEvetNm AS evetCdNm, " +
           "EV.evetGbCd AS evetGbCd, EV.outbDtm AS outbDtm, EV.outbPlac AS outbPlac, EV.procSt AS procSt, " +
           "CASE WHEN EV.procSt = '1' THEN '발생' " +
           "     WHEN EV.procSt = '2' THEN '지령' " +
           "     WHEN EV.procSt = '3' THEN '조치중' " +
           "     WHEN EV.procSt = '4' THEN '해제' " +
           "     WHEN EV.procSt = '5' THEN '종료' " +
           "     ELSE '' END AS procStNm, " +
           "EV.statEvetCd AS statEvetCd, EV.statEvetCntn AS statEvetCntn, EV.statEvetGdCd AS statEvetGdCd, EV.id AS statEvetOutbSeqn, " +
           "EV.svcThemeCd AS svcThemeCd, EV.svcThemeNm AS svcThemeCdNm, EV.uSvcOutbId AS usvcOutbId, EV.unitSvcCd AS unitSvcCd, " +
           "EV.unitSvcNm AS unitSvcCdNm, EV.x AS xcrdnt, EV.y AS ycrdnt, EV.zoneId AS znCd, EV.znNm AS znCdNm " +
           "FROM Event EV " +
           "INNER JOIN IocStatEvetGdInfo GD ON (" +
           "EV.zoneId = GD.znCd AND EV.unitSvcCd = GD.unitSvcCd AND EV.svcThemeCd = GD.svcThemeCd AND " +
           "EV.statEvetCd = GD.statEvetCd AND EV.statEvetGdCd = GD.statEvetGdCd) " +
           "WHERE EV.uSvcOutbId = :uSvcOutbId " +
           "ORDER BY EV.outbDtm DESC")
    DstrInfo findDstrEventByUSvcOutbId(@Param("uSvcOutbId") String uSvcOutbId);
}
