package com.bca.byc.repository;

import com.bca.byc.entity.StatusType;
import com.bca.byc.entity.User;
import com.bca.byc.model.SimpleUserDetailResponse;
import com.bca.byc.model.UserDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<SimpleUserDetailResponse> findInfoByEmail(@Param("email") String email);

    // Custom query to find all users where isSuspended is false and isDeleted is false
    @Query("SELECT u FROM User u WHERE u.isSuspended = false AND u.isDeleted = false AND u.status not in (:status1, :status2)")
    List<User> findUserPendingAndActive(@Param("status1") StatusType status1, @Param("status2") StatusType status2);

    // filter list by status
    List<User> findByStatus(StatusType status);

    // filter list by isSuspend
    List<User> findByIsSuspended(boolean isSuspended);

    // filter list by isDeleted
    List<User> findByIsDeleted(boolean isDeleted);

    boolean existsByEmail(String email);

    // list pagination
    Page<User> findByNameLikeIgnoreCase(String name, Pageable pageable);
}
