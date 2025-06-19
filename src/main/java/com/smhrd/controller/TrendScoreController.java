package com.smhrd.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.DTO.TrendScoreDto;
import com.smhrd.service.TrendScoreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/recommend")
@Tag(name = "Recommend API", description = "상품 추천 API")
//@Tag(name = "상품 추천 - Trend Score API", description = "상품 추천 로직 - 3단계(랭킹화) : Trend에 부합하는 상품 상위 50개")
@CrossOrigin(origins = "*")
public class TrendScoreController {
	
	 private final TrendScoreService trendScoreService;

	 public TrendScoreController(TrendScoreService trendScoreService) {
	    this.trendScoreService = trendScoreService;
	 }
	
	 // 상품 후보군 기준으로 trend_score 순으로 정렬(최대 상위 50개)
	 @Operation(
	            summary = "Trend Score 계산",
	            description = "상품 추천 로직 - 3단계(랭킹화) : Trend에 부합하는 상품 상위 50개"
	        )
	 @PostMapping("/trend")
	 public List<Integer> getTopTrendScores(@RequestBody List<Integer> prodIds) {
		 		 
		 List<Integer> trendProds = trendScoreService.getTopTrendScores(prodIds).stream()
				    .map(TrendScoreDto::getProdId)  // prod_id 값을 추출
				    .collect(Collectors.toList());  // 리스트로 수집
		 return trendProds;
	 }
	 
	 // 전체 상품 기준으로 trend_score 순으로 정렬
	 @Operation(summary = "전체 상품 중 Trend Score Top-N", description = "이번주 트렌드 상품들")
	 @GetMapping("/trend/all")
	 public List<Integer> getTopTrendScoresAll(@RequestParam(defaultValue = "50") int size) {
	     return trendScoreService.getTopTrendScoresAll(size).stream()
	             .map(TrendScoreDto::getProdId)
	             .collect(Collectors.toList());
	 }


}
