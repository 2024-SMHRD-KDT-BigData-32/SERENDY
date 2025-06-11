package com.smhrd.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "style_pref")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StylePref {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pref_id")
	private Integer perfId;
	
	@Column(nullable = false, length = 50)
	private String id; // 유저 아이디

	@Column(name = "style_code",nullable = false, length = 30)
	private String styleCode;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
}
