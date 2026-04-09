package com.eseict.apiServer.proc.hams

import com.eseict.apiServer.proc.hams.codec.ServerMessageHamsDecoder
import com.eseict.apiServer.proc.hams.codec.ServerMessageHamsEncoder


import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress

class SocketServerHamsMain private constructor(serverPort: Int) {

    val logger: Logger = LoggerFactory.getLogger(SocketServerHamsMain::class.java)


    private val bossGroup: EventLoopGroup = NioEventLoopGroup()
    private val workerGroup: EventLoopGroup = NioEventLoopGroup()
    private val workerThreadMaxCnt: Int = 128
    private var serverPort = serverPort


    companion object {

        @Volatile
        private var instance: SocketServerHamsMain? = null
        private var port = 0
        private val any: Any = Any()


        fun getInstance(port: String?): SocketServerHamsMain {
            return getInstance(Integer.parseInt(port))
        }

        fun getInstance() = instance

        @JvmStatic
        fun getInstance(port: Int): SocketServerHamsMain {
            return instance ?: synchronized(this) {
                instance ?: SocketServerHamsMain(port).also {
                    instance = it
                }
            }
        }


    }

    fun init() {

        try {
            //서버가 수신할 포트를 binding
            // handler에게 message를 알리도록 구성
            val sBootStrap = ServerBootstrap()
            sBootStrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java)
                .localAddress(InetSocketAddress(serverPort))
                .childHandler(object : ChannelInitializer<SocketChannel>() {

                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline()
                            //Inbound
                            .addLast(ServerMessageHamsDecoder())
                            //Outbound
                            .addLast(ServerMessageHamsEncoder())
                            .addLast(SocketServerHamsHandler())
                    }

                }).option(ChannelOption.SO_BACKLOG, workerThreadMaxCnt)
                .childOption(ChannelOption.SO_KEEPALIVE, true)

            // sync() => I/O 작업이 끝날때까지 대기 했다가 실패하면 실패원인을 반환한다.
            val future: ChannelFuture = sBootStrap.bind(serverPort).sync()
            logger.info("[ {} ] Socket Server Bind result is [{}]", serverPort, future.isSuccess);
//            future.channel().closeFuture().sync()

        } catch (e: Exception) {
            logger.trace("SOCKET BIND ERROR [{}]", serverPort)
            logger.error(e.message, e)
        }

    }

    fun close() {
        logger.info("CLOSE SERVER [{}]", serverPort)
        workerGroup.shutdownGracefully()
        bossGroup.shutdownGracefully()
    }

}


