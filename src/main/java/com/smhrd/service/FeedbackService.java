package com.smhrd.service;

import java.util.List;

import com.smhrd.DTO.FeedbackRequest;
import com.smhrd.entity.FeedbackInfo;

import enums.FeedbackType;

public interface FeedbackService {

	FeedbackType getFeedbackStatus(String id, Integer prodId);

	void likeProduct(FeedbackRequest request);

	void dislikeProduct(FeedbackRequest request);
		
	void cancelFeedback(FeedbackRequest request);

	List<FeedbackInfo> getLikedList(String id);

	List<FeedbackInfo> getDislikedList(String id);

	

	

}
