package com.example.cms.model.repository;

import com.example.cms.model.entity.Administrator;
import com.example.cms.model.entity.PlanDetail;
import com.example.cms.model.entity.PlanDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface PlanDetailRepository extends JpaRepository<PlanDetail, PlanDetailKey> {
    @Query(value = "select * from plans p " +
            "where p.workoutplanId =:searchTerm" , nativeQuery = true)
    List<PlanDetail> retrieveAllplans(@Param("searchTerm") Long searchTerm);

    @Modifying
    @Transactional
    @Query(value = "UPDATE plans p " +
            "SET p.calories = p.duration * " +
            "(SELECT e.calories " +
            "FROM exercises e " +
            "WHERE e.id = p.exerciseId) " , nativeQuery = true)
    void calcCalories();//calculate calories for a plan
}


