package com.bca.byc.repository;

import com.bca.byc.entity.UserHasFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHasFeedbackRepository extends JpaRepository<UserHasFeedback, Long> {
}
