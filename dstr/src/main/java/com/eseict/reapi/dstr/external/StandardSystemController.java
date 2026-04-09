package com.eseict.reapi.dstr.external;

import com.eseict.reapi.dstr.external.client.StandardSystemClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/standard/system")
public class StandardSystemController {

    private final StandardSystemClient standardSystemClient;

    public StandardSystemController(StandardSystemClient standardSystemClient) {
        this.standardSystemClient = standardSystemClient;
    }

    /** 표준시스템 소방차 정보 */
    @GetMapping
    public ResponseEntity<?> getFireCar(@RequestParam String statEvetOutbSeqn) {
        try {
            String response = standardSystemClient.getFireCar(statEvetOutbSeqn);
            return response != null
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(Map.of("result", "FAIL"));
        } catch (IllegalStateException e) {
            log.warn("표준시스템 설정 오류: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("result", "OCS URL not configured"));
        }
    }
}
