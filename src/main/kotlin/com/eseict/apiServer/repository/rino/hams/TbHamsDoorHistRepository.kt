package com.eseict.apiServer.repository.rino.hams

import com.eseict.apiServer.domain.rino.hams.TbHamsDoorHist
import com.eseict.apiServer.dto.hams.stat.HamsDoorLockStat
import com.eseict.apiServer.dto.hams.stat.HamsDoorStat
import com.eseict.apiServer.dto.hams.stat.HamsStat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TbHamsDoorHistRepository : JpaRepository<TbHamsDoorHist, String> {

    @Query(
        """
        select DH
        from TbHamsDoorHist DH
        where 1=1
        and (DH.eqId = :eqId)
        and DH.doorType = :doorType
    """
    )

    fun selectDoorHist(eqId: String, doorType: Char): List<TbHamsDoorHist>?


    @Query(
        """
        SELECT new com.eseict.apiServer.dto.hams.stat.HamsDoorStat(
            TO_CHAR(DH.occurDate , 'yyyy') ,
             sum((case when DH.doorState = '2' then 1 else 0 end)),
             sum((case when DH.doorState = '3' then 1 else 0 end))
            )
            
            
        FROM TbHamsDoorHist DH
            where
                (:startDtm = '' OR TO_CHAR(DH.occurDate , 'yyyyMMdd')  >= :startDtm)
                AND
                 (:endDtm = '' OR  TO_CHAR(DH.occurDate , 'yyyyMMdd') <= :endDtm )
                 AND 
                 (:eqId = '' OR :eqId = DH.eqId)
                 AND 
                 (:doorType = '' OR :doorType = DH.doorType)
                 
            GROUP BY TO_CHAR(DH.occurDate , 'yyyy') 
            ORDER BY TO_CHAR(DH.occurDate , 'yyyy') DESC
    """
    )
    fun getDoorStateHist4Y(
        eqId: String?,
        doorType: String,
        startDtm: String?,
        endDtm: String?
    ): MutableList<HamsDoorStat>?

    @Query(
        """
        SELECT new com.eseict.apiServer.dto.hams.stat.HamsDoorStat(
            TO_CHAR(DH.occurDate , 'yyyyMM') ,
             sum((case when DH.doorState = '2' then 1 else 0 end)),
             sum((case when DH.doorState = '3' then 1 else 0 end))
            )


        FROM TbHamsDoorHist DH
            where
                (:startDtm = '' OR TO_CHAR(DH.occurDate , 'yyyyMMdd')  >= :startDtm)
                AND
                 (:endDtm = '' OR  TO_CHAR(DH.occurDate , 'yyyyMMdd') <= :endDtm )
                 AND
                 (:eqId = '' OR :eqId = DH.eqId)
                 AND
                 (:doorType = '' OR :doorType = DH.doorType)

            GROUP BY TO_CHAR(DH.occurDate , 'yyyyMM')
            ORDER BY TO_CHAR(DH.occurDate , 'yyyyMM') DESC
    """
    )
    fun getDoorStateHist4YM(
        eqId: String?,
        doorType: String,
        startDtm: String?,
        endDtm: String?
    ): MutableList<HamsDoorStat>?

    @Query(
        """
        SELECT new com.eseict.apiServer.dto.hams.stat.HamsDoorStat(
            TO_CHAR(DH.occurDate , 'yyyyMMdd') ,
             sum((case when DH.doorState = '2' then 1 else 0 end)),
             sum((case when DH.doorState = '3' then 1 else 0 end))
            )


        FROM TbHamsDoorHist DH
            where
                (:startDtm = '' OR TO_CHAR(DH.occurDate , 'yyyyMMdd')  >= :startDtm)
                AND
                 (:endDtm = '' OR  TO_CHAR(DH.occurDate , 'yyyyMMdd') <= :endDtm )
                 AND
                 (:eqId = '' OR :eqId = DH.eqId)
                 AND
                 (:doorType = '' OR :doorType = DH.doorType)

            GROUP BY TO_CHAR(DH.occurDate , 'yyyyMMdd')
            ORDER BY TO_CHAR(DH.occurDate , 'yyyyMMdd') DESC
    """
    )
    fun getDoorStateHist4YMD(
        eqId: String?,
        doorType: String,
        startDtm: String?,
        endDtm: String?
    ): MutableList<HamsDoorStat>?


    //TODO
    @Query(
        """
        SELECT new com.eseict.apiServer.dto.hams.stat.HamsDoorLockStat(
            TO_CHAR(DH.occurDate , 'yyyy') ,
             sum((case when DH.doorState = '1' then 1 else 0 end)),
             sum((case when DH.doorState = '2' then 1 else 0 end)),
             sum((case when DH.doorState = '3' then 1 else 0 end)),
             sum((case when DH.doorState = '4' then 1 else 0 end)),
             sum((case when DH.doorState = '5' then 1 else 0 end))
            )
            
            
        FROM TbHamsDoorHist DH
            where
                (:startDtm = '' OR TO_CHAR(DH.occurDate , 'yyyyMMdd')  >= :startDtm)
                AND
                 (:endDtm = '' OR  TO_CHAR(DH.occurDate , 'yyyyMMdd') <= :endDtm )
                 AND 
                 (:eqId = '' OR :eqId = DH.eqId)
                 AND 
                 (:doorType = '' OR :doorType = DH.doorType)
                 
            GROUP BY TO_CHAR(DH.occurDate , 'yyyy') 
            ORDER BY TO_CHAR(DH.occurDate , 'yyyy') DESC
    """
    )
    fun getDoorLockStateHist4Y(
        eqId: String?,
        doorType: String,
        startDtm: String?,
        endDtm: String?
    ): MutableList<HamsDoorLockStat>?

    @Query(
        """
        SELECT new com.eseict.apiServer.dto.hams.stat.HamsDoorLockStat(
            TO_CHAR(DH.occurDate , 'yyyyMM') ,
             sum((case when DH.doorState = '1' then 1 else 0 end)),
             sum((case when DH.doorState = '2' then 1 else 0 end)),
             sum((case when DH.doorState = '3' then 1 else 0 end)),
             sum((case when DH.doorState = '4' then 1 else 0 end)),
             sum((case when DH.doorState = '5' then 1 else 0 end))
            )


        FROM TbHamsDoorHist DH
            where
                (:startDtm = '' OR TO_CHAR(DH.occurDate , 'yyyyMMdd')  >= :startDtm)
                AND
                 (:endDtm = '' OR  TO_CHAR(DH.occurDate , 'yyyyMMdd') <= :endDtm )
                 AND
                 (:eqId = '' OR :eqId = DH.eqId)
                 AND
                 (:doorType = '' OR :doorType = DH.doorType)

            GROUP BY TO_CHAR(DH.occurDate , 'yyyyMM')
            ORDER BY TO_CHAR(DH.occurDate , 'yyyyMM') DESC
    """
    )
    fun getDoorLockStateHist4YM(
        eqId: String?,
        doorType: String,
        startDtm: String?,
        endDtm: String?
    ): MutableList<HamsDoorLockStat>?

    @Query(
        """
        SELECT new com.eseict.apiServer.dto.hams.stat.HamsDoorLockStat(
            TO_CHAR(DH.occurDate , 'yyyyMMdd') ,
             sum((case when DH.doorState = '1' then 1 else 0 end)),
             sum((case when DH.doorState = '2' then 1 else 0 end)),
             sum((case when DH.doorState = '3' then 1 else 0 end)),
             sum((case when DH.doorState = '4' then 1 else 0 end)),
             sum((case when DH.doorState = '5' then 1 else 0 end))
            )


        FROM TbHamsDoorHist DH
            where
                (:startDtm = '' OR TO_CHAR(DH.occurDate , 'yyyyMMdd')  >= :startDtm)
                AND
                 (:endDtm = '' OR  TO_CHAR(DH.occurDate , 'yyyyMMdd') <= :endDtm )
                 AND
                 (:eqId = '' OR :eqId = DH.eqId)
                 AND
                 (:doorType = '' OR :doorType = DH.doorType)

            GROUP BY TO_CHAR(DH.occurDate , 'yyyyMMdd')
            ORDER BY TO_CHAR(DH.occurDate , 'yyyyMMdd') DESC
    """
    )
    fun getDoorLockStateHist4YMD(
        eqId: String?,
        doorType: String,
        startDtm: String?,
        endDtm: String?
    ): MutableList<HamsDoorLockStat>?
}