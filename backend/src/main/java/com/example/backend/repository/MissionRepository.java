package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.Mission;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    List<Mission> findByAgentIdAndStatus(Long agentId, String status);
    Optional<Mission> findByAgentIdAndStatusIn(Long agentId, List<String> statuses);
}