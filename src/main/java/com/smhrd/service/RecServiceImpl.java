package com.smhrd.service;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smhrd.DTO.IbcfInputRequest;
import com.smhrd.entity.ActionLog;
import com.smhrd.entity.ProductInfo;
import com.smhrd.repository.ActionLogRepository;
import com.smhrd.repository.FeedbackRepository;
import com.smhrd.repository.ProductInfoRepository;

@Service
public class RecServiceImpl implements RecService{

	private final RestTemplate restTemplate = new RestTemplate();
	
	private final ProductInfoRepository productInfoRepository;
	private final ActionLogRepository actionLogRepository;
	
	@Autowired
    public RecServiceImpl(ProductInfoRepository productInfoRepository, ActionLogRepository actionLogRepository) {
        this.productInfoRepository = productInfoRepository;
        this.actionLogRepository = actionLogRepository;
    }
	
	@Override
	public List<ProductInfo> finalRecomd(String userId, List<String> styleCodes) {

		List<ProductInfo> result = null; // 최종 추천 상품 리스트
		
		// 사용자 유형 구분 (상품 클릭 10개(중복 클릭 제외) 이상부터 기존 사용자로 분류)
		long clickCount = actionLogRepository.countByUserIdAndActionType(userId, "click"); // 해당 사용자의 클릭수 가져오기
		boolean isExistingUser = clickCount >= 10;
		
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
		System.out.println("cbf 결과 : " + cbfCandidates.toString());

		// 피드백이 있는 상품만 필터링 - IBCF를 위해서
		List<Integer> cbfWithFeedback = actionLogRepository.findProductIdsWithFeedback(cbfCandidates);
		
		// 신규사용자면 CBF 후 바로 Trend_score 계산 후 50개 출력
	    if (!isExistingUser) { // 기존 사용자가 아니라면! == 신규사용자
    		// 피드백이 없다면 Trend만으로 추천 진행
    		List<Integer> trendScored = restTemplate.postForObject(
    			"http://localhost:8081/api/recommend/trend",
    			cbfCandidates,
    			List.class
    		);
    		System.out.println("신규 유저 trendScore 결과 : " + trendScored);
    		result = productInfoRepository.findByProdIdIn(trendScored);
	    } else {
	    
		    // 기존 사용자면
			if (!cbfWithFeedback.isEmpty()) { // cbf 필터링 결과의 상품들의 피드백이 있다면
		    	IbcfInputRequest ibcfRequestBody = new IbcfInputRequest(cbfWithFeedback);
		    	
		    	// 2. IBCF 후보군
		    	List<Integer> ibcfCandidates = restTemplate.postForObject(
		    			"http://localhost:8081/api/recommend/multi",
		    			ibcfRequestBody,
		    			List.class // 실제론 List<Integer>. 추후 TypeReference로 개선 가능
		    			);
		    	System.out.println("ibcf 결과 : " + ibcfCandidates);
		    	
		    	// B. 랭킹화
		    	Map<String, Object> requestBody = new HashMap<>();
		    	requestBody.put("userId", userId);
		    	requestBody.put("candidateItems", ibcfCandidates);
		    	
		    	// 4. CTR 점수 부여
		//        List<Integer> CtrScored = callCTRModel(merged, userId);
		    	List<Integer> ctrScored = restTemplate.postForObject(
		    			"http://localhost:8081/api/recommend/ctr",
		    			requestBody,
		    			List.class // 실제론 List<Integer>. 추후 TypeReference로 개선 가능
		    			);
		    	System.out.println("기존 유저 ctrScore 결과 : " + ctrScored);
		    	
		    	// 5. Trend 점수 부여
		    	List<Integer> trendScored = restTemplate.postForObject(
		    			"http://localhost:8081/api/recommend/trend",
		    			ibcfCandidates,
		    			List.class // 실제론 List<Integer>. 추후 TypeReference로 개선 가능
		    			);
		    	System.out.println("기존 유저 trendScore 결과 : " + trendScored);
	    	
		    	// List<ProductInfo> merged = withCtr + trendScored;
		    	// 6. 정렬
		    	Set<Integer> mergedProd = new HashSet<>();
		    	mergedProd.addAll(ctrScored);
		    	mergedProd.addAll(trendScored);
		    	
		    	List<Integer> noDuplicates = new ArrayList<>(mergedProd);
		    	System.out.println("최종 중복 제거 결과 : " + noDuplicates);
		    	
		    	
		    	// 모델 api 설계 전까진 임시로 trendScored된 상품 리스트 출력하는 걸로
	    		result = productInfoRepository.findByProdIdIn(noDuplicates);
			}
	    }
		
	    List<ActionLog> logs = result.stream()
	    .map(p -> ActionLog.builder()
                .userId(userId)
                .prodId(p.getProdId())
                .actionType("RECOMMEND")
                .createdAt(LocalDateTime.now())
                .build())
        .toList();
	    
	    actionLogRepository.saveAll(logs);
	    
		return result; 
		
	}
}
