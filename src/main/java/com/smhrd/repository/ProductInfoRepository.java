package com.smhrd.repository;

import com.smhrd.entity.ProductInfo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInfoRepository extends JpaRepository<ProductInfo, Integer> {

	List<ProductInfo> findByProdCateIn(List<String> categories);
	List<ProductInfo> findByProdCateAndDetail(String prodCate, String detail);
	List<ProductInfo> findByProdCate(String prodCate);
	
	List<ProductInfo> findByProdIdIn(List<Integer> prodIds);

}
