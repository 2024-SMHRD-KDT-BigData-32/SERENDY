package com.smhrd.service;

import com.smhrd.entity.ProductInfo;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<ProductInfo> getProductById(Integer id);

    List<ProductInfo> getProductsByCategory(String areaCategory, String subCategory);

    List<ProductInfo> searchProducts(String keyword);
    
    List<ProductInfo> getRecommendedProducts(Integer targetId, String type);

    List<ProductInfo> getProductsByIds(List<Integer> ids);
    
    List<ProductInfo> getAllProducts();
}
