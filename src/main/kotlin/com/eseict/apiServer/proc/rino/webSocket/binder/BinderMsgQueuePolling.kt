package com.eseict.apiServer.proc.rino.webSocket.binder

import com.eseict.common.log.LoggerManager
import io.netty.channel.group.ChannelGroup
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

class BinderMsgQueuePolling : Runnable {

    var logger = LoggerManager.getLogger(BinderMsgQueuePolling::class.java)
    private val msgQueue = BinderMsgQueue.getInstance()

    var bRunning = true

    override fun run() {
        while (bRunning) {
            try {
                val msg = msgQueue.take()
                //				logger.trace("Taked Msg Queue : {}", msg);
                if (msg == null || msg.isEmpty()) {
                    Thread.sleep(0)
                    continue
                }
                val channels: ChannelGroup = BinderWebSocketServerHandler.slaveChannels
                if (channels == null || channels.size == 0) {
                    Thread.sleep(0)
                    continue
                }
                for (channel in channels) {
                    synchronized(channel) {
                        channel.write(TextWebSocketFrame(msg))
                        channel.flush()
                    }
                }
                Thread.sleep(0)
            } catch (e: InterruptedException) {
                // TODO Auto-generated catch block
//				logger.error(e.getMessage(),e);
                Thread.currentThread().interrupt()
            }
        }
    }

    fun stop() {
        bRunning = false
    }
}