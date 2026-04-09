package com.eseict.apiServer.proc.rino.webSocket.binder

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
import java.util.concurrent.ExecutionException

class BinderWebSocketServer private constructor() {

    var logger = LoggerManager.getLogger(BinderWebSocketServer::class.java)


    val masterPort = 26000 // WEB CLIENT

    val slavePort = 30000 // RINO BIG BOARD


    var serverBootstrap: ServerBootstrap? = null
    var bossGroup: EventLoopGroup? = null
    var workerGroup: EventLoopGroup? = null

    companion object {

        @Volatile
        private var instance: BinderWebSocketServer = BinderWebSocketServer()

        @JvmStatic
        fun getInstance(): BinderWebSocketServer =
            instance ?: synchronized(this) {
                instance ?: BinderWebSocketServer().also {
                    instance = it
                }
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
                    @Throws(TooLongFrameException::class)
                    override fun initChannel(ch: SocketChannel) {
                        logger.debug("initChannel Called")
                        val channelPipeline = ch.pipeline()
                        channelPipeline.addLast(HttpServerCodec())
                            .addLast(HttpObjectAggregator(65536))
                            .addLast("handler", BinderWebSocketServerHandler())
                    }
                }).option(ChannelOption.SO_BACKLOG, 128) //worker thread 최대 겟수
                .childOption(ChannelOption.SO_KEEPALIVE, true) // 연결 유지 셋팅
            val slaveCh = serverBootstrap!!.bind(slavePort).sync()
            val masterCh = serverBootstrap!!.bind(masterPort).sync()
            logger.debug("masterCh =[{}]", masterCh)
            logger.debug("slaveCh =[{}]", slaveCh)
            logger.debug("Open your web browser and navigate to {}", masterPort)
            logger.debug("Open your web browser and navigate to {}", slavePort)
        } catch (e: InterruptedException) {
            logger.trace(e.message)
            Thread.currentThread().interrupt()
        } finally {
            logger.debug("Web Socket Server Started !!")
            //bossGroup.shutdownGracefully();
            //workerGroup.shutdownGracefully();
        }
    }

    @Throws(InterruptedException::class, ExecutionException::class)
    fun destroy() {
//    	workerGroup.shutdownGracefully();
//    	bossGroup.shutdownGracefully();
        if (workerGroup != null) {
            val rtn = workerGroup!!.shutdownGracefully()
            rtn.get()
        }
        if (bossGroup != null) {
            val rtn = bossGroup!!.shutdownGracefully()
            rtn.get()
        }
    }


    fun finaliaze() {
        logger.debug("Websocket Server Finalized")
    }

}