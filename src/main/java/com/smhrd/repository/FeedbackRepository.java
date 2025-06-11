package com.smhrd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smhrd.entity.FeedbackInfo;

import enums.FeedbackType;
import jakarta.transaction.Transactional;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackInfo, Integer>{

	List<FeedbackInfo> findByIdAndFbType(String id, FeedbackType like);

	@Modifying
    @Transactional
	@Query("DELETE FROM FeedbackInfo f WHERE f.id = :id AND f.prodId = :prodId")
	void deleteByUserIdAndProdId(@Param("id") String id, Integer prodId);
	
	@Query("SELECT f FROM FeedbackInfo f WHERE f.id = :id AND f.prodId = :prodId")
	Optional<FeedbackInfo> findByUserIdAndProdId(@Param("id") String id, @Param("prodId") Integer prodId);
	
	@Query("SELECT f FROM FeedbackInfo f WHERE f.id = :id")
	List<FeedbackInfo> findByUserId(@Param("id") String id); 


}
