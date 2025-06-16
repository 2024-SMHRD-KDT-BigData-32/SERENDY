package com.smhrd.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class NluController {

	@PostMapping("/analyze")
	public ResponseEntity<?> analyze(@RequestBody Map<String, String> request) {
	    String userInput = request.get("text");

	    RestTemplate restTemplate = new RestTemplate();
	    String nluUrl = "http://localhost:8000/nlu"; // 검색 API (그대로 유지)

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    Map<String, String> jsonMap = new HashMap<>();
	    jsonMap.put("text", userInput);

	    HttpEntity<Map<String, String>> entity = new HttpEntity<>(jsonMap, headers);

	    ResponseEntity<String> response = restTemplate.postForEntity(nluUrl, entity, String.class);
	    return ResponseEntity.ok(response.getBody());
	}




}

