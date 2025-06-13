package com.smhrd.service;

import java.util.List;

import com.smhrd.DTO.FeedbackRequest;
import com.smhrd.entity.FeedbackInfo;
import com.smhrd.entity.ProductInfo;

import enums.FeedbackType;

public interface FeedbackService {

	FeedbackType getFeedbackStatus(String id, Integer prodId);

	List<ProductInfo> getLikedList(String id);

	List<FeedbackInfo> getDislikedList(String id);

	void submitFeedback(FeedbackRequest request);

}
