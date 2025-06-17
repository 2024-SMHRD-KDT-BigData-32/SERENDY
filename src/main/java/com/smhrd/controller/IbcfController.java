package com.smhrd.controller;

import com.smhrd.DTO.IbcfInputRequest;
import com.smhrd.entity.ProductInfo;
import com.smhrd.service.IbcfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommend")
@Tag(name = "Recommend API", description = "상품 추천 API")
//@Tag(name = "상품 추천-IBCF API", description = "상품 추천 로직 - 2단계(후보군 필터링) : IBCF")
@CrossOrigin(origins = "*")
public class IbcfController {

    private final IbcfService recommendationService;

    @Autowired
    public IbcfController(IbcfService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Operation(summary = "IBCF - 여러 상품 기반 유사 상품 추천 (각 상품마다 topN개 뽑고, 최종 topM개 반환)")
    @PostMapping("/multi")
    public ResponseEntity<List<Integer>> recommendProductsByMultipleIds(
            @RequestBody IbcfInputRequest request) { 
        int topN = request.getTopN();
        int finalTopCount = request.getFinalTopCount();
    	
        List<Integer> productIds = request.getProductIds();

        List<ProductInfo> recommended = recommendationService.getTopNSimilarProductsFiltered(productIds, topN, finalTopCount);
        
        List<Integer> ibcfCandidates = recommended.stream()
			    .map(ProductInfo::getProdId)
			    .collect(Collectors.toList());
        
        return ResponseEntity.ok(ibcfCandidates);
    }




}
