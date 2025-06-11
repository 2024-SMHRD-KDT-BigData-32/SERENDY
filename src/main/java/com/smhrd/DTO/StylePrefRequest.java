package com.smhrd.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StylePrefRequest {

	private String id;
	// 3개의 스타일 선택 정보 넘어올 거임
	private List<String> styleCodes;
	
}

