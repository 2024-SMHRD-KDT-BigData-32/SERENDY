package com.smhrd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.DTO.StylePrefRequest;
import com.smhrd.service.StylePrefService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "StylePref API", description = "선호 스타일 선택 API")
@CrossOrigin(origins = "*")
public class StylePrefController {

	private final StylePrefService stylePrefService;
	
	@Autowired
	public StylePrefController(StylePrefService stylePrefService) {
		this.stylePrefService = stylePrefService;
	}
	
	// 선호 스타일 정보 저장
	@Operation(summary = "선호 스타일 정보 저장", description = "Id에 해당하는 사용자의 선호 스타일 선택 정보를 저장합니다." )
	@PostMapping("/saveStyle")
	public ResponseEntity<?> saveStylePref(@RequestBody StylePrefRequest request) {
		System.out.println("DEBUG: id = " + request.getId());
		System.out.println("DEBUG: styleCodes = " + request.getStyleCodes());
		
		try {
			stylePrefService.saveStylePref(request.getId(), request.getStyleCodes());
			return ResponseEntity.ok(request.getStyleCodes());			
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("선택정보 저장 실패" + e.getMessage());
		}
	}
	
	// 선호 스타일 재선택
	@Operation(summary = "선호 스타일 재선택", description = "Id에 해당하는 사용자의 선호 스타일 재선택 정보를 저장합니다." )
	@PostMapping("/updateStyle")
	public ResponseEntity<?> updateStylePref(@RequestBody StylePrefRequest request) {
		System.out.println("DEBUG: id = " + request.getId());
		System.out.println("DEBUG: styleCodes = " + request.getStyleCodes());
		
		try {
			stylePrefService.updateStylePref(request.getId(), request.getStyleCodes());
			return ResponseEntity.ok(request.getStyleCodes());			
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("선택정보 저장 실패" + e.getMessage());
		}
	}
	
	@Operation(summary = "선호 스타일 정보 조회", description = "Id에 해당하는 사용자의 선호 스타일 선택 정보를 조회합니다.")
	@GetMapping("/getStylePref/{id}")
	public ResponseEntity<?> getStylePref(@PathVariable("id") String id) {
	    try {
	        List<String> styleCodes = stylePrefService.getStyleCodesByUserId(id);
	        return ResponseEntity.ok(styleCodes);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("선호 스타일 조회 실패: " + e.getMessage());
	    }
	}
	
}
