package com.smhrd.service;

import java.util.List;

import com.smhrd.DTO.TrendScoreDto;

public interface TrendScoreService {

	List<TrendScoreDto> getTopTrendScores(List<Integer> prodIds);

}
