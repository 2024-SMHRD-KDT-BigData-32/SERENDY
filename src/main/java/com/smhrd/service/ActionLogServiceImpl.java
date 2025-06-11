package com.smhrd.service;

import com.smhrd.entity.ActionLog;
import com.smhrd.repository.ActionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ActionLogServiceImpl implements ActionLogService {

    private final ActionLogRepository actionLogRepository;

    @Autowired
    public ActionLogServiceImpl(ActionLogRepository actionLogRepository) {
        this.actionLogRepository = actionLogRepository;
    }

    @Override
    public void saveClickLog(String userId, Integer prodId) {
        ActionLog log = ActionLog.builder()
                .userId(userId)
                .prodId(prodId)
                .actionType("CLICK")
                .createdAt(LocalDateTime.now())
                .build();

        actionLogRepository.save(log);
    }
}
