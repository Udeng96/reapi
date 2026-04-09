package com.eseict.apiServer.proc

import com.eseict.apiServer.conf.ApiConstants
import org.slf4j.LoggerFactory
import java.io.BufferedOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets


class SocketSendHamsImpl(var host: String,
                         var port: String) {

    private final val LOGGER = LoggerFactory.getLogger(SocketSendHamsImpl::class.java)


    private val NEW_LINE = "\r\n"
    fun send(message: ByteArray,isCLose:Boolean? = false): String? {
        var result: String? = null
        var client: Socket? = null
        var writer:PrintWriter? = null

        try {
            LOGGER.trace("hams send header \n{}", String(message, StandardCharsets.UTF_8))
            LOGGER.trace("hams send body \n{}", String(message, StandardCharsets.UTF_8))
        } catch (e1: Exception) {
            LOGGER.debug(e1.message, e1)
        }


        //TODO 송신 부
        val retryCnt = 3 // 재전송 횟수
        val respTimeout = 50000 // 응답 타임 아웃
        val connTimeout = 50000

        client = Socket()
        //재전송 횟수만큼 시도
        for (i in 0 until retryCnt) {
            try {

                client.connect(InetSocketAddress(host, port.toInt()), connTimeout) //소켓 연결 타입아웃 시간 설정(SWS connect)
                client.soTimeout = respTimeout
                client.keepAlive = true
                client.tcpNoDelay = false

                LOGGER.info("client.getLocalPort()   {}",client.remoteSocketAddress)
                LOGGER.info("client.getPort()  {}",client.localSocketAddress)
                writer = PrintWriter(BufferedOutputStream(client.getOutputStream()))
                writer.print(String(message)+NEW_LINE)
                writer.flush()
                var sb = StringBuilder()
                //filed 메세지가 바로 떨어진다 DB에 바로 쌓임
                //2022-09-23 17:33:35.569125,779,Client,Failed,"Checked, ip=127.0.0.1"
                result = readResponseMessage(client,sb,1)

                LOGGER.info("HAMS RESPONSE MESSAGE IS {}", sb.toString())
                break
            } catch (e: java.lang.Exception) {
//				 TODO 소켓 전송 실패
                LOGGER.error(e.message, e)
            } finally {
                try {
                    if (isCLose!!){
                        writer?.close()
                        client?.close()
                    }
                } catch (e: IOException) {
                    LOGGER.error(e.message, e)
                }
            }
            if (result != null){
                break
            }
        }
        if (result == null) {
            LOGGER.error("SOCKET SEND ERROR OR EMPTY MESSAGE")
        } else {
            LOGGER.info("SOCKET SEND SUCCESS [{}:{}]", host, port)
        }

        return result
    }

    fun readResponseMessage(socket : Socket,sb:StringBuilder, count:Int):String{
        var plusCount = 0
        if (count >= 5){
            return ""
        }else {
            Thread.sleep(100)
            plusCount = count.run { count.plus(1) }
        }

        var readBuffer = ByteArray(1024)
        var bis :InputStream? = null
        try {
            bis = socket.getInputStream()
        }catch(e:Exception){
            when (e){
                is StackOverflowError -> LOGGER.error(e.message,e)
                is IOException -> LOGGER.error(e.message,e)
            }
            return readResponseMessage(socket,sb,plusCount)
        }


        if (bis?.available() == 0){
           return readResponseMessage(socket,sb,plusCount)
        }else {
            try {
                if (bis != null){
                    while (bis.available() > 0) { //읽을 수 있는 값이 있는경우에 계속 실행

                        var readSize = 0
                        if (bis.available() < 1024) {
                            readSize = bis.available()
                            bis.read(readBuffer, 0, bis.available())
                        } else {
                            readSize = readBuffer.size
                            bis.read(readBuffer, 0, readBuffer.size)
                        }
                        sb.append(String(readBuffer.sliceArray(0 until readSize), Charset.forName(ApiConstants.ENCODING)))
                    }
                }

            }catch(e:Exception){
                LOGGER.error(e.message, e)
            }finally {
                bis?.close()
            }
        }
        return sb.toString().trim().filter { !it.isWhitespace() }

    }




}