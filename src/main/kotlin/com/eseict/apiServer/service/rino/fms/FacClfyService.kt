package com.eseict.apiServer.service.rino.fms

import com.eseict.apiServer.dto.common.ClfyInfo
import com.eseict.apiServer.dto.fac.CctvClfy
import com.eseict.apiServer.dto.fac.FacClfy
import com.eseict.apiServer.dto.fac.FacClfyImpl
import com.eseict.apiServer.dto.oms.UserInfo
import com.eseict.apiServer.repository.rino.fms.FacClfyRepository
import com.eseict.apiServer.util.geoJson.GeoResponse
import com.eseict.apiServer.util.geoJson.searchZoneName
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class FacClfyService(val facClfyRepository: FacClfyRepository) {


    @Value("\${platform.fms.url}")
    lateinit var fmsUrl: String

    @Value("\${platform.fms.subUrls.icon}")
    lateinit var fmsIconUrl: String


    private final val LOGGER = LoggerFactory.getLogger(FacClfyService::class.java)

    fun getCctvClfy(): List<CctvClfy>? {
        return facClfyRepository.getCctvClfy()
    }

    fun getFacClfy(): List<FacClfyImpl>? {

        return facClfyRepository.getFacClfy()

    }

    fun getFacClfyByClfyCd(clfyCd: String?): List<FacClfyImpl>? {

        return facClfyRepository.getFacClfyByClfyCd(clfyCd)

    }

    /**
     * 권한정보에 따른 시설물 분류 정보 출력, icon url을 함께 전달한다.
     */
    fun getClfyListWithIconByToken(user: UserInfo): List<FacClfy>? {
        return facClfyRepository.getFacClfy4Gis(null)
    }

    /**
     * 권한정보에 따른 시설물 분류 조회
     */
    fun getClfyWithIconByToken(user: UserInfo, clfyId: String): FacClfy? {
        return facClfyRepository.getFacClfy4Gis(null)!!.get(0)
    }


}