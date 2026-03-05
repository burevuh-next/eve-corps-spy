package com.example.backend.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.CurrentMissionDto;
import com.example.backend.dto.MissionStepDto;
import com.example.backend.dto.MissionTemplateDto;
import com.example.backend.entity.Agent;
import com.example.backend.entity.AgentSkill;
import com.example.backend.entity.Mission;
import com.example.backend.entity.MissionStep;
import com.example.backend.entity.MissionTemplate;
import com.example.backend.repository.AgentRepository;
import com.example.backend.repository.AgentSkillRepository;
import com.example.backend.repository.MissionRepository;
import com.example.backend.repository.MissionStepRepository;
import com.example.backend.repository.MissionTemplateRepository;

@Service
public class MissionService {

    @Autowired
    private MissionTemplateRepository templateRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private MissionStepRepository stepRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AgentSkillRepository agentSkillRepository;

    @Autowired
    private EveMarketService marketService;

    // Получить список доступных шаблонов миссий (все активные)
    public List<MissionTemplateDto> getAvailableTemplates() {
        return templateRepository.findByActiveTrue().stream()
                .map(t -> new MissionTemplateDto(
                        t.getId(), t.getType(), t.getName(), t.getDescription(),
                        t.getBaseReward(), t.getBaseRisk(),
                        t.getMinHackingLevel(), t.getMinSocialLevel(),
                        t.getMinStealthLevel(), t.getMinAnalysisLevel(),
                        t.getStepCount(), t.getMinReputation()))
                .collect(Collectors.toList());
    }

    // Принять миссию с проверкой навыков
    @Transactional
    public CurrentMissionDto acceptMission(Long agentId, Long templateId) {
        // Проверяем, нет ли уже активной миссии у агента
        List<Mission> activeMissions = missionRepository.findByAgentIdAndStatus(agentId, "in_progress");
        if (!activeMissions.isEmpty()) {
            throw new RuntimeException("Agent already has an active mission");
        }

        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        MissionTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        // Получаем навыки агента
        List<AgentSkill> agentSkills = agentSkillRepository.findByAgentId(agentId);
        Map<String, Integer> skillLevels = agentSkills.stream()
                .collect(Collectors.toMap(
                    as -> as.getSkill().getName().toLowerCase(),
                    AgentSkill::getLevel
                ));

        // Проверяем требования
        if (skillLevels.getOrDefault("hacking", 0) < template.getMinHackingLevel()) {
            throw new RuntimeException("Недостаточный уровень хакинга");
        }
        if (skillLevels.getOrDefault("social", 0) < template.getMinSocialLevel()) {
            throw new RuntimeException("Недостаточный уровень социальной инженерии");
        }
        if (skillLevels.getOrDefault("stealth", 0) < template.getMinStealthLevel()) {
            throw new RuntimeException("Недостаточный уровень скрытности");
        }
        if (skillLevels.getOrDefault("analysis", 0) < template.getMinAnalysisLevel()) {
            throw new RuntimeException("Недостаточный уровень анализа данных");
        }

        Mission mission = new Mission();
    
        int baseReward = template.getBaseReward();
        int repBonus = (int) (1.0 + (agent.getReputation()/1000.0)); // +0.1% за единицу
        int repReward = (int)(template.getBaseReward() * repBonus);
        int realReward = marketService.calculateMissionReward(template.getType(), baseReward + repReward);

        mission.setAgent(agent);
        mission.setTemplate(template);
        mission.setStatus("in_progress");
        mission.setCurrentStep(0);
        mission.setRiskAccumulated(0);
        mission.setReward(realReward);
        mission.setCreatedAt(Instant.now());
        mission.setAcceptedAt(Instant.now());

        mission = missionRepository.save(mission);

        // Создаём шаги для этой миссии
        List<MissionStep> steps = new ArrayList<>();
        for (int i = 1; i <= template.getStepCount(); i++) {
            MissionStep step = new MissionStep();
            step.setMission(mission);
            step.setStepNumber(i);
            step.setDescription("Шаг " + i + " миссии \"" + template.getName() + "\"");
            step.setCompleted(false);
            steps.add(step);
        }
        stepRepository.saveAll(steps);

        return buildCurrentMissionDto(mission, steps);
    }

    // Получить текущую активную миссию агента
    public CurrentMissionDto getCurrentMission(Long agentId) {
        List<Mission> missions = missionRepository.findByAgentIdAndStatus(agentId, "in_progress");
        if (missions.isEmpty()) {
            throw new RuntimeException("No active mission");
        }
        Mission mission = missions.get(0); // берём первую (должна быть одна)
        List<MissionStep> steps = stepRepository.findByMissionIdOrderByStepNumberAsc(mission.getId());
        return buildCurrentMissionDto(mission, steps);
    }

    // Выполнить шаг миссии
    @Transactional
    public CurrentMissionDto executeStep(Long missionId, Long agentId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission not found"));

        if (!mission.getAgent().getId().equals(agentId)) {
            throw new RuntimeException("Mission does not belong to this agent");
        }

        if (!"in_progress".equals(mission.getStatus())) {
            throw new RuntimeException("Mission is not in progress");
        }

        List<MissionStep> steps = stepRepository.findByMissionIdOrderByStepNumberAsc(missionId);
        int currentStepIndex = mission.getCurrentStep();
        if (currentStepIndex >= steps.size()) {
            throw new RuntimeException("All steps already completed");
        }

        // Выполняем текущий шаг
        MissionStep step = steps.get(currentStepIndex);
        step.setCompleted(true);
        step.setCompletedAt(Instant.now());
        stepRepository.save(step);

        mission.setCurrentStep(currentStepIndex + 1);

        // Начисляем риск за шаг
        int riskPerStep = mission.getTemplate().getBaseRisk() / steps.size();
        mission.setRiskAccumulated(mission.getRiskAccumulated() + riskPerStep);

        // Если все шаги выполнены, завершаем миссию
        if (mission.getCurrentStep() >= steps.size()) {
            mission.setStatus("completed");
            mission.setCompletedAt(Instant.now());
            // Начисляем награду агенту
            Agent agent = mission.getAgent();
            agent.setCredits(agent.getCredits() + mission.getReward());
            agentRepository.save(agent);

            // Начисляем опыт за миссию
            List<AgentSkill> agentSkills = agentSkillRepository.findByAgentId(agentId);
            Map<String, Integer> expReward = getExpReward(mission.getTemplate().getType());
            for (Map.Entry<String, Integer> entry : expReward.entrySet()) {
                String skillName = entry.getKey().toLowerCase();
                int exp = entry.getValue();
                agentSkills.stream()
                        .filter(as -> as.getSkill().getName().toLowerCase().equals(skillName))
                        .findFirst()
                        .ifPresent(as -> {
                            as.setExperience(as.getExperience() + exp);
                            checkLevelUp(as);
                            agentSkillRepository.save(as);
                        });
            }
            // Начисляем репутацию (например, базовая награда / 10)
            int repReward = mission.getTemplate().getBaseReward() / 10;
            agent.setReputation(agent.getReputation() + repReward);
            agentRepository.save(agent);
        }

        mission = missionRepository.save(mission);
        steps = stepRepository.findByMissionIdOrderByStepNumberAsc(missionId); // перечитываем обновлённые шаги
        return buildCurrentMissionDto(mission, steps);
    }

    // Построение DTO текущей миссии
    private CurrentMissionDto buildCurrentMissionDto(Mission mission, List<MissionStep> steps) {
        List<MissionStepDto> stepDtos = steps.stream()
                .map(s -> new MissionStepDto(s.getStepNumber(), s.getDescription(), s.isCompleted()))
                .collect(Collectors.toList());

        return new CurrentMissionDto(
                mission.getId(),
                mission.getTemplate().getName(),
                mission.getTemplate().getDescription(),
                mission.getCurrentStep(),
                steps.size(),
                mission.getRiskAccumulated(),
                mission.getReward(),
                stepDtos
        );
    }

    // Начисление опыта в зависимости от типа миссии
    private Map<String, Integer> getExpReward(String missionType) {
        Map<String, Integer> map = new HashMap<>();
        switch (missionType) {
            case "data_theft":
                map.put("hacking", 20);
                map.put("analysis", 10);
                break;
            case "sabotage":
                map.put("stealth", 20);
                break;
            case "isk_theft":
                map.put("hacking", 15);
                map.put("social", 15);
                break;
            case "assassination":
                map.put("stealth", 25);
                map.put("social", 15);
                map.put("analysis", 5);
                break;
            case "interception":
                map.put("hacking", 20);
                map.put("analysis", 15);
                break;
            case "bribery":
                map.put("social", 25);
                map.put("stealth", 10);
                break;
            case "tech_espionage":
                map.put("hacking", 25);
                map.put("analysis", 15);
                map.put("stealth", 15);
                break;
            case "supply_sabotage":
                map.put("stealth", 15);
                map.put("hacking", 10);
                break;
            default:
                map.put("hacking", 10);
        }
        return map;
    }

    // Проверка и повышение уровня навыка
    private void checkLevelUp(AgentSkill as) {
        int expNeeded = as.getLevel() * 100; // можно настроить формулу
        while (as.getExperience() >= expNeeded && as.getLevel() < as.getSkill().getMaxLevel()) {
            as.setLevel(as.getLevel() + 1);
            as.setExperience(as.getExperience() - expNeeded);
            expNeeded = as.getLevel() * 100;
        }
    }

    public List<MissionTemplateDto> getAvailableTemplatesForAgent(Long agentId) {
        // Получаем агента
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        // Получаем навыки агента
        List<AgentSkill> agentSkills = agentSkillRepository.findByAgentId(agentId);
        Map<String, Integer> skillLevels = agentSkills.stream()
                .collect(Collectors.toMap(
                    as -> as.getSkill().getName().toLowerCase(),
                    AgentSkill::getLevel
                ));

        int reputation = agent.getReputation();

        // Получаем все активные шаблоны и фильтруем
        return templateRepository.findByActiveTrue().stream()
                .filter(template -> reputation >= template.getMinReputation())
                .filter(template -> 
                    skillLevels.getOrDefault("hacking", 0) >= template.getMinHackingLevel() &&
                    skillLevels.getOrDefault("social", 0) >= template.getMinSocialLevel() &&
                    skillLevels.getOrDefault("stealth", 0) >= template.getMinStealthLevel() &&
                    skillLevels.getOrDefault("analysis", 0) >= template.getMinAnalysisLevel()
                )
                .map(t -> new MissionTemplateDto(
                    t.getId(), t.getType(), t.getName(), t.getDescription(),
                    t.getBaseReward(), t.getBaseRisk(),
                    t.getMinHackingLevel(), t.getMinSocialLevel(),
                    t.getMinStealthLevel(), t.getMinAnalysisLevel(),
                    t.getStepCount(), t.getMinReputation()))
                .collect(Collectors.toList());
    }
}