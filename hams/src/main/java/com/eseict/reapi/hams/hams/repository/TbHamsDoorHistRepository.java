package com.eseict.reapi.hams.hams.repository;

import com.eseict.reapi.hams.hams.domain.TbHamsDoorHist;
import com.eseict.reapi.hams.hams.dto.HamsDoorLockStatDto;
import com.eseict.reapi.hams.hams.dto.HamsDoorStatDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TbHamsDoorHistRepository extends JpaRepository<TbHamsDoorHist, String> {

    @Query("SELECT h FROM TbHamsDoorHist h WHERE h.eqId = :eqId AND h.doorType = :doorType ORDER BY h.occurDate DESC")
    List<TbHamsDoorHist> selectDoorHist(@Param("eqId") String eqId, @Param("doorType") Character doorType);

    // ── Door state stats ──────────────────────────────────────────────────────
    // CTE로 to_char() 한 번만 계산 → GROUP BY alias 사용
    // 이유: JPQL에서 :dateFormat 파라미터를 SELECT/GROUP BY/ORDER BY에 반복하면
    //       Hibernate가 각각 다른 ? 위치로 생성 → PostgreSQL GROUP BY 불일치 오류
    @Query(value =
        "WITH dated AS (" +
        "  SELECT to_char(occur_date, :dateFormat) AS date_str, door_state" +
        "  FROM hs_s_service.tb_hams_door_hist" +
        "  WHERE (:eqId = '' OR eq_id = :eqId)" +
        "  AND (:doorType = '' OR CAST(door_type AS text) = :doorType)" +
        "  AND (:startDtm = '' OR to_char(occur_date, 'YYYYMMDD') >= :startDtm)" +
        "  AND (:endDtm   = '' OR to_char(occur_date, 'YYYYMMDD') <= :endDtm)" +
        ") " +
        "SELECT date_str," +
        "  SUM(CASE WHEN door_state = '2' THEN 1 ELSE 0 END) AS open_cnt," +
        "  SUM(CASE WHEN door_state = '3' THEN 1 ELSE 0 END) AS close_cnt " +
        "FROM dated " +
        "GROUP BY date_str " +
        "ORDER BY date_str DESC",
        nativeQuery = true)
    List<Object[]> getDoorStatRaw(
        @Param("eqId") String eqId,
        @Param("doorType") String doorType,
        @Param("startDtm") String startDtm,
        @Param("endDtm") String endDtm,
        @Param("dateFormat") String dateFormat
    );

    default List<HamsDoorStatDto> getDoorStatByDateFormat(
            String eqId, String doorType, String startDtm, String endDtm, String dateFormat) {
        return getDoorStatRaw(eqId, doorType, startDtm, endDtm, dateFormat).stream()
            .map(row -> new HamsDoorStatDto(
                (String) row[0],
                row[1] != null ? ((Number) row[1]).longValue() : 0L,
                row[2] != null ? ((Number) row[2]).longValue() : 0L
            ))
            .toList();
    }

    // ── Door lock stats ───────────────────────────────────────────────────────
    @Query(value =
        "WITH dated AS (" +
        "  SELECT to_char(occur_date, :dateFormat) AS date_str, door_state" +
        "  FROM hs_s_service.tb_hams_door_hist" +
        "  WHERE (:eqId = '' OR eq_id = :eqId)" +
        "  AND (:doorType = '' OR CAST(door_type AS text) = :doorType)" +
        "  AND (:startDtm = '' OR to_char(occur_date, 'YYYYMMDD') >= :startDtm)" +
        "  AND (:endDtm   = '' OR to_char(occur_date, 'YYYYMMDD') <= :endDtm)" +
        ") " +
        "SELECT date_str," +
        "  SUM(CASE WHEN door_state = '1' THEN 1 ELSE 0 END) AS none_cnt," +
        "  SUM(CASE WHEN door_state = '2' THEN 1 ELSE 0 END) AS unlock_cnt," +
        "  SUM(CASE WHEN door_state = '3' THEN 1 ELSE 0 END) AS lock_cnt," +
        "  SUM(CASE WHEN door_state = '4' THEN 1 ELSE 0 END) AS unrecognized_cnt," +
        "  SUM(CASE WHEN door_state = '5' THEN 1 ELSE 0 END) AS lock_error_cnt " +
        "FROM dated " +
        "GROUP BY date_str " +
        "ORDER BY date_str DESC",
        nativeQuery = true)
    List<Object[]> getDoorLockStatRaw(
        @Param("eqId") String eqId,
        @Param("doorType") String doorType,
        @Param("startDtm") String startDtm,
        @Param("endDtm") String endDtm,
        @Param("dateFormat") String dateFormat
    );

    default List<HamsDoorLockStatDto> getDoorLockStatByDateFormat(
            String eqId, String doorType, String startDtm, String endDtm, String dateFormat) {
        return getDoorLockStatRaw(eqId, doorType, startDtm, endDtm, dateFormat).stream()
            .map(row -> new HamsDoorLockStatDto(
                (String) row[0],
                row[1] != null ? ((Number) row[1]).longValue() : 0L,
                row[2] != null ? ((Number) row[2]).longValue() : 0L,
                row[3] != null ? ((Number) row[3]).longValue() : 0L,
                row[4] != null ? ((Number) row[4]).longValue() : 0L,
                row[5] != null ? ((Number) row[5]).longValue() : 0L
            ))
            .toList();
    }
}
