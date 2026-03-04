package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.AgentSkill;

public interface AgentSkillRepository extends JpaRepository<AgentSkill, Long> {
    List<AgentSkill> findByAgentId(Long agentId);
    Optional<AgentSkill> findByAgentIdAndSkillId(Long agentId, Integer skillId);
}