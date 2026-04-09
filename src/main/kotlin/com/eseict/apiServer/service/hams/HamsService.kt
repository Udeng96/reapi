package com.eseict.apiServer.service.hams

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.domain.hams.TbEquipmentData
import com.eseict.apiServer.domain.rino.fms.*
import com.eseict.apiServer.domain.rino.hams.TbHamsDoorEventHist
import com.eseict.apiServer.domain.rino.hams.TbHamsDoorHist
import com.eseict.apiServer.domain.rino.hams.TbHamsSeq
import com.eseict.apiServer.dto.common.StatInfo
import com.eseict.apiServer.dto.hams.HamsCctvInfo
import com.eseict.apiServer.dto.hams.HamsCmd
import com.eseict.apiServer.dto.hams.HamsGisFacInfo
import com.eseict.apiServer.dto.hams.HamsStatus
import com.eseict.apiServer.dto.hams.stat.HamsDoorLockStat
import com.eseict.apiServer.proc.hams.client.HamsSendClient
import com.eseict.apiServer.repository.hams.TbEquipmentDataRepository
import com.eseict.apiServer.repository.hams.TbGroupDataRepository
import com.eseict.apiServer.repository.rino.fms.*
import com.eseict.apiServer.repository.rino.hams.TbHamsDoorEventHistRepository
import com.eseict.apiServer.repository.rino.hams.TbHamsDoorHistRepository
import com.eseict.apiServer.repository.rino.hams.TbHamsSeqRepository
import com.eseict.apiServer.util.*
import com.eseict.common.core.unique.IdGenerator
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import io.netty.util.CharsetUtil
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.parser.ParseException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.net.InetAddress
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


@Service(value = "hamsService")
class HamsService(
    val tbEquipmentDataRepository: TbEquipmentDataRepository,
//    val tbConnectorDataRepository: TbConnectorDataRepository,
    val tbGroupDataRepository: TbGroupDataRepository,
    val facRepository: FacRepository,
    val facEm: FacEm,
    val facClfyRepository: FacClfyRepository,
    val facClfyAddInfoRepo: FacClfyAddInfoRepo,
    val posCrdntRepository: PosCrdntRepository,
    val facClfyInfoAddInfoRepo: FacClfyInfoDataRepo,
    val tbHamsDoorHistRepository: TbHamsDoorHistRepository,
    val tbHamsDoorHistEventRepository: TbHamsDoorEventHistRepository,
    val tbHamsSeqRepository: TbHamsSeqRepository,
) {

    private final val gson : Gson = Gson();
    private final val MIN_SEQ = 500
    private final val MAX_SEQ = 999
    private final val LOGGER = LoggerFactory.getLogger(HamsService::class.java)

    @Value("\${hams.address}")
    lateinit var serverIp: String

    @Value("\${hams.port}")
    lateinit var serverPort: String

    @Value("\${hams.id}")
    lateinit var id: String

    @Value("\${hams.pw}")
    lateinit var pw: String

    @Value("\${hams.cryptKey}")
    lateinit var cryptKey: String


    @Value("\${hams.isUseCrypt}")
    lateinit var isUseCrypt: String

    /**
     * 아래는 fms와 연계 하기 위한 키값이다.
     */
    @Value("\${hams.facClfyId}")
    lateinit var facClfyId: String

    @Value("\${hams.facClfyAddData.ip}")
    lateinit var facClfyAddInfoIpKey: String

    @Value("\${hams.facClfyAddData.port}")
    lateinit var facClfyAddInfoPortKey: String


    fun findEq(id: Long) = tbEquipmentDataRepository.findById(id)

//    fun findConnectData(id: String) = tbConnectorDataRepository.findById(id)

    fun findLastSeq() = tbHamsSeqRepository.findFirstByOrderBySendDateDesc()

    @Transactional
    fun saveSeq(seq: Int, message: String): Boolean {
        val seqInfo = TbHamsSeq(IdGenerator.getPrefixedUUID32("SEQ_"), Date(), seq, message)
        val saveInfo = tbHamsSeqRepository.save(seqInfo)

        return saveInfo != null
    }


    /**
     *
     *
     * 최초의 HAMS SERVER 에 접속한다
     * 이때 접속이라는 단어는 최초의 단 한번(사업중 모든 요청전에 단 한번) 수행한다.
     *
     * 장비 연결, 장비 연결이 되면 장비 정보를 반환한다.
     *
     * 2022.12.20 추가
     * 최초연결을 수행 후 socket 연결을 계속해서 연결을 유지한다.
     *
     * 명령어를 전달하고
     *
     * @name : connectHams
     * @author : meiday
     * @since : 2022/08/19 9:42 AM
     * @param seq 식별값
     * @return 성공유무
     * @throws
     */
    fun connectHams(isCLose: Boolean? = true): Boolean {

        val hamsSendClient = HamsSendClient.getInstance(serverIp,serverPort)
        return hamsSendClient.doConnect()

    }


    /**
     * 장비 번호화 상태정보를 전달하면
     * 전체 조회를 통해 현재 상태를 체클한다.
     *
     * @param status 기존에 조회한 상태값이 있다면 그것에 덮씌우고 없으면 만들어서 return
     */
    fun checkDoor(eq: TbEquipmentData, status: HamsStatus? = HamsStatus()): HamsStatus {
        val hamsSendClient = HamsSendClient.getInstance(serverIp,serverPort)
        val seqInfo = tbHamsSeqRepository.findFirstByOrderBySendDateDesc()
        var seq = if (seqInfo?.seq == MAX_SEQ) MIN_SEQ else seqInfo?.seq?.plus(1) ?: MIN_SEQ

        val cmd = HamsCmd(isNoTSToC = true, targetId = eq.id.toString())
        val body = listOf("0") //현재는 둘다 사용하는것으로 앞 뒷문 값모두 가져온다.
        cmd.setHeader(body.size, HamsCmd.TARGET.DOOR_LOCK.target.plus(HamsCmd.CHAR.STATUS.word), seq.toString())
            .setBody(body).setTail()


        var responseMs: String? = null
        val socketClient = hamsSendClient!!
        val clientConnect = socketClient.isConnect()
        var isFinalConnect = false

        if (!clientConnect) {// 연결되어있는경우
            isFinalConnect = connectHams()
        }else {
            isFinalConnect = true
        }

        if (isFinalConnect) {
            val message = cmd.makeMessage()
            LOGGER.debug("CHECK HAMS DOOR !!!!!!!!")
            responseMs = socketClient.doWriteAndFlush(message!!)
            saveSeq(seq, String(message!!, CharsetUtil.UTF_8))

            val returnMessage = HamsCmd(responseMs!!)

            if (returnMessage != null) { //생성이 정상적으로 이루어진 경우

                val body = returnMessage.parse?.parseBody()

                LOGGER.debug("무인국사 Check Door  결과 {}", body.toString())

                if (body != null) {
                    status?.apply {
                        frontDoor = body[HamsCmd.FIELD_NM.FRONT_DOOR_S.name]
                        frontDoorLock = body[HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name]
                        backDoor = body[HamsCmd.FIELD_NM.BACK_DOOR_S.name]
                        backDoorLock = body[HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name]
                        isConnect = true
                    }
                }


            }
        }


        return status!!
    }


    /**
     *
     * 조작을 요청하고 조작이 된 경우에만 테이블에 삽입
     *
     * @name :
     * @author : meiday
     * @since : 2022/08/10 3:38 PM
     * @param eq 장비 정보
     * @param target 앞 1 뒷 2 문
     * @param lockType  1 잠금 해제, 2 잠금
     * @return
     * @throws
     */
    fun sendDoc(
        eq: TbEquipmentData,
        target: Int,
        lockType: Int,
        status: HamsStatus? = HamsStatus()
    ): HamsStatus {
        val hamsSendClient = HamsSendClient.getInstance(serverIp,serverPort)
        val seqInfo = tbHamsSeqRepository.findFirstByOrderBySendDateDesc()
        var seq = if (seqInfo?.seq == MAX_SEQ) MIN_SEQ else seqInfo?.seq?.plus(1) ?: MIN_SEQ

        //target ID는 총 9자리를 사용하며, 9자리가 안되는경에 padding 좌측으로 0을 채워준다.
        //010000001 식으로 요청을 해야한다.
        val cmd = HamsCmd(isNoTSToC = true, targetId = eq.id.toString())
        val body = listOf("$target", "$lockType")
        cmd.setHeader(body.size, HamsCmd.TARGET.DOOR_LOCK.target.plus(HamsCmd.CHAR.CONTROL.word), seq.toString())
            .setBody(body)
            .setTail()


        val clientConnect = hamsSendClient.isConnect()
        var isFinalConnect: Boolean = true
        if (!clientConnect) {// 연결이 안되어있는경우
            isFinalConnect = hamsSendClient.doConnect()
        } else {
            isFinalConnect = true
        }

        if (isFinalConnect) {
            val message = cmd.makeMessage()

            val responseMs = hamsSendClient.doWriteAndFlush(message!!)
//            val responseMs = SocketSendHamsImpl(serverIp,serverPort).send(message!!)

            val returnMessage = HamsCmd(responseMs!!)
            saveSeq(seq, String(message, CharsetUtil.UTF_8))

            if (returnMessage != null) {
                val body = returnMessage.parse?.parseBody()
                if (body != null) {

                    val doorType = body[HamsCmd.TARGET.DOOR_LOCK.name]?.toInt()

                    if (doorType == HamsCmd.DOOR_TYPE.front.num) {
                        status?.apply {
                            frontDoor = body[HamsCmd.FIELD_NM.FRONT_DOOR_S.name]
                            frontDoorLock = body[HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name]
                            isConnect = true
                        }
                        saveDoorHist(
                            eq.id!!.toString(),
                            doorType!!.toString().toCharArray()[0],
                            body[HamsCmd.FIELD_NM.FRONT_DOOR_S.name]!!.toCharArray()[0],
                            body[HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name]!!.toCharArray()[0]
                        )
                    } else {
                        status?.apply {
                            backDoor = body[HamsCmd.FIELD_NM.BACK_DOOR_S.name]
                            backDoorLock = body[HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name]
                            isConnect = true
                        }
                        saveDoorHist(
                            eq.id!!.toString(),
                            doorType!!.toString().toCharArray()[0],
                            body[HamsCmd.FIELD_NM.BACK_DOOR_S.name]!!.toCharArray()[0],
                            body[HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name]!!.toCharArray()[0]
                        )
                    }

                }
            }
        } else {
            LOGGER.debug("no connect hams client")
        }

        return status!!
    }


    fun checkTempHumi(eq: TbEquipmentData, status: HamsStatus? = HamsStatus()): HamsStatus {

        LOGGER.info("START SEND THS!!!!")
        val seqInfo = tbHamsSeqRepository.findFirstByOrderBySendDateDesc()
        var seq = if (seqInfo?.seq == MAX_SEQ) MIN_SEQ else seqInfo?.seq?.plus(1) ?: MIN_SEQ


        val hamsSendClient = HamsSendClient.getInstance(serverIp, serverPort)

        val cmd = HamsCmd(isNoTSToC = true, targetId = eq.id.toString())
        val body = listOf<String>() //현재는 둘다 사용하는것으로 앞 뒷문 값모두 가져온다.
        cmd.setHeader(
            body.size,
            HamsCmd.TARGET.TEMPERATURE_AND_HUMI.target.plus(HamsCmd.CHAR.STATUS.word),
            seq.toString()
        ).setBody(body).setTail()


        val message = cmd.makeMessage()
        val socketClient = hamsSendClient

        var responseMs: String? = null
        val clientConnect = socketClient.isConnect()
        var isFinalConnect = false
        if (!clientConnect) {// 연결되어있는경우
            isFinalConnect = connectHams()
        } else {
            isFinalConnect = true
        }

        if (isFinalConnect) {
            LOGGER.info("CHECK HAMS TEMP HUMI !!!!!!!!")
            responseMs = socketClient.doWriteAndFlush(message!!)
            val returnMessage = HamsCmd(responseMs!!)
            saveSeq(seq, String(message!!, CharsetUtil.UTF_8))


            if (returnMessage != null) { //생성이 정상적으로 이루어진 경우

                val body = returnMessage.parse?.parseBody()

                LOGGER.info("무인국사 Check THS  결과 {}", body.toString())

                if (body != null) {
                    status?.apply {
                        temp = body[HamsCmd.FIELD_NM.TEMP.name]!!.toBigDecimal()
                        humi = body[HamsCmd.FIELD_NM.HUMI.name]!!.toBigDecimal()
                        isConnect = true
                    }
                }


            }
        }


        return status!!
    }

    /**
     *
     *
     *
     * @name :
     * @author : meiday
     * @since : 2022/08/11 4:58 PM
     * @param : eqId 장비 ID 정보
     * @param : doorType 앞문, 뒷문 구분
     * @param : doorStatus 도어 상태
     * @param : doorLockStatus 도어락 상태
     * @return
     * @throws
     */
    @Transactional
    fun saveDoorHist(eqId: String, doorType: Char, doorStatus: Char, doorLockStatus: Char): Boolean {
        val hist =
            TbHamsDoorHist(IdGenerator.getPrefixedUUID32("HAMS_"), eqId, Date(), doorType, doorStatus, doorLockStatus)
        val result = tbHamsDoorHistRepository.save(hist)

        return result != null
    }


    @Transactional
    fun saveDoorEventHist(eqId: String, doorType: String, occurTarget: String, eventType: String): Boolean {
        val hist = TbHamsDoorEventHist(
            IdGenerator.getPrefixedUUID32("HAMS_EVENT_"),
            eqId,
            Date(),
            doorType,
            occurTarget,
            eventType
        )
        val result = tbHamsDoorHistEventRepository.save(hist)
        return result != null
    }


    fun getCctvList(hamsName:String) : List<HamsCctvInfo> {
        val data = getSampleData("/cctvData/cctvInfo.json");

        val type = object : TypeToken<List<HamsCctvInfo>?>() {}.type
        val result = gson.fromJson<List<HamsCctvInfo>>(data as String, type)
        val newResult: MutableList<HamsCctvInfo> = java.util.ArrayList<HamsCctvInfo>()
        for (cctv in result) {
            val newCctvMgrNo: List<String> = facClfyRepository.getCctvMgtNo(cctv.cctvName);
            if (newCctvMgrNo.isNotEmpty()) {
                cctv.mgrNo = newCctvMgrNo.get(0);
            }
            if(cctv.hamsName.equals(hamsName)){
                newResult.add(cctv)
            }
        }

        return newResult

    }

    fun getSampleData(fileName: String): String? {
        val gson = Gson()
        var array: JSONArray? = null
        val parser: JSONParser = JSONParser()
        val file: File = File(CommonUtil.homePath + File.separator + fileName)
        try {
            try {
                array = parser.parse(FileReader(file)) as JSONArray
            } catch (e: ClassCastException) {
                var obj: org.json.simple.JSONObject? = null
                obj = parser.parse(FileReader(file)) as JSONObject
                return gson.toJson(obj)
            }
        } catch (e: ParseException) {
            CommonUtil.logger.error(e.message, e)
            var obj: org.json.simple.JSONObject? = null
            obj = parser.parse(FileReader(file)) as JSONObject
            return gson.toJson(obj)
        } catch (e: FileNotFoundException) {
            CommonUtil.logger.error(e.message, e)
        } catch (e: IOException) {
            CommonUtil.logger.error(e.message, e)
        }
        return gson.toJson(array)
    }


    /**
     * fms에 등록된 hams를 불러온다.
     * 이때 부가정보 ip , port 와
     * 하위 시설물정보를 함께 호출한다. (CCTV 가 하위로 들어갈 예정, 일단 mapping되는 row 전부를 들고온다)
     */
    fun getList(): List<HamsGisFacInfo>? {

        //hams의 그룹 정보 mapping
        val hamsList = tbEquipmentDataRepository.findAllUsable("1")
        val hamsMap = hamsList?.groupBy { it.id }


        val clfyList = facClfyRepository.findByFacClfyNmIgnoreCaseContainingOrderByUseYnDesc("무인국사")
        var list: List<HamsGisFacInfo>? = null
        if (clfyList?.size ?: -1 > 0) {
            list = facRepository.getFacListByClfyIdForHams(clfyList!!.get(0).facClfyId!!)
            //하위 CCTV 정보를 각각 호출하여 mapping 해준다.
            list?.forEach {
                it.lowFacInfos = facRepository.getSubFacList(it.facId!!)
                //장비 조회
                val eq = findEq(it.hamsId!!.replace("HAMS_", "").toLong()).get()
                it.status = checkDoor(eq)

                it.groupNm =
                    hamsMap?.get(it.hamsId!!.replace("HAMS_", "").toLong())?.get(0)?.refTbGroupData?.name ?: "그룹확인불가"
            }

        }

        return list
    }


    /**
     * 개별 조회
     */
    fun get(id: String): HamsGisFacInfo? {
        val fac: HamsGisFacInfo? = facRepository.getFacByHamsIdForHams(id)

        if (fac != null) {
            fac.lowFacInfos = facRepository.getSubFacList(fac.facId!!)
            val eq = findEq(fac.hamsId!!.replace("HAMS_", "").toLong()).get()
            fac.status = checkTempHumi(eq, checkDoor(eq))

            fac.groupNm = tbEquipmentDataRepository.finByIdUsable(
                fac.hamsId!!.replace("HAMS_", "").toLong()
            )?.refTbGroupData?.name ?: "그룹확인불가"
        }
        return fac
    }

    //잠금 실시
    fun doLock(id: String, target: Int, lockType: Int): HamsGisFacInfo? {
        val fac: HamsGisFacInfo? = facRepository.getFacByHamsIdForHams(id)

        if (fac != null) {
            val eq = findEq(fac.hamsId!!.replace("HAMS_", "").toLong()).get()
            when {
                eq != null -> fac.status = sendDoc(eq!!, target, lockType)
                else -> {}
            }
        }
        return fac
    }


    /**
     * DOE 메세지를 받아서, 이벤트테이블에 저장, 이벤트 발생자의 ip 정보를 확인하고
     * local ip인경우 저장하지않고 나머지의 경우에만 이력 테이블에 저장한다.
     *
     * @name :
     * @author : meiday
     * @since : 2022/08/16 11:03 AM
     * @param msg  이벤트 전문
     * @return
     * @throws
     */
    fun receiveDoe(msg: String) {
        LOGGER.debug("GET DOE {}", msg)
        val ip = InetAddress.getLocalHost()
        val ipAddress = ip.hostAddress
        val hamsCmd = HamsCmd(msg)

        if (hamsCmd.parse != null) {
            val body = hamsCmd.parse!!.parseBody()
            LOGGER.debug("GET DOE {}", body!!.entries)

            val sendIpAddress = body[HamsCmd.FIELD_NM.ORDERER_IP.name]
            val eqId = body[HamsCmd.FIELD_NM.EQ_ID.name]
            val doorType = body[HamsCmd.TARGET.DOOR_LOCK.name]!!.toCharArray()[0]
            val doorState = try {
                body[HamsCmd.FIELD_NM.FRONT_DOOR_S.name]!!.toCharArray()[0]
            } catch (e: NullPointerException) {
                body[HamsCmd.FIELD_NM.BACK_DOOR_S.name]!!.toCharArray()[0]
            }
            val doorLockState = try {
                body[HamsCmd.FIELD_NM.FRONT_DOOR_LOCK_S.name]!!.toCharArray()[0]
            } catch (e: NullPointerException) {
                body[HamsCmd.FIELD_NM.BACK_DOOR_LOCK_S.name]!!.toCharArray()[0]
            }

            if (ipAddress != sendIpAddress) { //같은 IP인경우에는 조작후 전달받는 이벤트이기때문에 생략한다.
                val saveHist = saveDoorHist(eqId!!, doorType, doorState, doorLockState)
                if (saveHist) {
                    LOGGER.info(
                        "SUCCESS TO SAVE DOE, DOOR HIST INFO [eqId - {} ,doorType- {} ,doorState- {} ,doorLockState- {}]",
                        eqId,
                        doorType,
                        doorState,
                        doorLockState
                    )
                } else {
                    LOGGER.info(
                        "FAIL TO SAVE DOE, DOOR HIST INFO [eqId - {} ,doorType- {} ,doorState- {} ,doorLockState- {}]",
                        eqId,
                        doorType,
                        doorState,
                        doorLockState
                    )
                }

            } else {
                LOGGER.info("SEND IP == SERVER IP")
            }

            //만약 전달받은 전문이 비정상 작동한 경우에는 이벤트 이력 테이블에도 정보를 담는다.

            val userId = body[HamsCmd.FIELD_NM.USER_ID.name]
            val ordererType = body[HamsCmd.FIELD_NM.ORDERER_TYPE.name]
            val ordererIp = body[HamsCmd.FIELD_NM.ORDERER_IP.name]
            val doorDoorLock = body[HamsCmd.FIELD_NM.EVENT_DOOR_TYPE.name]
            val eventType = body[HamsCmd.FIELD_NM.EVENT_TYPE.name]
            if (userId == "0" && ordererIp == "0" && ordererType == "0") {
                val saveEventResult = saveDoorEventHist(eqId!!, doorType.toString(), doorDoorLock!!, eventType!!)

                if (saveEventResult) {
                    LOGGER.info(
                        "SUCCESS TO SAVE DOE, DOOR EVENT HIST INFO [eqId - {} ,doorType- {} ,doorDoorLock- {} ,eventType- {}]",
                        eqId,
                        doorType,
                        doorDoorLock,
                        eventType
                    )
                } else {
                    LOGGER.info(
                        "FAIL TO SAVE DOE, DOOR EVENT HIST INFO [eqId - {} ,doorType- {} ,doorDoorLock- {} ,eventType- {}]",
                        eqId,
                        doorType,
                        doorDoorLock,
                        eventType
                    )
                }
            }

        }


    }


    /**
     * fms 에 연동되어있는 시설물 정보를 업데이트한다.
     * 기존의 값이 있다면 업데이트를 진행하고 해당하는것이없다면 insert를 실시한다.
     *
     * 시설물의 mapping 여부 확인은 mgrNo로 한다. (관리번호로 맵핑실시 , HAMS_targetID로 사용)
     */
    fun updateFmsData() {

        //시섦물 정보를 받아오고 /기존리스트를 불러온다.
        //각각의 HAMS정보를 호출하고, 맵핑되어야하는정보는, CCTV(이거는 업데이트할땐는 빠지는정보), 상태정보도 맵핑을 해주어야하는데 이것은 사용자 요청이 있을때만, ip, port 정보 등 부가정보를 다 들고와야한다.
        val clfyList = facClfyRepository.findByFacClfyNmIgnoreCaseContainingOrderByUseYnDesc("무인국사");
        var findClfy: ErfFacClfy
        if (clfyList?.size ?: -1 > 0) {//등록된 시설물 분류가 있는경우
            //등록된 시설물들을 불러와서 누락된것이 있으면 등록
            findClfy = clfyList?.get(0)!!
        } else {//등록된 시설물 분류가 없는경우 등록후 실시한다.
            //시설물 분류를 먼저등록하고, 시설물을 등록한다.
            val hamsClfy = ErfFacClfy(
                IdGenerator.getPrefixedUUID32("FCC_"), '1', "무인국사", 1, LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_14)
                ), "SYSTEM", "#", 1
            )
            findClfy = facClfyRepository.save(hamsClfy)

            //부가 정보 업데이트
            val addInfoIp = ErfFacClfyAddInfo("HAMS_IP", "장비 IP", "장비 IP", 0, findClfy.facClfyId, "1")
            val addInfoPort = ErfFacClfyAddInfo("HAMS_PORT", "장비 PORT", "장비 PORT", 0, findClfy.facClfyId, "1")
            val saveAddInfos = facClfyAddInfoRepo.saveAll(listOf(addInfoIp, addInfoPort))
        }

        //등록된 시설물 정보를 호출한다.
        val findFacList = facRepository.findByFacClfyIdOrderByMgtNoDesc(findClfy.facClfyId!!)
        val findHamsEq = tbEquipmentDataRepository.findAllUsable("1")

        var findFacMap = findFacList.groupBy { it.mgtNo } as MutableMap<String, List<ErfFacInfo>>

        var insertList = mutableListOf<ErfFacInfo>()


        var updateList = mutableListOf<ErfFacInfo>()
        var deleteList = mutableListOf<ErfFacInfo>()

        findHamsEq?.forEach { hams ->
            val findHams = findFacMap.get("HAMS_" + hams.id)?.get(0)

            if (findHams != null) { //값이 있는경우
                if (!hams.compareFac(findHams)) {//다를경우 업데이트
                    findHams.apply {
                        //좌표정보도 비교해서 넣고 ,
                        //ip port 정보도 업데이트해서 넣어줘야한다.
                        //좌표변환
                        posNm = hams.jibunAddress
                        addrShort = hams.jibunAddress
                        addrShort = hams.jibunAddress
                        facNm = hams.name

                        refErfPosCrdnts?.forEach {
                            it.XCrdnt = hams.longitude.toString()
                            it.YCrdnt = hams.latitude.toString()
                        }
                        //부가정보 변환
                        refErfFacClfyAddInfoDatas?.forEach {
                            if (it.addInfoId == "HAMS_IP") it.addInfoData = hams.ip else it.addInfoData =
                                hams.port.toString()
                        }
                    }
                    updateList.add(findHams)
                    findFacMap.remove(findHams.mgtNo)
                } else { //같은경우 기존 리스트에서 샂게
                    findFacMap.remove(findHams.mgtNo)
                }
            } else {//없으면 insert
                var newFac = ErfFacInfo()
                val facId = IdGenerator.getPrefixedUUID32("FAC_")
                val posGrupId = IdGenerator.getPrefixedUUID32("PGR_")
                newFac.facId = facId
                newFac.facClfyId = findClfy.facClfyId
                newFac.coId = null
                newFac.facNm = hams.name
                newFac.mgtNo = "HAMS_" + hams.id
                newFac.setuDtm = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_8)
                )
                newFac.psstPeriod = 0
                newFac.evetRcvYn = 0
                newFac.posNm = hams.jibunAddress
                newFac.addrShort = hams.jibunAddress
                newFac.useYn = 1
                newFac.facTyp = "001"
                newFac.regrt = "SYSTEM"
                newFac.regDtm = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern(ApiConstants.DATE.FORMAT_14)
                )
                newFac.aprvSt = "003"
                newFac.sevrty = "1"
                newFac.mobileYn = 0
                newFac.addrLong = hams.jibunAddress
                newFac.geoFtrCd = ""
                newFac.posGrupId = posGrupId
                newFac.coId = "!CO" //없음으로 고정

                newFac.refErfFacClfyAddInfoDatas = mutableListOf<ErfFacClfyAddInfoData>(
                    ErfFacClfyAddInfoData(facId, "HAMS_IP", hams.ip),
                    ErfFacClfyAddInfoData(facId, "HAMS_PORT", hams.port.toString())
                )
                newFac.refErfPosCrdnts = mutableListOf(
                    ErfPosCrdnt(
                        IdGenerator.getPrefixedUUID32("ZNP_"),
                        facId,
                        posGrupId,
                        "0",
                        1,
                        hams.latitude.toString(),
                        hams.longitude.toString()
                    )
                )
                insertList.add(newFac)
            }

        }

        findFacMap.entries.forEach { delFac ->
            deleteList.add(delFac.value.get(0))
        }

        repeat(deleteList.size) {
            facRepository.delete(deleteList[it])
        }

        facRepository.saveAll(updateList)

        //시설물 insert를 //jparepository save의 isNew 함수로 인하여
        //어쩔수없이 구현체를 하나만듬
        facEm.saveAll(insertList)
        insertList.forEach {
            posCrdntRepository.saveAll(it.refErfPosCrdnts!!)
            facClfyInfoAddInfoRepo.saveAll(it.refErfFacClfyAddInfoDatas!!)
        }


//        facRepository.saveAll(insertList)

    }


    /**
     * dateType 에 따라서 최대 개수가 달라진다
     *
     * dateType Y - max row : 10 , M - max row : 12, D - max row : 31
     *
     * 통계임으로 날짜 내림차순 정렬, 중간중간에 없는데이터는 생성후 삽입,
     * 최대 갯수보다 적으면 그값만을 표출
     *
     */
    fun getStat(
        eqId: String,
        frontBack: Char,
        doorType: Char, //door , doorLock 구분
        dateType: Char,
        startDtm: String?,
        endDtm: String?
    ): MutableList<StatInfo>? {
        var chronoUnit = when (dateType) {
            'Y' -> ChronoUnit.YEARS
            'M' -> ChronoUnit.MONTHS
            else -> ChronoUnit.DAYS
        }
        var result = mutableListOf<StatInfo>()

        val dateFormat = getDateFormat(dateType)
        val datas = when (doorType) {
            '1' -> {
                when (dateType) {
                    'Y' -> tbHamsDoorHistRepository.getDoorStateHist4Y(eqId, frontBack.toString(), startDtm, endDtm)
                    'M' -> tbHamsDoorHistRepository.getDoorStateHist4YM(eqId, frontBack.toString(), startDtm, endDtm)
                    else -> tbHamsDoorHistRepository.getDoorStateHist4YMD(eqId, frontBack.toString(), startDtm, endDtm)
                }
            }
            else -> {
                when (dateType) {
                    'Y' -> tbHamsDoorHistRepository.getDoorLockStateHist4Y(eqId, frontBack.toString(), startDtm, endDtm)
                    'M' -> tbHamsDoorHistRepository.getDoorLockStateHist4YM(
                        eqId,
                        frontBack.toString(),
                        startDtm,
                        endDtm
                    )
                    else -> tbHamsDoorHistRepository.getDoorLockStateHist4YMD(
                        eqId, frontBack.toString(), startDtm, endDtm
                    )
                }
            }
        }

        if (!datas.isNullOrEmpty()) {//값이 있는경우에만 시행
            val from =
                if (!startDtm.isNullOrEmpty() && startDtm !== datas!![datas.size - 1].date) startDtm else datas!![datas.size - 1].date
            var to = 종료일을검사하여오늘과같은지검사하고오늘자로돌려주는메소드(datas!![0].date, dateType)  // endDate 혹은

            var toLocalDate = changeStrDateToLocalDate(to, dateType)
            var fromLocalDate = changeStrDateToLocalDate(from!!, dateType)

            val dateDayGab = toLocalDate.toEpochDay() - fromLocalDate.toEpochDay()//이거는 날짜 빼기

            val dateGab: Int = when (dateType) {
                'Y' -> (dateDayGab / 365).toInt()
                'M' -> getMonthGab(fromLocalDate, toLocalDate)
                else -> dateDayGab.toInt()
            }


            var resultMap = mutableMapOf<String, StatInfo>()

            //날짜 차이만큼 만들기
            for (plusDate in 0..dateGab) {
                val dateKeyDtm = fromLocalDate.plus(plusDate.toLong(), chronoUnit)
                val dateKeyStr = dateKeyDtm.format(dateFormat)

                resultMap[dateKeyStr] = HamsDoorLockStat(dateKeyStr, 0)
            }

            //결과물에 값에 셋팅을 해준다.

            datas.forEach {
                var findResult = resultMap[it.date]

                if (findResult != null) {
                    resultMap[it.date] = it
                }
            }


            result = resultMap?.entries!!.map { rm ->
                var v = rm.value
                v.convertDate(dateType)
                v
            }.toMutableList()

            //년 월 일 에따라 max 값을 달리한다.
            val maxRow: Int = when (dateType) {
                'Y' -> 10
                'M' -> 12
                else -> 31
            }

            if (result.size > maxRow) {
                result = result.slice(0 until maxRow).toMutableList()
            }


        }

        return result
    }


}