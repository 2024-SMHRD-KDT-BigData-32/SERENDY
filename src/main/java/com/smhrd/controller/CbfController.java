package com.smhrd.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.DTO.CbfProdResponse;
import com.smhrd.DTO.StylePrefRequest;
import com.smhrd.entity.FeedbackInfo;
import com.smhrd.service.FeedbackService;
import com.smhrd.service.CbfService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/recommend")
@Tag(name = "상품 추천-CBF API", description = "상품 추천 로직 - 1단계(후보군 필터링) : CBF")
@CrossOrigin(origins = "*")
public class CbfController {

	private final CbfService recomdService;
	private final FeedbackService feedbackService;

    public CbfController(CbfService recomdService, FeedbackService feedbackService) {
        this.recomdService = recomdService;
        this.feedbackService = feedbackService;
    }

    // 사용자 선호 스타일 3개 받고 추천 상품 리스트 리턴
    @Operation(
            summary = "CBF 상품 필터링(추천)",
            description = "사용자의 선호 스타일 리스트(3개)를 기반으로 콘텐츠 기반 필터링(CBF)을 활용해 유사한 상품 필터링"
        )
    @PostMapping("/cbf")
    public ResponseEntity<List<Integer>> getRecommendedProducts(@RequestBody StylePrefRequest userPreference) {
        List<String> preferredStyles = userPreference.getStyleCodes();
        String userId = userPreference.getId(); // 사용자 id 받아오기

        List<FeedbackInfo> dislikedFeedbacks = feedbackService.getDislikedList(userId);

		// prodId만 뽑아서 List<Integer>로 변환
		List<Integer> dislikedProdIds = dislikedFeedbacks.stream()
		    .map(FeedbackInfo::getProdId)
		    .collect(Collectors.toList());

     	// prodId, prod_img(현재 다 null로 es에 적재됨), score(유사도 점수)
        List<CbfProdResponse> recommended = recomdService.recommendProductsByStyle(preferredStyles, dislikedProdIds);

        // 이 중에서 prodId List만 주기
        List<Integer> prodIdList = recommended.stream()
        	    .map(CbfProdResponse::getProdId)
        	    .collect(Collectors.toList());
        
        return ResponseEntity.ok(prodIdList);
    }
	
}
