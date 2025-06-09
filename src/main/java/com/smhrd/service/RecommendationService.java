package com.smhrd.service;

import com.smhrd.entity.ProductInfo;
import java.util.List;

public interface RecommendationService {

    // 특정 상품 ID와 유사한 상품을 추천
    List<ProductInfo> getRecommendedProducts(Integer targetProductId, int topN);
}
