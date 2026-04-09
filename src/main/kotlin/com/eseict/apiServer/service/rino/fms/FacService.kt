package com.eseict.apiServer.service.rino.fms

import com.eseict.apiServer.dto.GisFacInfo
import com.eseict.apiServer.dto.common.Paging
import com.eseict.apiServer.dto.fac.CctvMonitor
import com.eseict.apiServer.repository.rino.fms.FacClfyRepository
import com.eseict.apiServer.repository.rino.fms.FacEm
import com.eseict.apiServer.repository.rino.fms.FacRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class FacService(
    val facEm: FacEm,
    val facRepository: FacRepository,
    val facClfyReposiory: FacClfyRepository
) {

    private final val LOGGER = LoggerFactory.getLogger(FacService::class.java)

    @Value("\${platform.fms.url}")
    lateinit var fmsUrl: String

    @Value("\${platform.fms.subUrls.fac}")
    lateinit var fmsFacUrl: String

    @Value("\${usePtzCctvIds}")
    lateinit var usePtzCctvIds : List<String>


    fun getFacList(
        type: String?,
        facNm: String?,
        page: Int?,
        rows: Int? = 10,

        ): Paging<GisFacInfo>? {
        val pageable: Pageable = if (page == -1) {
            Pageable.unpaged()
        } else {
            PageRequest.of(page?.minus(1) ?: 0, rows ?: 10)
        }

        var facList = facEm.getFacList(type, "%$facNm%", page ?: -1, rows, facClfyNm = "%cctv%")
        facList = facList?.filter { it -> it.lat != null && it.lng != null }
        val facListSize = facEm.getFacList(type, "%$facNm%", -1, 100, facClfyNm = "%cctv%")?.size ?: 0


//        val facList = facRepository.getFacList(type, "%$facNm%", pageable, facClfyNm = "%cctv%")?.content
//        val facListSize =
//            facRepository.getFacList(type, "%$facNm%", Pageable.unpaged(), facClfyNm = "%cctv%")?.content?.size ?: 0


        return Paging<GisFacInfo>(facListSize, facList)
    }


    fun getNewCctvMonitorList(cctvClfy:CctvMonitor):List<GisFacInfo>{

        val cctvList = cctvClfy.cctvList;
        val newCctvList : MutableList<GisFacInfo> = ArrayList();

        if (cctvList != null) {
            for(cctv in cctvList){
                val newMgtNo = getFacMgtNo(cctv.facNm);
                cctv.mgrNo = newMgtNo;
                newCctvList.add(cctv);
            }
        }

        return newCctvList;


    }

    fun getFacMgtNo(facNm: String?):String{
        val mgtNoList = facClfyReposiory.getCctvMgtNo(facNm);
        var mgtNo : String = "";
        if(mgtNoList.isNotEmpty()){
            mgtNo = mgtNoList.get(0);
        }

        return mgtNo;
    }

    fun getCctvListGrpByClfy(
        clfyId: String?
    ): List<CctvMonitor> {
        val monitorList: MutableList<CctvMonitor> = ArrayList()
        var typeList = if (clfyId?.isNotEmpty() == true) { //요청한 값이 있는경우
            facClfyReposiory.getClfy(clfyId)
        } else {
            facClfyReposiory.getCctvClfy()
        }

        typeList.forEach { it ->
            var facList = facEm.getFacList(it.id, null, -1, 1000, facClfyNm = "%cctv%")
            facList = facList?.filter { it -> it.lat != null && it.lng != null }
            val facListCnt = facList?.size

            facList?.forEach { fac-> fac.ptzYn=usePtzCctvIds?.contains(fac.mgrNo) }


            val cctvMonitor = CctvMonitor(it.nm, it.id, facListCnt, facList?.sortedBy { fac-> fac.facNm})
            monitorList.add(cctvMonitor)
        }

        return monitorList

    }


    fun getFac(facId: String): GisFacInfo? {
        return facRepository.getFacById(facId)
    }

    fun getFacListByClfyCd(clfyCd: String): List<GisFacInfo>? {
        var facList = facRepository.getFacListByClfyId(clfyCd)
        facList = facList?.filter { it -> it.lat != null && it.lng != null }
        return facList
    }


}