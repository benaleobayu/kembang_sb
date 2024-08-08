package com.bca.byc.repository;

import com.bca.byc.entity.Business;
import com.bca.byc.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

}
