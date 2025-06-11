package com.smhrd.DTO;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IbcfInputRequest {
	private List<Integer> productIds;
	@Schema(description = "각 상품별 뽑을 유사상품 수", defaultValue = "2")
    private int topN = 2; // 각 상품별 뽑을 유사상품 수
	@Schema(description = "최종 결과에서 뽑을 개수", defaultValue = "500")
    private int finalTopCount = 500;
}
