package com.smhrd.controller;

import com.smhrd.entity.ProductInfo;
import com.smhrd.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "상품 관련 API")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품 상세 조회", description = "상품 ID로 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Integer id) {
        Optional<ProductInfo> productOpt = productService.getProductById(id);
        return productOpt
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "카테고리 영역별 상품 조회", description = "상의, 하의, 아우터, 원피스 중 하나를 입력하세요.")
    @GetMapping("/byCategory")
    public ResponseEntity<List<ProductInfo>> getProductsByAreaCategory(
            @Parameter(description = "영역 카테고리 (상의, 하의, 아우터, 원피스)") 
            @RequestParam("area") String areaCategory) {

        List<ProductInfo> products = productService.getProductsByAreaCategory(areaCategory);
        return ResponseEntity.ok(products);
    }

    
    
}
