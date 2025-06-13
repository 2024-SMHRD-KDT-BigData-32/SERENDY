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

	@Query("SELECT s.styleCode FROM StylePref s WHERE s.id = :id")
	List<String> findStyleCodesByUserId(@Param("id") String id);

}
