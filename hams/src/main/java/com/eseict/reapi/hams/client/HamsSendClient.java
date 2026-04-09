package com.eseict.reapi.hams.client;

import com.eseict.reapi.hams.client.config.HamsProperties;
import com.eseict.reapi.hams.client.protocol.HamsCmd;
import com.eseict.reapi.hams.client.util.Aes128;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * HAMS 무인국사 TCP 소켓 클라이언트.
 * 레거시 HamsSendClient.kt 이식 (Java 21).
 *
 * Spring Bean(@Component)으로 Singleton 관리.
 *
 * [Graceful degradation]
 * - hams.tcp.enabled=false 이면 연결 시도 자체를 skip → isConnect() == false 반환
 * - 연결 실패(IOException) 시 WARN 로그만 남기고 false 반환 (예외 전파 없음)
 * - checkDoor/checkTempHumi/sendDoc 호출 측에서 isConnect() == false 이면 empty DTO 사용
 *
 * [응답 대기 전략]
 * - setSoTimeout(responseTimeoutMs) 으로 읽기 timeout 설정
 * - BufferedReader.readLine() 으로 \r\n 까지 블로킹 → 데이터 도착 즉시 반환
 * - 기존 Thread.sleep(2000) × 5회(최대 10초) 재귀 폴링 제거
 */
@Slf4j
@Component
public class HamsSendClient {

    private final HamsProperties props;

    private volatile boolean connect = false;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public HamsSendClient(HamsProperties props) {
        this.props = props;
    }

    // ──────────────────────────────────────────────────────────
    // 연결
    // ──────────────────────────────────────────────────────────

    /**
     * 장비에 TCP 연결 후 CON(로그인) 명령을 전송한다.
     *
     * @return 연결·로그인 성공이면 true, 실패이면 false (예외 전파 없음)
     */
    public synchronized boolean doConnect() {
        if (!props.getTcp().isEnabled()) {
            log.debug("HAMS TCP disabled (hams.tcp.enabled=false). skip connect.");
            return false;
        }

        if (socket != null && socket.isConnected()) {
            log.debug("HAMS already connected.");
            return true;
        }

        try {
            log.info("HAMS TCP connecting to {}:{}", props.getAddress(), props.getPort());
            socket = new Socket();
            socket.connect(new InetSocketAddress(props.getAddress(), props.getPort()), 5000);
            socket.setKeepAlive(true);
            socket.setSoTimeout(props.getResponseTimeoutMs());
            socket.setTcpNoDelay(false);
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
            );
        } catch (IOException e) {
            log.warn("HAMS TCP 연결 실패 ({}:{}): {}", props.getAddress(), props.getPort(), e.getMessage());
            clearResources();
            return false;
        }

        // CON 명령으로 로그인
        try {
            String encryptedPw = buildEncryptedPw();
            HamsCmd cmd = new HamsCmd("000000002");
            cmd.setHeader(2, HamsCmd.COMMAND.CON, "000")
               .setBody(List.of(props.getId(), encryptedPw))
               .setTail();

            byte[] message = cmd.makeMessage();
            if (message == null) {
                log.warn("HAMS CON 메시지 생성 실패");
                clearResources();
                return false;
            }

            String response = doWriteAndFlushInternal(new String(message, StandardCharsets.UTF_8));
            if (response == null || response.isEmpty()) {
                log.warn("HAMS CON 응답 없음");
                clearResources();
                return false;
            }

            HamsCmd returnCmd = new HamsCmd(response, true);
            if (returnCmd.getParse() != null) {
                var body = returnCmd.getParse().parseBody();
                connect = body != null
                    && String.valueOf(HamsCmd.RESULT_CODE.SUCCESS.num)
                             .equals(body.get(HamsCmd.FIELD_NM.LOGIN_RESULT.name()));
            }

            if (connect) {
                log.info("HAMS TCP 연결·로그인 성공");
            } else {
                log.warn("HAMS TCP 연결됐지만 로그인 실패. response: {}", response);
            }

        } catch (Exception e) {
            log.warn("HAMS CON 명령 오류: {}", e.getMessage());
            clearResources();
            return false;
        }

        return connect;
    }

    // ──────────────────────────────────────────────────────────
    // 쓰기/읽기
    // ──────────────────────────────────────────────────────────

    public String doWriteAndFlush(byte[] msg) {
        if (writer == null || reader == null) {
            log.debug("HAMS writer/reader is null");
            return "";
        }
        return doWriteAndFlushInternal(new String(msg, StandardCharsets.UTF_8));
    }

    public String doWriteAndFlush(String msg) {
        if (writer == null || reader == null) {
            log.debug("HAMS writer/reader is null");
            return "";
        }
        return doWriteAndFlushInternal(msg);
    }

    /**
     * 메시지 전송 후 응답 한 줄(\r\n 기준) 읽기.
     *
     * setSoTimeout 이 걸려 있으므로 데이터가 없으면 responseTimeoutMs 후 SocketTimeoutException.
     * 데이터가 도착해 있으면 즉시 반환.
     */
    private String doWriteAndFlushInternal(String msg) {
        try {
            writer.print(msg);
            writer.flush();
            String line = reader.readLine(); // \r\n 까지 블로킹
            return line != null ? line.trim() : "";
        } catch (SocketTimeoutException e) {
            log.warn("HAMS 응답 timeout ({}ms)", props.getResponseTimeoutMs());
            return "";
        } catch (IOException e) {
            log.error("HAMS read error: {}", e.getMessage());
            clearResources();
            return "";
        }
    }

    // ──────────────────────────────────────────────────────────
    // 상태 확인
    // ──────────────────────────────────────────────────────────

    public boolean isConnect() {
        return socket != null && socket.isConnected() && connect;
    }

    // ──────────────────────────────────────────────────────────
    // 유틸
    // ──────────────────────────────────────────────────────────

    private String buildEncryptedPw() {
        if (Boolean.parseBoolean(props.getIsUseCrypt())) {
            Aes128 aes = new Aes128(props.getCryptKey());
            String encrypted = aes.encrypt(props.getPw());
            return encrypted != null ? encrypted : props.getPw();
        }
        return props.getPw();
    }

    @PreDestroy
    public void close() {
        clearResources();
    }

    private synchronized void clearResources() {
        connect = false;
        try { if (writer != null) writer.close(); } catch (Exception e) { log.error("writer close: {}", e.getMessage()); }
        try { if (reader != null) reader.close(); } catch (Exception e) { log.error("reader close: {}", e.getMessage()); }
        try { if (socket != null) socket.close(); } catch (Exception e) { log.error("socket close: {}", e.getMessage()); }
        writer = null;
        reader = null;
        socket = null;
    }
}
