package com.smhrd.controller;

import com.smhrd.entity.ProductInfo;
import com.smhrd.repository.ProductInfoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.smhrd.DTO.ProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Recommend API", description = "상품 추천 API")
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class CtrController {

    private final ProductInfoRepository productRepo;
    
    @PostMapping("/ctr")
    @Operation(summary = "CTR Score 계산", description = "FastAPI 모델 서버를 호출하여 상품 추천 결과를 반환합니다.")
    public ResponseEntity<?> getRecommendation(@RequestBody ProfileRequest profile) {

        // 1. FastAPI에 요청 보내기
        String fastApiUrl = "http://localhost:8001/recommend"; // 포트 번호 정확히 확인

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = """
        	    {
        	        "age": "20대",
        	        "style": ["에스닉", "내추럴", "페미닌"],
        	        "fit": ["루즈"],
        	        "color": ["블랙"],
        	        "candidate_items": [9614, 13463, 27871, 27151, 43223, 39571, 32435, 48171, 11240, 46269]
        	    }
        	    """;
        
        HttpEntity<ProfileRequest> request = new HttpEntity<>(profile, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(fastApiUrl, request, Map.class);

        System.out.println(response);
        
        // 2. 응답에서 prod ID 추출
        List<Integer> prodIds = (List<Integer>) response.getBody().get("recommended_items");

        // 4. DB에서 상품 정보 조회
        List<ProductInfo> products = productRepo.findByProdIdIn(prodIds);

        System.out.println(products);
        return ResponseEntity.ok(products);
    }
}
