package com.eseict.apiServer.proc.rino.codec

import com.eseict.apiServer.dto.event.GeneralMessage
import com.eseict.apiServer.util.HeaderUtil
import com.eseict.common.base.ByteSupport
import com.eseict.common.base.ByteSupportException
import com.eseict.common.core.unique.IdGeneratorException
import com.eseict.common.datetime.DateTimeUtilException
import com.eseict.common.log.LoggerManager
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import java.io.UnsupportedEncodingException
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class ServerMEssageDecoder : ByteToMessageDecoder() {

    private val logger = LoggerManager.getLogger(ServerMEssageDecoder::class.java)

    @Throws(UnsupportedEncodingException::class, DateTimeUtilException::class, IdGeneratorException::class)
    override fun decode(
        ctx: ChannelHandlerContext, `in`: ByteBuf,
        out: MutableList<Any?>
    ) {
        try {
            // 헤더 타입 코드 크기와 비교
            if (`in`.readableBytes() < HeaderUtil.getHeaderTotalLen()) {
//				logger.info("헤더 사이즈 에러");
                return
            }
            // 읽는 위치 마크
            `in`.markReaderIndex()
            logger.trace(
                "capacity {} , readable {}",
                `in`.capacity(),
                `in`.readableBytes()
            )
            if (HeaderUtil.getHeaderTotalLen() > `in`.readableBytes()) {
                return
            }

            //헤더 전체 read
            val headerArr = ByteArray(HeaderUtil.getHeaderTotalLen())
            `in`.readBytes(headerArr)


            // 헤더 타입 코드
            val headerTypCdByte = ByteArray(HeaderUtil.getHeaderItemLength(HeaderUtil.HEDR_TYP_CD))
            System.arraycopy(
                headerArr,
                HeaderUtil.getHeaderItemStartPos(HeaderUtil.HEDR_TYP_CD),
                headerTypCdByte,
                0,
                HeaderUtil.getHeaderItemLength(HeaderUtil.HEDR_TYP_CD)
            )
            // MEP
            val mepArr = ByteArray(HeaderUtil.getHeaderItemLength(HeaderUtil.MSG_EXCH_PATRN))
            System.arraycopy(
                headerArr,
                HeaderUtil.getHeaderItemStartPos(HeaderUtil.MSG_EXCH_PATRN),
                mepArr,
                0,
                HeaderUtil.getHeaderItemLength(HeaderUtil.MSG_EXCH_PATRN)
            )

            // 바디 사이즈 읽기
            val bodyLenArr = ByteArray(HeaderUtil.getHeaderItemLength(HeaderUtil.BODY_LEN))
            System.arraycopy(
                headerArr,
                HeaderUtil.getHeaderItemStartPos(HeaderUtil.BODY_LEN),
                bodyLenArr,
                0,
                HeaderUtil.getHeaderItemLength(HeaderUtil.BODY_LEN)
            )
            val bodyLen =
                ByteSupport.byteArrayToInt(bodyLenArr, ByteOrder.LITTLE_ENDIAN) // byte[] - > int  (little endian)
            if (bodyLen > 0) { // ACK가 아닌 경우
                // 남은 길이와 바디사이즈 일치 체크
                // 헤더의 body 길이보다 남은 버퍼의 크기가 클 경우 해당 body 길이만큼만 핸들러로 전달
                if (`in`.readableBytes() >= bodyLen) {


                    // 바디 읽기
                    val bodyArr = ByteArray(bodyLen)
                    `in`.readBytes(bodyArr)

                    // GeneralMessage 셋팅
                    val message = GeneralMessage()
                    message.headerTyp = headerTypCdByte
                    message.header = headerArr
                    message.bodyLen = bodyLen
                    message.body = bodyArr

                    // 핸들러로 전달
                    out.add(message)
                    return
                } else {

                    // 메시지가 덜 들어왓을 경우 decode 재호출 (소켓 연결이 끈길 때까지)
                    if (ctx.channel().isActive || ctx.channel().isOpen) {
                        `in`.resetReaderIndex()
                        return
                    }

                    // 메시지 길이 불일치 오류
                    // 읽는 위치 초기화
                    `in`.resetReaderIndex()
                    val msgOrginByte = ByteArray(`in`.readableBytes())
                    `in`.readBytes(msgOrginByte)
                    val headerTypCdByteStr = String(headerTypCdByte, StandardCharsets.UTF_8)
                    //						errOccrHistService.setErrOutbHist(errCd, new String(msgOrginByte, MRSConstants.CHAR_SET));
                    logger.error(
                        "[{}] [ SIZE ERROR ] [ REMAIN : {}, BODY DEMAND : {} ]",
                        headerTypCdByteStr,
                        msgOrginByte.size - HeaderUtil.getHeaderTotalLen(),
                        bodyLen
                    )
                    return
                }
            } else { // ACK 수신시
                // GeneralMessage 셋팅
                val message = GeneralMessage()
                message.headerTyp = headerTypCdByte
                message.header = headerArr
                message.bodyLen = bodyLen
                message.body = null

                // 핸들러로 전달
                out.add(message)
            }
        } catch (e: ByteSupportException) {
            // 메시지 변환 오류
            // 읽는 위치 초기화
            `in`.resetReaderIndex()
        }
    }
}