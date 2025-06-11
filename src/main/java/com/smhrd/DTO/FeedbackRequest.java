package com.smhrd.DTO;

import enums.FeedbackType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequest {
	
	private String id; // 사용자 id
	private Integer prodId; // 상품 id
	
	@Schema(description = "피드백 타입 (LIKE, DISLIKE)")
	private FeedbackType fbType; // LIKE 또는 DISLIKE
	

}
