package com.smhrd.repository;

import com.smhrd.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserInfo, String> {

	@Query("SELECT u.ageGroup FROM UserInfo u WHERE u.id = :id")
    String findAgeGroupById(@Param("id") String id);
	
    // 추가 메서드 필요 없음! JpaRepository가 기본으로 제공함
}
