package com.eseict.apiServer.service.rino.event

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.domain.rino.event.VwIocEvetInfo
import com.eseict.apiServer.dto.common.ClfyInfo
import com.eseict.apiServer.dto.common.Paging
import com.eseict.apiServer.dto.event.*
import com.eseict.apiServer.dto.oms.UserInfo
import com.eseict.apiServer.proc.SocketSendProcImpl
import com.eseict.apiServer.repository.rino.event.*
import com.eseict.apiServer.util.HeaderUtil
import com.eseict.apiServer.util.getToday
import com.eseict.common.core.unique.IdGenerator
import com.google.common.base.Strings
import com.google.gson.Gson
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class EventService(
    val eventRepository: EventRepository,
    val eventEm: EventEm,
    val vwIocEvetInfoRepository: VwIocEvetInfoRepository,
    val socketSendProcImpl: SocketSendProcImpl,
    val eventItemHistRepository: EventItemHistRepository
) {
    private final val LOGGER = LoggerFactory.getLogger(EventService::class.java)


    fun getEvents(
        useEventAuth: Boolean,
        eventIdList: List<String>,
        locationCdList: List<String>,
        eventAuthList: List<VwIocEvetInfo>?,
        startDtm: String,
        endDtm: String,
        page: Int? = -1,
        rows: Int? = 10
    ): Paging<EventsDto>? {

        var paramEventIdList: List<String>? = null;
        var searchStartDtm = startDtm
        var searchEndDtm = endDtm


        //param의 정보들을 가져와서 eventId List 생성
        paramEventIdList = makeParamEventIdList(locationCdList, eventIdList)

        // Filter를 거치지 않은 모든 정보가져오기
        val allEvent: List<VwIocEvetInfo> = vwIocEvetInfoRepository.findAll()


        // 지역구 정보만 있을경우,  eventId 만 있는경우, 둘다 있는경우에 따라서 조회하는것을 하나 만들어야함
        var finalParamEventInfo: List<VwIocEvetInfo>? =
            getAccordWithAuthorizedEventParam(if (useEventAuth) eventAuthList else allEvent, paramEventIdList)
        paramEventIdList = finalParamEventInfo?.map { it.evetId!! }

        if (startDtm == "" && endDtm == "") {
            searchStartDtm = getToday()
            searchEndDtm = getToday()
        }

        val events = eventEm.getEvents(paramEventIdList, searchStartDtm, searchEndDtm, page ?: -1, rows)
        val allEvents = eventEm.getEvents(paramEventIdList, searchStartDtm, searchEndDtm, -1, rows)

        return Paging(allEvents?.size ?: 0, events)
    }

    fun getEvent(id: String): EventInfo {
        val result = eventRepository.findEvent(id)

        return result
    }

    fun getEventByUSvcId(id: String): EventInfo= eventRepository.findEventByUSvcId(id)

    fun getTypes(sites: List<String>, isFire: Boolean): List<ClfyInfo> {
        val types = eventRepository.findTypes(isFire)
        return types
    }

    /**
     * 히트맵 count 조회
     * 20220602 기간조회 추가
     */
    fun getHeatmapCount(
        omsApi: UserInfo?,
        svcThemeCd: String,
        unitCd: String,
        startDtm: String?,
        endDtm: String?
    ): List<HeatmapCount> {


        val znCdList = if (omsApi != null) {
            omsApi.data.eventAuthList?.map { item -> item.znCd }?.distinct()
        } else {
            vwIocEvetInfoRepository.findAll().map { item -> item.znCd }?.distinct()
        }

        val eventAuthList = znCdList?.map { item -> item + svcThemeCd + unitCd }


        val result = eventRepository.findHeatmapCount(
            svcThemeCd, unitCd,
            if (!Strings.isNullOrEmpty(startDtm)) startDtm!!.plus("000001") else startDtm,
            if (!Strings.isNullOrEmpty(endDtm)) endDtm!!.plus("235959") else endDtm,
            eventAuthList
        )
        return result
    }


    // 만약 front에 오늘 날짜를 구하는 코드가 들어가있다면
    // 아래 코드 삭제하고 아닐 경우 해당 코드 사용하기

//    fun getDate(
//        dateType : String,
//        isStart: Boolean
//
//    )
//    : String {
//        val current = LocalDate.now()
//        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
//
//
//        // 당월
//        val firstDate = current.withDayOfMonth(1).format(formatter); // 당월 1일
//
//
//        // 3개월
//        val threeMonthsAgo = current.minusMonths(3).format(formatter);
//
//        return if (isStart) {
//            when (dateType) {
//                ApiConstants.HEATMAPDATE.ALL_DATE -> ""
//                ApiConstants.HEATMAPDATE.TODAY -> getToday()
//                ApiConstants.HEATMAPDATE.FOR_A_MONTH -> firstDate
//                else -> threeMonthsAgo
//            }
//        } else {
//            when (dateType) {
//                ApiConstants.HEATMAPDATE.ALL_DATE -> ""
//                ApiConstants.HEATMAPDATE.TODAY -> getToday()
//                ApiConstants.HEATMAPDATE.FOR_A_MONTH -> getToday()
//                else -> getToday()
//            }
//
//        }
//    }

//    fun generateEvent(
//        eventId: String,
//        evetNm: String,
//        address: String?,
//        dateTime: String?,
//        x: String,
//        y: String
//    ): Any? {
//        val uSvcOutbId = IdGenerator.getUUID64()
////        val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_17))
//        val statEvetItemMap = arrayOf(EventBody.StatEvetItem("accidentId", ""))
//
//        val clientCd = clientCd
//
//        var geoResponse: GeoResponse = searchZoneName(x.toDouble(), y.toDouble())!!
//        val siteCd = geoResponse.znCd
//
//        val gm = createGeneralMessage(
//            uSvcOutbId,
//            "$clientCd-$siteCd-$eventId",
//            evetNm,
//            "등급은?",
//            "1",
//            x,
//            y,
//            address ?: vWorldService.getPositionNameByLatLng(x.toDouble(), y.toDouble()),
//            "이벤트 발생 [$evetNm]",
//            dateTime ?: LocalDateTime.now().format(DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_17)),
//            statEvetItemMap,
//            "",
//            clientCd,
//            siteCd!!
//        )
//
//        var gdList = eventGdRepository.findByEvetId("$clientCd-$siteCd-$eventId")
//
//        var itemList = eventItemRepo.findByEvetId("$clientCd-$siteCd-$eventId")
//
//
//        var hist = eventRepository.findEventWithAccidentId("", eventId, "", "")
//
//        LOGGER.info("hist {}", hist!!.get(0).toString())
//
//        LOGGER.debug("check box")
//
//        return hist
////        send(gm)
//    }

    private fun createGeneralMessage(
        uSvcOutbId: String,
        statEvetId: String,
        statEvetNm: String,
        statEvetGdCd: String,
        procSt: String,
        x: String,
        y: String,
        outbPosNm: String,
        statEvetCntn: String,
        statEvetOutbDtm: String,
        statEvetItemMap: Array<EventBody.StatEvetItem>,
        statEvetClrDtm: String,
    ): GeneralMessage {
        val eventBody = EventBody(
            uSvcOutbId, statEvetId, statEvetNm,
            statEvetGdCd, procSt, x, y, outbPosNm, statEvetCntn, statEvetOutbDtm,
            statEvetItemMap, statEvetClrDtm
        )


        val gson = Gson()
        val body = gson.toJson(eventBody).encodeToByteArray()
        val header = HeaderUtil.makeHeader(
            "A1",
            "API", "001", "1", IdGenerator.getUUID64(), body.size
        )
        return GeneralMessage("A1".encodeToByteArray(), header!!, body, body.size)
    }


    fun getDstrEvents(rows: Int, startDtm: String, endDtm: String, clientSiteIdList: List<String>?): List<DstrInfo> {
        var paddedEndDtm: String = ""
        if (endDtm.isNotEmpty()) {
            paddedEndDtm = LocalDate.parse(endDtm, DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_8))
                .atTime(LocalTime.MAX).format(DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_17))
        }
        val pageable: Pageable = PageRequest.of(0, rows)
        return eventRepository.findDstrEvents(pageable, startDtm, paddedEndDtm).content
    }

    fun getDstrEvent(uSvcOutbId: String): DstrInfo {
        return eventRepository.findDstrEventByUSvcOutbId(uSvcOutbId)
    }

    /**
     * 이벤트 종료로직
     */
    fun closeEvent(statEvetOutbSeqn: String): Boolean {
        var result = false

        //기존이벤트 조회하고 item 및 좌표를 셋팅해주고 전달
        // 닫는 시간
        val closeDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_17))
        // 지난 이벤트 정보 가져오기
        val histEvent = eventRepository.findById(statEvetOutbSeqn).orElse(null)
        // 해당 이벤트의 item들 가져오기 (적어도 "" 라도 존재해야한다.)
        val itemList = eventItemHistRepository.findItemListBySeqn(statEvetOutbSeqn)

        // 이 리시트를 Map의 형태(key, value)로 변환한다.
        //itemList가 null이 아니면 itemList의 itemNm을 key로 itemVal을 value로 넣는데
        // 만약 이 itemNm 과 itemVal이 null이면 할당을 건너 뛴고, 아니면 해당 값을 넣는다..?
        // 그럼 item.itemNm!!하고 똑같은 의미인가..? 맞네.
        // 그럼 itemList의 값은 늘 하나라도 있어야 하니까 null이 아님을 확신해줘도 괜찮지 않을까..?
        // 한 번 물어보기!

        val itemMap = itemList.map { item ->
//            val key = item.itemNm?.let { it }
//            val value = item.itemVal?.let { it }
//            EventBody.StatEvetItem(key!!, value!!)
            val key = item.itemNm!! // 안쓰길 권장한다고는 하는데..
            val value = item.itemVal!!
            EventBody.StatEvetItem(key, value)
        }.toTypedArray()


        if (histEvent != null) {//조회한 이벤트정보가 있는경우만 종료로직을 수행
            val msg = createGeneralMessage(
                histEvent.uSvcOutbId!!, // 이벤트 id
                "${histEvent.zoneId}-${histEvent.svcThemeCd}-${histEvent.unitSvcCd}", // 이벤트 코드
                histEvent.statEvetNm!!,// 이벤트 이름
                histEvent.statEvetGdCd!!,
                "5", // 1 = 발생 & 5= 종료
                histEvent.x!!, // x좌표
                histEvent.y!!, // y좌표
                histEvent.outbPlac!!, //발생 장소
                histEvent.statEvetCntn!!,
                histEvent.outbDtm!!, // 발생 날짜
                itemMap,
                closeDtm,
            )

            result = socketSendProcImpl.send(msg)
        }

        return result
    }


    /**
     * 사용자의 요청을 받아 ID를 생성하는경우로
     */
    fun makeParamEventIdList(znCdList: List<String>?, eventIdList: List<String>?): List<String>? {

        var result: MutableList<String>? = null

        // 지역구 리스트가 있다면
        if (!znCdList.isNullOrEmpty()) {
            result = mutableListOf<String>()
            znCdList.forEach { znCd ->
                // 이벤트 타입 리스트가 있다면
                if (!eventIdList.isNullOrEmpty()) {
                    // znCd+eventId 의 값을 result에 넣는다.
                    eventIdList?.forEach { eventId ->
                        result!!.add(znCd.plus(eventId))
                    }
                    //아니면 result에 새로운 지역구 구분 코드만 넣는다.
                } else {
                    result!!.add(znCd)
                }
            }
            //지역구 코드 리스트가 null이 아니고 비어있기만 한다면
        } else if (znCdList != null) {
            result = mutableListOf<String>()
            result = eventIdList?.toMutableList()!!
        }
        // 둘다 있다면
        return result
    }


    /**
     * filer로 처리하기 전에
     */
    fun getAccordWithAuthorizedEventParam(
        eventAuthList: List<VwIocEvetInfo>?,
        paramEventIdList: List<String>?
    ): List<VwIocEvetInfo>? {
        return eventAuthList?.filter {
            if (!paramEventIdList.isNullOrEmpty()) {
//                paramEventIdList!!.contains(it.evetId)
                it.evetId!!.substring(0, 3) in paramEventIdList || it.evetId!!.substring(
                    3,
                    12
                ) in paramEventIdList || it.evetId!! in paramEventIdList
            } else {
                true
            }
        }
    }


}