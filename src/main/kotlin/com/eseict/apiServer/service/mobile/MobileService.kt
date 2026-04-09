package com.eseict.apiServer.service.mobile

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.common.Paging
import com.eseict.apiServer.dto.mobile.MobileInfo
import com.eseict.apiServer.repository.mobile.MobileLiveRepository
import com.eseict.apiServer.repository.mobile.MobilePastRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Service
class MobileService(
    val mobileLiveRepository: MobileLiveRepository,
    val mobilePastRepository: MobilePastRepository
) {

    private final val logger: Logger = LoggerFactory.getLogger(MobileService::class.java)


    fun getLiveList(): List<MobileInfo> = mobileLiveRepository.getLiveListWithUserInfo().map { item ->


        //날짜 포맷을 변경한다.
        item.apply {
            if (!userDtm.isNullOrEmpty()) {
                userDtm = changeDateFormat(userDtm!!)
            }

        }
        item
    }

    fun getLiveInfo(id: String) =
        mobileLiveRepository.getLiveWithUserInfo(id).apply {
            if (!userDtm.isNullOrEmpty()) {
                userDtm = changeDateFormat(userDtm!!)
            }

        }


    fun getPastList(startDtm: String, endDtm: String, row: Int, page: Int): Paging<MobileInfo> {

        val paramStartDtm = if (!startDtm.isNullOrEmpty()) {
            LocalDate.parse(startDtm, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .format(DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_8))
        } else {
            ""
        }

        val paramEndDtm = if (!endDtm.isNullOrEmpty()) {
            LocalDate.parse(endDtm, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .format(DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_8))
        } else {
            ""
        }


        val pageable: Pageable = if (page == -1) {
            Pageable.unpaged()
        } else {
            PageRequest.of(page?.minus(1) ?: 0, row ?: 10)
        }

        val pasts =
            mobilePastRepository.getPastListWithUserInfo(paramStartDtm, paramEndDtm, pageable)?.content?.map { item ->
                item.apply {
                    if (!userDtm.isNullOrEmpty()) {
                        userDtm = changeDateFormat(userDtm!!)
                    }

                }
                item
            }
        val allPasts = mobilePastRepository.getPastListWithUserInfo(paramStartDtm, paramEndDtm, Pageable.unpaged())


        return Paging(allPasts?.size ?: 0, pasts)
    }

    fun getPestInfo(seq: String) =
        mobilePastRepository.getPastWithUserInfo(seq.toInt())?.apply {
            if (!userDtm.isNullOrEmpty()) {
                userDtm = changeDateFormat(userDtm!!)
            }

        }


    //dtm 14자리
    fun changeDateFormat(dtm: String): String = LocalDateTime.of(
        dtm.substring(0, 4).toInt(),
        dtm.substring(4, 6).toInt(),
        dtm.substring(6, 8).toInt(),
        dtm.substring(8, 10).toInt(),
        dtm.substring(10, 12).toInt(),
        dtm.substring(12, 14).toInt(),
    ).format(DateTimeFormatter.ofPattern(ApiConstants.DATE.MILLISECOND_ONE_FORMAT))

}