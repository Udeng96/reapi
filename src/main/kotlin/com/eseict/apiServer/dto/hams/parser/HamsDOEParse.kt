package com.eseict.apiServer.dto.hams.parser

import com.eseict.apiServer.dto.hams.HamsCmd
import com.eseict.apiServer.util.hams.HamsParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class HamsDOEParse(
    override val message: String,
    override var headerMap: Map<String, String>?,
    override var logger: Logger = LoggerFactory.getLogger(HamsDOEParse::class.java)
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

        return try {

            if(cmd == HamsCmd.TARGET.DOOR_LOCK.target.plus(HamsCmd.CHAR.EVENT.word)){
                val eqId = msgSplit[2]//장비 아이디


                val rcvDate = msgSplit[7] //날짜
                val ordererType = msgSplit[8] //명령자 타입
                val userId = msgSplit[9] //명령자 타입
                val ordererIp = msgSplit[10] //명령자 타입

                val doorType = msgSplit[11].toInt() //앞문(1) 뒷문(2)
                val eventDoorType = msgSplit[12] //도어(1), 도어락(2)
                val eventType = msgSplit[13] //이벤트 타입

                val doorState = msgSplit[14]    //도어 게폐상태
                val doorLockState = msgSplit[15].split(HamsCmd.CHAR.START_TAIL.word)[0] //도어락상태

                val doorKey =
                    if (doorType == HamsCmd.DOOR_TYPE.front.num) HamsCmd.FIELD_NM.FRONT_DOOR_S.name else HamsCmd.FIELD_NM.BACK_DOOR_S.name
                val doorLockKey =
                    if (doorType == HamsCmd.DOOR_TYPE.front.num) HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name else HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name
                mapOf(
                    HamsCmd.TARGET.DOOR_LOCK.name to msgSplit[11],
                    doorKey to doorState,
                    doorLockKey to doorLockState,
                    HamsCmd.FIELD_NM.ORDERER_TYPE.name to ordererType,
                    HamsCmd.FIELD_NM.USER_ID.name to userId,
                    HamsCmd.FIELD_NM.ORDERER_IP.name to ordererIp,
                    HamsCmd.FIELD_NM.EVENT_TYPE.name to eventType,
                    HamsCmd.FIELD_NM.EVENT_DOOR_TYPE.name to eventDoorType,
                    HamsCmd.FIELD_NM.RCV_DATE.name to rcvDate,
                    HamsCmd.FIELD_NM.EQ_ID.name to eqId
                )
            }else {
                null
            }

        } catch (e: Exception) {
            null
        }
    }

}