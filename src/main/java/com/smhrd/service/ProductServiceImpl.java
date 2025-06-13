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
	        Map<String, List<String>> keywordsMap = callNluService(keyword);
	        if (keywordsMap.isEmpty()) return List.of();

	        Map<String, Object> query = buildSearchQuery(keywordsMap);

	        List<Integer> prodIds = callElasticsearch(query);
	        if (prodIds.isEmpty()) return List.of();

	        return productRepository.findAllById(prodIds);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return List.of();
	    }
	}

	private Map<String, List<String>> callNluService(String keyword) {
	    try {
	        RestTemplate restTemplate = new RestTemplate();
	        String nluUrl = "http://localhost:8000/translate_keyword";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<Map<String, String>> request = new HttpEntity<>(Map.of("query", keyword), headers);

	        ResponseEntity<Map> response = restTemplate.postForEntity(nluUrl, request, Map.class);
	        Map<String, Object> body = response.getBody();

	        if (body == null || !body.containsKey("keywords")) return Map.of();

	        return (Map<String, List<String>>) body.get("keywords");
	    } catch (Exception e) {
	        System.err.println("NLU 호출 실패: " + e.getMessage());
	        return Map.of();
	    }
	}

	private List<Integer> callElasticsearch(Map<String, Object> query) {
	    try {
	        RestTemplate restTemplate = new RestTemplate();
	        String esUrl = "https://search-dev6-elasticsearch-qhs6iyg7t7ml24pcydcfdhebvq.ap-northeast-2.es.amazonaws.com/product-info/_search";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.setBasicAuth("admin", "Dev6250529!");

	        HttpEntity<Map<String, Object>> request = new HttpEntity<>(query, headers);
	        ResponseEntity<Map> response = restTemplate.postForEntity(esUrl, request, Map.class);
	        Map<String, Object> body = response.getBody();

	        if (body == null || !body.containsKey("hits")) return List.of();

	        Map<String, Object> hitsWrapper = (Map<String, Object>) body.get("hits");
	        List<Map<String, Object>> hits = (List<Map<String, Object>>) hitsWrapper.get("hits");

	        List<Integer> prodIds = new ArrayList<>();
	        for (Map<String, Object> hit : hits) {
	            Map<String, Object> source = (Map<String, Object>) hit.get("_source");
	            Object prodIdObj = source.get("prod_id");
	            if (prodIdObj != null) {
	                try {
	                    prodIds.add(Integer.parseInt(prodIdObj.toString()));
	                } catch (NumberFormatException e) {
	                    System.err.println("잘못된 prod_id: " + prodIdObj);
	                }
	            }
	        }

	        return prodIds;
	    } catch (Exception e) {
	        System.err.println("Elasticsearch 호출 실패: " + e.getMessage());
	        return List.of();
	    }
	}

	private Map<String, Object> buildSearchQuery(Map<String, List<String>> mappedTerms) {
	    List<Map<String, Object>> mustQueries = new ArrayList<>();

	    for (Map.Entry<String, List<String>> entry : mappedTerms.entrySet()) {
	        String field = entry.getKey();
	        List<String> values = entry.getValue();

	        List<Map<String, Object>> shouldQueries = new ArrayList<>();
	        for (String value : values) {
	            shouldQueries.add(Map.of("term", Map.of(field + ".keyword", value)));
	        }

	        mustQueries.add(Map.of("bool", Map.of("should", shouldQueries)));
	    }

	    return Map.of(
	        "size", 100,
	        "query", Map.of(
	            "bool", Map.of("must", mustQueries)
	        )
	    );
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
