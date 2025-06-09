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

    @Override
    public List<ProductInfo> getRecommendedProducts(Integer targetProductId, int topN) {
        // 1. 사용자-상품 점수 행렬 생성
        Map<String, Map<Integer, Double>> userItemMatrix = buildUserItemMatrix();

        // 2. 코사인 유사도 기반 유사 상품 계산
        Map<Integer, Double> similarItems = calculateItemSimilarity(userItemMatrix, targetProductId);

        // 3. 유사도 기준 상위 topN 상품 ID 추출
        List<Integer> topProductIds = similarItems.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .toList();

        // 4. 실제 상품 정보 가져오기
        return productInfoRepository.findAllById(topProductIds);
    }

    // 사용자-상품 점수 행렬 만들기
    private Map<String, Map<Integer, Double>> buildUserItemMatrix() {
        Map<String, Map<Integer, Double>> matrix = new HashMap<>();

        // ✅ 사용자 행동 로그 기반 점수 계산
        actionLogRepository.findAll().forEach(log -> {
            double score = 0.0;

            // 여러 타입이 있을 수 있으므로 ,로 분리
            String[] types = log.getActionType().split(",");

            for (String type : types) {
                score += switch (type.trim()) {
                    case "RECOMMEND" -> 1.0;
                    case "CLICK" -> 2.0;
                    // VIEW는 무시
                    default -> 0.0;
                };
            }

            // 점수가 0보다 큰 경우만 반영
            if (score > 0.0) {
                matrix
                    .computeIfAbsent(log.getUserId(), k -> new HashMap<>())
                    .merge(log.getProdId(), score, Double::sum);
            }
        });

        // ✅ 피드백 로그 점수 계산
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


    // 코사인 유사도 계산
    private Map<Integer, Double> calculateItemSimilarity(Map<String, Map<Integer, Double>> matrix, Integer targetProdId) {
        Map<Integer, Double> similarityMap = new HashMap<>();

        for (Integer otherProdId : getAllProductIds(matrix)) {
            if (Objects.equals(otherProdId, targetProdId)) continue;

            double similarity = computeCosineSimilarity(matrix, targetProdId, otherProdId);
            if (similarity > 0) {
                similarityMap.put(otherProdId, similarity);
            }
        }

        return similarityMap;
    }

    private Set<Integer> getAllProductIds(Map<String, Map<Integer, Double>> matrix) {
        Set<Integer> allProductIds = new HashSet<>();
        for (Map<Integer, Double> itemScores : matrix.values()) {
            allProductIds.addAll(itemScores.keySet());
        }
        return allProductIds;
    }

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
}
