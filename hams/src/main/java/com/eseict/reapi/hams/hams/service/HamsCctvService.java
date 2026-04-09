package com.eseict.reapi.hams.hams.service;

import com.eseict.reapi.hams.hams.dto.HamsCctvInfoDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class HamsCctvService {

    private final ObjectMapper objectMapper;
    private List<HamsCctvInfoDto> allCctvList = Collections.emptyList();

    public HamsCctvService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadCctvData() {
        try {
            ClassPathResource resource = new ClassPathResource("data/cctvInfo.json");
            List<Map<String, String>> raw = objectMapper.readValue(
                resource.getInputStream(),
                new TypeReference<List<Map<String, String>>>() {}
            );
            allCctvList = raw.stream()
                .map(m -> new HamsCctvInfoDto(
                    m.get("cctvId"),
                    m.get("cctvName"),
                    m.getOrDefault("mgrNo", ""),
                    m.get("hamsName")
                ))
                .toList();
        } catch (IOException e) {
            allCctvList = Collections.emptyList();
        }
    }

    public List<HamsCctvInfoDto> getCctvList(String hamsName) {
        if (hamsName == null || hamsName.isBlank()) {
            return Collections.emptyList();
        }
        return allCctvList.stream()
            .filter(c -> hamsName.equals(c.hamsName()))
            .toList();
    }
}
