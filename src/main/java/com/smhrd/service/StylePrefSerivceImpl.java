package com.smhrd.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smhrd.entity.StylePref;
import com.smhrd.repository.StylePrefRepository;

import jakarta.transaction.Transactional;

@Service
public class StylePrefSerivceImpl implements StylePrefService{
	
	private final StylePrefRepository stylePrefRepository;

	@Autowired
    public StylePrefSerivceImpl(StylePrefRepository stylePrefRepository) {
        this.stylePrefRepository = stylePrefRepository;
    }

	// 선호 스타일 저장
	@Override
	public void saveStylePref(String id, List<String> styleCodes) {
		
		for (String styleCode : styleCodes) {
			// 동일한 스타일이 이미 저장되어 있는지 확인 (중복 방지)
            boolean exists = stylePrefRepository.existsByIdAndStyleCode(id, styleCode);
            
            if (!exists) {
                StylePref pref = new StylePref();
                pref.setId(id);
                pref.setStyleCode(styleCode);
                pref.setCreatedAt(LocalDateTime.now());

                stylePrefRepository.save(pref);
            }
			
		}
		
	}

	// 선호 스타일 재선택
	@Override
	@Transactional
	public void updateStylePref(String id, List<String> styleCodes) {

		// 기존에 선택된 스타일 모두 삭제
        stylePrefRepository.deleteAllByUserId(id);

        // 새롭게 선택한 스타일 저장
        for (String styleCode : styleCodes) {
            StylePref pref = new StylePref();
            pref.setId(id);
            pref.setStyleCode(styleCode);
            pref.setCreatedAt(LocalDateTime.now());

            stylePrefRepository.save(pref);
        }
	}

	// 선호 스타일 조회
	@Override
	public List<String> getStyleCodesByUserId(String id) {

		List<String> styleCodes = stylePrefRepository.findStyleCodesByUserId(id);
		return styleCodes;
	}
	

}
