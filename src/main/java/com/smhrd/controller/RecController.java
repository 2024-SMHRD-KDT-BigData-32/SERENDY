package com.smhrd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.entity.ProductInfo;
import com.smhrd.service.CbfService;
import com.smhrd.service.IbcfService;
import com.smhrd.service.RecService;
import com.smhrd.service.StylePrefService;
import com.smhrd.service.TrendScoreService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/recommend")
@Tag(name = "최종 상품 추천 API", description = "최종 상품 추천 로직")
@CrossOrigin(origins = "*")
public class RecController {

	@Autowired
    private RecService recService;
	
	@Autowired
	private final StylePrefService stylePrefService;
	
	public RecController(StylePrefService stylePrefService) {
        this.stylePrefService = stylePrefService;
    }
	
    @GetMapping("/{id}")
    public List<Integer> getFinalRecomd(@PathVariable String id) {

    	List<String> styleCodes = stylePrefService.getStyleCodesByUserId(id);
    	System.out.println("들어온 id" + id);
    	System.out.println("id의 선호 스타일 리스트" + styleCodes.toString());
    	return recService.finalRecomd(id, styleCodes);
    }
	
}
