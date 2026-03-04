package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.MissionTemplate;

public interface MissionTemplateRepository extends JpaRepository<MissionTemplate, Long> {
    List<MissionTemplate> findByActiveTrue();
}