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
	@Operation(summary = "피드백 여부 조회", description = "사용자가 해당 상품에 남긴 피드백(LIKE/DISLIKE/없음)을 조회")
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
	
	// 좋아요
	@Operation(summary = "상품 좋아요", description = "사용자가 해당 상품에 좋아요 선택")
	@PostMapping("/like")
	public ResponseEntity<?> likeProduct(@RequestBody FeedbackRequest request){
		
		try {
	        feedbackService.likeProduct(request);
	        return ResponseEntity.ok("상품 좋아요 완료");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("좋아요 처리 중 오류 발생");
	    }
		
	}
	
	// 싫어요
	@PostMapping("/dislike")
	@Operation(summary = "상품 싫어요", description = "사용자가 해당 상품에 싫어요 선택")
	public ResponseEntity<?> dislikeProduct(@RequestBody FeedbackRequest request){

		try {
			feedbackService.dislikeProduct(request);
	        return ResponseEntity.ok("상품 싫어요 성공");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("싫어요 처리 중 오류 발생");
	    }
		
	}
	
	// 상품 좋아요 혹은 싫어요 취소
	@Operation(summary = "상품 좋아요/싫어요 취소", description = "사용자가 피드백한 해당 상품에 대해 좋아요/싫어요 취소 ")
	@PostMapping("/cancelFeedback")
	public ResponseEntity<?> cancelFeedback(@RequestBody FeedbackRequest request) {
		
		try {
	        feedbackService.cancelFeedback(request);
	        return ResponseEntity.ok("피드백 취소 완료");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("피드백 취소 중 오류가 발생했습니다.");
	    }  
	    
	}
	
	// 선호 상품 리스트 조회
	@Operation(summary = "선호 상품 리스트 조회", description = "사용자가 좋아요한 상품 목록 조회 ")
	@GetMapping("/likeList/{id}")
	public ResponseEntity<?> likeList(@PathVariable String id){
		try {
	        List<FeedbackInfo> likedList = feedbackService.getLikedList(id);
	        return ResponseEntity.ok(likedList); // 좋아요한 상품 리스트 조회
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("좋아요한 상품 목록 조회 중 오류가 발생했습니다.");
	    }
		
	}
	
}
