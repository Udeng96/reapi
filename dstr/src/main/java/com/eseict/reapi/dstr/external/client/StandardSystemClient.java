package com.eseict.reapi.dstr.external.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 표준시스템(OCS) HTTP 클라이언트.
 *
 * - 소방차 출동 정보 조회
 */
@Slf4j
@Component
public class StandardSystemClient {

    @Value("${platform.ocs.url:}")
    private String ocsUrl;

    @Value("${platform.ocs.subUrls.outTruck:}")
    private String outTruckSubUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 표준시스템 소방차 출동 정보 조회.
     *
     * @param statEvetOutbSeqn 상황 이벤트 출동 일련번호
     * @return 원본 JSON 문자열, 오류 시 null
     * @throws IllegalStateException OCS URL 미설정 시
     */
    public String getFireCar(String statEvetOutbSeqn) {
        if (ocsUrl.isBlank()) {
            throw new IllegalStateException("OCS URL이 설정되지 않았습니다 (platform.ocs.url)");
        }
        try {
            String url = ocsUrl + outTruckSubUrl.replace("{orcNo}", statEvetOutbSeqn);
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            log.error("표준시스템 API 호출 실패 (statEvetOutbSeqn={}): {}", statEvetOutbSeqn, e.getMessage());
            return null;
        }
    }
}
