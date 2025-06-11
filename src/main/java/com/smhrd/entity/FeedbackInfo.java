package com.smhrd.entity;

import java.time.LocalDateTime;

import enums.FeedbackType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feedback_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fb_id")
	private Integer fbId;
	
	@Column(nullable = false, length = 50)
	private String id; // 유저 아이디
	
	@Column(name = "prod_id", nullable = false)
	private Integer prodId; // 상품 고유 번호
	
	@Enumerated(EnumType.STRING)
	@Column(name = "fb_type", nullable = false)
	private FeedbackType fbType; // LIKE 또는 DISLIKE. enum으로 관리
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	
	public FeedbackInfo(String id, Integer prodId, FeedbackType fbType, LocalDateTime createdAt) {
	    this.id = id;
	    this.prodId = prodId;
	    this.fbType = fbType;
	    this.createdAt = createdAt;
	}
	
}
