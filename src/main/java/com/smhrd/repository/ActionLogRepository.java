package com.smhrd.repository;

import com.smhrd.entity.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
    List<ActionLog> findByUserId(String userId);
    
    @Query("SELECT COUNT(DISTINCT a.prodId) FROM ActionLog a WHERE a.userId = :userId AND a.actionType = :actionType")
    long countByUserIdAndActionType(String userId, String actionType);

    @Query("SELECT DISTINCT a.prodId FROM ActionLog a WHERE a.prodId IN :prodIds AND a.actionType IN ('click', 'recommend')")
	List<Integer> findProductIdsWithFeedback(@Param("prodIds") List<Integer> cbfCandidates);

    @Query("SELECT al.prodId FROM ActionLog al WHERE al.userId = :userId AND al.actionType = 'click'")
    List<Integer> findClickedProdIdsByUser(@Param("userId") String userId);

}