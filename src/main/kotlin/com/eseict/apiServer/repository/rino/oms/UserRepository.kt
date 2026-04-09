package com.eseict.apiServer.repository.rino.oms

import com.eseict.apiServer.domain.rino.oms.OmsUserInfoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository :JpaRepository<OmsUserInfoEntity , String>{


    @Query("""
        select
        ooi.orgnNm

     from OmsUserInfoEntity userr
        inner join OmsDptInfoEntity dpt
            on dpt.dptId = userr.dptId
            join
               OmsOrgnInfoEntity ooi on dpt.orgnId = ooi.orgnId

            where userr.userLoginId = :userLoginId
    """)
    fun findOmsUserInfoEntitiesAndDptOrgnByUserLoginId(userLoginId :String):String


    fun findByUserLoginId(userLoginId: String):OmsUserInfoEntity?
}