package com.smhrd.controller;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Recommend API", description = "상품 추천 API")
@RequestMapping("/api/recommend")
public class CtrConnectController {

    @PostMapping("/ctrConnect")
    @Operation(summary = "Ctr 모델 연결하는 API", description = "CTR 예측하는 모델의 FastAPI와 연결하는 API")
    public ResponseEntity<?> recommend(@RequestBody Map<String, Object> userInfo) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8001/recommend";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(userInfo, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        return ResponseEntity.ok(response.getBody());
    }

}
