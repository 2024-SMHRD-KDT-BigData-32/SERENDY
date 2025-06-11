package com.smhrd.service;

import java.util.List;

import com.smhrd.DTO.CbfProdResponse;

public interface RecomdService {

	List<CbfProdResponse> recommendProductsByStyle(List<String> preferredStyles, List<Integer> dislikedProdIds);
	
}
