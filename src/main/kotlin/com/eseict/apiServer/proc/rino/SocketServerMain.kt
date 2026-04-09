package com.eseict.apiServer.proc.rino

import com.eseict.apiServer.proc.rino.codec.ServerMEssageDecoder
import com.eseict.apiServer.proc.rino.codec.ServerMessageEncoder
import com.eseict.common.log.LoggerManager
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler

class SocketServerMain private constructor() {


    private val logger = LoggerManager.getLogger(SocketServerMain::class.java)

    private var serverPort = 0
    var serverBootstrap: ServerBootstrap? = null
    var bossGroup: EventLoopGroup? = null
    var workerGroup: EventLoopGroup? = null


    private val lock = Any()

    constructor(port: Int) : this() {
        this.serverPort = port
    }

    companion object {
        @Volatile
        private var instance: SocketServerMain = SocketServerMain()

        fun getInstance(serverPortStr: String) = getInstance(serverPortStr.toInt())

        @JvmStatic
        fun getInstance(serverPort: Int): SocketServerMain {

            instance ?: synchronized(this) {
                instance ?: SocketServerMain().also {
                    instance = it
                    instance.serverPort = serverPort
                }
            }
            if (instance != null) {
                instance.serverPort = serverPort
            }

            return instance
        }

    }

    fun init() {
        // NioEventLoopGroup 는 멀티 쓰레드 이벤트 루프
        // bossGroup는 들어오는 연결을 수락
        // workerGroup는 연결의 트레픽 처리
        bossGroup = NioEventLoopGroup()
        workerGroup = NioEventLoopGroup()
        try {

            // ServerBootstrap는 서버설정 도우미 클래스
            serverBootstrap = ServerBootstrap()
            serverBootstrap!!.group(bossGroup, workerGroup) // NioServerSocketChannel클래스를 이용하여 새로운 채널 생성 (인스턴스화)
                .channel(NioServerSocketChannel::class.java)
                .childHandler(object : ChannelInitializer<SocketChannel>() {
                    @Throws(Exception::class)
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline()
                            .addLast("encoder", ServerMessageEncoder())
                            .addLast("decoder", ServerMEssageDecoder())
                            .addLast("handler", SocketServerHandler())
                    }
                }).option(ChannelOption.SO_BACKLOG, 128) // worker thread 최대 겟수
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_LINGER, 0) // 연결 유지 셋팅

            // 바인딩 해서 들어오는 연결을 수락
            val fText = serverBootstrap!!.bind(serverPort).sync() // 서버 시작
            logger.info("[ {} ] Socket Server Bind", serverPort)
        } catch (e: InterruptedException) {
            // 소켓 바인드 에러
            logger.trace("SOCKET BIND ERROR [{}]", serverPort)
            logger.error(e.message, e)
            System.exit(1)
        } finally {
            // workerGroup.shutdownGracefully();
            // bossGroup.shutdownGracefully();
        }
    }


}