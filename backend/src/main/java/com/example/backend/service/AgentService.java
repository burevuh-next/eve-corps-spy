package com.example.backend.service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.AgentInfoResponse;
import com.example.backend.dto.AgentResponse;
import com.example.backend.dto.AgentSkillDto;
import com.example.backend.dto.CreateAgentRequest;
import com.example.backend.entity.Agent;
import com.example.backend.entity.AgentSkill;
import com.example.backend.entity.Skill;
import com.example.backend.entity.User;
import com.example.backend.repository.AgentRepository;
import com.example.backend.repository.AgentSkillRepository;
import com.example.backend.repository.SkillRepository;
import com.example.backend.repository.UserRepository;

@Service
public class AgentService {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private AgentSkillRepository agentSkillRepository;

    @Transactional
    public AgentResponse createAgent(Long userId, CreateAgentRequest request) {
        // Проверяем, есть ли уже агент у пользователя
        if (agentRepository.existsByUserId(userId)) {
            throw new RuntimeException("User already has an agent");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Agent agent = new Agent(
                user,
                request.getName(),
                request.getCorporation(),
                request.getSpecialization(),
                request.getBio()
        );

        // Сначала сохраняем агента, чтобы получить его ID
        Agent saved = agentRepository.save(agent);

        // Получаем все навыки
        List<Skill> allSkills = skillRepository.findAll();

        // Определяем базовые уровни в зависимости от специализации
        Map<String, Integer> baseLevels = getBaseLevels(request.getSpecialization());

        for (Skill skill : allSkills) {
            int level = baseLevels.getOrDefault(skill.getName(), 1);
            AgentSkill agentSkill = new AgentSkill(saved, skill, level, 0);
            agentSkillRepository.save(agentSkill);
        }

        String formattedDate = DateTimeFormatter.ISO_INSTANT.format(saved.getCreatedAt());
        return new AgentResponse(
                saved.getId(),
                saved.getName(),
                saved.getCorporation(),
                saved.getSpecialization(),
                saved.getBio(),
                saved.getSuspicionLevel(),
                saved.getCredits(),
                formattedDate,
                saved.getReputation()
        );
    }

    public AgentResponse getAgentByUserId(Long userId) {
        Agent agent = agentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        String formattedDate = DateTimeFormatter.ISO_INSTANT.format(agent.getCreatedAt());
        return new AgentResponse(
                agent.getId(),
                agent.getName(),
                agent.getCorporation(),
                agent.getSpecialization(),
                agent.getBio(),
                agent.getSuspicionLevel(),
                agent.getCredits(),
                formattedDate,
                agent.getReputation()
        );
    }
    
    public AgentInfoResponse getAgentInfoByUserId(Long userId) {
        Agent agent = agentRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        // Получаем название корпорации (из entity Corporation, если она есть)
        String corporationName = agent.getCorporation() != null ? agent.getCorporation() : "Unknown";

        return new AgentInfoResponse(
                agent.getId(),
                agent.getName(),
                corporationName,
                agent.getSpecialization(),
                agent.getSuspicionLevel(),
                agent.getCredits(),
                agent.getBio(),
                agent.getReputation()
        );
    }

    public Agent getActiveAgentByUserId(Long userId) {
        return agentRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));
    }

    private Map<String, Integer> getBaseLevels(String specialization) {
        Map<String, Integer> map = new HashMap<>();
        map.put("hacking", 1);
        map.put("social", 1);
        map.put("stealth", 1);
        map.put("analysis", 1);

        switch (specialization) {
            case "hacker":
                map.put("hacking", 3);
                break;
            case "social":
                map.put("social", 3);
                break;
            case "saboteur":
                map.put("stealth", 3);
                break;
            // можно добавить другие
        }
        return map;
    }

    public List<AgentSkillDto> getAgentSkills(Long agentId) {
        List<AgentSkill> skills = agentSkillRepository.findByAgentId(agentId);
        return skills.stream()
                .map(as -> new AgentSkillDto(
                        as.getSkill().getId(),
                        as.getSkill().getName(),
                        as.getLevel(),
                        as.getExperience(),
                        as.getSkill().getMaxLevel()))
                .collect(Collectors.toList());
    }

}