package com.smhrd.repository;

import com.smhrd.entity.FeedbackInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackInfoRepository extends JpaRepository<FeedbackInfo, Long> {
    List<FeedbackInfo> findByUserId(String userId); // 사용자별 피드백 가져오기
}
