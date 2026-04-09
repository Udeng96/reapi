package com.eseict.apiServer.proc.rino.msgProc

import com.google.common.collect.Queues
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SocketMsgQueue private constructor() {

    private var msgQueue = Queues.newLinkedBlockingQueue<String>()

    companion object {

        @Volatile
        private var instance: SocketMsgQueue = SocketMsgQueue()

        @JvmStatic
        fun getInstance(): SocketMsgQueue =
            instance ?: synchronized(this) {
                instance ?: SocketMsgQueue().also {
                    instance = it
                }
            }
    }

    fun put(msg: String?): Boolean = msgQueue.add(msg)

    @Throws(InterruptedException::class)
    fun take(): String = msgQueue.take()

    fun clear() {
        msgQueue.clear()
    }

    fun size(): Int = msgQueue.size
}