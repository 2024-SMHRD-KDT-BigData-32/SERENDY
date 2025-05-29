package com.smhrd.repository;

import com.smhrd.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserInfo, String> {
    // 추가 메서드 필요 없음! JpaRepository가 기본으로 제공함
}
