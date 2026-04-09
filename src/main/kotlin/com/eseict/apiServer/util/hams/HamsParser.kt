package com.eseict.apiServer.util.hams

import com.eseict.apiServer.dto.hams.HamsCmd
import org.slf4j.LoggerFactory

class HamsParser {


    companion object {

        enum class KEY_HEADER {
            SID,SERVER, targetId, cmd, seq , packetV,bodyFiledCnt
        }



        private final val logger = LoggerFactory.getLogger(HamsParser::class.java)
        fun parse(msg: String): Pair<String, Map<String, String>>? {

            try {
                val msgSplit = msg.split(HamsCmd.CHAR.SPLIT.word)

                msg.split(HamsCmd.CHAR.SPLIT.word,limit = 6)

                val headerMap:Map<String, String>  = parseHeader(msg)
                val bodyMap = parseBody(headerMap[KEY_HEADER.bodyFiledCnt.name]!!.toInt())
                val tailMsg = HamsCmd.CHAR.START_TAIL.word + msgSplit[msgSplit.size-1].split(HamsCmd.CHAR.START_TAIL.word)[1]

                //* 로 나눠지는거 찾기



            } catch (e: Exception) {
                logger.error("An error occurred during the paring hams message :[]",msg )
                logger.error(e.message, e)
            }
            return null
        }


        fun parseHeader(msg :String): Map<String, String> {
            val msgSplit = msg.split(HamsCmd.CHAR.SPLIT.word)

            return mapOf(KEY_HEADER.SID.name to msgSplit[0], KEY_HEADER.targetId.name to msgSplit[1], KEY_HEADER.cmd.name to msgSplit[2],
                KEY_HEADER.seq.name to msgSplit[3], KEY_HEADER.packetV.name to msgSplit[4], KEY_HEADER.bodyFiledCnt.name to msgSplit[5])
        }

        /**
         * 받아온 filed 의 개수만큼 읽어들임
         */
        fun parseBody(filedCnt: Int): Map<String, String>? {
            return null
        }

        fun parseTail(): Map<String, String>? {
            return null
        }

    }


}