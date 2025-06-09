package com.smhrd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedback_info") // 실제 테이블명 지정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fb_id") // 실제 컬럼명 지정
    private Long fbId;

    @Column(name = "id")
    private String userId;  // Integer → String 으로 변경

    @Column(name = "prod_id")
    private Integer prodId;

    @Column(name = "fb_type")
    private String fbType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

