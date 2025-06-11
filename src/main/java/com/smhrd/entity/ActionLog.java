package com.smhrd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "action_log") // 실제 테이블명 지정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id") // 실제 컬럼명 지정
    private Long logId;

    @Column(name = "id")
    private String userId;  // Integer → String 으로 변경

    @Column(name = "prod_id")
    private Integer prodId;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

