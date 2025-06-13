package com.smhrd.service;

import com.smhrd.entity.ProductInfo;
import java.util.List;
import java.util.Map;

public interface IbcfService {

    // 기존 메서드
    List<ProductInfo> getRecommendedProductsByList(List<Integer> targetProductIds, int topN);

    Map<Integer, List<ProductInfo>> getRecommendedProductsIndividually(List<Integer> productIds, int topN);
    
    // 새로 추가할 메서드: 각 상품별 topN 유사상품을 뽑고, 최종 topM개를 필터링하여 반환
    List<ProductInfo> getTopNSimilarProductsFiltered(List<Integer> productIds, int topN, int finalTopCount);
}
