package com.eseict.apiServer.proc.hams

import com.eseict.apiServer.service.hams.HamsService
import com.eseict.common.log.LoggerManager
import com.eseict.common.web.WebApplicationContextUtil
import io.netty.channel.*
import org.slf4j.Logger

//serverNettyHandler 는 ChannelInboundHandler를 상속받는다.
class SocketServerHamsHandler : ChannelInboundHandlerAdapter() {
    private val logger: Logger = LoggerManager.getLogger(SocketServerHamsHandler::class.java)

    lateinit var localPort: String
    private val BUFFER_MIN_SIZE: Int = 1024 // 버퍼 최소 사이즈
    private val BUFFER_INIT_SIZE: Int = 8192 // 초기 버퍼 사이즈
    private val BUFFER_MAX_SIZE: Int = 16384; // 버서 최고 사이즈

    //ByteBuf 사이즈 변경
    //replaying decoder 사용시 replay 횟수 감소
    //AdaptiveRecvByteBufAllocator = 예측된 버퍼 크기를 자동으로 늘리거나 줄이는 기능
    private val recvByteAllocator: RecvByteBufAllocator =
        AdaptiveRecvByteBufAllocator(BUFFER_MIN_SIZE, BUFFER_INIT_SIZE, BUFFER_MAX_SIZE)

    // 활성화된 channel의 port 받아오는 부분
    override fun channelActive(ctx: ChannelHandlerContext?) {
        localPort = ctx?.channel()?.localAddress().toString().split(":")[1]
        logger.trace("CHANNEL ACTIVE [{}]", localPort)
    }

    // 새로운 바이트를 수신할 때 데이터로 채우고 충분한 데이터를 얻으면 처리를 시작한다.
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any?) {

        try {

            logger.trace("MESSAGE RECEIVED=============================================================")
            logger.trace("CHANNEL READ [{}]", msg)

            val hamsService =
                WebApplicationContextUtil.getRequiredWebApplicationContext().getBean("hamsService") as HamsService;
            hamsService.receiveDoe(msg as String)

            //NOTE 수신한 DOE 이벤트는 ACK를 전달하지 않는다.
            // 버퍼에 쓰고 채널 파이프라인에 저장된 버퍼 전송하기.
//            val bm = Unpooled.copiedBuffer(msg.toString(), CharsetUtil.UTF_8)
//            val channel: Channel = ctx.channel()
//            channel.write(bm)
//            ctx.flush()
            ctx.close()
        } catch (e: Exception) {
            logger.error(e.message, e)
            ctx.close()

        }

    }

}