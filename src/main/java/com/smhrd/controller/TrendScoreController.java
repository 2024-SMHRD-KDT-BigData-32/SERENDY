package com.smhrd.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.DTO.TrendScoreDto;
import com.smhrd.service.TrendScoreService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/recommend")
@Tag(name = "상품 추천 - Trend Score API", description = "상품 추천 로직 - 3단계(랭킹화) : Trend에 부합하는 상품 상위 50개")
@CrossOrigin(origins = "*")
public class TrendScoreController {
	
	 private final TrendScoreService trendScoreService;

	 public TrendScoreController(TrendScoreService trendScoreService) {
	    this.trendScoreService = trendScoreService;
	 }
	
	 @PostMapping("/trend")
	 public List<Integer> getTopTrendScores(@RequestBody List<Integer> prodIds) {
		 		 
		 List<Integer> trendProds = trendScoreService.getTopTrendScores(prodIds).stream()
				    .map(TrendScoreDto::getProdId)  // prod_id 값을 추출
				    .collect(Collectors.toList());  // 리스트로 수집
		 return trendProds;
	 }

}
