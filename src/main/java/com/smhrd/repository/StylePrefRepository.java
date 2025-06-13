package com.smhrd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smhrd.entity.StylePref;

public interface StylePrefRepository extends JpaRepository<StylePref, Integer>{

	// 유저 ID 기준으로 전체 스타일 선호 정보 삭제
	@Modifying
	@Query("DELETE FROM StylePref s WHERE s.id = :id")
	void deleteAllByUserId(@Param("id") String id);
	
	boolean existsByIdAndStyleCode(@Param("id") String id, String styleCode);

	// userId인 사람의 상품 클릭수 가져오기(동일 상품 중복 클릭 제외)
	@Query("SELECT s.styleCode FROM StylePref s WHERE s.id = :id")
	List<String> findStyleCodesByUserId(@Param("id") String id);

}
