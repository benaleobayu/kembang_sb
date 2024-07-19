package com.dev.byc.repository;

import com.dev.byc.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email);

    List<Admin> findByNameContainingIgnoreCase(String name);

    @Query("SELECT a FROM Admin a WHERE a.email = :email AND a.password = :password")
    Admin findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    // Add more custom queries as needed
}
