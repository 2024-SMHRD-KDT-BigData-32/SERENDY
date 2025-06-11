package com.smhrd.controller;

import com.smhrd.DTO.IbcfInputRequest;
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

    @Operation(summary = "IBCF - 여러 상품 기반 유사 상품 추천 (각 상품마다 topN개 뽑고, 최종 topM개 반환)")
    @PostMapping("/multi")
    public ResponseEntity<List<ProductInfo>> recommendProductsByMultipleIds(
            @RequestBody IbcfInputRequest request) { 
        int topN = request.getTopN();
        int finalTopCount = request.getFinalTopCount();
    	
        List<Integer> productIds = request.getProductIds();

        List<ProductInfo> recommended = recommendationService.getTopNSimilarProductsFiltered(productIds, topN, finalTopCount);
        return ResponseEntity.ok(recommended);
    }




}
