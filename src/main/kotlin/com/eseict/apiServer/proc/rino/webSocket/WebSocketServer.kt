package com.eseict.apiServer.proc.rino.webSocket

import com.eseict.common.log.LoggerManager
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.TooLongFrameException
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec

class WebSocketServer private constructor() {

    var logger = LoggerManager.getLogger(WebSocketServer::class.java)
    private var serverPort = 0
    var serverBootstrap: ServerBootstrap? = null
    var bossGroup: EventLoopGroup? = null
    var workerGroup: EventLoopGroup? = null

    companion object {

        @Volatile
        private var instance: WebSocketServer = WebSocketServer()

        fun getInstance(serverPort: String) = getInstance(serverPort.toInt())

        @JvmStatic
        fun getInstance(serverPort: Int): WebSocketServer {

            instance ?: synchronized(this) {
                instance ?: WebSocketServer().also {
                    instance = it
                    it.serverPort = serverPort
                }
            }

            if (instance != null) {
                instance.serverPort = serverPort
            }

            return instance
        }

    }

    fun init() {
        bossGroup = NioEventLoopGroup()
        workerGroup = NioEventLoopGroup()
        try {
            // ServerBootstrap는 서버설정 도우미 클래스
            serverBootstrap = ServerBootstrap()
            serverBootstrap!!.group(bossGroup, workerGroup) // NioServerSocketChannel클래스를 이용하여 새로운 채널 생성 (인스턴스화)
                .channel(NioServerSocketChannel::class.java)
                .childHandler(object : ChannelInitializer<SocketChannel>() {
                    @Throws(TooLongFrameException::class)
                    override fun initChannel(ch: SocketChannel) {
                        val channelPipeline = ch.pipeline()
                        channelPipeline.addLast(HttpServerCodec())
                            .addLast(HttpObjectAggregator(65536))
                            .addLast("handler", WebSocketServerHandler())
                    }
                }).option(ChannelOption.SO_BACKLOG, 128) //worker thread 최대 겟수
                .childOption(ChannelOption.SO_KEEPALIVE, true) // 연결 유지 셋팅
            val ch = serverBootstrap!!.bind(serverPort).sync()
            logger.debug("[{}] ch", ch.toString())
            logger.info("[ {} ] WebSocket Server Bind", serverPort)
        } catch (e: Exception) {
            // 웹소켓 바인드 에러
            logger.trace("WEBSOCKET BIND ERROR [{}]", serverPort)
            logger.error(e.message, e)
            System.exit(1)
        } finally {
            logger.debug("init() finally {}")
            //bossGroup.shutdownGracefully();
            //workerGroup.shutdownGracefully();
        }
    }

    // 언바인드
    fun wsocServerStop() {
        if (workerGroup != null) {
            workerGroup!!.shutdownGracefully()
        }
        if (bossGroup != null) {
            bossGroup!!.shutdownGracefully()
        }
    }

    // Destroy
    fun destroy() {
        if (workerGroup != null) {
            workerGroup!!.shutdownGracefully()
        }
        if (bossGroup != null) {
            bossGroup!!.shutdownGracefully()
        }
    }
}