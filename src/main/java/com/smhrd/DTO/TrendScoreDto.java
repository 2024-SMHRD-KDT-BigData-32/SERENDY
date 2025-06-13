package com.smhrd.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrendScoreDto {
	private Integer prodId;
    private Float trend_score;
}
