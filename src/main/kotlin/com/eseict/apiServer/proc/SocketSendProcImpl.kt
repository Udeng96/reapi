package com.eseict.apiServer.proc

import com.eseict.apiServer.dto.event.GeneralMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.BufferedOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.StandardCharsets


@Component
class SocketSendProcImpl : SendProc {

    private final val LOGGER = LoggerFactory.getLogger(SocketSendProcImpl::class.java)

    @Value("\${platform.mrs.address}")
    lateinit var host: String

    @Value("\${platform.mrs.port.template}")
    lateinit var port: String


    override fun send(gm: GeneralMessage): Boolean {
        var result = false
        var client: Socket? = null
        var bos: BufferedOutputStream? = null

        try {
            LOGGER.trace("sws send header \n{}", String(gm.header, StandardCharsets.UTF_8))
            LOGGER.trace("sws send body \n{}", String(gm.body!!, StandardCharsets.UTF_8))
        } catch (e1: Exception) {
            LOGGER.debug(e1.message, e1)
        }


        //TODO 송신 부
        val retryCnt = 3 // 재전송 횟수
        val respTimeout = 5000 // 응답 타임 아웃
        val connTimeout = 5000


        //재전송 횟수만큼 시도
        for (i in 0 until retryCnt) {
            try {
                client = Socket()
                client.connect(InetSocketAddress(host, port.toInt()), connTimeout) //소켓 연결 타입아웃 시간 설정(SWS connect)
                client.soTimeout = respTimeout
                bos = BufferedOutputStream(client.getOutputStream())
                bos.write(gm.header)
                bos.write(gm.body)
                bos.flush()
                result = true

                break
            } catch (e: java.lang.Exception) {
//				 TODO 소켓 전송 실패
                LOGGER.error(e.message, e)
            } finally {
                try {
                    bos?.close()
                    client?.close()
                } catch (e: IOException) {
                    LOGGER.error(e.message, e)
                }
            }
            if (result) {
                break
            }
        }
        if (!result) {
            LOGGER.error("SOCKET SEND ERROR")
        } else {
            LOGGER.info("SOCKET SEND SUCCESS [{}:{}]", host, port)
        }

        return result
    }


}