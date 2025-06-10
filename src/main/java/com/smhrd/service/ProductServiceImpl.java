package com.smhrd.service;

import com.smhrd.entity.ProductInfo;
import com.smhrd.repository.ProductInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductInfoRepository productRepository;

	@Autowired
	public ProductServiceImpl(ProductInfoRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public Optional<ProductInfo> getProductById(Integer id) {
		return productRepository.findById(id);
	}

	@Override
	public List<ProductInfo> getProductsByCategory(String areaCategory, String subCategory) {
		if (subCategory != null && !subCategory.isEmpty()) {
			return productRepository.findByProdCate(subCategory);
		} else {
			List<String> categories = switch (areaCategory.toLowerCase()) {
			case "상의" -> List.of("탑", "블라우스", "티셔츠", "니트웨어", "셔츠", "브라탑", "후드티");
			case "하의" -> List.of("청바지", "팬츠", "스커트", "레깅스", "조거팬츠");
			case "아우터" -> List.of("코트", "재킷", "점퍼", "패딩", "베스트", "가디건", "짚업");
			case "원피스" -> List.of("드레스", "점프수트");
			default -> List.of();
			};
			return productRepository.findByProdCateIn(categories);
		}
	}

	@Override
	public List<ProductInfo> searchProducts(String keyword) {
	    try {
	        RestTemplate restTemplate = new RestTemplate();

	        // 1. NLU 서버 요청
	        String nluUrl = "http://localhost:8000/nlu";
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<Map<String, String>> request = new HttpEntity<>(Map.of("text", keyword), headers);
	        ResponseEntity<Map> nluResponse = restTemplate.postForEntity(nluUrl, request, Map.class);
	        Map<String, Object> responseBody = nluResponse.getBody();
	        if (responseBody == null || !responseBody.containsKey("fields")) {
	            return List.of();
	        }

	        Map<String, List<String>> fields = (Map<String, List<String>>) responseBody.get("fields");

	        // 2. Elasticsearch 쿼리 생성
	        Map<String, Object> query = buildSearchQuery(fields);

	        // 3. Elasticsearch 검색 요청
	        String esUrl = "https://search-dev6-elasticsearch-qhs6iyg7t7ml24pcydcfdhebvq.ap-northeast-2.es.amazonaws.com/product-info/_search";
	        HttpHeaders esHeaders = new HttpHeaders();
	        esHeaders.setContentType(MediaType.APPLICATION_JSON);
	        esHeaders.setBasicAuth("admin", "Dev6250529!");

	        HttpEntity<Map<String, Object>> esRequest = new HttpEntity<>(query, esHeaders);
	        ResponseEntity<Map> esResponse = restTemplate.postForEntity(esUrl, esRequest, Map.class);

	        Map<String, Object> esBody = esResponse.getBody();
	        if (esBody == null || !esBody.containsKey("hits")) {
	            return List.of();
	        }

	        Map<String, Object> hitsWrapper = (Map<String, Object>) esBody.get("hits");
	        List<Map<String, Object>> hits = (List<Map<String, Object>>) hitsWrapper.get("hits");

	        // 4. prodId 리스트 추출
	        List<Integer> prodIds = new ArrayList<>();
	        for (Map<String, Object> hit : hits) {
	            Map<String, Object> source = (Map<String, Object>) hit.get("_source");

	            Object prodIdObj = source.get("prod_id");
	            if (prodIdObj != null) {
	                try {
	                    prodIds.add(Integer.parseInt(prodIdObj.toString()));
	                } catch (NumberFormatException e) {
	                    System.err.println("Invalid prod_id: " + prodIdObj);
	                }
	            }
	        }

	        // 5. DB에서 전체 ProductInfo 조회
	        return productRepository.findAllById(prodIds);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return List.of();
	    }
	}


	private Map<String, Object> buildSearchQuery(Map<String, List<String>> mappedTerms) {
		List<Map<String, Object>> mustQueries = new ArrayList<>();

		for (Map.Entry<String, List<String>> entry : mappedTerms.entrySet()) {
			String field = entry.getKey();
			List<String> values = entry.getValue();

			for (String value : values) {
				mustQueries.add(Map.of("match", Map.of(field, value)));
			}
		}

		return Map.of("query", Map.of("bool", Map.of("must", mustQueries)));
	}
	
	@Override
	public List<ProductInfo> getProductsByIds(List<Integer> ids) {
	    return productRepository.findAllById(ids);
	}


	@Override
	public List<ProductInfo> getRecommendedProducts(Integer targetId, String type) {
		// TODO: 모델 결과 기반 추천 ID 리스트 받아오기
		List<Integer> recommendedIds = List.of(3, 5, 7, 9);
		return productRepository.findAllById(recommendedIds);
	}
	
	@Override
	public List<ProductInfo> getAllProducts() {
	    return productRepository.findAll();
	}
	
}
