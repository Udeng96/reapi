package com.eseict.reapi.dstr.external;

import com.eseict.reapi.dstr.external.client.VWorldApiClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class VWorldController {

    private final VWorldApiClient vWorldApiClient;

    public VWorldController(VWorldApiClient vWorldApiClient) {
        this.vWorldApiClient = vWorldApiClient;
    }

    /** 좌표 → 위치명 */
    @GetMapping("/byPos/posNm")
    public ResponseEntity<Map<String, String>> getPosNm(
        @RequestParam double lat,
        @RequestParam double lng
    ) {
        String name = vWorldApiClient.getPositionNameByLatLng(lat, lng);
        return ResponseEntity.ok(Map.of("result", name));
    }

    /** 주소 검색 */
    @GetMapping("/addr/juso")
    public ResponseEntity<List<Map<String, Object>>> getJuso(
        @RequestParam String word,
        @RequestParam String type
    ) {
        return ResponseEntity.ok(vWorldApiClient.searchPositionName(word, type));
    }
}
