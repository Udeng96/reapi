package com.eseict.apiServer.proc.rino

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.event.GeneralMessage
import com.eseict.apiServer.proc.rino.msgProc.SocketMsgQueue
import com.eseict.apiServer.util.HeaderUtil
import com.eseict.common.log.LoggerManager
import io.netty.channel.AdaptiveRecvByteBufAllocator
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.RecvByteBufAllocator
import java.nio.charset.Charset


class SocketServerHandler() : ChannelInboundHandlerAdapter() {

    private val logger = LoggerManager.getLogger(SocketServerHandler::class.java)


    private var localPort: String? = null

    override fun channelActive(ctx: ChannelHandlerContext) {    // ChannelHandlerContext 오픈시 실행 ( 처음 접속시 )
        localPort = ctx.channel().localAddress().toString().split(":".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[1]
        val remoteIp =
            ctx.channel().remoteAddress().toString().substring(1).split(":".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0]
    }

    private val recvByteBufAllocator: RecvByteBufAllocator = AdaptiveRecvByteBufAllocator(1024, 8192, 32768)

    @Throws(Exception::class)
    override fun handlerAdded(ctx: ChannelHandlerContext) {
        // TODO Auto-generated method stub
        ctx.channel().config()
            .setRecvByteBufAllocator(recvByteBufAllocator)
    }

    @Throws(java.lang.Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val message: GeneralMessage = msg as GeneralMessage
        logger.info("[MESSAGE RECEIVE SUCCESS]")
        logger.debug(
            "header Typ Cd = {}",
            String(message.headerTyp, Charset.forName(ApiConstants.ENCODING))
        )
        logger.info(
            "header = {}",
            String(message.header, Charset.forName(ApiConstants.ENCODING))
        )
        logger.info(
            "body = {}",
            String(message.body ?: "".toByteArray(), Charset.forName(ApiConstants.ENCODING))
        )
        val msgTypCd = ByteArray(HeaderUtil.getHeaderItemLength(HeaderUtil.MSG_TYP_CD)) // 메시지 타입 코드
        val sndSysCd = ByteArray(HeaderUtil.getHeaderItemLength(HeaderUtil.SND_SYS_CD))
        System.arraycopy(
            message.header,
            HeaderUtil.getHeaderItemStartPos(HeaderUtil.MSG_TYP_CD),
            msgTypCd,
            0,
            msgTypCd.size
        )
        System.arraycopy(
            message.header,
            HeaderUtil.getHeaderItemStartPos(HeaderUtil.SND_SYS_CD),
            sndSysCd,
            0,
            sndSysCd.size
        )
        val mepByte = ByteArray(HeaderUtil.getHeaderItemLength(HeaderUtil.MSG_EXCH_PATRN))
        System.arraycopy(
            message.header,
            HeaderUtil.getHeaderItemStartPos(HeaderUtil.MSG_EXCH_PATRN),
            mepByte,
            0,
            mepByte.size
        )
        val mep = String(mepByte)

        // 메시지 교환 방식에 따른 로직
        if (mep == "1") {    // one way
            logger.info("mep :: $mep")
        } else if (mep == "2") {    // one way ack
            val ackGm = GeneralMessage()
            ackGm.header =
                HeaderUtil.makeAckHeader(
                    message.header,
                    "API",
                    "3"
                )
            ctx.writeAndFlush(ackGm)
            logger.info(
                "ACK HEADER [{}]",
                String(ackGm.header, Charset.forName(ApiConstants.ENCODING))
            )
        }

        SocketMsgQueue.getInstance().put(String(message.body!!, Charset.forName(ApiConstants.ENCODING)))

    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        logger.error(cause.message)
        ctx.close()
    }
}