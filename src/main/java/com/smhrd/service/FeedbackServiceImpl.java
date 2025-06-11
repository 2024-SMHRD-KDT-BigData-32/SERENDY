package com.smhrd.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smhrd.DTO.FeedbackRequest;
import com.smhrd.entity.FeedbackInfo;
import com.smhrd.repository.FeedbackRepository;

import enums.FeedbackType;

@Service
public class FeedbackServiceImpl implements FeedbackService{

	private final FeedbackRepository feedbackRepository;
	
	@Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }
	
	// 피드백 여부 조회
	@Override
	public FeedbackType getFeedbackStatus(String id, Integer prodId) {
		Optional<FeedbackInfo> feedback = feedbackRepository.findByUserIdAndProdId(id, prodId);

		if (feedback.isPresent()) {
		    return feedback.get().getFbType();
		} else {
		    return null;
		}
	}
	
	// 상품 좋아요
	@Override
	public void likeProduct(FeedbackRequest request) {
		
		// 기존 LIKE 또는 DISLIKE 모두 제거 (있다면)
	    feedbackRepository.deleteByUserIdAndProdId(request.getId(), request.getProdId());
		
		FeedbackInfo feedback = new FeedbackInfo(
	            request.getId(),
	            request.getProdId(),
	            FeedbackType.LIKE,
	            LocalDateTime.now()
	        );
		
		feedbackRepository.save(feedback);
		
	}
	
	// 상품 싫어요
	@Override
	public void dislikeProduct(FeedbackRequest request) {

		// 기존 LIKE 또는 DISLIKE 모두 제거 (있다면)
	    feedbackRepository.deleteByUserIdAndProdId(request.getId(), request.getProdId());
		
		FeedbackInfo feedback = new FeedbackInfo(
	            request.getId(),
	            request.getProdId(),
	            FeedbackType.DISLIKE,
	            LocalDateTime.now()
	        );
		
        feedbackRepository.save(feedback);
	}

	// 좋아요나 싫어요 취소
	public void cancelFeedback(FeedbackRequest request) {
		// 삭제할 대상 없으면 그냥 넘어감. 있으면 삭제
	    feedbackRepository.deleteByUserIdAndProdId(request.getId(), request.getProdId());
	}
	
	// 좋아요 상품 리스트 조회
	@Override
	public List<FeedbackInfo> getLikedList(String id) {
		return feedbackRepository.findByIdAndFbType(id, FeedbackType.LIKE);
	}

	// 싫어요 상품 리스트 조회
	@Override
	public List<FeedbackInfo> getDislikedList(String id) {
		return feedbackRepository.findByIdAndFbType(id, FeedbackType.DISLIKE);
	}
}
