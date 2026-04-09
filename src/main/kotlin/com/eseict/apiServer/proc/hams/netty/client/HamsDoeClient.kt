package com.eseict.apiServer.proc.hams.netty.client

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.net.SocketAddress


class HamsDoeClient {

    private val logger: Logger = LoggerFactory.getLogger(HamsDoeClient::class.java)

    private val bs: Bootstrap = Bootstrap()
    private var addr_: SocketAddress? = null
    private var msgArr: List<String>? = null

    private val clientHandler: NettyDoeClientHandler = NettyDoeClientHandler()



    companion object {
        @Volatile
        private var instance: HamsDoeClient? = null

        @JvmStatic
        fun getInstance(host: String, port: Int): HamsDoeClient =
            instance ?: synchronized(this) {
                instance ?: HamsDoeClient(host,port).also {
                    instance = it
                }
            }

        @JvmStatic
        fun getInstance(host: String, port: String): HamsDoeClient =
            instance ?: synchronized(this) {
                instance ?: HamsDoeClient(host,port.toInt()).also {
                    instance = it
                }
            }

        @JvmStatic
        fun getInstance(): HamsDoeClient? = instance
    }



    constructor(addr: SocketAddress? ){
        this.addr_ = addr
    }

    constructor(host: String?, port: Int): this(InetSocketAddress(host, port))


    //실제로 동작시킬 메소드 Bootstrap 연결 옵션 설정 및 연결 처리
    fun run() {
        if (addr_ == null) {
            logger.error("주소 정보가 없습니다.")
        } else if (msgArr == null || msgArr!!.size == 0) {
            logger.error("보낼 메시지가 없습니다.")
        }

        bs.group(NioEventLoopGroup(3))
            .channel(NioSocketChannel::class.java)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .handler(object : ChannelInitializer<SocketChannel>() {
                @Throws(Exception::class)
                override fun initChannel(ch: SocketChannel) {
                                                         ch.pipeline().addLast("clientHandler", clientHandler)
                }
            })
        val f: ChannelFuture = bs.connect(addr_)
        f.channel()
        logger.info(addr_.toString() + " connect()")

    }

    fun writeMessage(msg: ByteArray) {
        clientHandler.sendMsg(msg)
    }
}