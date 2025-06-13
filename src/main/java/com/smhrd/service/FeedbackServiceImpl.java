package com.smhrd.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smhrd.DTO.FeedbackRequest;
import com.smhrd.entity.FeedbackInfo;
import com.smhrd.entity.ProductInfo;
import com.smhrd.repository.FeedbackRepository;
import com.smhrd.repository.ProductInfoRepository;

import enums.FeedbackType;

@Service
public class FeedbackServiceImpl implements FeedbackService{

	private final FeedbackRepository feedbackRepository;
	private final ProductInfoRepository productInfoRepository;
	
	@Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, ProductInfoRepository productInfoRepository) {
        this.feedbackRepository = feedbackRepository;
        this.productInfoRepository = productInfoRepository;
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
	
	// 피드백 정보 저장
	@Override
	public void submitFeedback(FeedbackRequest request) {
	    // 기존 피드백 제거 (LIKE or DISLIKE가 DB에 있을 경우 삭제됨)
	    feedbackRepository.deleteByUserIdAndProdId(request.getId(), request.getProdId());

	    // 취소 상태면 종료 (DB에 아무 것도 저장 안함)
	    if (request.getFbType() == null || request.getFbType() == FeedbackType.NONE) {
	        return;
	    }

	    // 피드백 저장
	    FeedbackInfo feedback = new FeedbackInfo(
	        request.getId(),
	        request.getProdId(),
	        request.getFbType(),
	        LocalDateTime.now()
	    );
	    feedbackRepository.save(feedback);
	}
	
	// 좋아요 상품 리스트 조회
	@Override
	public List<ProductInfo> getLikedList(String id) {
		// 1. 해당 유저가 좋아요한 피드백 가져오기
		List<FeedbackInfo> likedFeedbacks = feedbackRepository.findByIdAndFbType(id, FeedbackType.LIKE);
		
		// 2. 피드백 리스트에서 prodId만 추출
	    List<Integer> prodIds = likedFeedbacks.stream()
	                                     .map(FeedbackInfo::getProdId)
	                                     .toList();
	    
	    // 3. 상품 ID 리스트로 상품 정보 조회
	    return productInfoRepository.findByProdIdIn(prodIds);
		
	}

	// 싫어요 상품 리스트 조회
	@Override
	public List<FeedbackInfo> getDislikedList(String id) {
		return feedbackRepository.findByIdAndFbType(id, FeedbackType.DISLIKE);
	}

}
