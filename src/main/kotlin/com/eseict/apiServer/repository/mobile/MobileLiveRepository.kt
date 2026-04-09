package com.eseict.apiServer.repository.mobile

import com.eseict.apiServer.domain.eas.MonMbDesc
import com.eseict.apiServer.dto.mobile.MobileInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MobileLiveRepository : JpaRepository<MonMbDesc, String> {

    @Query(
        """
        
        SELECT  new com.eseict.apiServer.dto.mobile.MobileInfo(
              LIVE.mgrNo, 0, LIVE.userId, LIVE.userNm, LIVE.dptNm, LIVE.userDtm , LIVE.lat, LIVE.lon,
               LIVE.mobileChk , LIVE.mbUrl
        )
        FROM MonMbDesc LIVE 

        
        
        WHERE LIVE.mobileChk IN (2,3)
        
        ORDER BY LIVE.userDtm DESC
    """
    )
    fun getLiveListWithUserInfo(): List<MobileInfo>

    @Query(
        """
        
        SELECT  new com.eseict.apiServer.dto.mobile.MobileInfo(
              LIVE.mgrNo, 0, LIVE.userId, LIVE.userNm, LIVE.dptNm, 
               LIVE.userDtm , LIVE.lat, LIVE.lon,
               LIVE.mobileChk , LIVE.mbUrl
        )
        FROM MonMbDesc LIVE 
        
        
        
        WHERE LIVE.mobileChk IN (2,3)
                AND LIVE.userId = :userId
        
        ORDER BY LIVE.userDtm DESC
    """
    )
    fun getLiveWithUserInfo(userId: String): MobileInfo


    fun findByUserId(userId: String):MonMbDesc

    fun findByMgrNo(mgrNo:String):MonMbDesc


}