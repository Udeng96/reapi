package com.eseict.reapi.dstr.facility.service;

import com.eseict.reapi.dstr.facility.dto.PtzInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class CctvControlService {

    private static final Logger log = LoggerFactory.getLogger(CctvControlService.class);

    @Value("${platform.xeus.url}")
    private String xeusUrl;

    @Value("${platform.xeus.subUrls.setPtz}")
    private String setPtzUrl;

    private final RestTemplate restTemplate;

    public CctvControlService() {
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Object> doControl(PtzInfo controlInfo) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(xeusUrl + setPtzUrl)
                .queryParam("cctvMgrNo", controlInfo.getCctvId())
                .queryParam("action", controlInfo.getAction())
                .queryParam("code", controlInfo.getCode())
                .toUriString();

            log.info("PTZ control request: {}", url);

            String response = restTemplate.getForObject(url, String.class);
            log.info("PTZ control response: {}", response);

            if (response != null && response.contains("\"result\"")) {
                return Map.of("result", "SUCCESS", "message", response);
            } else {
                return Map.of("result", "FAIL", "message", response != null ? response : "No response");
            }
        } catch (Exception e) {
            log.error("PTZ control failed: {}", e.getMessage(), e);
            return Map.of("result", "FAIL", "message", e.getMessage());
        }
    }
}
