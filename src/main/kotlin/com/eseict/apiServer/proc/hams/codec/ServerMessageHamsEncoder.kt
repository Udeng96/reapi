package com.eseict.apiServer.proc.hams.codec

import com.eseict.apiServer.dto.event.GeneralMessage
import com.eseict.common.log.LoggerManager
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.slf4j.Logger

class ServerMessageHamsEncoder : MessageToByteEncoder<GeneralMessage>() {

    private val logger: Logger = LoggerManager.getLogger(ServerMessageHamsEncoder::class.java)

    override fun encode(ctx: ChannelHandlerContext?, gm: GeneralMessage?, out: ByteBuf?) {

        out?.writeBytes(gm?.header)
        out?.writeBytes(gm?.body)
    }

}