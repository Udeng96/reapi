package com.eseict.apiServer.dto.hams.parser

import com.eseict.apiServer.dto.hams.HamsCmd
import com.eseict.apiServer.util.hams.HamsParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open interface HamsParsePolicy {

    var logger: Logger

    val message: String

    var headerMap: Map<String, String>?

    fun parse(cmd: HamsCmd): Pair<String, Map<String, String>>?

    fun parse(msg: String): Pair<String, Map<String, String>>?

    /**
     * 헤더 분해
     */
    companion object {
        fun parseHeader(message: String): Map<String, String> {
            val msgSplit = message.split(HamsCmd.CHAR.SPLIT.word)

            return mapOf(
                HamsParser.Companion.KEY_HEADER.SID.name to msgSplit[0],
                HamsParser.Companion.KEY_HEADER.SERVER.name to msgSplit[1],//3.3.0버전에서 추가됨
                HamsParser.Companion.KEY_HEADER.targetId.name to msgSplit[2],
                HamsParser.Companion.KEY_HEADER.cmd.name to msgSplit[3],
                HamsParser.Companion.KEY_HEADER.seq.name to msgSplit[4],
                HamsParser.Companion.KEY_HEADER.packetV.name to msgSplit[5],
                HamsParser.Companion.KEY_HEADER.bodyFiledCnt.name to msgSplit[6]
            )
        }
    }

    /**
     * 받아온 filed 의 개수만큼 읽어들임
     * 명령어 filed 수 를 받아서 처리를해주는데
     *
     */
    @Throws(NullPointerException::class)
    fun parseBody(): Map<String, String>?

    fun parseTail(): Map<String, String>? {
        return try {
            if (message != null) { //받아온 값이 있는경우
                val allTails = message.split(HamsCmd.CHAR.START_TAIL.word)[1]
                val splitTail = allTails.split(HamsCmd.CHAR.SPLIT.word)


                mapOf<String, String>(
                    HamsCmd.FIELD_NM.CRC16.name to splitTail[0],
                    HamsCmd.FIELD_NM.ETX.name to (splitTail[0] ?: "")
                )

            } else {
                null
            }

        } catch (e: Exception) {
            logger.error("FAIL TO PARSING HAMS TAIL")
            null
        }

    }


}