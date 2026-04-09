package com.eseict.apiServer.proc.rino.webSocket.binder

import com.google.common.collect.Queues

class BinderMsgQueue private constructor() {
    private val msgQueue = Queues.newLinkedBlockingQueue<String>()

    companion object {

        @Volatile
        private var instance: BinderMsgQueue = BinderMsgQueue()

        @JvmStatic
        fun getInstance(): BinderMsgQueue =
            instance ?: synchronized(this) {
                instance ?: BinderMsgQueue().also {
                    instance = it
                }
            }
    }

    fun put(msg: String?): Boolean {
        return msgQueue.add(msg)
    }

    @Throws(InterruptedException::class)
    fun take(): String? {
        return msgQueue.take()
    }

    fun clear() {
        msgQueue.clear()
    }

    fun size(): Int {
        return msgQueue.size
    }
}