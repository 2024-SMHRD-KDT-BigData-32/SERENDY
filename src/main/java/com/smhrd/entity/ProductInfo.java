package com.smhrd.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ← 자동 증가 설정
    @Column(name = "prod_id", nullable = false)
    private Integer prodId;

    @Column(name = "prod_img", nullable = false)
    private String prodImg;

    @Column(name = "prod_cate", nullable = false)
    private String prodCate;

    @Column(name = "style_code", nullable = false)
    private String styleCode;

    @Column(name = "fit", nullable = false)
    private String fit;

    @Column(name = "length", nullable = false)
    private String length;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "material", nullable = false)
    private String material;

    @Column(name = "print", nullable = false)
    private String print;

    @Column(name = "detail", nullable = false)
    private String detail;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
