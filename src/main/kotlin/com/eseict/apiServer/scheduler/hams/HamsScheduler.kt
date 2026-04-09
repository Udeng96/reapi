package com.eseict.apiServer.scheduler.hams

import com.eseict.apiServer.service.hams.HamsService
import com.eseict.common.base.SystemProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


@Component
class HamsScheduler(val hamsService: HamsService) {
    private final val logger: Logger = LoggerFactory.getLogger(HamsScheduler::class.java)

    private val isDev = SystemProperties.getProperty("IS_DEV").toBoolean()


//    @Scheduled(cron = "0 0 0 * * *")
//    @Scheduled(fixedDelay = 10000)
    fun updateHamsFmsData() {
        //FIXME 추후에 변경바람
        if (isDev) {
            logger.info("START HAMS UPDATE!!!")
            hamsService.updateFmsData()
            logger.info("END HAMS UPDATE!!!")
        }
    }
}