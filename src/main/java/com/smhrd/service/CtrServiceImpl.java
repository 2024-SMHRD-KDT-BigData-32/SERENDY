package com.smhrd.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smhrd.entity.ProductInfo;
import com.smhrd.repository.ActionLogRepository;
import com.smhrd.repository.ProductInfoRepository;
import com.smhrd.repository.StylePrefRepository;
import com.smhrd.repository.UserRepository;

@Service
public class CtrServiceImpl implements CtrService{

    private final ActionLogRepository actionLogRepository;
    private final ProductInfoRepository productInfoRepository;
    private final StylePrefRepository stylePrefRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public CtrServiceImpl(ActionLogRepository actionLogRepository, ProductInfoRepository productInfoRepository, StylePrefRepository stylePrefRepository, UserRepository userRepository) {
        this.actionLogRepository = actionLogRepository;
        this.productInfoRepository = productInfoRepository;
        this.stylePrefRepository = stylePrefRepository;
        this.userRepository = userRepository;
    }

    public Map<String, String> getMostFrequentFitAndColor(String userId) {
        List<Integer> clickedProdIds = actionLogRepository.findClickedProdIdsByUser(userId);
        if (clickedProdIds == null || clickedProdIds.isEmpty()) {
            return new HashMap<>(); // null 말고 빈 Map 리턴!
        }
        
        List<ProductInfo> clickedProducts = productInfoRepository.findByProdIdIn(clickedProdIds);

     // fit 최빈값 계산
        String mostCommonFit = clickedProducts.stream()
                .filter(p -> p.getFit() != null)
                .collect(Collectors.groupingBy(ProductInfo::getFit, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);
     // color 최빈값 계산
        String mostCommonColor = clickedProducts.stream()
                .filter(p -> p.getColor() != null)
                .collect(Collectors.groupingBy(ProductInfo::getColor, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);

        return Map.of("fit", mostCommonFit, "color", mostCommonColor);
    }

	@Override
	public List<String> getStylePref(String userId) {
		List<String> styleCodes = stylePrefRepository.findStyleCodesByUserId(userId);
		return styleCodes;
	}

	@Override
	public String getAge(String userId) {
		String age = userRepository.findAgeGroupById(userId);
		return age;
	}
}
