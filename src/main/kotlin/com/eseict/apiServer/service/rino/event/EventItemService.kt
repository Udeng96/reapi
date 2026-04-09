package com.eseict.apiServer.service.rino.event

import com.eseict.apiServer.controller.event.EventController
import com.eseict.apiServer.domain.rino.event.VwIocEvetInfo
import com.eseict.apiServer.dto.common.ClfyInfo
import com.eseict.apiServer.dto.event.HeatmapClfyInfo
import com.eseict.apiServer.repository.rino.event.EventItemRepository
import flexjson.factories.MapObjectFactory
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EventItemService(
    val eventItemRepository: EventItemRepository,
) {

    private val LOGGER = LoggerFactory.getLogger(EventController::class.java)


    fun getTypeList(useEventAuth: Boolean, eventAuthList: List<VwIocEvetInfo>?): List<ClfyInfo> {

        var result: List<ClfyInfo> = listOf()
        result = if (useEventAuth) {
            eventAuthList?.map { item ->
                val id = item.evetId?.substring(3) ?: ""
                val category = when {
                    id.startsWith("ANM") -> "법무부"
                    id.startsWith("NDM") -> "재난상황"
                    else -> item.evetNm ?: "" // null-safe 처리
                }
                ClfyInfo(id, category) // 오류 발생
            } ?: listOf()
        } else {
            var allEventAuth = eventItemRepository.findAll()
            allEventAuth.map { item ->
                val id = item.evetId?.substring(3) ?: ""
                val category = when {
                    id.startsWith("ANM") -> "법무부"
                    id.startsWith("NDM") -> "재난상황"
                    else -> item.evetNm ?: "" // null-safe 처리
                }
                ClfyInfo(id, category)
            }
        }


        LOGGER.debug("!!!!")

        //FIXME 그룹핑 실시
        //resultMap nullsafety로 !! 추가
        val resultMap = result?.groupBy({ it.id }, { it.nm })
        result = resultMap!!.entries.map { item ->
            val nameMap: Map<String, List<String>> = item.value.groupBy { it }

            var count = 0;
            var maxCountNm = ""
            nameMap.entries.forEach {
                if (it.value.size >= count) {
                    count = it.value.size
                    maxCountNm = it.key
                }
            }
            ClfyInfo(item.key, maxCountNm)
        }

        return result

    }

    fun getZnCdList(useEventAuth: Boolean, eventAuthList: List<VwIocEvetInfo>?): List<ClfyInfo> {

        var result = if (useEventAuth) {
            eventAuthList?.map { item ->
                ClfyInfo(item.znCd ?: "", item.znNm ?:"")
            }?.distinctBy { it.id }

        } else {
            val allAuth: List<VwIocEvetInfo> = eventItemRepository.findAll()
            allAuth.map {
                ClfyInfo(it.znCd ?: "", it.znNm ?: "")
            }?.distinctBy { it.id }
        }



        return result ?: listOf()
    }

    fun getHeatmapClfy(useEventAuth: Boolean, eventAuthList: List<VwIocEvetInfo>?): List<HeatmapClfyInfo>? {

        // 코드 분석...굳이 찾아올 필요 없이 eventAuthList(이미 그룹핑이 되어있는 것이기 때문에..)로 받아오거나 전체일 경우에는 findHeatmapClfy로 찾아와서 하면 된다.
        var heatmapClfys = if (useEventAuth) eventAuthList else eventItemRepository.findHeatmapClfy()
        val groups = heatmapClfys?.groupBy { heatmapClfy ->
            Pair(
                heatmapClfy.svcThemeCd,
                heatmapClfy.svcThemeNm
            )
        }//key: pair, value: list
        return groups?.entries?.map { group ->

            var filterValue = group.value.distinctBy { it.unitSvcCd }

            HeatmapClfyInfo(
                svcThemeCd = group.key.first!!,
                svcThemeNm = group.key.second!!,
                itemList = filterValue.map { unitSvc ->
                    HeatmapClfyInfo.UnitItem(unitSvc.unitSvcCd!!, unitSvc.unitSvcNm!!)
                }
            )
        }?.onEach { t->t.itemList = t.itemList.sortedBy { unit->unit.unitCd }}
    }

    fun <T> countListItem(list: List<T>, value: T): Int {
        var count = 0
        list.forEach { item ->
            if (item == value) count += 1
        }
        return count
    }

}