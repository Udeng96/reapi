package com.eseict.apiServer.dto.hams.parser

import com.eseict.apiServer.dto.hams.HamsCmd
import com.eseict.apiServer.util.hams.HamsParser
import org.apache.commons.collections4.multiset.HashMultiSet
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class HamsDOSParse(
    override val message: String,
    override var headerMap: Map<String, String>?,
    override var logger: Logger = LoggerFactory.getLogger(HamsDOSParse::class.java)
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

        logger.info("Check DOS POLICY{}",msgSplit.toString())

        return try {
            if (cmd == (HamsCmd.CHAR.ACK.word + HamsCmd.TARGET.DOOR_LOCK.target + HamsCmd.CHAR.STATUS.word)) {
                val result = msgSplit[7]

                if (filedCnt!!.toInt() == 4) { //개별조회
                    val checkTarget = msgSplit.get(7)

                    if (checkTarget == "1") { //앞문
                        mapOf(
                            HamsCmd.FIELD_NM.FRONT_DOOR_S.name to msgSplit[8],
                            HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name to msgSplit[9].split(HamsCmd.CHAR.START_TAIL.word)[0]
                        )
                    } else {//뒷문 "2"
                        mapOf(
                            HamsCmd.FIELD_NM.BACK_DOOR_S.name to msgSplit[8],
                            HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name to msgSplit[9].split(HamsCmd.CHAR.START_TAIL.word)[0]
                        )
                    }


                } else { //전체 조회
                    if (result == HamsCmd.RESULT_CODE.SUCCESS.num.toString()) { //성공의 경우에만 return
                        mapOf(
                            HamsCmd.FIELD_NM.FRONT_DOOR_S.name to msgSplit[9],
                            HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name to msgSplit[10],
                            HamsCmd.FIELD_NM.BACK_DOOR_S.name to msgSplit[11],
                            HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name to msgSplit[12].split(HamsCmd.CHAR.START_TAIL.word)[0]
                        )
                    } else {
                        null
                    }


                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

}