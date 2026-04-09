package com.eseict.reapi.hams.client.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * DOE 서버 파이프라인 초기화.
 *
 * LineBasedFrameDecoder: \r\n 구분자 기준으로 프레임을 완성 후 ByteBuf emit.
 *   → TCP 부분 수신(partial read) 방지, "nothing" 처리 불필요.
 * StringDecoder: ByteBuf → UTF-8 String 변환.
 * HamsDoeServerHandler: 비즈니스 처리.
 */
@Component
public class HamsDoeServerInitializer extends ChannelInitializer<SocketChannel> {

    /** HAMS 프로토콜 최대 메시지 길이 (이 이상이면 예외 발생) */
    private static final int MAX_FRAME_LENGTH = 8192;

    private final HamsDoeServerHandler handler;

    public HamsDoeServerInitializer(HamsDoeServerHandler handler) {
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline()
                .addLast(new LineBasedFrameDecoder(MAX_FRAME_LENGTH))
                .addLast(new StringDecoder(StandardCharsets.UTF_8))
                .addLast(handler);
    }
}
