package com.example.cms.model.repository;


import com.example.cms.model.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    @Query(value = "select * from exercises e " +
            "where lower(e.name) like lower(concat('%', :searchTerm, '%')) " , nativeQuery = true)
    List<Exercise> search(@Param("searchTerm") String searchTerm);
}
