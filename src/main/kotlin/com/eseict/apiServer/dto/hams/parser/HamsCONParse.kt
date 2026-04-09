package com.eseict.apiServer.dto.hams.parser

import com.eseict.apiServer.dto.hams.HamsCmd
import com.eseict.apiServer.util.hams.HamsParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class HamsCONParse(
    override val message: String,
    override var headerMap: Map<String, String>?,
    override var logger: Logger = LoggerFactory.getLogger(HamsCONParse::class.java)
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
        val filedCnt = headerMap!!.get(HamsParser.Companion.KEY_HEADER.bodyFiledCnt.name)

        val msgSplit = message.split(HamsCmd.CHAR.SPLIT.word)

        return if (cmd == (HamsCmd.CHAR.ACK.word + HamsCmd.COMMAND.CON)) {//일반 return 일 경우
//            msgSplit.slice(5..5+filedCnt)
            if (filedCnt!!.toInt() == 1) {
                mapOf(HamsCmd.FIELD_NM.LOGIN_RESULT.name to msgSplit.get(7).split(HamsCmd.CHAR.START_TAIL.word)[0])
            }  else {
                null
            }
        }else {
            null
        }
    }

}