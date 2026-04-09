package com.eseict.apiServer.scheduler.mobile

import com.eseict.apiServer.service.mobile.XeusLiveViewEventService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class MobileScheduler(val xeusLiveViewEventService: XeusLiveViewEventService) {


    private final val logger = LoggerFactory.getLogger(MobileScheduler::class.java)

    @Scheduled(cron = "0 0 * * * *")
    fun clearLive(){
        logger.info("=======================================")
        logger.info("DO CLEAR MOBILE LIVE!!!!!")
        xeusLiveViewEventService.clearLive()
        logger.info("END CLEAR MOBILE LIVE!!!!!")
        logger.info("=======================================")
    }
}