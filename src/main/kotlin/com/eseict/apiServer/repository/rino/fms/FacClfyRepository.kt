package com.eseict.apiServer.repository.rino.fms

import com.eseict.apiServer.domain.rino.fms.ErfFacClfy
import com.eseict.apiServer.dto.common.ClfyInfo
import com.eseict.apiServer.dto.fac.CctvClfy
import com.eseict.apiServer.dto.fac.FacClfy
import com.eseict.apiServer.dto.fac.FacClfyImpl
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FacClfyRepository : JpaRepository<ErfFacClfy, String> {

    @Query(
        """
        SELECT new com.eseict.apiServer.dto.fac.CctvClfy(
        CLFY.facClfyId,CLFY.facClfyNm)
        FROM ErfFacClfy AS CLFY
        WHERE CLFY.useYn <> '0' AND CLFY.topFacClfyId = '#'
        AND ((:clfyId = '') OR CLFY.facClfyId = :clfyId)
    """
    )
    fun getClfy(clfyId:String?): List<CctvClfy>


    // cctv 분류 조회
    @Query(
        """
        SELECT new com.eseict.apiServer.dto.fac.CctvClfy(
        CLFY.facClfyId,CLFY.facClfyNm)
        FROM ErfFacClfy AS CLFY
        WHERE CLFY.useYn <> '0' AND CLFY.topFacClfyId = '#'
        AND upper(CLFY.facClfyNm) LIKE upper('%cctv%')
    """
    )
    fun getCctvClfy(): List<CctvClfy>


    // 시설물 분류 그룹 조회
    @Query(
        """
            SELECT new com.eseict.apiServer.dto.fac.FacClfyImpl(
            CLFY.facClfyId, CLFY.facClfyNm,
            CLFY.regDtm,
            (SELECT COUNT(*) FROM ErfFacInfo FAC WHERE FAC.facClfyId = CLFY.facClfyId AND FAC.useYn IS 1)
            )
            FROM ErfFacClfy CLFY
            WHERE CLFY.useYn <> '0'
            AND upper(CLFY.facClfyNm) NOT LIKE upper('%cctv%')

        """
    )
    fun getFacClfy(): List<FacClfyImpl>?


    // 시설물 분류 그룹 개별 조회

    @Query(
        """
            SELECT new com.eseict.apiServer.dto.fac.FacClfyImpl(
            CLFY.facClfyId, CLFY.facClfyNm,
            CLFY.regDtm,
            (SELECT COUNT(*) FROM ErfFacInfo FAC WHERE FAC.facClfyId = CLFY.facClfyId AND FAC.useYn IS 1)
            )
            FROM ErfFacClfy CLFY
            WHERE CLFY.facClfyId = :clfyCd
            AND CLFY.useYn <> '0'
        """
    )
    fun getFacClfyByClfyCd(clfyCd: String?): List<FacClfyImpl>?

    @Query(
        """
            SELECT f.mgtNo from ErfFacInfo as f
            where f.facNm = :facNm
            and (f.facClfyId = 'FCC_46572f5370e94a3c86b03cf28b85a9ae' or f.facClfyId = 'FCC_0e3b16826fed43e88b7e9beeaa2a1622')
        """
    )

    fun getCctvMgtNo(facNm : String?) : List<String>


    @Query(
        """
        SELECT
                CLFY.facClfyId as id, CLFY.facClfyNm as nm,
                CLFY.regDtm as regDtm, 
                (SELECT COUNT(*) FROM ErfFacInfo FAC WHERE FAC.facClfyId = CLFY.facClfyId AND FAC.useYn IS NOT 1 ) as facCount
        FROM ErfFacClfy CLFY

        WHERE ((:id = '') OR CLFY.facClfyId = :id)
            AND CLFY.useYn IS NOT '0'
    """
    )
    fun getFacClfy4Gis(id: String?): List<FacClfy>?


    fun findByFacClfyNmIgnoreCaseContainingOrderByUseYnDesc(facClfyNm: String): List<ErfFacClfy>?


}