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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.DTO.FeedbackRequest;
import com.smhrd.entity.FeedbackInfo;
import com.smhrd.entity.ProductInfo;
import com.smhrd.service.FeedbackService;

import enums.FeedbackType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Feedback API", description = "상품에 대한 행동(좋아요, 싫어요, 저장 등) API")
@CrossOrigin(origins = "*")
public class FeedbackController {

	private final FeedbackService feedbackService;

	@Autowired
	public FeedbackController(FeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}
	
	// 현재 피드백(좋아요/싫어요) 여부 조회 - 프론트에서 피드백 버튼 상태 점검용
	@Operation(summary = "피드백 여부 조회 - 프론트에서 피드백 버튼 상태 점검용", description = "사용자가 해당 상품에 남긴 피드백(LIKE/DISLIKE/없음)을 조회")
	@GetMapping("/feedbackStatus")
	public ResponseEntity<?> getFeedbackStatus(@RequestParam String id, @RequestParam Integer prodId) {
		
		try {
	        FeedbackType status = feedbackService.getFeedbackStatus(id, prodId);
	        return ResponseEntity.ok(status); // 프론트에서 피드백 버튼 상태 점검용
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("피드백 상태 조회 중 오류가 발생했습니다.");
	    }
		
	}
	
	// 피드백 정보 저장
	@Operation(summary = "최종 피드백 저장", description = "사용자가 페이지에서 나갈 때 최종적으로 선택한 피드백(LIKE, DISLIKE, NONE)을 저장")
	@PostMapping("/submitFeedback")
	public ResponseEntity<?> submitFeedback(@RequestBody FeedbackRequest request) {
	    try {
	        feedbackService.submitFeedback(request);
	        return ResponseEntity.ok("피드백 저장 완료");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("피드백 저장 중 오류 발생");
	    }
	}

	// 선호 상품 리스트 조회
	@Operation(summary = "선호 상품 리스트 조회", description = "사용자가 좋아요한 상품 목록 조회 ")
	@GetMapping("/likeList/{id}")
	public ResponseEntity<?> likeList(@PathVariable String id){
		try {
			List<ProductInfo> likedList = feedbackService.getLikedList(id);
	        return ResponseEntity.ok(likedList); // 좋아요한 상품 리스트 조회
	    } catch (Exception e) {
	    	e.printStackTrace(); // 콘솔에 로그 출력 (필수!)
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("좋아요한 상품 목록 조회 중 오류가 발생했습니다.");
	    }
		
	}
	
}
