package com.eseict.apiServer.dto.hams.parser

import com.eseict.apiServer.dto.hams.HamsCmd
import com.eseict.apiServer.util.hams.HamsParser
import org.apache.commons.collections4.multiset.HashMultiSet
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class HamsTHSParse(
    override val message: String,
    override var headerMap: Map<String, String>?,
    override var logger: Logger = LoggerFactory.getLogger(HamsTHSParse::class.java)
) : HamsParsePolicy {

    /**
     * 변환 정책 구현 String
     */
    override fun parse(cmd: HamsCmd): Pair<String, Map<String, String>>? {
        TODO("Not yet implemented")
//        parseHeader()
    }

    override fun parse(msg: String): Pair<String, Map<String, String>>? {
        TODO("Not yet implemented")
    }

    override fun parseBody(): Map<String, String>? {

        val cmd = headerMap!![HamsParser.Companion.KEY_HEADER.cmd.name]
        val msgSplit = message.split(HamsCmd.CHAR.SPLIT.word)

        logger.info("Check THS POLICY{}",msgSplit.toString())

        return try {
            if (cmd == (HamsCmd.CHAR.ACK.word + HamsCmd.TARGET.TEMPERATURE_AND_HUMI.target + HamsCmd.CHAR.STATUS.word)) {
                val result = msgSplit[7]


                if (result == HamsCmd.RESULT_CODE.SUCCESS.num.toString()) { //성공의 경우에만 return
                    mapOf(
                        HamsCmd.FIELD_NM.TEMP.name to msgSplit[8],
                        HamsCmd.FIELD_NM.HUMI.name to msgSplit[9].split(HamsCmd.CHAR.START_TAIL.word)[0],
                    )
                } else {
                    null
                }

            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

}