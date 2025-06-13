package com.smhrd.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smhrd.DTO.IbcfInputRequest;
import com.smhrd.entity.ProductInfo;

@Service
public class RecServiceImpl implements RecService{

	private final RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public List<Integer> finalRecomd(String userId, List<String> styleCodes) {

		// A. 후보군 필터링
		
		// 1. CBF 후보군
		Map<String, Object> cbfRequest = new HashMap<>();
		cbfRequest.put("userId", userId);
		cbfRequest.put("styleCodes", styleCodes); // List<String> 타입

		List<Integer> cbfCandidates = restTemplate.postForObject(
		    "http://localhost:8081/api/recommend/cbf",
		    cbfRequest,
		    List.class // 실제론 List<Integer>. 추후 TypeReference로 개선 가능
		);

		IbcfInputRequest ibcfRequestBody = new IbcfInputRequest(cbfCandidates);
		
        // 2. IBCF 후보군
		List<ProductInfo> ibcfCandidates = restTemplate.postForObject(
			    "http://localhost:8081/api/recommend/multi",
			    ibcfRequestBody,
			    List.class // 실제론 List<ProductInfo>. 추후 TypeReference로 개선 가능
			);
		

		// B. 랭킹화
		
        // 4. CTR 점수 부여
//        List<ProductInfo> withCtr = callCTRModel(merged, userId);

        // 5. Trend 점수 부여
		List<Integer> trendScored = restTemplate.postForObject(
			    "http://localhost:8081/api/recommend/trend",
			    ibcfCandidates,
			    List.class // 실제론 List<Integer>. 추후 TypeReference로 개선 가능
			);

        // List<ProductInfo> merged = withCtr + trendScored;
        // 6. 정렬
        return trendScored; // 모델 api 설계 전까진 임시로 trendScored된 상품 리스트 출력하는 걸로
	}
	

}
