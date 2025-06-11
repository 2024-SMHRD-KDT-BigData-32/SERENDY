package com.smhrd.controller;

import com.smhrd.DTO.ProductBasicInfoDto;
import com.smhrd.entity.ProductInfo;
import com.smhrd.service.ProductService;
import com.smhrd.service.ActionLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "상품 관련 API")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;
    private final ActionLogService actionLogService;
    
    @Autowired
    public ProductController(ProductService productService, ActionLogService actionLogService) {
        this.productService = productService;
        this.actionLogService = actionLogService;
    }

    @Operation(summary = "상품 상세 조회", description = "상품 ID로 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable("id") Integer id,
            @RequestParam("userId") String userId) { // 프론트에서 userId 쿼리 파라미터로 넘김

        // CLICK 로그 저장
        actionLogService.saveClickLog(userId, id);

        // 상품 상세 조회
        Optional<ProductInfo> productOpt = productService.getProductById(id);
        return productOpt
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    @Operation(
    	    summary = "카테고리 및 세부 카테고리별 상품 조회",
    	    description = "영역 카테고리(상의, 하의, 아우터, 원피스)와 세부 카테고리(예: 니트웨어, 드레스)를 기준으로 상품을 조회합니다."
    	)
    	@GetMapping("/byCategory")
    	public ResponseEntity<List<ProductInfo>> getProductsByCategory(
    	        @Parameter(description = "영역 카테고리 (상의, 하의, 아우터, 원피스)")
    	        @RequestParam("area") String areaCategory,

    	        @Parameter(description = "세부 카테고리 (예: 니트웨어, 드레스 등)", required = false)
    	        @RequestParam(value = "sub", required = false) String subCategory) {

    	    List<ProductInfo> products = productService.getProductsByCategory(areaCategory, subCategory);
    	    return ResponseEntity.ok(products);
    	}
    
    @Operation(summary = "상품 검색", description = "키워드를 기반으로 상품을 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<List<ProductInfo>> searchProducts(
            @Parameter(description = "검색 키워드 (예: 파란 드레스)") 
            @RequestParam("keyword") String keyword) {
        
        List<ProductInfo> results = productService.searchProducts(keyword);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/products/basic-info")
    public ResponseEntity<List<ProductBasicInfoDto>> getBasicInfo(@RequestParam List<Integer> ids) {
        List<ProductInfo> products = productService.getProductsByIds(ids);
        List<ProductBasicInfoDto> dtos = products.stream()
            .map(p -> new ProductBasicInfoDto(p.getProdId()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @Operation(summary = "추천 상품 목록 조회", description = "사용자 ID 또는 상품 기반으로 추천 상품 목록을 조회합니다.")
    @GetMapping("/recommendations")
    public ResponseEntity<List<ProductInfo>> getRecommendedProducts(
            @Parameter(description = "사용자 ID 또는 기준 상품 ID") @RequestParam("targetId") Integer targetId,
            @Parameter(description = "추천 기준 (user 또는 item)") @RequestParam("type") String type) {

        List<ProductInfo> recommendations = productService.getRecommendedProducts(targetId, type);
        return ResponseEntity.ok(recommendations);
    }
    
    @Operation(summary = "전체 상품 목록 조회", description = "모든 상품 정보를 반환합니다.")
    @GetMapping("/all")
    public ResponseEntity<List<ProductInfo>> getAllProducts() {
        List<ProductInfo> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }




    
    
}
