package com.eseict.apiServer.proc.rino.webSocket.binder

import com.eseict.common.log.LoggerManager
import com.google.common.collect.Maps
import flexjson.JSONDeserializer
import flexjson.JSONSerializer
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
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class BinderWebSocketServerHandler : ChannelInboundHandlerAdapter() {


    var logger = LoggerManager.getLogger(BinderWebSocketServerHandler::class.java)

    private val masterChannelMap: ConcurrentMap<String, Channel> = ConcurrentHashMap()

    val STATE_OFF = 0
    val STATE_ON = 1
    val STATE_DIS_OFF = 2

    private val WEBSOCKET_PATH = "/websocket"

    private var handshaker: WebSocketServerHandshaker? = null


    companion object {
        val masterChannels: ChannelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE) // CLIENT
        val slaveChannels: ChannelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE) // RINO BOARD
    }

    @Throws(
        NumberFormatException::class,
        IllegalArgumentException::class,
        ClassCastException::class,
        UnsupportedOperationException::class
    )
    override fun handlerAdded(ctx: ChannelHandlerContext) {
        val incoming = ctx.channel()
        val connPort = incoming.localAddress().toString().split("\\:".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[1].trim { it <= ' ' }.toInt()
        var channelKind = "Master"
        // Slave
        if (BinderWebSocketServer.getInstance().slavePort === connPort) {
            slaveChannels.add(incoming)
            channelKind = "Slave"
        } else if (BinderWebSocketServer.getInstance().masterPort === connPort) {
            masterChannels.add(incoming)
        }
        logger.debug(
            "[SERVER] : {} has left >> [{} PORT] : {}",
            incoming.remoteAddress(),
            channelKind,
            connPort
        )
    }

    @Throws(NumberFormatException::class, ClassCastException::class, java.lang.UnsupportedOperationException::class)
    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        val incoming = ctx.channel()
        val connPort = incoming.localAddress().toString().split("\\:".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[1].trim { it <= ' ' }.toInt()
        var channelKind = "Master"
        //slave
        if (BinderWebSocketServer.getInstance().slavePort === connPort) {
            slaveChannels.remove(incoming)
            channelKind = "Slave"
        } else if (BinderWebSocketServer.getInstance().masterPort === connPort) {
            masterChannels.remove(incoming)
        }
        logger.debug(
            "[SERVER] : {} has left >> [{} PORT] : {}",
            incoming.remoteAddress(),
            channelKind,
            connPort
        )
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any?) {
        if (msg is FullHttpRequest) {                                    // Client 최초 연결 시
            handleHttpRequest(ctx, msg as FullHttpRequest)

            // 연결 시 연결 Check
            val stateMap: MutableMap<String, String> = Maps.newHashMap()
            stateMap["command"] = "connection"
            ctx.channel().writeAndFlush(TextWebSocketFrame(JSONSerializer().serialize(stateMap)))
        } else if (msg is WebSocketFrame) {                        // 데이터 받을때 와 연결 종료 시
            handleWebSocketFrame(ctx, msg as WebSocketFrame)
        }
    }

    private fun handleHttpRequest(ctx: ChannelHandlerContext, req: FullHttpRequest) {
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
            val resMap = JSONDeserializer<Any>().deserialize(frame.text()) as Map<String, Any>
            //        	logger.debug("WEB SOCKET MSG : {} ", StringUtil.toString(resMap));
            if (resMap["command"] == "connection") {
                logger.debug("[WebSocket Recieve Connection Command]")
            } else if (resMap["command"] == "connectionCheck") {
                logger.debug("[Connection Check] : {}", resMap.toString())
                val bindModeObj = resMap["bindMode"]
                if (bindModeObj != null) {
                    val binderMode = bindModeObj.toString()
                    if (binderMode.equals("master", ignoreCase = true)) {
                        var binderId = resMap["binderId"].toString()
                        if (binderId == null) {
                            binderId = ""
                        }
                        logger.debug("[BinderId : {}", binderId)
                        if (STATE_ON == resMap["state"].toString().toInt()) {
                            val oldMasterChannel: Channel = masterChannelMap[binderId]!!
                            if (oldMasterChannel == null) {
                                logger.debug("OldChannel is NULL")
                                masterChannelMap.put(binderId, ctx.channel())
                            } else {
                                logger.debug("[BinderId : {}", binderId)
                                for (channel in masterChannels) {
                                    if (channel == oldMasterChannel) {
                                        masterChannels.remove(channel)
                                        val stateMap: MutableMap<String, Any> = Maps.newHashMap()
                                        stateMap["command"] = "connectionCheck"
                                        stateMap["state"] = STATE_DIS_OFF
                                        channel.writeAndFlush(TextWebSocketFrame(JSONSerializer().serialize(stateMap)))
                                    }
                                }
                                masterChannelMap.put(binderId, ctx.channel())
                            }
                        }
                    }
                }
            } else if (resMap["command"] == "message") {
                BinderMsgQueue.getInstance().put(JSONSerializer().serialize(resMap))
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