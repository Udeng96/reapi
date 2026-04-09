package com.eseict.reapi.hams.client.netty;

import com.eseict.reapi.hams.client.config.HamsProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * HAMS DOE 이벤트 수신 Netty TCP 서버.
 * 레거시 SocketServerHamsMain.kt 이식 (Java 21).
 *
 * hams.tcp.enabled = false 또는 hams.doe-port = 0 이면 기동하지 않음 (개발 환경 graceful skip).
 */
@Slf4j
@Component
public class HamsDoeServer {

    private final HamsProperties props;
    private final HamsDoeServerInitializer initializer;

    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;

    public HamsDoeServer(HamsProperties props, HamsDoeServerInitializer initializer) {
        this.props = props;
        this.initializer = initializer;
    }

    @PostConstruct
    public void start() {
        if (!props.getTcp().isEnabled() || props.getDoePort() <= 0) {
            log.info("HAMS DOE 서버 비활성화 (tcp.enabled={}, doePort={})",
                    props.getTcp().isEnabled(), props.getDoePort());
            return;
        }

        bossGroup  = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        Thread thread = new Thread(() -> {
            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true)
                        .childHandler(initializer);

                ChannelFuture future = bootstrap.bind(props.getDoePort()).sync();
                log.info("HAMS DOE 서버 기동 완료: port={}", props.getDoePort());
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("HAMS DOE 서버 스레드 인터럽트");
            } catch (Exception e) {
                log.error("HAMS DOE 서버 기동 실패: {}", e.getMessage());
            }
        });
        thread.setName("hams-doe-server");
        thread.setDaemon(true);
        thread.start();
    }

    @PreDestroy
    public void stop() {
        log.info("HAMS DOE 서버 종료 중...");
        if (bossGroup  != null) bossGroup.shutdownGracefully();
        if (workerGroup != null) workerGroup.shutdownGracefully();
    }
}
