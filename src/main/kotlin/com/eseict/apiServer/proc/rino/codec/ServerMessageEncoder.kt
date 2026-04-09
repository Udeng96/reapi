package com.eseict.apiServer.proc.rino.codec

import com.eseict.apiServer.dto.event.GeneralMessage
import com.eseict.common.log.LoggerManager
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class ServerMessageEncoder : MessageToByteEncoder<GeneralMessage>() {

    private val logger = LoggerManager.getLogger(ServerMessageEncoder::class.java)

    override fun encode(ctx: ChannelHandlerContext?, gm: GeneralMessage, out: ByteBuf) {
        out.writeBytes(gm.header)
    }


}