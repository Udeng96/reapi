package com.eseict.apiServer.proc.hams.codec

import com.eseict.common.log.LoggerManager
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import org.slf4j.Logger
import java.nio.charset.Charset

class ServerMessageHamsDecoder : ByteToMessageDecoder() {

    private val logger: Logger = LoggerManager.getLogger(ServerMessageHamsDecoder::class.java)

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {

        // ByteToMesageDecoder 누적 버퍼의 읽기 부분을 버린다.
        var message: String? = null
        if (buf.readableBytes() > 0) {
            message = buf.readBytes(buf.readableBytes()).toString(Charset.forName("UTF-8"))
            logger.info("message $message")
        }

        out.add(message ?: "nothing")


    }
}