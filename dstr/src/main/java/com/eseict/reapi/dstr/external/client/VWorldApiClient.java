package com.eseict.reapi.dstr.external.client;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * VWorld 공간정보 오픈플랫폼 API 클라이언트.
 *
 * - 역지오코딩: 좌표 → 위치명
 * - 주소/장소 검색
 */
@Slf4j
@Component
public class VWorldApiClient {

    @Value("${vworld.api.url:https://api.vworld.kr}")
    private String vWorldUrl;

    @Value("${vworld.api.key:}")
    private String vWorldKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /** 좌표 → 위치명 역지오코딩 */
    public String getPositionNameByLatLng(double lat, double lng) {
        try {
            String point = lng + "," + lat;
            String url = vWorldUrl + "/req/address?" +
                "service=address&request=getAddress&version=2.0" +
                "&crs=epsg:4326&point=" + point +
                "&type=both&format=json&key=" + vWorldKey;

            String response = restTemplate.getForObject(url, String.class);
            if (response != null) {
                return JsonParser.parseString(response)
                    .getAsJsonObject().get("response")
                    .getAsJsonObject().get("result")
                    .getAsJsonArray().get(0)
                    .getAsJsonObject().get("text").getAsString();
            }
        } catch (Exception e) {
            log.error("VWorld 역지오코딩 실패: {}", e.getMessage());
        }
        return "";
    }

    /** 주소/장소 검색 */
    public List<Map<String, Object>> searchPositionName(String word, String type) {
        if (word == null || word.isBlank()) return List.of();

        try {
            String encoded = URLEncoder.encode(word, StandardCharsets.UTF_8);
            String searchType = "place".equals(type) ? "place" : "address";
            String url = vWorldUrl + "/req/search?" +
                "service=search&request=search&version=2.0" +
                "&crs=epsg:4326&size=10&page=1&type=" + searchType +
                "&format=json&key=" + vWorldKey +
                "&query=" + encoded;

            String response = restTemplate.getForObject(url, String.class);
            if (response != null) {
                var items = JsonParser.parseString(response)
                    .getAsJsonObject().get("response")
                    .getAsJsonObject().get("result")
                    .getAsJsonObject().get("items");
                Gson gson = new Gson();
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> result = gson.fromJson(items, List.class);
                return result != null ? result : List.of();
            }
        } catch (Exception e) {
            log.error("VWorld 주소 검색 실패: {}", e.getMessage());
        }
        return List.of();
    }
}
