package com.eseict.apiServer.repository.rino.fms

import com.eseict.apiServer.domain.rino.fms.ErfFacInfo
import com.eseict.apiServer.dto.GisFacInfo
import com.eseict.apiServer.dto.hams.HamsGisFacInfo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FacRepository : JpaRepository<ErfFacInfo, String> {


    // CCTV, 시설물 목록 조회
    @Query(
        """
        SELECT new com.eseict.apiServer.dto.GisFacInfo(
        F.facId, F.facNm,F.mgtNo,F.addrShort, POS.YCrdnt , POS.XCrdnt , F.facClfyId, CLFY.facClfyNm,
        F.useYn, F.updDtm , F.fileGrupId)
        FROM ErfFacInfo F
        INNER JOIN F.refErfPosCrdnts POS
        INNER JOIN F.refErfFacClfy CLFY
        
        WHERE (F.facClfyId = :type OR :type = '')
        AND (upper(F.facNm) LIKE upper(:facNm) OR F.facNm LIKE :facNm)
        AND CLFY.useYn = 1
        AND F.useYn = 1
        AND POS.useYn = 1
        AND (POS.XCrdnt IS NOT NULL AND POS.XCrdnt<>'')
        AND (POS.YCrdnt IS NOT NULL AND POS.YCrdnt<>'')
        AND (upper(CLFY.facClfyNm) LIKE upper(:facClfyNm) OR CLFY.facClfyNm LIKE :facClfyNm)

    """
    )
    fun getFacList(type: String?, facNm: String?, pageable: Pageable?, facClfyNm: String? = ""): Page<GisFacInfo>?


    //todo : topFacClfyId 의 값에 따라 수정 필요

    // cctv, 시설물 단일 조회
    @Query(
        """
       SELECT new com.eseict.apiServer.dto.GisFacInfo(
        F.facId, F.facNm, F.mgtNo , F.addrShort, 
        POS.YCrdnt , POS.XCrdnt ,
        F.facClfyId, POS.refErfFacInfo.refErfFacClfy.facClfyNm,
        F.useYn, F.updDtm , F.fileGrupId )
        FROM ErfFacInfo F
        INNER JOIN F.refErfPosCrdnts POS
        WHERE F.facId = :facId
        AND F.useYn = 1
        AND POS.useYn = 1

    """
    )
    fun getFacById(facId: String): GisFacInfo


    // 시설물 분류에 따른 시설물 리스트 출력
    @Query(
        """
        SELECT  new com.eseict.apiServer.dto.GisFacInfo(
        F.facId, F.facNm,F.mgtNo, F.addrShort, 
        POS.YCrdnt , POS.XCrdnt ,
        F.facClfyId, POS.refErfFacInfo.refErfFacClfy.facClfyNm,
        F.useYn, F.updDtm , F.fileGrupId
        )
            FROM ErfFacInfo AS F
            INNER JOIN F.refErfPosCrdnts POS
            INNER JOIN F.refErfFacClfy CLFY
            
            WHERE F.facClfyId = :clfyId
                AND CLFY.useYn = 1
                AND F.useYn = 1
                AND POS.useYn = 1
                AND (POS.XCrdnt IS NOT NULL AND POS.XCrdnt<>'') 
                AND (POS.YCrdnt IS NOT NULL AND POS.YCrdnt<>'')
        
    """
    )
    fun getFacListByClfyId(clfyId: String): List<GisFacInfo>?


    /**
     * 하위 시설물은 따로 조회한다.
     */
    @Query(
        """
        SELECT  DISTINCT NEW com.eseict.apiServer.dto.hams.HamsGisFacInfo(
           F.facId, F.mgtNo, F.facNm , F.posNm , CAST(POS.YCrdnt as double) , CAST(POS.XCrdnt as double),
            CLFY.facClfyId, CLFY.facClfyNm , F.useYn , '', A_IP.addInfoData , A_PORT.addInfoData,''
        )
            FROM ErfFacInfo AS F
            INNER JOIN F.refErfPosCrdnts POS
            INNER JOIN F.refErfFacClfy CLFY
            
            LEFT OUTER JOIN FETCH  ErfFacClfyAddInfoData A_IP ON (A_IP.facId = F.facId AND A_IP.addInfoId = 'HAMS_IP') 
            LEFT OUTER JOIN FETCH  ErfFacClfyAddInfoData A_PORT ON (A_PORT.facId = F.facId AND A_PORT.addInfoId = 'HAMS_PORT') 
            
            WHERE F.facClfyId = :clfyId
                AND CLFY.useYn = 1
                AND F.useYn = 1
                AND POS.useYn = 1
                AND (POS.XCrdnt IS NOT NULL AND POS.XCrdnt<>'') 
                AND (POS.YCrdnt IS NOT NULL AND POS.YCrdnt<>'')
        
    """
    )
    fun getFacListByClfyIdForHams(clfyId: String): List<HamsGisFacInfo>?


    @Query(
        """
        SELECT  DISTINCT NEW com.eseict.apiServer.dto.hams.HamsGisFacInfo(
           F.facId, F.mgtNo, F.facNm , F.posNm , CAST(POS.YCrdnt as double) , CAST(POS.XCrdnt as double),
            CLFY.facClfyId, CLFY.facClfyNm , F.useYn , '', A_IP.addInfoData , A_PORT.addInfoData , ''
        )
            FROM ErfFacInfo AS F
            INNER JOIN F.refErfPosCrdnts POS
            INNER JOIN F.refErfFacClfy CLFY
            
            LEFT OUTER JOIN FETCH  ErfFacClfyAddInfoData A_IP ON (A_IP.facId = F.facId AND A_IP.addInfoId = 'HAMS_IP') 
            LEFT OUTER JOIN FETCH  ErfFacClfyAddInfoData A_PORT ON (A_PORT.facId = F.facId AND A_PORT.addInfoId = 'HAMS_PORT') 
            
            WHERE F.mgtNo = :hamsId
                AND CLFY.useYn = 1
                AND F.useYn = 1
                AND POS.useYn = 1
                AND (POS.XCrdnt IS NOT NULL AND POS.XCrdnt<>'') 
                AND (POS.YCrdnt IS NOT NULL AND POS.YCrdnt<>'')
        
    """
    )
    fun getFacByHamsIdForHams(hamsId: String): HamsGisFacInfo?


    //하위 시설물 맵핑

    @Query(
        """
        SELECT DISTINCT new com.eseict.apiServer.dto.GisFacInfo(
                F.facId, F.facNm,F.mgtNo, F.addrShort, 
                POS.YCrdnt , POS.XCrdnt ,
                F.facClfyId, CLFY.facClfyNm,
                F.useYn, F.updDtm , F.fileGrupId
                )
            FROM ErfFacInfo AS F
            INNER JOIN F.refErfPosCrdnts POS
            INNER JOIN F.facGrupMapps MAPP
            INNER JOIN F.refErfFacClfy CLFY
            LEFT OUTER JOIN ErfFacInfo  HIF ON (HIF.facId = MAPP.hiFacId)
            
            WHERE HIF.facId = :facId
            
    """
    )
    fun getSubFacList(facId: String): List<GisFacInfo>?


    fun findByFacClfyIdOrderByMgtNoDesc(facClfyId: String): List<ErfFacInfo>

}