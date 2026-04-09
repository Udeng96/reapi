package com.eseict.apiServer.repository.mobile

import com.eseict.apiServer.domain.eas.MonMbDescLog
import com.eseict.apiServer.dto.mobile.MobileInfo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MobilePastRepository : JpaRepository<MonMbDescLog, Int> {


    @Query(
        """
        
        SELECT  new com.eseict.apiServer.dto.mobile.MobileInfo(
                PAST.mgrNo, PAST.mbSeq,
                PAST.userId, PAST.userNm, PAST.dptNm,
                PAST.userDtm , PAST.lat, PAST.lon,
               PAST.mbChk , PAST.mbUrl
        )
        
        FROM MonMbDescLog PAST
        
        WHERE  1=1
            AND (substring(PAST.userDtm,0,9) >= :startDtm or :startDtm = ''  )
            AND (substring(PAST.userDtm,0,9) <= :endDtm or :endDtm = ''  )
            AND PAST.liveChk = 'vod'
       ORDER BY PAST.userDtm DESC 
    """
    )
    fun getPastListWithUserInfo(startDtm: String, endDtm: String, pageable: Pageable): Page<MobileInfo>?


    @Query(
        """
        
        SELECT  new com.eseict.apiServer.dto.mobile.MobileInfo(
                PAST.mgrNo, PAST.mbSeq, PAST.userId, PAST.userNm, PAST.dptNm, PAST.userDtm , PAST.lat, PAST.lon,
               PAST.mbChk , PAST.mbUrl
        )
        
        FROM MonMbDescLog PAST
        
        WHERE PAST.mbSeq = :seq
            AND PAST.liveChk = 'vod'
            
        ORDER BY PAST.userDtm DESC
        
    """
    )
    fun getPastWithUserInfo(seq: Int): MobileInfo?

}