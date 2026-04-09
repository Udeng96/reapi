package com.eseict.apiServer.proc.rino.webSocket

import com.eseict.common.log.LoggerManager
import com.google.common.collect.Maps
import io.netty.buffer.Unpooled
import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.handler.codec.http.*
import io.netty.handler.codec.http.websocketx.*
import io.netty.util.CharsetUtil
import io.netty.util.concurrent.GlobalEventExecutor

class WebSocketServerHandler : ChannelInboundHandlerAdapter() {

    var logger = LoggerManager.getLogger(WebSocketServerHandler::class.java)


    private val cctvFollowingChannelMap: MutableMap<String, Channel> = Maps.newHashMap()

    private val WEBSOCKET_PATH = "/websocket"

    private var handshaker: WebSocketServerHandshaker? = null


    fun getCctvFollowingChannelMap(): Map<String, Channel>? {
        return cctvFollowingChannelMap
    }

    companion object {
        val gisCctvChannels: ChannelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
    }


    @Throws(IllegalArgumentException::class, ClassCastException::class, UnsupportedOperationException::class)
    override fun handlerAdded(ctx: ChannelHandlerContext) {
        val incoming = ctx.channel()
        logger.info("[SERVER] : {} has joined", incoming.remoteAddress())
    }

    @Throws(ClassCastException::class, java.lang.UnsupportedOperationException::class)
    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        val incoming = ctx.channel()
        logger.info("[SERVER] : {} has left", incoming.remoteAddress())
        //        channels.remove(incoming);
        gisCctvChannels.remove(incoming)
        for (userId in cctvFollowingChannelMap.keys) {
            val cctvFollowingIncoming: Channel = cctvFollowingChannelMap.get(userId)!!
            if (cctvFollowingIncoming === incoming) {
                cctvFollowingChannelMap.remove(userId)
                break
            }
        }
    }

    // Client에서 송신 시 호출
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any?) {
        if (msg is FullHttpRequest) {                                    // Client 최초 연결 시
            handleHttpRequest(ctx, msg as FullHttpRequest)
        } else if (msg is WebSocketFrame) {                        // 데이터 받을때 와 연결 종료 시
            handleWebSocketFrame(ctx, msg as WebSocketFrame)
        }
    }

    private fun handleHttpRequest(ctx: ChannelHandlerContext, req: FullHttpRequest) {
        val incoming = ctx.channel()
        val uri = req.uri
        if (uri.contains("websocket")) {
            gisCctvChannels.add(incoming)
        }

        // Handle a bad request.
        if (!req.decoderResult.isSuccess) {
            sendHttpResponse(
                ctx.channel(),
                req,
                DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST)
            )
            return
        }

        // Allow only GET methods.
        if (req.method !== HttpMethod.GET) {
            sendHttpResponse(
                ctx.channel(),
                req,
                DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN)
            )
            return
        }
        if ("/favicon.ico" == req.uri) {
            val res: FullHttpResponse = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND)
            sendHttpResponse(ctx.channel(), req, res)
            return
        }

        // Handshake
        val wsFactory = WebSocketServerHandshakerFactory(
            getWebSocketLocation(req), null, true
        )
        handshaker = wsFactory.newHandshaker(req)
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel())
        } else {
            handshaker!!.handshake(ctx.channel(), req)
        }
    }

    private fun handleWebSocketFrame(ctx: ChannelHandlerContext, frame: WebSocketFrame) {
        // Check for closing frame
        if (frame is CloseWebSocketFrame) {
            handshaker!!.close(ctx.channel(), frame.retain() as CloseWebSocketFrame)
            return
        }

        // Check for connect frame
        if (frame is PingWebSocketFrame) {
            ctx.channel().write(PongWebSocketFrame(frame.content().retain()))
            return
        }

        // Data Send frame
        if (frame is TextWebSocketFrame) {
            for (channel in gisCctvChannels) {
                synchronized(channel) {
                    channel.write(TextWebSocketFrame(frame.text()))
                    channel.flush()
                }
            }
        }
    }

    private fun sendHttpResponse(channel: Channel, req: FullHttpRequest, res: FullHttpResponse) {
        // Generate an error page if response getStatus code is not OK (200).
        if (res.status.code() != 200) {
            val buf = Unpooled.copiedBuffer(res.status.toString(), CharsetUtil.UTF_8)
            res.content().writeBytes(buf)
            buf.release()
            HttpHeaders.setContentLength(res, res.content().readableBytes().toLong())
        }

        // Send the response and close the connection if necessary.
        val f = channel.writeAndFlush(res)
        if (!HttpHeaders.isKeepAlive(req) || res.status.code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE)
        }
    }

    // SERVER의 WebSocket Path Get
    private fun getWebSocketLocation(req: FullHttpRequest): String? {
        val location = req.headers()[HttpHeaders.Names.HOST] + WEBSOCKET_PATH
        return "ws://$location"
    }

}