package com.eseict.apiServer.dto.hams.parser

import com.eseict.apiServer.dto.hams.HamsCmd
import com.eseict.apiServer.util.hams.HamsParser
import org.apache.commons.collections4.multiset.HashMultiSet
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class HamsDOCParse(
    override val message: String,
    override var headerMap: Map<String, String>?,
    override var logger: Logger = LoggerFactory.getLogger(HamsDOCParse::class.java)
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

        val cmd = headerMap!!.get(HamsParser.Companion.KEY_HEADER.cmd.name)
        val msgSplit = message.split(HamsCmd.CHAR.SPLIT.word)

        logger.info("Check DOC POLICY{}",msgSplit.toString())

        return try {
            if (cmd == (HamsCmd.CHAR.ACK.word + HamsCmd.TARGET.DOOR_LOCK.target + HamsCmd.CHAR.CONTROL.word)) {
                val result = msgSplit[7]

                if (result == HamsCmd.RESULT_CODE.SUCCESS.num.toString()) { //성공의 경우에만 return

                    val doorType = msgSplit[8].toInt()

                    val firstKey =
                        if (doorType == HamsCmd.DOOR_TYPE.front.num) HamsCmd.FIELD_NM.FRONT_DOOR_S.name else HamsCmd.FIELD_NM.BACK_DOOR_S.name
                    val secondKey =
                        if (doorType == HamsCmd.DOOR_TYPE.front.num) HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name else HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name
                    mapOf(
                        HamsCmd.TARGET.DOOR_LOCK.name to msgSplit[8],
                        firstKey to msgSplit[9],
                        secondKey to msgSplit[10].split(HamsCmd.CHAR.START_TAIL.word)[0],
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