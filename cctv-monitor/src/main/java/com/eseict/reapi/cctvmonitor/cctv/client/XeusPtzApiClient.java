package com.eseict.reapi.cctvmonitor.cctv.client;

import com.eseict.reapi.cctvmonitor.cctv.dto.PtzRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class XeusPtzApiClient {

    private static final Logger log = LoggerFactory.getLogger(XeusPtzApiClient.class);

    @Value("${platform.xeus.url}")
    private String xeusUrl;

    @Value("${platform.xeus.subUrls.setPtz}")
    private String setPtzUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String controlPtz(PtzRequest request) {
        boolean isDev = Boolean.parseBoolean(System.getenv("IS_DEV"));
        if (isDev) {
            return "true";
        }

        String url = UriComponentsBuilder.fromHttpUrl(xeusUrl + setPtzUrl)
            .queryParam("cctvMgrNo", request.cctvId())
            .queryParam("action", request.action())
            .queryParam("code", request.code())
            .toUriString();

        try {
            String response = restTemplate.getForObject(url, String.class);
            log.info("PTZ RESPONSE: {}", response);
            return response != null && response.contains("result") ? "true" : "FAIL";
        } catch (Exception e) {
            log.error("PTZ control failed", e);
            return "FAIL";
        }
    }
}
