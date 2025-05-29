package com.smhrd.service;

import com.smhrd.entity.ProductInfo;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<ProductInfo> getProductById(Integer id);

    List<ProductInfo> getProductsByAreaCategory(String areaCategory);
}
