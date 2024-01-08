package com.example.cms.model.repository;
import com.example.cms.model.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AdministratorRepository extends JpaRepository<Administrator, Long>{
    @Query(value = "select * from administrators a " +
            "where lower(a.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(a.lastName) like lower(concat('%', :searchTerm, '%'))", nativeQuery = true)
    List<Administrator> search(@Param("searchTerm") String searchTerm);
}
