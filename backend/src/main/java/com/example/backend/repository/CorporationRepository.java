package com.example.backend.repository;

import java.time.Instant;
import java.util.List;

import com.example.backend.entity.Corporation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import org.springframework.data.repository.query.Param;

public interface CorporationRepository extends JpaRepository<Corporation, Long> {
    Optional<Corporation> findByEveCorporationId(Long eveCorporationId);
    
	@Query("SELECT c FROM Corporation c WHERE c.lastUpdated < :threshold")
	List<Corporation> findStaleCorporations(@Param("threshold") Instant threshold);
}