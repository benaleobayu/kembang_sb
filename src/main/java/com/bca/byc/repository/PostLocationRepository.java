package com.bca.byc.repository;

import com.bca.byc.entity.PostLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLocationRepository extends JpaRepository<PostLocation, Long> {


    PostLocation findByName(String postLocationName);
}
