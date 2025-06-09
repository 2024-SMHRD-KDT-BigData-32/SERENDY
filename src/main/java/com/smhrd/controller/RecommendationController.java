package com.smhrd.controller;

import com.smhrd.entity.ProductInfo;
import com.smhrd.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "상품 기반 유사 상품 추천", description = "특정 상품 ID를 기준으로 유사한 상품을 추천합니다.")
    @GetMapping("/{productId}")
    public ResponseEntity<List<ProductInfo>> recommendProducts(
            @PathVariable("productId") Integer productId,
            @RequestParam(defaultValue = "10") int topN) {

        List<ProductInfo> recommended = recommendationService.getRecommendedProducts(productId, topN);
        return ResponseEntity.ok(recommended);
    }
}
