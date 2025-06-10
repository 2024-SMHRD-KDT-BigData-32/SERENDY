package com.smhrd.service;

import com.smhrd.entity.ProductInfo;
import com.smhrd.repository.ProductInfoRepository;
import com.smhrd.repository.ActionLogRepository;
import com.smhrd.repository.FeedbackInfoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private ActionLogRepository actionLogRepository;

    @Autowired
    private FeedbackInfoRepository feedbackInfoRepository;

    // ✅ [1] 하나의 점수 행렬 기반으로 여러 상품에 대한 유사도 집계 추천
    @Override
    public List<ProductInfo> getRecommendedProductsByList(List<Integer> targetProductIds, int topN) {
        Map<String, Map<Integer, Double>> matrix = buildUserItemMatrix();
        Map<Integer, Double> aggregatedSimilarity = new HashMap<>();

        for (Integer targetId : targetProductIds) {
            Map<Integer, Double> similarityMap = calculateItemSimilarity(matrix, targetId);

            for (Map.Entry<Integer, Double> entry : similarityMap.entrySet()) {
                aggregatedSimilarity.merge(entry.getKey(), entry.getValue(), Double::sum);
            }
        }

        // 기준 상품 제외
        targetProductIds.forEach(aggregatedSimilarity::remove);

        List<Integer> topProductIds = getTopNFromSimilarityMap(aggregatedSimilarity, topN);
        return productInfoRepository.findAllById(topProductIds);
    }

    // ✅ [2] 각 상품별로 개별 Top-N 유사상품 추천
    @Override
    public Map<Integer, List<ProductInfo>> getRecommendedProductsIndividually(List<Integer> targetProductIds, int topN) {
        Map<String, Map<Integer, Double>> matrix = buildUserItemMatrix();
        Map<Integer, List<ProductInfo>> resultMap = new HashMap<>();

        for (Integer targetId : targetProductIds) {
            Map<Integer, Double> similarityMap = calculateItemSimilarity(matrix, targetId);
            similarityMap.remove(targetId); // 자기 자신 제거

            List<Integer> topProductIds = getTopNFromSimilarityMap(similarityMap, topN);
            List<ProductInfo> products = productInfoRepository.findAllById(topProductIds);

            resultMap.put(targetId, products);
        }

        return resultMap;
    }
    
    @Override
    public List<ProductInfo> getTopNSimilarProductsFiltered(List<Integer> productIds, int topN, int finalTopCount) {
        Map<String, Map<Integer, Double>> userItemMatrix = buildUserItemMatrix();

        // 전체 후보 상품과 유사도 점수 저장 (중복 상품은 유사도 누적 or 최대값 등으로 처리 가능)
        Map<Integer, Double> aggregatedSimilarity = new HashMap<>();

        for (Integer targetId : productIds) {
            Map<Integer, Double> similarityMap = calculateItemSimilarity(userItemMatrix, targetId);

            // targetId 제외
            similarityMap.remove(targetId);

            // TopN 유사 상품 필터링
            similarityMap.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(topN)
                .forEach(entry -> 
                    aggregatedSimilarity.merge(entry.getKey(), entry.getValue(), Double::sum)
                );
        }

        // 전체 유사 상품 중에서 최종 Top finalTopCount개 뽑기
        List<Integer> topFilteredIds = aggregatedSimilarity.entrySet().stream()
            .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
            .limit(finalTopCount)
            .map(Map.Entry::getKey)
            .toList();

        return productInfoRepository.findAllById(topFilteredIds);
    }


    // ✅ 사용자-상품 점수 행렬 생성
    private Map<String, Map<Integer, Double>> buildUserItemMatrix() {
        Map<String, Map<Integer, Double>> matrix = new HashMap<>();

        // ▶ 행동 로그 점수 반영
        actionLogRepository.findAll().forEach(log -> {
            double score = 0.0;
            for (String type : log.getActionType().split(",")) {
                score += switch (type.trim()) {
                    case "RECOMMEND" -> 1.0;
                    case "CLICK" -> 2.0;
                    default -> 0.0;
                };
            }

            if (score > 0.0) {
                matrix
                    .computeIfAbsent(log.getUserId(), k -> new HashMap<>())
                    .merge(log.getProdId(), score, Double::sum);
            }
        });

        // ▶ 피드백 로그 점수 반영
        feedbackInfoRepository.findAll().forEach(fb -> {
            double score = switch (fb.getFbType()) {
                case "LIKE" -> 3.0;
                case "DISLIKE" -> -1.0;
                default -> 0.0;
            };

            if (score != 0.0) {
                matrix
                    .computeIfAbsent(fb.getUserId(), k -> new HashMap<>())
                    .merge(fb.getProdId(), score, Double::sum);
            }
        });

        return matrix;
    }

    // ✅ 특정 상품 기준으로 유사도 계산
    private Map<Integer, Double> calculateItemSimilarity(Map<String, Map<Integer, Double>> matrix, Integer targetProdId) {
        Map<Integer, Double> similarityMap = new HashMap<>();

        for (Integer otherProdId : getAllProductIds(matrix)) {
            if (!Objects.equals(otherProdId, targetProdId)) {
                double similarity = computeCosineSimilarity(matrix, targetProdId, otherProdId);
                if (similarity > 0) {
                    similarityMap.put(otherProdId, similarity);
                }
            }
        }

        return similarityMap;
    }

    // ✅ 코사인 유사도 계산
    private double computeCosineSimilarity(Map<String, Map<Integer, Double>> matrix, Integer prodA, Integer prodB) {
        double dot = 0.0, normA = 0.0, normB = 0.0;

        for (Map<Integer, Double> userRatings : matrix.values()) {
            double scoreA = userRatings.getOrDefault(prodA, 0.0);
            double scoreB = userRatings.getOrDefault(prodB, 0.0);

            dot += scoreA * scoreB;
            normA += scoreA * scoreA;
            normB += scoreB * scoreB;
        }

        return (normA != 0 && normB != 0) ? dot / (Math.sqrt(normA) * Math.sqrt(normB)) : 0.0;
    }

    // ✅ 유저 행렬에서 등장한 모든 상품 ID 추출
    private Set<Integer> getAllProductIds(Map<String, Map<Integer, Double>> matrix) {
        Set<Integer> allProductIds = new HashSet<>();
        for (Map<Integer, Double> userRatings : matrix.values()) {
            allProductIds.addAll(userRatings.keySet());
        }
        return allProductIds;
    }

    // ✅ 유사도 맵에서 상위 N개 상품 ID 추출
    private List<Integer> getTopNFromSimilarityMap(Map<Integer, Double> similarityMap, int topN) {
        return similarityMap.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .toList();
    }
}
