package com.eseict.apiServer.scheduler.cctv

import com.eseict.apiServer.service.cctv.CctvService
import com.eseict.common.base.SystemProperties
import org.slf4j.LoggerFactory

//@Component
class CctvScheduler(val cctvService: CctvService) {
    private final val LOGGER = LoggerFactory.getLogger(CctvScheduler::class.java)
    private val isDev = SystemProperties.getProperty("IS_DEV").toBoolean()

    //    @Scheduled(cron = "0 0 0 * * *")
    fun cctvReset() {
        if (!isDev) {
            LOGGER.info("START CCTV RESET")
//            val result = cctvService.cctvReset()
//            LOGGER.info("CCTV RESET RESULT IS {}", result)
            LOGGER.info("END CCTV RESET")
        }

    }
}