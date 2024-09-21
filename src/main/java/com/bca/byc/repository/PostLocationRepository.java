package com.bca.byc.repository;

import com.bca.byc.entity.PostLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLocationRepository extends JpaRepository<PostLocation, Long> {


    PostLocation findByPlaceName(String postLocationName);
}
