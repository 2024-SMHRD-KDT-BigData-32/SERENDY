package com.smhrd.service;

import java.util.List;

import com.smhrd.DTO.TrendScoreDto;
import com.smhrd.entity.ProductInfo;

public interface TrendScoreService {

	List<TrendScoreDto> getTopTrendScores(List<Integer> prodIds);

	List<TrendScoreDto> getTopTrendScoresAll(int size);
	
	List<ProductInfo> trendAllProd(List<TrendScoreDto> trendAll);

}
