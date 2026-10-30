package com.eseict.apiServer.proc.hams.client

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.hams.HamsCmd
import com.eseict.apiServer.util.Aes128
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.Charset


/**
 * 무인국사 소켓 client로
 * 연결 유무만을 확인하고
 *
 * socket으로 전송만 담당하게 해야한다.
 *
 * br이랑 bos만 마지막에 꺼지게만해주면 되는데
 * 그리고 socket에 계속해서 써주기만 하면되는 객체이다.
 * 연결이 안되어있으면 연결을 하는게 아니라
 * 연결은 밖에서 알아서 해주는것이고
 *
 *
 */
class HamsSendClient() {

    var host: String = "198.51.100.7"

    var port: String = "25170"

    val id: String = System.getenv("HAMS_ID") ?: ""

    val pw: String = System.getenv("HAMS_PW") ?: ""

    val cryptKey: String = System.getenv("HAMS_CRYPT_KEY") ?: ""

    val isUseCrypt: String = "true"

    val logger: Logger = LoggerFactory.getLogger(HamsSendClient::class.java)

    var connect = false

    var writer: PrintWriter? = null
    var bis: BufferedInputStream? = null

    val sb: StringBuilder = StringBuilder()
    var readBuffer = ByteArray(1024)


    var client: Socket? = null

    companion object {
        @Volatile
        private var instance: HamsSendClient? = null

        @JvmStatic
        fun getInstance(host: String, port: Int): HamsSendClient =
            instance ?: synchronized(this) {
                instance ?: HamsSendClient().also {
                    it.host = host
                    it.port = port.toString()
                    instance = it
                }
            }

        @JvmStatic
        fun getInstance(host: String, port: String): HamsSendClient =
            instance ?: synchronized(this) {
                instance ?: HamsSendClient().also {
                    it.host = host
                    it.port = port
                    instance = it
                }
            }

        @JvmStatic
        fun getInstance(): HamsSendClient =
            instance ?: synchronized(this) {
                instance ?: HamsSendClient().also {
                    instance = it
                }
            }
    }

    /**
     * client 생성
     */
    fun doConnect(): Boolean {
        // Real device connections require explicit opt-in and external credentials.
        if (System.getenv("HAMS_TCP_ENABLED") != "true" || id.isBlank() || pw.isBlank()
            || (isUseCrypt.toBoolean() && cryptKey.isBlank())) return false


        if (this.client != null){
            return true
        }else{
            logger.debug("empty hams client")
        }

        logger.debug("DO CONNECT!!!!!!")

        this.client = Socket()
        this.client!!.connect(InetSocketAddress(host, port.toInt()))
        this.client!!.keepAlive = true
        this.client!!.soTimeout = 0
        this.client!!.tcpNoDelay = false
        this.writer = PrintWriter(client!!.getOutputStream())

        val cmd = HamsCmd(targetId = "000000002")
        var encryptPw = pw
        if (isUseCrypt.toBoolean()) {
            var crypt = Aes128(cryptKey)
            encryptPw = crypt.encrypt(encryptPw)!!
        }
        val body = listOf(id, if (isUseCrypt.toBoolean()) encryptPw else pw)
        cmd.setHeader(body.size, HamsCmd.COMMAND.CON, "000").setBody(body).setTail()
        val message = cmd.makeMessage()
//        val message = "\$PGL,CL,000000002,CON,000,1,2,admin,xAi2q95P2YKO55Vl1pkWGg==*04BF\r\n".toByteArray()


        val response = doWriteAndFlush(message!!)

        this.bis = BufferedInputStream(client!!.getInputStream())

        if (response == null) {
            logger.info("FAIL TO HAMS LOGIN TRY!!")
        } else {
            //parse 해줘야함
            val returnMessage = HamsCmd(response)
            val body = returnMessage.parse?.parseBody()

            connect = body?.get(HamsCmd.FIELD_NM.LOGIN_RESULT.name) == HamsCmd.RESULT_CODE.SUCCESS.num.toString()

        }

        return connect
    }

    fun doWriteAndFlush(msg: ByteArray): String {

        return if (writer != null) {
            try {
                this.doWriteAndFlush(String(msg, Charset.forName(ApiConstants.ENCODING)))
            } catch (e: Exception) {
                logger.error(e.message, e)
                ApiConstants.RESULT.FAIL_CODE
            }
        } else {
            logger.debug("empty writer")
            ApiConstants.RESULT.FAIL_CODE
        }
    }

    /**
     *
     * socket 통신으로 write and flush를 진행하며
     *
     * reponse msg를 return
     *
     * @name : doWriteAndFlush
     * @author : meiday
     * @since : 2022/10/04 5:33 PM
     * @param : msg - 메세지 전문
     * @return  : success - msg , fail - "FAIL"
     * @throws
     */
    fun doWriteAndFlush(msg: String): String {
        this.writer?.print(msg)
        this.writer?.flush()
        val sb = StringBuilder()

        return readResponseMessage(sb,1)
    }


    private fun read(): String? {

        return if (bis != null) {
            while (bis!!.available() > 0) { //읽을 수 있는 값이 있는경우에 계속 실행
                var readSize = 0
                if (bis!!.available() < 1024) {
                    readSize = bis!!.available()
                    bis!!.read(readBuffer, 0, bis!!.available())
                } else {
                    readSize = readBuffer.size
                    bis!!.read(readBuffer, 0, readBuffer.size)
                }
                sb.append(String(readBuffer.sliceArray(0 until readSize), Charset.forName(ApiConstants.ENCODING)))
            }
            sb.toString().trim().filter { !it.isWhitespace() }
        } else {
            null
        }
    }


    fun clear() {
        if (client != null) {
            try {
                this.client!!.close()
                logger.info("HAMS client close")
            } catch (e: Exception) {
                logger.error(e.message, e)
            }
        }
        if (writer != null) {
            try {
                this.writer!!.close()
                logger.info("HAMS outputStream close")
            } catch (e: Exception) {
                logger.error(e.message, e)
            }
        }

        if (bis != null) {
            try {
                this.bis!!.close()
                logger.info("HAMS inputStream close")
            } catch (e: Exception) {
                logger.error(e.message, e)
            }
        }
    }


    fun isConnect(): Boolean {
        return (this.client?.isConnected ?: false) && connect
    }


    private fun readResponseMessage( sb: StringBuilder, count: Int = 1): String {

        var plusCount = 0
        if (count >= 5) {
            return ""
        } else {
            Thread.sleep(2000)
            plusCount = count.run { count.plus(1) }
        }

        var readBuffer = ByteArray(1024)
        var bis: InputStream? = null
        try {
            bis = client?.getInputStream()
        } catch (e: Exception) {
            when (e) {
                is StackOverflowError -> logger.error(e.message, e)
                is IOException -> logger.error(e.message, e)
                else ->logger.error(e.message, e)
            }
            return readResponseMessage(sb, plusCount)
        }


        logger.debug("bis?.available() , {}",bis?.available())

        if (bis?.available() == 0) {
            return readResponseMessage(sb, plusCount)
        } else {
            try {
                if (bis != null) {
                    while (bis.available() > 0) { //읽을 수 있는 값이 있는경우에 계속 실행

                        var readSize = 0
                        if (bis.available() < 1024) {
                            readSize = bis.available()
                            bis.read(readBuffer, 0, bis.available())
                        } else {
                            readSize = readBuffer.size
                            bis.read(readBuffer, 0, readBuffer.size)
                        }
                        sb.append(
                            String(
                                readBuffer.sliceArray(0 until readSize),
                                Charset.forName(ApiConstants.ENCODING)
                            )
                        )

                    }
                }

            } catch (e: Exception) {
                logger.error(e.message, e)
            } finally {
//                bis?.close()
            }
        }
        return sb.toString().trim().filter { !it.isWhitespace() }

    }

}