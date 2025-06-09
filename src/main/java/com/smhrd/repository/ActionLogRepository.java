package com.smhrd.repository;

import com.smhrd.entity.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
    List<ActionLog> findByUserId(String userId);
}