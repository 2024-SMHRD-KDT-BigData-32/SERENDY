package com.smhrd.service;

import java.util.List;

import com.smhrd.entity.ProductInfo;

public interface RecService {

	List<ProductInfo> finalRecomd(String userId, List<String> styleCodes);

}
