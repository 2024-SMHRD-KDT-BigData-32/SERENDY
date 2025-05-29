package com.smhrd.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_info")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfo {

	@Id
	@Column(nullable = false, length = 50)
	private String id; // 유저 아이디 (PK)

	@Column(nullable = false, length = 255)
	private String pw;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, length = 1)
	private String gender;

	@Column(name = "age_group", nullable = false, length = 10)
	private String ageGroup;

	@Column(name = "joined_at", nullable = false)
	private LocalDateTime joinedAt;
	

}
