package com.eseict.apiServer.service.facAll

import com.eseict.apiServer.dto.GisFacInfo
import com.eseict.apiServer.dto.common.Paging
import com.eseict.apiServer.service.rino.fms.FacClfyService
import com.eseict.apiServer.service.rino.fms.FacService
import com.eseict.apiServer.service.rino.oms.OmsService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class FacAllService(
    val omsService: OmsService,
    val facService: FacService,
    val facClfyService: FacClfyService,
) {
    private val logger = LoggerFactory.getLogger(FacAllService::class.java)

    @Value("\${filter.fac.fms}")
    lateinit var filterFms: String


    fun getFacAllList(
        clfyId: String,
        facNm: String,
        page: Int? = -1,
        rows: Int? = 10
    ): Paging<GisFacInfo>? {

        //fms 리스트
        val fmsList: List<GisFacInfo>? = facService.getFacList(clfyId, facNm, -1, 10)?.data

        //전부 sum
        var allList = mutableListOf<GisFacInfo>()
        if (fmsList != null) {

            if (!filterFms.toBoolean()) {
                allList.addAll(fmsList)
            }
        }


        //전체 수
        val allCnt = allList.size
        //페이징 처리

        if (page != -1) { //페이징 요청   page 및 row 만큼이니까
            //startIndex =0 부터 나옴
            var startIndex = (rows!! * page!!) - rows!!
            var endIndex = startIndex + rows

            //전체 리스트보다 시작과 엔드가 클경우 예외 처리
            if (allCnt < startIndex) {
                startIndex = allCnt
            }

            if (allCnt < endIndex) {
                endIndex = allCnt
            }


            allList = allList.subList(startIndex, endIndex)
        }

        return Paging<GisFacInfo>(allCnt, allList)
    }

    /**
     * 전체 시설물 분류를 받아서 종합하여 return
     */
//    fun getAllClfy(user:UserInfo):List<FacClfy>{
//        var result = mutableListOf<FacClfy>()
//        //fms 리스트
////        val fmsClfys : List<FacClfy>?=facClfyService.getClfyListWithIconByToken(user)?.map {
////            FacClfyImpl(it.id , "${it.locationNm}_시설물_".plus(it.nm),"","","","",0, it.locationCd, it.locationNm)
////        }
//        //화진 분류 리스트
//
//        //재난 예경보 , 장비 // 재난예경보 장비로 병합
//        val deq = FacClfyImpl("DISASTER","재난예경보 장비","","","","",0)
//        //재난 예경보 방송장비
//        val deBq = FacClfyImpl("BROADCAST", "재난예경보 방송장비","","","","",0)
////        result.add(deq)
//        result.add(deBq)
//        if (fmsClfys!=null){
//            if (!filterFms.toBoolean()){
//                result.addAll(fmsClfys)
//            }
//        }
//
//        result.sortBy { it.nm }
//
//        return result
//
//    }

}

