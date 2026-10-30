package com.eseict.reapi.hams.client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "hams")
public class HamsProperties {

    private String address = "198.51.100.7";
    private int port = 25170;
    private String id = "";
    private String pw = "";
    private String cryptKey = "";
    private String isUseCrypt = "true";

    /** 장비 → BE 방향 DOE 이벤트 수신 포트 (Netty TCP 서버) */
    private int doePort = 25171;

    /** 장비 응답 대기 timeout (ms). 이 시간 내 응답 없으면 빈 문자열 반환. */
    private int responseTimeoutMs = 3000;

    /** 개발 환경에서 TCP 연결 시도를 skip하려면 false로 설정 */
    private Tcp tcp = new Tcp();

    @Getter
    @Setter
    public static class Tcp {
        private boolean enabled = false;
    }
}
