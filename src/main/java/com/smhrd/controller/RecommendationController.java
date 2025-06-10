package com.smhrd.controller;

import com.smhrd.entity.ProductInfo;
import com.smhrd.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommend")
@Tag(name = "Recommendation API", description = "상품 추천 관련 API")
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Operation(summary = "여러 상품 기반 유사 상품 추천 (각 상품마다 topN개 뽑고, 최종 topM개 반환)")
    @GetMapping("/multi")
    public ResponseEntity<List<ProductInfo>> recommendProductsByMultipleIds(
            @RequestParam("productIds") String productIdsStr,
            @RequestParam(defaultValue = "2") int topN,                // 각 상품별 뽑을 유사상품 수
            @RequestParam(defaultValue = "500") int finalTopCount) {   // 최종 결과에서 뽑을 개수

        List<Integer> productIds = Arrays.stream(productIdsStr.split(","))
                                         .map(String::trim)
                                         .map(Integer::parseInt)
                                         .toList();

        List<ProductInfo> recommended = recommendationService.getTopNSimilarProductsFiltered(productIds, topN, finalTopCount);
        return ResponseEntity.ok(recommended);
    }




}
