package com.bca.byc.repository.old;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bca.byc.entity.old.Publisher;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
	
	public Optional<Publisher> findBySecureId(String secureId);
	
	public Page<Publisher> findByNameLikeIgnoreCase(String publisherName, Pageable  pageable);

}
