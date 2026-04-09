package com.eseict.reapi.hams.client.netty;

import com.eseict.reapi.hams.hams.service.HamsDoeService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * DOE 이벤트 수신 핸들러.
 * 레거시 SocketServerHamsHandler.kt 이식 (Java 21).
 *
 * 수신 후 즉시 채널을 close (단발성 연결 — 레거시와 동일).
 * @Sharable: HamsDoeServerInitializer 에서 단일 인스턴스를 재사용하기 위해 필요.
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class HamsDoeServerHandler extends ChannelInboundHandlerAdapter {

    private final HamsDoeService doeService;

    public HamsDoeServerHandler(HamsDoeService doeService) {
        this.doeService = doeService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String message = (String) msg;
        log.debug("HAMS DOE 수신: {}", message);
        doeService.receiveDoe(message);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("HamsDoeServerHandler 오류: {}", cause.getMessage());
        ctx.close();
    }
}
