package com.smhrd.controller;

import com.smhrd.DTO.CtrRequest;
import com.smhrd.entity.ProductInfo;
import com.smhrd.repository.ProductInfoRepository;
import com.smhrd.repository.StylePrefRepository;
import com.smhrd.service.CtrService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@Tag(name = "Recommend API", description = "상품 추천 API")
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class CtrController {

    private final ProductInfoRepository productInfoRepository;
    private final CtrService ctrService;

    @PostMapping("/ctr")
    @Operation(summary = "CTR Score 계산", description = "FastAPI 모델 서버를 호출하여 상품 추천 결과를 반환합니다.")
    public ResponseEntity<?> getRecommendation(@RequestBody CtrRequest request) {

    	String userId = request.getUserId();
        List<Integer> candidate_items = request.getCandidateItems();
        
        // 1. 클릭 기반 속성 추출 (fit, color)
        Map<String, String> userClickPreference = ctrService.getMostFrequentFitAndColor(userId);
        List<String> fitList = userClickPreference.get("fit") != null ? List.of(userClickPreference.get("fit")) : List.of();
        List<String> colorList = userClickPreference.get("color") != null ? List.of(userClickPreference.get("color")) : List.of();
        
        List<String> styleCodes = ctrService.getStylePref(userId);
        String userAge = ctrService.getAge(userId); 
        
        // 2. FastAPI 요청 준비
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("age", userAge);
        requestBody.put("style", styleCodes);
        requestBody.put("fit", fitList);
        requestBody.put("color", colorList);
        requestBody.put("candidate_items", candidate_items);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        String fastApiUrl = "http://localhost:8001/recommend";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(fastApiUrl, requestEntity, Map.class);

        // 3. 추천 상품 ID 추출
        List<Integer> prodIds = (List<Integer>) response.getBody().get("recommended_items");

        return ResponseEntity.ok(prodIds);
    }
    
    @PostMapping("/ctrAll")
    @Operation(summary = "전체 상품 기준 CTR Score 계산", description = "FastAPI 모델 서버를 호출하여 전체 상품 기준으로 상품 추천 결과를 반환합니다.")
    public ResponseEntity<?> getRecommendationAll(@RequestParam String userId) {
    	
        // 1. 클릭 기반 속성 추출 (fit, color)
        Map<String, String> userClickPreference = ctrService.getMostFrequentFitAndColor(userId);
        List<String> fitList = userClickPreference.get("fit") != null ? List.of(userClickPreference.get("fit")) : List.of();
        List<String> colorList = userClickPreference.get("color") != null ? List.of(userClickPreference.get("color")) : List.of();
        
        System.out.println(userId + "의 선호 : " + userClickPreference);
        List<String> styleCodes = ctrService.getStylePref(userId);
        System.out.println(userId + "의 선호 스타일 : " + styleCodes);
        String userAge = ctrService.getAge(userId).trim();
        System.out.println(userId + "의 나이 : " + userAge);
        
        // 클릭로그나 선호 정보, 나이 정보가 없으면 빈 리스트 반환
        if (userClickPreference == null || userClickPreference.isEmpty() || styleCodes == null || styleCodes.isEmpty() || userAge == null || ctrService.getAge(userId).isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        
        // 2. FastAPI 요청 준비
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("age", userAge);
        requestBody.put("style", styleCodes);
        requestBody.put("fit", fitList);
        requestBody.put("color", colorList);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        String fastApiUrl = "http://localhost:8001/recommend/all";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(fastApiUrl, requestEntity, Map.class);

        // 3. 추천 상품 ID 추출
        List<Integer> prodIds = (List<Integer>) response.getBody().get("recommended_items");

        // 4. DB에서 추천 상품 정보 조회
        List<ProductInfo> products = productInfoRepository.findByProdIdIn(prodIds);

        return ResponseEntity.ok(products);
    }
    
}
