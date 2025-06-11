package com.smhrd.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CbfProdResponse {

	@JsonProperty("prod_id")
	private Integer prodId;
    private String prodImg; // 혹은 imageUrl 등 필요한 최소 정보
    private double score; // 유사도 점수 (옵션)
}
