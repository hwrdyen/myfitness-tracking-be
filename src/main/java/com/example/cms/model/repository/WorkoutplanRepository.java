package com.example.cms.model.repository;

import com.example.cms.model.entity.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface WorkoutplanRepository extends JpaRepository <WorkoutPlan, Long>{
    @Query(value = "select * from WORKOUTPLAN w " +
            "where w.clientId=:clientId" , nativeQuery = true)
    List<WorkoutPlan> findMyPlans(@Param("clientId") Long clientId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE workoutplan w " +
            "SET w.caloriesburned = " +
            "( SELECT SUM(p.calories) " +
            "FROM plans p " +
            "WHERE p.workoutplanId = w.id)" +
            "WHERE w.id =:id" , nativeQuery = true)
    void calcCalories(@Param("id") Long id);//calculate workoutplan result
}
