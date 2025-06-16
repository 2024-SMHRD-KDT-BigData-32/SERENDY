package com.smhrd.controller;

import com.smhrd.entity.ProductInfo;
import com.smhrd.repository.ProductInfoRepository;
import com.smhrd.DTO.ProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RecommendController {

    private final ProductInfoRepository productRepo;

    @PostMapping("/recommend")
    public ResponseEntity<?> getRecommendation(@RequestBody ProfileRequest profile) {

        // 1. FastAPI에 요청 보내기
        String fastApiUrl = "http://localhost:8001/recommend"; // 포트 번호 정확히 확인

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProfileRequest> request = new HttpEntity<>(profile, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(fastApiUrl, request, Map.class);

        // 2. 응답에서 prod ID 추출
        List<String> prodIdStrings = (List<String>) response.getBody().get("recommendations");

        // 3. "prod1" → 1 형식으로 변환
        List<Integer> prodIds = prodIdStrings.stream()
                .map(id -> Integer.parseInt(id.replace("prod", "")))
                .collect(Collectors.toList());

        // 4. DB에서 상품 정보 조회
        List<ProductInfo> products = productRepo.findByProdIdIn(prodIds);

        return ResponseEntity.ok(products);
    }
}
