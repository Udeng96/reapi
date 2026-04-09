package com.eseict.apiServer.proc.rino.msgProc

import com.eseict.apiServer.proc.rino.webSocket.WebSocketServerHandler
import com.eseict.common.log.LoggerManager
import io.netty.channel.group.ChannelGroup
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

class SocketMsgQueuePolling : Runnable {

    var logger = LoggerManager.getLogger(SocketMsgQueuePolling::class.java)
    private val msgQueue: SocketMsgQueue = SocketMsgQueue.getInstance()

    var bRunning = true

    override fun run() {
        while (bRunning) {
            try {
                val msg: String = msgQueue.take()
                logger.info("msg:[{}]", msg)
                if (msg == null || msg.isEmpty()) {
                    Thread.sleep(0)
                    continue
                }
                val channels: ChannelGroup = WebSocketServerHandler.gisCctvChannels

                if (channels == null || channels.size == 0) {
                    Thread.sleep(0)
                    continue
                }

                val notiJsonStr: String = try {
                    ClientNotiMsgConverter.getInstance().msgConvert(msg)
                } catch (e: Exception) {
                    msg
                }


                for (channel in channels) {
                    synchronized(channel) {

                        channel.write(TextWebSocketFrame(notiJsonStr))
                        channel.flush()
                    }
                }
                Thread.sleep(0)
            } catch (e: InterruptedException) {
                //logger.error(e.getMessage(),e);
                logger.error(e.message, e)
                Thread.currentThread().interrupt()
            }
        }
    }

    fun stop() {
        bRunning = false
    }
}