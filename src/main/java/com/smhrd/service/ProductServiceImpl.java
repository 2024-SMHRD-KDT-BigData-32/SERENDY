package com.smhrd.service;

import com.smhrd.entity.ProductInfo;
import com.smhrd.repository.ProductInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductInfoRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductInfoRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<ProductInfo> getProductById(Integer id) {
        return productRepository.findById(id);
    }
    
    @Override
    public List<ProductInfo> getProductsByAreaCategory(String areaCategory) {
        List<String> categories = switch (areaCategory.toLowerCase()) {
            case "상의" -> List.of("탑", "블라우스", "티셔츠", "니트웨어", "셔츠", "브라탑", "후드티");
            case "하의" -> List.of("청바지", "팬츠", "스커트", "레깅스", "조거팬츠");
            case "아우터" -> List.of("코트", "재킷", "점퍼", "패딩", "베스트", "가디건", "짚업");
            case "원피스" -> List.of("드레스", "점프수트");
            default -> List.of(); // 잘못된 경우 빈 목록 반환
        };
        return productRepository.findByProdCateIn(categories);
    }
}
