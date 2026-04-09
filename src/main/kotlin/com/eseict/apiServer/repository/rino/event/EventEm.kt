package com.eseict.apiServer.repository.rino.event

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.domain.rino.event.Event
import com.eseict.apiServer.dto.event.EventsDto
import com.eseict.apiServer.repository.rino.RinoRepositorySupport
import com.google.common.base.Strings
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Predicate

@Repository
class EventEm : RinoRepositorySupport<Event>() {

    @PersistenceContext
    lateinit var em: EntityManager

    @Transactional(readOnly = true)
    fun getEvents(
        eventIdList: List<String>?,
        startDtm: String,
        endDtm: String,
        page: Int?,
        rows: Int?
    ): List<EventsDto>? {
        var result: List<EventsDto>? = null
//        val em = getEntityManager()

        em.createNativeQuery("SET LOCAL jit = off").executeUpdate()

        try {
            val cb = em.criteriaBuilder
            val cq = cb.createQuery(EventsDto::class.java)
            val root = cq.from(Event::class.java)

            //얘가 조건문 (AND / OR)
            val predicate = mutableListOf<Predicate>()

            // eventIdList가 존재한다면 null이 아니라면
            // eventIdList 안에 있는 eventId를 가져오기
            if (eventIdList?.isNotEmpty() == true) {
                predicate.add(root.get<String>("statEvetId").`in`(eventIdList))
            }

            //outbDtm 에서 startDtm 보다 크거나 같은 것을 가져오기.
            if (!Strings.isNullOrEmpty(startDtm)) {
                predicate.add(cb.greaterThanOrEqualTo(root.get<String>("outbDtm"), startDtm))
            }

            // 시간까지 있으므로 해당 날짜의 마지막 시간까지 구하여 그 시간보다 작거나 같은 날짜 가져오기
            if (!Strings.isNullOrEmpty(endDtm)) {
                val paddedEndDtm = if (endDtm.isNotEmpty()) LocalDate.parse(
                    endDtm,
                    DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_8)
                ).atTime(LocalTime.MAX)
                    .format(DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_17)) else ""

                // outbDtm에서 작거나 같은 것을 가지고 오기
                predicate.add(cb.lessThanOrEqualTo(root.get<String>("outbDtm"), paddedEndDtm))
            }

            predicate.add(cb.equal(root.get<String>("procSt"),"1"));



            cq.where(*predicate.toTypedArray())
            cq.orderBy(cb.desc(root.get<String>("outbDtm")))
            cq.select(
                cb.construct(
                    EventsDto::class.java,
                    root.get<String>("id").alias("statEvetOutbSeqn"),
                    root.get<String>("statEvetId").alias("eventId"),
//                    root.get<String>("statEvetNm").alias("eventNm"),
                    root.get<String>("svcThemeNm").alias("eventNm"),
                    root.get<String>("zoneId").alias("locationCd"),
                    root.get<String>("znNm").alias("locationNm"),
                    root.get<String>("y").alias("lat"),
                    root.get<String>("x").alias("lng"),
                    root.get<String>("outbPlac").alias("addr"),
                    root.get<String>("statEvetCntn").alias("cntn"),
                    root.get<String>("outbDtm").alias("outbDtm"),
                    root.get<String>("clsDtm").alias("clsDtm"),
                    root.get<String>("uSvcOutbId").alias("uSvcOutbId")
                )
            )


            result = if (page!! < 0) {  //-1 을 요청하는 경우 전체 검색
                em.createQuery(cq).resultList
            } else {
                val first = (page?.minus(1))?.times(rows ?: 10) ?: 0
                em.createQuery(cq).setFirstResult(first).setMaxResults(rows ?: 10).resultList
            }

        }finally {

        }
        return result
    }

    fun concat(
        cb: CriteriaBuilder, separator: String?,
        vararg expressions: Expression<String>
    ): Expression<String> {
        // Returns an empty string if no expression is provided
        if (expressions.size == 0) {
            return cb.literal("")
        }
        // Start with the first expression
        var result: Expression<String> = expressions[0]
        // Then concat subsequent expressions (starting from the second one)
        for (i in 1 until expressions.size) {
            result = cb.concat(result, cb.concat(separator, expressions[i]))
        }
        return result
    }

}