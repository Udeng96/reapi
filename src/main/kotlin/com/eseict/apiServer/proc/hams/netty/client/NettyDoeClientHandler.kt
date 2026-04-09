package com.eseict.apiServer.proc.hams.netty.client

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.hams.HamsCmd
import com.eseict.apiServer.proc.rino.msgProc.SocketMsgQueue
import com.eseict.apiServer.util.Aes128
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.json.simple.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.Charset


class NettyDoeClientHandler : ChannelInboundHandlerAdapter {

    private val logger: Logger = LoggerFactory.getLogger(NettyDoeClientHandler::class.java)

    private var msgArr: List<String> = listOf<String>()
    private var idx = 0

    val id: String = "admin"

    val pw: String = "1"

    val cryptKey: String = "GoodLinkAESKey&&"


    var ctx: ChannelHandlerContext? = null

    constructor(msgArr: List<String>) : super() {
        this.msgArr = msgArr
    }

    constructor() : super() {
    }

    fun NettyClientHandler(msgArr: List<String>) {
        this.msgArr = msgArr
    }

    @Throws(Exception::class)
    override fun channelRegistered(ctx: ChannelHandlerContext?) {
        logger.info("채널 등록")
    }

    @Throws(Exception::class)
    override fun channelUnregistered(ctx: ChannelHandlerContext?) {
        println("채널 연결이 종료됨.")
    }

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        logger.info("채널이 메시지 발송할 준비가 됨.")
        this.ctx = ctx


        val cmd = HamsCmd(targetId = "000000002")
        var encryptPw = pw

        var crypt = Aes128(cryptKey)
        encryptPw = crypt.encrypt(encryptPw)!!

        val body = listOf(id, encryptPw )
        cmd.setHeader(body.size, HamsCmd.COMMAND.CON, "000").setBody(body).setTail()
        val message = cmd.makeMessage()

        sendMsg(message!!)
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        logger.info("메시지를 받는 메소드.")
        val buf = msg as ByteBuf
        val n = buf.readableBytes()
        if (n > 0) {
            val b = ByteArray(n)
            buf.readBytes(b)
            //수신메시지 출력
            val receiveMsg = String(b)
            logger.info("수신된 메시지 >$receiveMsg")


            val returnMessage = HamsCmd(receiveMsg!!)
            ///전달받은 메세지를 pasing 하고 허용하는 형식이 아니라면 WebSocket으로 전달하지않는다.
            if(returnMessage !=null){
                val body = returnMessage.parse?.parseBody()
                if (body != null && body.isNotEmpty() && body.size>1){ //정상적으로 parsing 된경우
                    logger.info("DOE RESULT = [{}]",body.toString())
                    //make event message and send web socket message
                    var json = JSONObject()
                    json["data"] = "hams"
                    json["info"] = body
                    SocketMsgQueue.getInstance()
                        .put(String(json.toJSONString().toByteArray(), Charset.forName(ApiConstants.ENCODING)))

                }
            }




            //보낼 메시지가 없으면 연결 종료
            if (msgArr.size == ++idx) {
                channelClose(ctx)
            } else {
//                sendMsg(ctx, idx)
            }
        }
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext?) {
        logger.info("메시지를 받는 동작이 끝나면 동작하는 메소드.")
    }

    private fun sendMsg(ctx: ChannelHandlerContext, i: Int) {
        val msg = msgArr[idx]
        val messageBuffer = Unpooled.buffer()
        messageBuffer.writeBytes(msg.toByteArray())
        ctx.writeAndFlush(messageBuffer) //메시지를 발송하고 flush처리
        logger.info("발송 메시지 >$msg")
    }

    fun sendMsg(msg: ByteArray) {
        logger.info("발송 메시지 >${String(msg)}")
        val messageBuffer = Unpooled.buffer()
        messageBuffer.writeBytes(msg)
        ctx?.writeAndFlush(messageBuffer) //메시지를 발송하고 flush처리

    }

    private fun channelClose(ctx: ChannelHandlerContext) {
        logger.error("채널 연결 종료")
        ctx.close()
    }
}