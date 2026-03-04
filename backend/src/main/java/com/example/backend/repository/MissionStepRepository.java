package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.MissionStep;

public interface MissionStepRepository extends JpaRepository<MissionStep, Long> {
    List<MissionStep> findByMissionIdOrderByStepNumberAsc(Long missionId);
}